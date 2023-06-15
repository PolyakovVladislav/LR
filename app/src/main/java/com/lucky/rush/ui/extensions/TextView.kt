package com.lucky.rush.ui.extensions

import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat

fun TextView.applyGradientToText(@ColorRes vararg colorList: Int) {
    paint.applyGradientToText(
        0f,
        0f,
        width.toFloat(),
        textSize + paddingTop,
        colorList.map { ContextCompat.getColor(context, it) }.toIntArray()
    )
}