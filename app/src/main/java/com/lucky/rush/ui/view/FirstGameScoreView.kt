package com.lucky.rush.ui.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.lucky.rush.R
import com.lucky.rush.databinding.ViewScoresBinding
import com.lucky.rush.ui.extensions.setTextGradient


class FirstGameScoreView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0,
) : FrameLayout(context, attrs, defStyle) {

    private val vb by lazy {
        ViewScoresBinding.inflate(LayoutInflater.from(context), this, true)
    }

    init {
        vb.textViewTitle.setTextGradient(R.color.orange, R.color.yellow_2)
        vb.textViewScore.setTextGradient(R.color.orange, R.color.yellow_2)
    }

    var score: Long = 0
        set(value) {
            field = value
            vb.textViewScore.text = value.toString()
        }

    var title: String = ""
        set(value) {
            field = value
            vb.textViewTitle.text = value
        }
}