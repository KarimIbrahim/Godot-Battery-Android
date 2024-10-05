# Godot Battery Android Plugin
An Android plugin for Godot to listen for the battery status. The plugin is using the new Godot 4 plugin system.

The plugin is generated from the [`Godot Android templates`](https://github.com/m4gr3d/Godot-Android-Plugin-Template).

## Contents
* The gradle project for Android plugin: [`plugin`](plugin)
* The pre-built binaries for the plugin to be used as-is in your Godot project: [`plugin/demo/addons/BatteryPlugin`](plugin/demo/addons/BatteryPlugin)
* A wrapper/helper class to work with the plugin in GDScript: [`plugin/demo/android_battery_plugin.gd`](plugin/demo/android_battery_plugin.gd)
* A demo project to test the plugin: [`plugin/demo`](plugin/demo)

## Release
You can find the latest binaries in the [`releases page`](https://github.com/KarimIbrahim/Godot-Battery-Android/releases).

## Usage
**Note:** [Android Studio](https://developer.android.com/studio) is the recommended IDE for developing the Godot Android plugins.

### `I don't know what I'm doing` Guide
1. Copy [`plugin/demo/addons/BatteryPlugin`](plugin/demo/addons/BatteryPlugin) directory under the `addons` directory in your Godot project. You can also download the latest binaries from the [`releases page`](https://github.com/KarimIbrahim/Godot-Battery-Android/releases)
2. Copy [`plugin/demo/android_battery_plugin.gd`](plugin/demo/android_battery_plugin.gd) to your scripts directory in your Godot project
3. Create a `Node` in your scene and call it `AndroidBatteryPlugin`
4. Attach the `android_battery_plugin.gd` to the `AndroidBatteryPlugin` node (Alternatively, you can experiment with `Globals`, however, I haven't tested that setup yet)
5. Reference the `AndroidBatteryPlugin` in your GDScripts either by path, or through an export e.g. `@export var android_plugin: AndroidBatteryPlugin`
6. Connect the `AndroidBatteryPlugin` node with your exported variable
7. Create a listener for the battery updates in your script:
  ```gdscript
  func _on_battery_level_changed(level: int) -> void:
	  battery_level_label.text = 'Battery Level: %d' % level


  func _on_battery_state_changed(state: int) -> void:
	  var state_string: String = 'Unknown'
	  match state:
		  0:
			  state_string = 'Unplugged'
		  1:
			  state_string = 'Charging'
		  _: 
			  state_string = 'Unknown'	

	  battery_state_label.text = 'Battery State: %s' % state_string
  ```
8. Connect the location update signal with the listener you just created in the `_ready()` method:
  ```gdscript
  func _ready():
	android_plugin.android_battery_level_changed.connect(self._on_battery_level_changed)
	android_plugin.android_battery_state_changed.connect(self._on_battery_state_changed)
  ```
9. Export your project using the Android template
10. Have fun!!


### `Show me the secret sauce` Guide
1. Start from the [`plugin/demo`](plugin/demo) project
2. Open the project in Godot
3. Navigate to [`plugin/demo/main.gd`](plugin/demo/main.gd). This is the entry point which uses the plugin wrapper [`plugin/demo/android_battery_plugin.gd`](plugin/demo/android_battery_plugin.gd) to communicate with the plugin
4. The plugin wrapper exposes the below methods and signals. This class is what you want to use in your project to call the plugin. Feel free to modify as need to suit your use-case:
  ```gdscript
  # Emitted when the battery level changes.
  signal android_battery_level_changed(level: int)

  # Emitted when the battery state changes.
  signal android_battery_state_changed(state: int)

  # Pings the plugin and returns its name and version.
  func ping() -> String

  # Returns the battery level
  func get_battery_level() -> int

  # Returns the battery state
  func get_battery_state() -> int
  ```
5. The Android plugin code is in [`GodotAndroidPlugin.kt`](plugin/src/main/java/com/karimibrahim/godot/android/battery/GodotAndroidPlugin.kt)
8. All operations are idempotent
9. Min SDK version is 21


### Building the Android plugin
- You don't technically need to build the plugin, unless you need to modify the gradle project. The pre-built binaries (aar) are included under the [`plugin/demo/addons/BatteryPlugin/bin`](plugin/demo/addons/BatteryPlugin/bin) and in the [`releases page`](https://github.com/KarimIbrahim/Godot-Battery-Android/releases)
- In a terminal window, navigate to the project's root directory ([`Godot-Battery-Android`](Godot-Battery-Android)) and run the following command:
```
./gradlew assemble
```
- On successful completion of the build, the output files can be found in [`plugin/demo/addons`](plugin/demo/addons)

### Testing the Android plugin
You can use the included [Godot demo project](plugin/demo/project.godot) to test the built Android plugin

- Open the demo in Godot (4.2 or higher)
- Navigate to `Project` -> `Project Settings...` -> `Plugins`, and ensure the plugin is enabled
- Install the Godot Android build template by clicking on `Project` -> `Install Android Build Template...`
- Open [`plugin/demo/main.gd`](plugin/demo/main.gd) and update the logic as needed 
- Connect an Android device to your machine and run the demo on it

#### Tips
Additional dependencies added to [`plugin/build.gradle.kts`](plugin/build.gradle.kts) should be added to the `_get_android_dependencies`
function in [`plugin/export_scripts_template/export_plugin.gd`](plugin/export_scripts_template/export_plugin.gd).
