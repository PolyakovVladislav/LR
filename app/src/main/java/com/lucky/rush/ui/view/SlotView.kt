package com.lucky.rush.ui.view

import android.animation.LayoutTransition
import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.view.children
import androidx.core.view.setPadding
import com.lucky.rush.ui.extensions.convertDpToPx
import com.lucky.rush.ui.extensions.getCallbackOnFirstGlobalLayout
import com.lucky.rush.ui.models.Slot

class SlotView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0,
) : FrameLayout(context, attrs, defStyle) {

    private var currentSlots = listOf<Slot>()
    private var itemsInColumn = 0

    init {
        getCallbackOnFirstGlobalLayout {
            submitChange(currentSlots, itemsInColumn)
        }
        layoutTransition = LayoutTransition()
    }

    fun submitChange(list: List<Slot>, itemsInColumn: Int) {
        if (list.isEmpty()) return
        currentSlots = list
        this.itemsInColumn = itemsInColumn
        val viewsPendingRemoval = mutableListOf<AppCompatImageView>()
        children.forEach { view ->
            if (list.any { slot -> slot.slotId == view.tag }.not()) {
                viewsPendingRemoval.add(view as AppCompatImageView)
            }
        }
        list.forEach { slot ->
            if (slot.pos !in -1f..2f) return@forEach
            val view = children.find { view -> view.tag == slot.slotId }
            if (view != null) {
                (view as AppCompatImageView).setImageResource(slot.drawableId)
                view.layoutParams = LayoutParams(
                    LayoutParams.MATCH_PARENT,
                    height / itemsInColumn,
                )
                view.y = height * slot.pos - height / (itemsInColumn * 2)
            } else {
                val iv = if (viewsPendingRemoval.isEmpty()) {
                    val internalImageView = AppCompatImageView(context)
                    addView(internalImageView)
                    internalImageView
                } else {
                    val internalImageView = viewsPendingRemoval.first()
                    viewsPendingRemoval.removeFirst()
                    internalImageView
                }
                iv.layoutParams = LayoutParams(
                    LayoutParams.MATCH_PARENT,
                    height / 3,
                )
                iv.setPadding(6.convertDpToPx())
                iv.x = 0f
                iv.y = height * slot.pos - height / (itemsInColumn * 2)
                iv.setImageResource(slot.drawableId)
                iv.tag = slot.slotId
            }
        }
        viewsPendingRemoval.forEach { view ->
            removeView(view)
        }
    }
}
