package com.lucky.rush.ui.fragment.thirdGame

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lucky.rush.R
import com.lucky.rush.ui.models.ThirdGameItem

class ThirdGameViewModel : ViewModel() {

    companion object {
        val trefa = R.drawable.ic_trefa
        val bubna = R.drawable.ic_bubna
        val pika = R.drawable.ic_pika
        val empty = 0
        private val list = listOf(
            trefa,
            bubna,
            pika,
            0,
        )
    }

    private val _total = MutableLiveData<Long>()
    val total: LiveData<Long> = _total

    private val _win = MutableLiveData<Long>()
    val win: LiveData<Long> = _win

    private val _items = MutableLiveData<List<ThirdGameItem>>()
    val items: LiveData<List<ThirdGameItem>> = _items

    private val _gameState = MutableLiveData(GameState.Idle)
    val gameState: LiveData<GameState> = _gameState

    private var bet = 0L

    private var internalGameState: GameState
        get() = _gameState.value ?: GameState.Finished
        set(value) {
            _gameState.value = value
        }

    fun play(bet: Long, balance: Long) {
        if (internalGameState == GameState.Finishing) {
            internalGameState = GameState.Finished
        }
        val win: Long
        val newTotal: Long
        when (internalGameState) {
            GameState.Finished -> {
                win = 0
                newTotal = balance - this.bet
            }
            GameState.Idle -> {
                win = -1
                newTotal = balance
            }
            else -> {
                win = getWin(this.bet)
                newTotal = balance + win
            }
        }
        Log.d("timb", "newTotal = $newTotal, win = $win")
        if (win != -1L) _win.value = win
        _total.value = newTotal
        generateItems()
        this.bet = bet
        internalGameState = GameState.Playing
    }

    enum class GameState {
        Finished, Playing, Idle, Finishing
    }

    private fun getWin(bet: Long): Long {
        var win = bet.toFloat()
        if (_items.value?.all { !it.appeared } == true) {
            return 0L
        }
        _items.value?.forEach { item ->
            if (item.appeared) {
                when (item.drawableId) {
                    empty -> return -bet
                    trefa -> win *= 1.25f
                    bubna -> win *= 1.5f
                    pika -> win *= 1.75f
                }
            }
        }
        return (win).toLong()
    }

    fun onItemClicked(item: ThirdGameItem) {
        if (internalGameState == GameState.Finished || internalGameState == GameState.Finishing) {
            return
        }
        val items = _items.value ?: return
        items.forEach { item ->
            item.animateAppearance = false
        }
        when (item.drawableId) {
            empty -> {
                if (item.appeared.not()) {
                    item.apply {
                        this.appeared = true
                        this.animateAppearance = true
                    }
                    _win.value = 0
                    internalGameState = GameState.Finishing
                }
            }
            else -> {
                if (item.appeared.not()) {
                    item.apply {
                        this.appeared = true
                        this.animateAppearance = true
                    }
                }
            }
        }
        _items.value = items
    }

    private fun generateItems() {
        _items.value = list.shuffled().map { drawableId ->
            ThirdGameItem(false, false, drawableId)
        }
    }
}
