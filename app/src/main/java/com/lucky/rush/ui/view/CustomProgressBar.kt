package com.lucky.rush.ui.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import com.lucky.rush.databinding.ViewCustomProgressBarBinding


class CustomProgressBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0,
) : FrameLayout(context, attrs, defStyle) {

    private val binding by lazy {
        ViewCustomProgressBarBinding.inflate(LayoutInflater.from(context), this, true)
    }

    private var progressListener: ((Int) -> Unit)? = null

    var clicksEnabled = true

    init {
        binding.root.setOnTouchListener { v, event ->
            if (clicksEnabled) {
                val newProgress = ((event.x - binding.root.x) / binding.root.width * 100).toInt()
                progress = newProgress
            }
            clicksEnabled
        }
        updateProgress()
    }

    var progress: Int = 0
        set(value) {
            field = value
            updateProgress()
            progressListener?.let { it(value) }
        }

    fun setOnChangeListener(listener: (Int) -> Unit) {
        progressListener = listener
    }

    private fun updateProgress() {
        binding.imageViewProgress.layoutParams =
            LayoutParams(
                (progress / 100f * binding.root.width).toInt(),
                ViewGroup.LayoutParams.MATCH_PARENT
            )
    }
}