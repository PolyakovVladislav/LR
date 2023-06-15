package com.lucky.rush.ui.extensions

import android.graphics.LinearGradient
import android.graphics.Shader
import android.graphics.Shader.TileMode
import android.text.TextPaint


fun TextPaint.applyGradientToText(xStart: Float, yStart: Float, xEnd: Float, yEnd: Float, colorList: IntArray) {
    val textShader: Shader = LinearGradient(
        xStart, yStart, xEnd, yEnd, colorList, null, TileMode.CLAMP
    )
    shader = textShader
}