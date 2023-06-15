package com.lucky.rush.ui.extensions

import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import com.google.android.material.button.MaterialButton

fun MaterialButton.applyGradientToText(@ColorRes vararg colorList: Int) {
    paint.applyGradientToText(
        0f,
        0f,
        width.toFloat(),
        textSize + paddingTop,
        colorList.map { ContextCompat.getColor(context, it) }.toIntArray()
    )
}