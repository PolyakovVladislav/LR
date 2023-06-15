package com.lucky.rush.ui.fragment.firstGame

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

    private var gameState = GameState.Idle

    private val _slot1LD = MutableLiveData<List<Slot>>()
    val slot1LD: LiveData<List<Slot>> = _slot1LD

    private val _slot2LD = MutableLiveData<List<Slot>>()
    val slot2LD: LiveData<List<Slot>> = _slot2LD

    private val _slot3LD = MutableLiveData<List<Slot>>()
    val slot3LD: LiveData<List<Slot>> = _slot3LD

    private val _slot4LD = MutableLiveData<List<Slot>>()
    val slot4LD: LiveData<List<Slot>> = _slot4LD

    private val _total = MutableLiveData<Long>()
    val total: LiveData<Long> = _total

    private val _win = MutableLiveData<Long>()
    val win: LiveData<Long> = _win

    companion object {
        private const val RATE = 20L
        private val drawableIdList = listOf(
            R.drawable.ic_temple,
            R.drawable.ic_tiger,
            R.drawable.ic_puma,
            R.drawable.ic_bear,
            R.drawable.ic_bull,
            R.drawable.ic_fox
        )
        const val VISIBLE_AMOUNT = 4f
    }

    init {
        generateGame()
    }

    fun test(bet: Long): Long {
        generateGame()
        val mult = getMult()
        val wined = bet * mult
        return wined.toLong()
    }

    fun roll(bet: Long, data: Data) {
        viewModelScope.launch {
            if (gameState == GameState.Idle) {
                generateGame()
                _total.value = data.total - bet

                gameState = GameState.Rolling
                launch { spinSlot(_slot1LD) }
                delay(300)
                launch { spinSlot(_slot2LD) }
                delay(300)
                launch { spinSlot(_slot3LD) }
                delay(300)
                spinSlot(_slot4LD)

                val multiplier = getMult()
                if (multiplier == 0f) {
                    if (data.total == 0L) {
                        _total.value = 5000L
                    }
                } else {
                    _total.value = (data.total + multiplier * bet + bet).toLong()
                    _win.value = (multiplier * bet).toLong()
                }

                gameState = GameState.Idle
            }
        }
    }

    private fun generateGame() {
        val slotList1 = mutableListOf<Slot>().apply {
            addAll(
                _slot1LD.value?.filter { it.pos in 0f..1f } ?: emptyList(),
            )
        }
        val slotList2 = mutableListOf<Slot>().apply {
            addAll(
                _slot2LD.value?.filter { it.pos in 0f..1f } ?: emptyList(),
            )
        }
        val slotList3 = mutableListOf<Slot>().apply {
            addAll(
                _slot3LD.value?.filter { it.pos in 0f..1f } ?: emptyList(),
            )
        }
        val slotList4 = mutableListOf<Slot>().apply {
            addAll(
                _slot4LD.value?.filter { it.pos in 0f..1f } ?: emptyList(),
            )
        }
        val offset = 1 / (VISIBLE_AMOUNT * 2)
        for (i in slotList1.size..48) {
            slotList1.add(
                Slot(
                    i,
                    drawableIdList.random(),
                    offset + i * offset * 2,
                ),
            )
            slotList2.add(
                Slot(
                    i,
                    drawableIdList.random(),
                    offset + i * offset * 2,
                ),
            )
            slotList3.add(
                Slot(
                    i,
                    drawableIdList.random(),
                    offset + i * offset * 2,
                ),
            )
            slotList4.add(
                Slot(
                    i,
                    drawableIdList.random(),
                    offset + i * offset * 2,
                ),
            )
        }
        for (i in 0..1) {
            slotList1.add(
                generate(
                    slotList1.last().slotId + 1,
                    slotList1.last().pos + 1 / VISIBLE_AMOUNT,
                ),
            )
            slotList2.add(
                generate(
                    slotList2.last().slotId + 1,
                    slotList2.last().pos + 1 / VISIBLE_AMOUNT,
                ),
            )
            slotList3.add(
                generate(
                    slotList3.last().slotId + 1,
                    slotList3.last().pos + 1 / VISIBLE_AMOUNT,
                ),
            )
            slotList4.add(
                generate(
                    slotList4.last().slotId + 1,
                    slotList4.last().pos + 1 / VISIBLE_AMOUNT,
                ),
            )
        }
        _slot1LD.value = slotList1
        _slot2LD.value = slotList2
        _slot3LD.value = slotList3
        _slot4LD.value = slotList4
    }

    private fun generate(id: Int, relativePosition: Float): Slot {
        val drawableRes = drawableIdList.random()
        return Slot(
            id,
            drawableRes,
            relativePosition,
        )
    }

    internal enum class GameState {
        Idle, Rolling
    }

    private fun getMult(): Float {
        val result = listOf(
            _slot1LD.value?.let { slots ->
                slots[slots.size - 3].drawableId
            } ?: return 0f,
            _slot2LD.value?.let { slots ->
                slots[slots.size - 3].drawableId
            } ?: return 0f,
            _slot3LD.value?.let { slots ->
                slots[slots.size - 3].drawableId
            } ?: return 0f,
            _slot4LD.value?.let { slots ->
                slots[slots.size - 3].drawableId
            } ?: return 0f,
            _slot1LD.value?.let { slots ->
                slots[slots.size - 2].drawableId
            } ?: return 0f,
            _slot2LD.value?.let { slots ->
                slots[slots.size - 2].drawableId
            } ?: return 0f,
            _slot3LD.value?.let { slots ->
                slots[slots.size - 2].drawableId
            } ?: return 0f,
            _slot4LD.value?.let { slots ->
                slots[slots.size - 2].drawableId
            } ?: return 0f
        )

        if (result.distinct().size == 1) {
            return if (result.contains(R.drawable.ic_temple)) {
                15f
            } else {
                12f
            }
        }

        if (result.distinct().size == 2) {
            return if (result.subList(0, 3).distinct().size == 1 &&
                result.subList(4, 7).distinct().size == 1
            ) {
                10f
            } else {
                7f
            }
        }


        if (result.distinct().size == 3) {
            return 6f
        }

        val twoGroups = result.chunked(2)
        if (twoGroups.size == 4 && twoGroups.all { it[0] == it[1] }) {
            return 5f
        }

        val threeGroups = result.chunked(3)
        if (threeGroups.all {
                if (it.size == 3) it[0] == it[1] && it[0] == it[2] else true
        }
                    ) {
            return 3f
        }

        if (result.distinct().size == 4) {
            return 1.25f
        }

        return 0f
    }

    private suspend fun spinSlot(ld: MutableLiveData<List<Slot>>) {
        val list = ld.value ?: return
        var pos =
            list.reversed().indexOfFirst { it.pos in 1 / VISIBLE_AMOUNT..2 / VISIBLE_AMOUNT }
        while (list.last().pos > 7 / (VISIBLE_AMOUNT * 2f) ) {
            delay(RATE)
            val posDif = getNewSpeed(pos) * RATE / 1000
            list.forEach { it.pos = it.pos - posDif }
            ld.value = list
            pos = list.reversed().indexOfFirst { slot ->
                slot.pos in 1 / VISIBLE_AMOUNT..2 / VISIBLE_AMOUNT
            }
        }
        val posAdjust = list.last().pos - 7 / (VISIBLE_AMOUNT * 2f)
        list.forEach { it.pos = it.pos - posAdjust }
        ld.value = list
    }

    private fun getNewSpeed(pos: Int): Float {
        return pos.toFloat().pow(1 / 2f)
    }
}
