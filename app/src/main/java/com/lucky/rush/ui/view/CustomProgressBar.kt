package com.lucky.rush.ui.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import com.lucky.rush.databinding.ViewCustomProgressBarBinding
import com.lucky.rush.ui.extensions.getCallbackOnFirstGlobalLayout


class CustomProgressBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0,
) : FrameLayout(context, attrs, defStyle) {

    private val vb by lazy {
        ViewCustomProgressBarBinding.inflate(LayoutInflater.from(context), this, true)
    }

    private var progressListener: ((Int) -> Unit)? = null

    var clicksEnabled = true

    init {
        vb.root.setOnTouchListener { v, event ->
            if (clicksEnabled) {
                val newProgress = ((event.x - vb.root.x) / vb.root.width * 100).toInt()
                progress = newProgress
            }
            clicksEnabled
        }
        getCallbackOnFirstGlobalLayout {
            update()
        }
    }

    var progress: Int = 0
        set(value) {
            field = value
            update()
            progressListener?.let { it(value) }
        }

    fun setOnProgressListener(listener: (Int) -> Unit) {
        progressListener = listener
    }

    private fun update() {
        vb.imageViewProgress.layoutParams =
            LayoutParams(
                (progress / 100f * vb.root.width).toInt(),
                ViewGroup.LayoutParams.MATCH_PARENT
            )
    }
}