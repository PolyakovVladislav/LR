package com.lucky.rush.ui.extensions

import android.content.res.Resources

fun Int.convertDpToPx(): Int = (this * Resources.getSystem().displayMetrics.density).toInt()