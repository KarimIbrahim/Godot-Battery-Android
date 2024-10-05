extends Node

# A wrapper/helper class for the  Android Battery Plugin.
#
# The wrapper exposes 2 signals that will help you work with the plugin:
# 1. android_battery_level_changed: Emitted when the battery level changes.
# 2. android_battery_state_changed: Emitted when the battery state changes.
class_name AndroidBatteryPlugin

var _plugin_name = "BatteryPlugin"
var _android_plugin

# Emitted when the battery level changes.
signal android_battery_level_changed(level: int)

# Emitted when the battery state changes.
signal android_battery_state_changed(state: int)


func _ready():
	if Engine.has_singleton(_plugin_name):
		_android_plugin = Engine.get_singleton(_plugin_name)
		_android_plugin.connect("battery_level", self._on_battery_level_changed)
		_android_plugin.connect("battery_state", self._on_battery_state_changed)
	else:
		printerr("Couldn't find plugin " + _plugin_name)


# Pings the plugin and returns its name and version.
func ping() -> String:
	if _android_plugin:
		return _android_plugin.ping()
	else:
		return "Couldn't find plugin" + _plugin_name


# Returns the battery level
func get_battery_level() -> int:
	if _android_plugin:
		return _android_plugin.getBatteryLevel()
	else:
		return -1


# Returns the battery state
func get_battery_state() -> int:
	if _android_plugin:
		return _android_plugin.getBatteryState()
	else:
		return -1

func _on_battery_level_changed(level: int) -> void:
	android_battery_level_changed.emit(level)


func _on_battery_state_changed(state: int) -> void:
	android_battery_state_changed.emit(state)
