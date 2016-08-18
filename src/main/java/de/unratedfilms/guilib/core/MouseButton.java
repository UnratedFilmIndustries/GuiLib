
package de.unratedfilms.guilib.core;

public enum MouseButton {

    LEFT (0), RIGHT (1), MIDDLE (2), UNKNOWN (-1);

    public static MouseButton fromCode(int code) {

        for (MouseButton mouseButton : values()) {
            if (code == mouseButton.getCode()) {
                return mouseButton;
            }
        }

        return UNKNOWN;
    }

    private final int code;

    private MouseButton(int code) {

        this.code = code;
    }

    /**
     * Returns the internal code that is used by LWJGL do describe which button has been pressed.
     *
     * @return The LWJGL mouse button code, or {@code -1} if the button is unknown.
     */
    public int getCode() {

        return code;
    }

}
