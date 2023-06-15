package com.lucky.rush.ui.extensions

import android.view.View
import android.view.ViewPropertyAnimator
import android.view.ViewTreeObserver
import androidx.core.view.isVisible


fun View.alpha(duration: Long = 800): ViewPropertyAnimator {
    isVisible = true
    alpha = if (alpha == 1.0f) 0f else alpha
    return animate().alpha(1.0f).setDuration(duration)
}

fun View.revertAlpha(duration: Long = 800): ViewPropertyAnimator {
    return animate().alpha(0f).setDuration(duration)
}

fun View.getCallbackOnFirstGlobalLayout(action: () -> Unit) {
    viewTreeObserver.addOnGlobalLayoutListener(
        object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                action()
                viewTreeObserver.removeOnGlobalLayoutListener(this)
            }
        }
    )
}