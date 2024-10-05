extends Node2D

# This is the entry point for the test project.
# The file contains several buttons and signals to test the plugin.
# Feel free to change this file as much as you for you own testing.

@export var log_label: Label
@export var battery_level_label: Label
@export var battery_state_label: Label
@export var android_plugin: AndroidBatteryPlugin


func _ready():
	android_plugin.android_battery_level_changed.connect(self._on_battery_level_changed)
	android_plugin.android_battery_state_changed.connect(self._on_battery_state_changed)

	_on_battery_level_changed(android_plugin.get_battery_level())
	_on_battery_state_changed(android_plugin.get_battery_state())


func _on_Button_pressed() -> void:
	log_label.text = android_plugin.ping()


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
