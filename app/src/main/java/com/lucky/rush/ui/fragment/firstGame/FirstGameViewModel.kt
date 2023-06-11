package com.lucky.rush.ui.fragment.firstGame

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lucky.rush.R
import com.lucky.rush.ui.models.Slot
import com.lucky.rush.ui.utils.Data
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.pow

class FirstGameViewModel : ViewModel() {

    companion object {
        private const val FREQUENCY = 20L
        private val values = listOf(
            R.drawable.ic_temple,
            R.drawable.ic_tiger,
            R.drawable.ic_puma,
            R.drawable.ic_bear,
            R.drawable.ic_bull,
            R.drawable.ic_fox
        )
        const val VISIBLE_AMOUNT = 4f
    }

    private val _slot1LiveData = MutableLiveData<List<Slot>>()
    val slot1LiveData: LiveData<List<Slot>> = _slot1LiveData

    private val _slot2LiveData = MutableLiveData<List<Slot>>()
    val slot2LiveData: LiveData<List<Slot>> = _slot2LiveData

    private val _slot3LiveData = MutableLiveData<List<Slot>>()
    val slot3LiveData: LiveData<List<Slot>> = _slot3LiveData

    private val _slot4LiveData = MutableLiveData<List<Slot>>()
    val slot4LiveData: LiveData<List<Slot>> = _slot4LiveData

    private val _balance = MutableLiveData<Long>()
    val total: LiveData<Long> = _balance

    private val _win = MutableLiveData<Long>()
    val win: LiveData<Long> = _win

    private var gameState = GameState.Idle

    init {
        generateSlots()
    }

    fun test(bet: Long): Long {
        generateSlots()
        val multiplier = getMultiplier()
        val win = bet * multiplier
        return win.toLong()
    }

    fun play(bet: Long, prefs: Data) {
        viewModelScope.launch {
            if (gameState == GameState.Idle) {
                generateSlots()
                _balance.value = prefs.total - bet

                gameState = GameState.Rolling
                launch { rollSlot(_slot1LiveData) }
                delay(300)
                launch { rollSlot(_slot2LiveData) }
                delay(300)
                launch { rollSlot(_slot3LiveData) }
                delay(300)
                rollSlot(_slot4LiveData)

                val multiplier = getMultiplier()
                if (multiplier == 0f) {
                    if (prefs.total == 0L) {
                        _balance.value = 5000L
                    }
                } else {
                    _win.value = (multiplier * bet).toLong()
                    _balance.value = (prefs.total + multiplier * bet + bet).toLong()
                }

                gameState = GameState.Idle
            }
        }
    }

    private fun getMultiplier(): Float {
        val combination = listOf(
            _slot1LiveData.value?.let { slots ->
                slots[slots.size - 3].drawableRes
            } ?: return 0f,
            _slot2LiveData.value?.let { slots ->
                slots[slots.size - 3].drawableRes
            } ?: return 0f,
            _slot3LiveData.value?.let { slots ->
                slots[slots.size - 3].drawableRes
            } ?: return 0f,
            _slot4LiveData.value?.let { slots ->
                slots[slots.size - 3].drawableRes
            } ?: return 0f,
            _slot1LiveData.value?.let { slots ->
                slots[slots.size - 2].drawableRes
            } ?: return 0f,
            _slot2LiveData.value?.let { slots ->
                slots[slots.size - 2].drawableRes
            } ?: return 0f,
            _slot3LiveData.value?.let { slots ->
                slots[slots.size - 2].drawableRes
            } ?: return 0f,
            _slot4LiveData.value?.let { slots ->
                slots[slots.size - 2].drawableRes
            } ?: return 0f
        )

        if (combination.distinct().size == 1) {
            return if (combination.contains(R.drawable.ic_temple)) {
                15f
            } else {
                12f
            }
        }

        if (combination.distinct().size == 2) {
            return if (combination.subList(0, 3).distinct().size == 1 &&
                combination.subList(4, 7).distinct().size == 1
            ) {
                10f
            } else {
                7f
            }
        }


        if (combination.distinct().size == 3) {
            return 6f
        }

        val twoGroups = combination.chunked(2)
        if (twoGroups.size == 4 && twoGroups.all { it[0] == it[1] }) {
            return 5f
        }

        val threeGroups = combination.chunked(3)
        if (threeGroups.all {
                if (it.size == 3) it[0] == it[1] && it[0] == it[2] else true
        }
                    ) {
            return 3f
        }

        if (combination.distinct().size == 4) {
            return 1.25f
        }

        return 0f
    }

    private suspend fun rollSlot(liveData: MutableLiveData<List<Slot>>) {
        val currentList = liveData.value ?: return
        var currentPosition =
            currentList.reversed().indexOfFirst { it.relativePosition in 1 / VISIBLE_AMOUNT..2 / VISIBLE_AMOUNT }
        while (currentList.last().relativePosition > 7 / (VISIBLE_AMOUNT * 2f) ) {
            delay(FREQUENCY)
            val positionChange = calculateSpeed(currentPosition) * FREQUENCY / 1000
            currentList.forEach { it.relativePosition = it.relativePosition - positionChange }
            liveData.value = currentList
            currentPosition = currentList.reversed().indexOfFirst { slot ->
                slot.relativePosition in 1 / VISIBLE_AMOUNT..2 / VISIBLE_AMOUNT
            }
        }
        val positionAdjust = currentList.last().relativePosition - 7 / (VISIBLE_AMOUNT * 2f)
        currentList.forEach { it.relativePosition = it.relativePosition - positionAdjust }
        liveData.value = currentList
    }

    private fun calculateSpeed(currentPosition: Int): Float {
        return currentPosition.toFloat().pow(1 / 2f)
    }

    private fun generateSlots() {
        val slotList1 = mutableListOf<Slot>().apply {
            addAll(
                _slot1LiveData.value?.filter { it.relativePosition in 0f..1f } ?: emptyList(),
            )
        }
        val slotList2 = mutableListOf<Slot>().apply {
            addAll(
                _slot2LiveData.value?.filter { it.relativePosition in 0f..1f } ?: emptyList(),
            )
        }
        val slotList3 = mutableListOf<Slot>().apply {
            addAll(
                _slot3LiveData.value?.filter { it.relativePosition in 0f..1f } ?: emptyList(),
            )
        }
        val slotList4 = mutableListOf<Slot>().apply {
            addAll(
                _slot4LiveData.value?.filter { it.relativePosition in 0f..1f } ?: emptyList(),
            )
        }
        val constOffset = 1 / (VISIBLE_AMOUNT * 2)
        for (i in slotList1.size..48) {
            slotList1.add(
                Slot(
                    i,
                    values.random(),
                    constOffset + i * constOffset * 2,
                ),
            )
            slotList2.add(
                Slot(
                    i,
                    values.random(),
                    constOffset + i * constOffset * 2,
                ),
            )
            slotList3.add(
                Slot(
                    i,
                    values.random(),
                    constOffset + i * constOffset * 2,
                ),
            )
            slotList4.add(
                Slot(
                    i,
                    values.random(),
                    constOffset + i * constOffset * 2,
                ),
            )
        }
        for (i in 0..1) {
            slotList1.add(
                generateLastSlot(
                    slotList1.last().id + 1,
                    slotList1.last().relativePosition + 1 / VISIBLE_AMOUNT,
                ),
            )
            slotList2.add(
                generateLastSlot(
                    slotList2.last().id + 1,
                    slotList2.last().relativePosition + 1 / VISIBLE_AMOUNT,
                ),
            )
            slotList3.add(
                generateLastSlot(
                    slotList3.last().id + 1,
                    slotList3.last().relativePosition + 1 / VISIBLE_AMOUNT,
                ),
            )
            slotList4.add(
                generateLastSlot(
                    slotList4.last().id + 1,
                    slotList4.last().relativePosition + 1 / VISIBLE_AMOUNT,
                ),
            )
        }
        _slot1LiveData.value = slotList1
        _slot2LiveData.value = slotList2
        _slot3LiveData.value = slotList3
        _slot4LiveData.value = slotList4
    }

    private fun generateLastSlot(id: Int, relativePosition: Float): Slot {
        val drawableRes = values.random()
        return Slot(
            id,
            drawableRes,
            relativePosition,
        )
    }

    internal enum class GameState {
        Idle, Rolling
    }
}
