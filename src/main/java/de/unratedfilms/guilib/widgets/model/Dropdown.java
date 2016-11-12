
package de.unratedfilms.guilib.widgets.model;

import java.util.Collection;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.google.common.collect.ImmutableList;
import de.unratedfilms.guilib.core.WidgetFlexible;
import de.unratedfilms.guilib.core.WidgetFocusable;
import de.unratedfilms.guilib.widgets.model.Dropdown.Option;;

/**
 * Abstract representation of a dropdown menu / combo box.
 * The dropdown calls {@link DropdownHandler#optionSelected(Dropdown, Option)} when another option is selected by the player.
 *
 * @param <O> The type of {@link Option} that can be selected through this dropdown menu.
 */
public interface Dropdown<O extends Option<?>> extends WidgetFlexible, WidgetFocusable {

    public DropdownHandler<O> getHandler();

    public Dropdown<O> setHandler(DropdownHandler<O> handler);

    public ImmutableList<O> getOptions();

    /**
     * Sets the options that can be chosen through this dropdown menu.
     * If the {@link #getSelectedOption() currently selected option} isn't part of the new options list, the currently selected option is set to the first entry of the new list.
     *
     * @param options The new option list.
     */
    public Dropdown<O> setOptions(Collection<O> options);

    public O getSelectedOption();

    /**
     * Sets the option which is currently selected by this dropdown menu.
     *
     * @param selectedOption The new currently selected option.
     * @throws IllegalArgumentException If the new selected option isn't part of the {@link #getOptions() options list}.
     */
    public Dropdown<O> setSelectedOption(O selectedOption);

    public static interface DropdownHandler<O extends Option<?>> {

        public void optionSelected(Dropdown<O> dropdown, O option);

    }

    /**
     * An option that can be selected through a {@link Dropdown} menu.
     *
     * @param <D> The type of object that should be somehow shown to the player.
     *        For example, this could be a string, in which the dropdown menu would consist out of labels.
     *        Note that this does <b>not</b> have to replace a custom user object with some internal data that's only relevant to the program!
     */
    public interface Option<D> {

        public D getDisplayObject();

    }

    public final class GenericOption<D> implements Option<D> {

        private final D displayObject;

        public GenericOption(D displayObject) {

            this.displayObject = displayObject;
        }

        @Override
        public D getDisplayObject() {

            return displayObject;
        }

        @Override
        public int hashCode() {

            return HashCodeBuilder.reflectionHashCode(this);
        }

        @Override
        public boolean equals(Object obj) {

            return EqualsBuilder.reflectionEquals(this, obj);
        }

        @Override
        public String toString() {

            return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
        }

    }

    public final class GenericUserObjectOption<U, D> implements Option<D> {

        private final U userObject;
        private final D displayObject;

        public GenericUserObjectOption(U userObject, D displayObject) {

            this.userObject = userObject;
            this.displayObject = displayObject;
        }

        public U getUserObject() {

            return userObject;
        }

        @Override
        public D getDisplayObject() {

            return displayObject;
        }

        @Override
        public int hashCode() {

            return HashCodeBuilder.reflectionHashCode(this);
        }

        @Override
        public boolean equals(Object obj) {

            return EqualsBuilder.reflectionEquals(this, obj);
        }

        @Override
        public String toString() {

            return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
        }

    }

}
