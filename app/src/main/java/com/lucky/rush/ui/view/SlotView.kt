package com.lucky.rush.ui.view

import android.animation.LayoutTransition
import android.content.Context
import android.content.res.Resources
import android.util.AttributeSet
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.widget.FrameLayout
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.view.children
import androidx.core.view.setPadding
import com.lucky.rush.ui.models.Slot


class SlotView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0,
) : FrameLayout(context, attrs, defStyle) {

    private var currentList = listOf<Slot>()
    private var itemsInColumn = 0

    init {
        layoutTransition = LayoutTransition()

        viewTreeObserver.addOnGlobalLayoutListener(
            object : OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    update(currentList, itemsInColumn)
                    viewTreeObserver.removeOnGlobalLayoutListener(this)
                }
            }
        )
    }

    fun update(list: List<Slot>, itemsInColumn: Int) {
        if (list.isEmpty()) return
        currentList = list
        this.itemsInColumn = itemsInColumn
        val viewsForRemoval = mutableListOf<AppCompatImageView>()
        children.forEach { view ->
            if (list.any { slot -> slot.id == view.tag }.not()) {
                viewsForRemoval.add(view as AppCompatImageView)
            }
        }
        list.forEach { slot ->
            if (slot.relativePosition !in -1f..2f) return@forEach
            val view = children.find { view -> view.tag == slot.id }
            if (view != null) {
                (view as AppCompatImageView).setImageResource(slot.drawableRes)
                view.layoutParams = LayoutParams(
                    LayoutParams.MATCH_PARENT,
                    height / itemsInColumn,
                )
                view.y = height * slot.relativePosition - height / (itemsInColumn * 2)
            } else {
                val imageView = if (viewsForRemoval.isEmpty()) {
                    val internalImageView = AppCompatImageView(context)
                    addView(internalImageView)
                    internalImageView
                } else {
                    val internalImageView = viewsForRemoval.first()
                    viewsForRemoval.removeFirst()
                    internalImageView
                }
                imageView.layoutParams = LayoutParams(
                    LayoutParams.MATCH_PARENT,
                    height / 3,
                )
                imageView.setPadding(6.dpToPx)
                imageView.x = 0f
                imageView.y = height * slot.relativePosition - height / (itemsInColumn * 2)
                imageView.setImageResource(slot.drawableRes)
                imageView.tag = slot.id
            }
        }
        viewsForRemoval.forEach { view ->
            removeView(view)
        }
    }

    private val Int.dpToPx: Int get() = (this * Resources.getSystem().displayMetrics.density).toInt()
}
