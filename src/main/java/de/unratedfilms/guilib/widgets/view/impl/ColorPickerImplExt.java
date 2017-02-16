
package de.unratedfilms.guilib.widgets.view.impl;

import java.awt.Color;
import java.util.function.Supplier;
import org.apache.logging.log4j.core.helpers.Integers;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.GlStateManager.DestFactor;
import net.minecraft.client.renderer.GlStateManager.LogicOp;
import net.minecraft.client.renderer.GlStateManager.SourceFactor;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.client.config.GuiUtils;
import de.unratedfilms.guilib.core.Axis;
import de.unratedfilms.guilib.core.MouseButton;
import de.unratedfilms.guilib.core.Viewport;
import de.unratedfilms.guilib.core.Viewport.Drawer;
import de.unratedfilms.guilib.core.WidgetRigid;
import de.unratedfilms.guilib.extra.ContextHelperWidgetAdapter;
import de.unratedfilms.guilib.widgets.model.Button.FilteredButtonHandler;
import de.unratedfilms.guilib.widgets.model.ButtonLabel;
import de.unratedfilms.guilib.widgets.model.ContainerRigid;
import de.unratedfilms.guilib.widgets.model.Slider;
import de.unratedfilms.guilib.widgets.model.TextField;
import de.unratedfilms.guilib.widgets.view.adapters.ContainerAdapter;
import de.unratedfilms.guilib.widgets.view.adapters.SliderAdapter;

/**
 * The popup control used by {@link ColorPickerImpl} for the actual color picking.
 * Inspired by mumfrey's color picker he created for WorldEditCUI.
 */
class ColorPickerImplExt extends ContainerAdapter implements ContainerRigid {

    // Indices into the HSB array
    private static final int              H                       = 0, S = 1, B = 2;

    private static final ResourceLocation TEXTURE_VANILLA_WIDGETS = new ResourceLocation("textures/gui/widgets.png");
    private static final ResourceLocation TEXTURE_COLOR_PICKER    = new ResourceLocation("guilib", "textures/gui/color_picker.png");

    private final ColorPickerImpl         parent;

    // HSB values from Colour.RGBtoHSB, combined with opacity this is the authoritative version of the colour we are editing.
    private float[]                       hsb;
    // Current alpha, stored as float from 0 to 1
    private float                         alpha;

    private final PickerAreaImpl          pickerArea;
    private final Slider<Float>           brightnessSlider;
    private final Slider<Float>           alphaSlider;

    private final TextField               redTextField;
    private final TextField               greenTextField;
    private final TextField               blueTextField;
    private final TextField               alphaTextField;

    private final ButtonLabel             confirmButton;
    private final ButtonLabel             cancelButton;

    public ColorPickerImplExt(ColorPickerImpl parent) {

        this.parent = parent;

        pickerArea = new PickerAreaImpl();
        brightnessSlider = new SpecialSliderImpl(I18n.format("gui.colorPicker.brightness"), () -> Color.HSBtoRGB(hsb[H], hsb[S], 1))
                .setDraggingAxis(Axis.Y).setHandler(sl -> pullColorFromSliders());
        alphaSlider = new SpecialSliderImpl(I18n.format("gui.colorPicker.alpha"), () -> 0xFF_FF_FF_FF)
                .setDraggingAxis(Axis.Y).setHandler(sl -> pullColorFromSliders());

        redTextField = new TextFieldImpl().setOuterColor(0xFF_FF_00_00).setMaxLength(3).setFilter(Character::isDigit).setHandler((txt, ch, code) -> pullColorFromTextFields());
        greenTextField = new TextFieldImpl().setOuterColor(0xFF_00_FF_00).setMaxLength(3).setFilter(Character::isDigit).setHandler((txt, ch, code) -> pullColorFromTextFields());
        blueTextField = new TextFieldImpl().setOuterColor(0xFF_00_00_FF).setMaxLength(3).setFilter(Character::isDigit).setHandler((txt, ch, code) -> pullColorFromTextFields());
        alphaTextField = new TextFieldImpl().setOuterColor(0xFF_A0_A0_A0).setMaxLength(3).setFilter(Character::isDigit).setHandler((txt, ch, code) -> pullColorFromTextFields());

        confirmButton = new ButtonLabelImpl(I18n.format("gui.done"), new FilteredButtonHandler(MouseButton.LEFT, (b, mb) -> {
            parent.setColorBits(asRGBABits());
            parent.focusLost();

            if (parent.getHandler() != null) {
                parent.getHandler().colorPicked(parent);
            }
        }));
        cancelButton = new ButtonLabelImpl(I18n.format("gui.cancel"), new FilteredButtonHandler(MouseButton.LEFT, (b, mb) -> parent.focusLost()));

        addWidgets(
                redTextField, greenTextField, blueTextField, alphaTextField,
                pickerArea, brightnessSlider, alphaSlider,
                confirmButton, cancelButton);

        // ----- Revalidation -----

        appendLayoutManager(c -> {
            setSize(229, 174);

            pickerArea.setPosition(9, 9);
            brightnessSlider.setBounds(142, 9, 17, 130);
            alphaSlider.setBounds(162, 9, 17, 130);

            redTextField.setBounds(188, 10, 32, 16);
            greenTextField.setBounds(188, 30, 32, 16);
            blueTextField.setBounds(188, 50, 32, 16);
            alphaTextField.setBounds(188, 70, 32, 16);

            confirmButton.setBounds(9, 145, 55, 20);
            cancelButton.setBounds(70, 145, 65, 20);
        });
    }

    private int[] asRGBA() {

        int rgba = asRGBABits();
        return new int[] { rgba >> 16 & 0xFF /* red */, rgba >> 8 & 0xFF /* green */, rgba & 0xFF /* blue */, rgba >> 24 & 0xFF /* alpha */ };
    }

    private int asRGBABits() {

        return (int) (alpha * 0xFF) << 24 | 0x00_FF_FF_FF & Color.HSBtoRGB(hsb[H], hsb[S], hsb[B]);
    }

    public void pullColorFromParent() {

        Color color = parent.getColor();
        pushRGBA(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
    }

    private void pullColorFromSliders() {

        hsb[B] = brightnessSlider.getValue();
        alpha = alphaSlider.getValue();

        pushColorIntoControls();
    }

    private void pullColorFromTextFields() {

        int red = MathHelper.clamp(Integers.parseInt(redTextField.getText(), 0), 0, 255);
        int green = MathHelper.clamp(Integers.parseInt(greenTextField.getText(), 0), 0, 255);
        int blue = MathHelper.clamp(Integers.parseInt(blueTextField.getText(), 0), 0, 255);
        int alpha = MathHelper.clamp(Integers.parseInt(alphaTextField.getText(), 0), 0, 255);

        pushRGBA(red, green, blue, alpha);
    }

    private void pushRGBA(int red, int green, int blue, int alpha) {

        hsb = Color.RGBtoHSB(red, green, blue, null);
        this.alpha = alpha / (float) 0xFF;

        pushColorIntoControls();
    }

    private void pushColorIntoControls() {

        // Sliders; note that the picker area updates automatically since it pulls its data directly from the HSB array
        brightnessSlider.setValue(hsb[B]);
        alphaSlider.setValue(alpha);

        // Text fields
        int[] rgba = asRGBA();
        redTextField.setText(Integer.toString(rgba[0]));
        greenTextField.setText(Integer.toString(rgba[1]));
        blueTextField.setText(Integer.toString(rgba[2]));
        alphaTextField.setText(Integer.toString(rgba[3]));
    }

    /*
     * Event handlers
     */

    @Override
    public void draw(Viewport viewport, int mx, int my) {

        super.draw(viewport, mx, my);

        viewport.drawInLocalContext(mx, my, new Drawer() {

            @Override
            public void draw(int lmx, int lmy) {

                drawInLocalContext(viewport, lmx, lmy);
            }

        });
    }

    private void drawInLocalContext(Viewport viewport, int lmx, int lmy) {

        // Draw background
        drawRect(getX(), getY(), getRight(), getBottom(), 0xAA_00_00_00);

        // Draw preview
        drawRect(getX() + 187, getY() + 105, getX() + 221, getY() + 139, 0xFFA0A0A0); // Background
        ColorPickerImpl.drawCheckerboard(getX() + 188, getY() + 106, getX() + 220, getY() + 138); // Checkerboard
        drawRect(getX() + 188, getY() + 106, getX() + 220, getY() + 138, asRGBABits()); // Actual color preview
    }

    // A vertical slider that's designed for this color picker (value range 0.0 - 1.0)
    private static class SpecialSliderImpl extends SliderAdapter<Float> {

        private static final int        HANDLE_SIZE = 8;

        private final Supplier<Integer> upperColorSupplier;

        public SpecialSliderImpl(String label, Supplier<Integer> upperColorSupplier) {

            super(0f, 1f, s -> label, 0f /* set by the update method */);

            this.upperColorSupplier = upperColorSupplier;
        }

        @Override
        protected Float convertFromDegree(float degree) {

            return 1 - (degree * (getMaxValue() - getMinValue()) + getMinValue());
        }

        @Override
        protected float convertToDegree(Float value) {

            return 1 - (value - getMinValue()) / (getMaxValue() - getMinValue());
        }

        @Override
        public void drawInLocalContext(Viewport viewport, int lmx, int lmy) {

            super.drawInLocalContext(viewport, lmx, lmy);

            // Border
            drawRect(getX(), getY(), getRight(), getBottom(), 0xFF_A0_A0_A0);

            // Rail
            drawGradientRect(getX() + 1, getY() + 1, getRight() - 1, getBottom() - 1, upperColorSupplier.get(), 0xFF_00_00_00);

            // Handle
            int handleCoord = getY() + 1 + (int) (getDegree() * (getHeight() - 2 - HANDLE_SIZE));
            GuiUtils.drawContinuousTexturedBox(TEXTURE_VANILLA_WIDGETS, getX() + 1, handleCoord, 0, 66, getWidth() - 2, HANDLE_SIZE, 200, 20, 2, 3, 2, 2, zLevel);

            // Label
            GlStateManager.pushMatrix();
            {
                GlStateManager.translate(getX() + (getWidth() - MC.fontRendererObj.FONT_HEIGHT) / 2, getY() + getHeight() / 2, 0);
                GlStateManager.rotate(-90, 0, 0, 1);
                drawCenteredString(MC.fontRendererObj, getLabelFormatter().formatLabel(this), 0, 0,
                        inLocalBounds(viewport, lmx, lmy) ? 16777120 : 0xFF_FF_FF);
            }
            GlStateManager.popMatrix();
        }

    }

    private class PickerAreaImpl extends ContextHelperWidgetAdapter implements WidgetRigid {

        private static final int CROSSHAIR_SIZE = 5;

        private boolean          dragging;

        public PickerAreaImpl() {

            setSize(128 + 2, 128 + 2);
        }

        private float getDegreeX() {

            return hsb[H];
        }

        private void setDegreeX(float degreeX) {

            hsb[H] = degreeX;
        }

        private float getDegreeY() {

            return 1 - hsb[S];
        }

        private void setDegreeY(float degreeY) {

            hsb[S] = 1 - degreeY;
        }

        @Override
        public void drawInLocalContext(Viewport viewport, int lmx, int lmy) {

            if (dragging) {
                setDegreeX(MathHelper.clamp((float) (lmx - (getX() + 1)) / (getWidth() - 2), 0, 1));
                setDegreeY(MathHelper.clamp((float) (lmy - (getY() + 1)) / (getHeight() - 2), 0, 1));

                pushColorIntoControls();
            }

            // Border
            drawRect(getX(), getY(), getRight(), getBottom(), 0xFF_A0_A0_A0);

            // Picker
            MC.renderEngine.bindTexture(TEXTURE_COLOR_PICKER);
            GlStateManager.color(1, 1, 1, 1);
            drawScaledCustomSizeModalRect(getX() + 1, getY() + 1, 0, 0, 64, 64, getWidth() - 2, getHeight() - 2, 64, 64);

            // Handle
            GlStateManager.color(0, 0, 0, 1);
            drawCrosshair((int) (getX() + 1 + (getWidth() - 2) * getDegreeX()), (int) (getY() + 1 + (getHeight() - 2) * getDegreeY()));
        }

        private void drawCrosshair(int x, int y) {

            GlStateManager.blendFunc(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA);
            GlStateManager.enableBlend();
            GlStateManager.disableTexture2D();
            GlStateManager.disableLighting();
            GlStateManager.enableColorLogic();
            GlStateManager.colorLogicOp(LogicOp.OR_REVERSE);

            Tessellator tes = Tessellator.getInstance();
            VertexBuffer buf = tes.getBuffer();

            // Horizontal line
            buf.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION);
            buf.pos(x - CROSSHAIR_SIZE, y, 0).endVertex();
            buf.pos(x + CROSSHAIR_SIZE, y, 0).endVertex();
            tes.draw();

            // Vertical line
            buf.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION);
            buf.pos(x, y - CROSSHAIR_SIZE, 0).endVertex();
            buf.pos(x, y + CROSSHAIR_SIZE, 0).endVertex();
            tes.draw();

            GlStateManager.disableColorLogic();
            GlStateManager.enableTexture2D();
        }

        @Override
        public boolean mousePressedInLocalContext(Viewport viewport, int lmx, int lmy, MouseButton mouseButton) {

            if (mouseButton == MouseButton.LEFT && inLocalBounds(viewport, lmx, lmy)) {
                dragging = true;
                MC.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0F));

                return true;
            }

            return false;
        }

        @Override
        public void mouseReleasedInLocalContext(Viewport viewport, int lmx, int lmy, MouseButton mouseButton) {

            if (mouseButton == MouseButton.LEFT) {
                dragging = false;
            }
        }

    }

}
