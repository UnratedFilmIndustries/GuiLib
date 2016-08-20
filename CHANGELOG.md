1.2.0
-----

### Additions
* You can now handle all mouse clicks and not just the ones with the left mouse button.
* Replaced the somewhat obscure and counter-intuitive tooltip system with a way more intuitive and flexible one.
* All widgets are now described through interfaces and then implemented by concrete classes. That adds an additional layer of possible abstraction if you need it -- and in many cases, you actually do.

### Fixes
* Fixed the mcmod.info file not containing any version information in the dev JAR.

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
