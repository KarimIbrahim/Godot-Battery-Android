package com.karimibrahim.godot.android.battery

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import org.godotengine.godot.Godot
import org.godotengine.godot.plugin.GodotPlugin
import org.godotengine.godot.plugin.SignalInfo
import org.godotengine.godot.plugin.UsedByGodot

/**
 * A Godot plugin that listens for Battery updates.
 */
class GodotAndroidPlugin(godot: Godot) : GodotPlugin(godot) {
    private val batteryLevelSignal =
        SignalInfo("battery_level", Int::class.javaObjectType)
    private val batteryStateSignal = SignalInfo("battery_state", Int::class.javaObjectType)

    private val intentFilter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
    private var batteryReceiver: BroadcastReceiver =
        registerBatteryLevelReceiver(activity!!.applicationContext)

    override fun onMainDestroy() {
        super.onMainDestroy()
        activity!!.applicationContext.unregisterReceiver(batteryReceiver)
    }

    private fun onBatteryChange(context: Context, intent: Intent) {
        emitSignal(batteryLevelSignal.name, getBatteryLevel(context, intent))
        emitSignal(batteryStateSignal.name, getBatteryState(context, intent))
    }

    private fun getBatteryLevel(context: Context, intent: Intent): Int {
        val level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
        val scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
        return level * 100 / scale
    }

    private fun getBatteryState(context: Context, intent: Intent): Int {
        val status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1)
        val isCharging: Boolean = status == BatteryManager.BATTERY_STATUS_CHARGING
                || status == BatteryManager.BATTERY_STATUS_FULL
        return if (isCharging) 1 else 0
    }

    private fun registerBatteryLevelReceiver(context: Context): BroadcastReceiver {
        val batteryLevelReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                onBatteryChange(context!!, intent!!)
            }
        }
        context.registerReceiver(batteryLevelReceiver, intentFilter)
        return batteryLevelReceiver
    }

    override fun getPluginName() = BuildConfig.GODOT_PLUGIN_NAME

    override fun getPluginSignals() = setOf(
        batteryLevelSignal,
        batteryStateSignal,
    )

    /*
    A tester method to make sure the plugin is wired correctly and can be called
    from Godot.
     */
    @UsedByGodot
    fun ping(): String = "${BuildConfig.GODOT_PLUGIN_NAME}-${BuildConfig.GODOT_PLUGIN_VERSION}!"

    /**
     * Returns the battery level.
     */
    @UsedByGodot
    fun getBatteryLevel() = getBatteryLevel(
        activity!!.applicationContext,
        activity!!.registerReceiver(null, intentFilter)!!
    )

    /**
     * Returns the battery state.
     */
    @UsedByGodot
    fun getBatteryState() = getBatteryState(
        activity!!.applicationContext,
        activity!!.registerReceiver(null, intentFilter)!!
    )

}
