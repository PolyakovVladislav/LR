package com.lucky.rush.ui.utils

import android.app.Activity
import android.content.Context
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class Data(context: Context) {

    private val sharedPreferences = context.getSharedPreferences("Main", Context.MODE_PRIVATE)

    var isUserSigned by PreferenceFieldDelegate.Boolean("isUserSigned")

    var musicVolume by PreferenceFieldDelegate.Int("musicVolume", 50)

    var vibratingVolume by PreferenceFieldDelegate.Int("vibratingVolume", 50)

    var total by PreferenceFieldDelegate.Long("total", 5000L)

    var betGameFirst by PreferenceFieldDelegate.Long("betGameFirst", 500L)

    var wingGameFirst by PreferenceFieldDelegate.Long("wingGameFirst", 0L)

    private sealed class PreferenceFieldDelegate<T>(protected val key: kotlin.String) :
        ReadWriteProperty<Data, T> {

        class Boolean(key: kotlin.String) : PreferenceFieldDelegate<kotlin.Boolean>(key) {

            override fun getValue(thisRef: Data, property: KProperty<*>) =
                thisRef.sharedPreferences.getBoolean(key, false)

            override fun setValue(
                thisRef: Data,
                property: KProperty<*>,
                value: kotlin.Boolean,
            ) = thisRef.sharedPreferences.edit().putBoolean(key, value).apply()
        }

        class Int(
            key: kotlin.String,
            private val defValue: kotlin.Int
        ) : PreferenceFieldDelegate<kotlin.Int>(key) {

            override fun getValue(thisRef: Data, property: KProperty<*>) =
                thisRef.sharedPreferences.getInt(key, defValue)

            override fun setValue(thisRef: Data, property: KProperty<*>, value: kotlin.Int) =
                thisRef.sharedPreferences.edit().putInt(key, value).apply()
        }

        class Long(
            key: kotlin.String,
            private val defaultValue: kotlin.Long,
        ) : PreferenceFieldDelegate<kotlin.Long>(key) {

            override fun getValue(thisRef: Data, property: KProperty<*>) =
                thisRef.sharedPreferences.getLong(key, defaultValue)

            override fun setValue(
                thisRef: Data,
                property: KProperty<*>,
                value: kotlin.Long,
            ) = thisRef.sharedPreferences.edit().putLong(key, value).apply()
        }
    }

    companion object {
        @Volatile
        private var instance: Data? = null

        fun getInstance(activity: Activity): Data =
            instance ?: synchronized(this) {
                instance ?: Data(activity).also { instance = it }
            }
    }
}
