1.3.0
-----

### Additions
* Recursion is now done by external classes which take care of the whole recursion process. The event handlers on individual containers shall no longer go about calling event handlers on their children! Instead, they should implement `WidgetParent` and provide a list of their child widgets through that interface.
* All flexible widgets are now supplied with a viewport when revalidated. That viewport is final, i.e. it represents exactly the viewport the widget will receive when it is being drawn. You could, for example, use that viewport to ensure some parts of your widget don't clip out of the screen, e.g. a dropdown menu.
* Focus shifts via the tab key through multiple levels of containers are now possible and automatically executed.
* Instead of shifting the scrollbar by a fixed amount each time focus is shifted, the scrollable container now shifts the scrollbar such that the newly focused widget will be in frame.

### Removals
* The `mouseReleased()` event will no longer notify you automatically after a mouse button press you have captured is released, since that kind of functionality really should be defined by the widget itself. Instead, all widgets now receive all mouseReleased() events (without a capturing mechanism built in).
* Removed the `Container.getFocusableWidgets()` method since no one used it anyway, and calling it would create a totally new list due to some changes under the hood.

### Notes
* Upgraded to Minecraft version 1.11.2.

### Fixes
* A new fix prevents Minecraft from crashing if a tooltip-supporting widget returns a null tooltip.

1.2.0
-----

### Additions
* All widgets are now described through interfaces and then implemented by concrete classes. That adds an additional layer of possible abstraction if you need it -- and in many cases, you actually do.
* Added the flexible viewport architecture that allows widgets to decide what parts should be rendered where and with which scissor. It basically allows for things like dropdown menus that peek out of scrollable containers to happen.
* Added immutable classes for points, dimensions and rectangles.
* Added the Axis enum which can be used to dynamically query or change either x/y coordinates or width/height extents, possibly reducing code duplication.
* Added the possibility to nest containers.
* Added a proper revalidation process and layout managers to containers. Also added a distinction between rigid and flexible widgets, which is needed for the layouting to be strict and predictable.
* Widgets can now handle all mouse clicks and not just the ones with the left mouse button.
* Whether a widget is focused is now stored in the focused widget itself, instead of each container storing a reference to the focused widget.
* Replaced the somewhat obscure and counter-intuitive tooltip system with a way more intuitive and flexible one.
* Added the dropdown widget.
* Added a container widget that adjusts its size to fit its contents.
* Added new handlers to the text field and checkbox widgets.
* Many methods now return the object they have been called on, meaning that you can now chain those methods together, saving tons of unnecessary lines.
* Button labels are now abbreviated with ellipsis if they are too long.

### Fixes
* Fixed the mcmod.info file not containing any version information in the dev JAR.
* Fixed the buttons not scaling properly.
* Fixed lots of other minor bugs that I forgot two minutes after fixing them.

### Notes
* Restructured all the packages and untangled a lot of dependencies.
* A lot of fundamental changes and general cleanups will probably break your code. Watch out for that when upgrading!
* Upgraded to Java 8 for lambdas and stuff.

1.1.2
-----

### Additions
* Added this very changelog.

1.1.1
-----

### Notes
* Added deployment information to the build.gradle file. That means that a CI can now properly build and deploy this mod.

1.1.0
-----

### Notes
* This is the first iteration of the mod, so there aren't any changes to mention here. Note, however, that this mod was forked from: https://github.com/DavidGoldman/GuiLib
