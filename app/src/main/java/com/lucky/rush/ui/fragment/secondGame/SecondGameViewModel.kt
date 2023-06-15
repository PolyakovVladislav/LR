package com.lucky.rush.ui.fragment.secondGame

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

class SecondGameViewModel : ViewModel() {

    private var gameState = GameState.Idle

    private val _total = MutableLiveData<Long>()
    val total: LiveData<Long> = _total

    private val _win = MutableLiveData<Long>()
    val win: LiveData<Long> = _win

    private val _slot1LD = MutableLiveData<List<Slot>>()
    val slot1LD: LiveData<List<Slot>> = _slot1LD

    private val _slot2LD = MutableLiveData<List<Slot>>()
    val slot2LD: LiveData<List<Slot>> = _slot2LD

    private val _slot3LD = MutableLiveData<List<Slot>>()
    val slot3LD: LiveData<List<Slot>> = _slot3LD

    companion object {
        private const val RATE = 20L
        private val drawableIdList = listOf(
            R.drawable.ic_a,
            R.drawable.ic_k,
            R.drawable.ic_q,
            R.drawable.ic_j,
            R.drawable.ic_flower
        )
        private val combVariants = mutableListOf<IntArray>().apply {
            for (i in 0..5) {
                add(intArrayOf(drawableIdList[0], drawableIdList[0], drawableIdList[0]))
                add(intArrayOf(drawableIdList[1], drawableIdList[1], drawableIdList[1]))
            }
            for (i in 0..3) {
                add(intArrayOf(drawableIdList[2], drawableIdList[2], drawableIdList[2]))
                add(intArrayOf(drawableIdList[3], drawableIdList[3], drawableIdList[3]))
            }
            for (i in 0..1) {
                add(intArrayOf(drawableIdList[4], drawableIdList[4], drawableIdList[4]))
            }
            for (i in 0..100) {
                add(intArrayOf(drawableIdList.random(), drawableIdList.random(), drawableIdList.random()))
            }
        }
        const val VISIBLE_AMOUNT = 3f
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
                spinSlot(_slot3LD)

                val mult = getMult()
                if (mult == 0f) {
                    if (data.total == 0L) {
                        _total.value = 5000L
                    }
                } else {
                    _win.value = (mult * bet).toLong()
                    _total.value = (data.total + mult * bet + bet).toLong()
                }

                gameState = GameState.Idle
            }
        }
    }

    internal enum class GameState {
        Idle, Rolling
    }

    private fun generateGame() {
        val slots1 = mutableListOf<Slot>().apply {
            addAll(
                _slot1LD.value?.filter { it.pos in 0f..1f } ?: emptyList(),
            )
        }
        val slots2 = mutableListOf<Slot>().apply {
            addAll(
                _slot2LD.value?.filter { it.pos in 0f..1f } ?: emptyList(),
            )
        }
        val slots3 = mutableListOf<Slot>().apply {
            addAll(
                _slot3LD.value?.filter { it.pos in 0f..1f } ?: emptyList(),
            )
        }

        val offset = 1 / (VISIBLE_AMOUNT * 2)
        val variant = combVariants.random()
        for (i in slots1.size..48) {
            slots1.add(
                Slot(
                    i,
                    drawableIdList.random(),
                    offset + i * offset * 2,
                ),
            )
            slots2.add(
                Slot(
                    i,
                    drawableIdList.random(),
                    offset + i * offset * 2,
                ),
            )
            slots3.add(
                Slot(
                    i,
                    drawableIdList.random(),
                    offset + i * offset * 2,
                ),
            )
        }
        for (i in 0..1) {
            slots1.add(
                Slot(
                    slots1.last().slotId + 1,
                    variant[0],
                    slots1.last().pos + 1 / VISIBLE_AMOUNT,
                ),
            )
            slots2.add(
                Slot(
                    slots2.last().slotId + 1,
                    variant[1],
                    slots2.last().pos + 1 / VISIBLE_AMOUNT,
                ),
            )
            slots3.add(
                Slot(
                    slots3.last().slotId + 1,
                    variant[2],
                    slots3.last().pos + 1 / VISIBLE_AMOUNT,
                ),
            )
        }
        _slot1LD.value = slots1
        _slot2LD.value = slots2
        _slot3LD.value = slots3
    }

    private suspend fun spinSlot(ld: MutableLiveData<List<Slot>>) {
        val list = ld.value ?: return
        var pos =
            list.reversed().indexOfFirst { it.pos in 1 / VISIBLE_AMOUNT..2 / VISIBLE_AMOUNT }
        while (list.last().pos > 7 / (VISIBLE_AMOUNT * 2f) ) {
            delay(RATE)
            val posChange = getNewSpeed(pos) * RATE / 1000
            list.forEach { it.pos = it.pos - posChange }
            ld.value = list
            pos = list.reversed().indexOfFirst { slot ->
                slot.pos in 1 / VISIBLE_AMOUNT..2 / VISIBLE_AMOUNT
            }
        }
        val posAdjust = list.last().pos - 7 / (VISIBLE_AMOUNT * 2f)
        list.forEach { it.pos = it.pos - posAdjust }
        ld.value = list
    }

    private fun getNewSpeed(currentPosition: Int): Float {
        return currentPosition.toFloat().pow(1 / 2f)
    }

    private fun getMult(): Float {
        val result = listOf(
            _slot1LD.value?.let { slots ->
                slots[slots.size - 2].drawableId
            } ?: return 0f,
            _slot2LD.value?.let { slots ->
                slots[slots.size - 2].drawableId
            } ?: return 0f,
            _slot3LD.value?.let { slots ->
                slots[slots.size - 2].drawableId
            } ?: return 0f,
        )

        if (result.distinct().size == 1) {
            return when (result.first()) {
                drawableIdList[0] -> 3f
                drawableIdList[1] -> 3f
                drawableIdList[2] -> 6f
                drawableIdList[3] -> 6f
                else -> 9f
            }
        }

        return 0f
    }
}
