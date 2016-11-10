1.2.0
-----

### Additions
* All widgets are now described through interfaces and then implemented by concrete classes. That adds an additional layer of possible abstraction if you need it -- and in many cases, you actually do.
* Added the flexible viewport architecture that allows widgets to decide what parts should be rendered where and with which scissor. It basically allows for things like dropdown menus that peek out of scrollable containers to happen.
* Added immutable classes for points, dimensions and rectangles.
* Added the possibility to nest containers.
* Added a proper revalidation process and layout managers to containers. Also added a distinction between rigid and flexible widgets, which is needed for the layouting to be strict and predictable.
* Widgets can now handle all mouse clicks and not just the ones with the left mouse button.
* Whether a widget is focused is now stored in the focused widget itself, instead of each container storing a reference to the focused widget.
* Replaced the somewhat obscure and counter-intuitive tooltip system with a way more intuitive and flexible one.
* Added the dropdown widget.
* Added a container widget that adjusts its size to fit its contents.

### Fixes
* Fixed the mcmod.info file not containing any version information in the dev JAR.
* Fixed the buttons not scaling properly.
* Fixed lots of other minor bugs that I forgot two minutes after fixing them.

### Notes
* Restructured all the packages and untangled a lot of dependencies.
* A lot of fundamental changes and general cleanups will probably break your code. Watch out for that when upgrading!

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
