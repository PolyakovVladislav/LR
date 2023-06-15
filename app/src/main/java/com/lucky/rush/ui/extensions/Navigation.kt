package com.lucky.rush.ui.extensions

import android.os.Bundle
import android.util.Log
import androidx.annotation.IdRes
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import androidx.navigation.Navigator

internal fun NavController.safeNavigate(
    @IdRes id: Int,
    bundle: Bundle? = null,
    options: NavOptions? = null,
    extras: Navigator.Extras? = null
): Boolean {
    val a = currentDestination?.getAction(id) ?: graph.getAction(id)
    return try {
        if ((a != null && currentDestination?.id != a.destinationId) ||
            (a == null && currentDestination?.id != id)
        ) {
            navigate(id, bundle, options, extras)
            true
        } else {
            Log.e("Debug", "Action is null or destination screen is set already")
            false
        }
    } catch (ex: IllegalArgumentException) {
        Log.e("Debug", ex.message ?: "Navigation error")
        false
    }
}

internal fun NavController.safeNavigate(directions: NavDirections) {
    safeNavigate(directions.actionId, directions.arguments)
}

