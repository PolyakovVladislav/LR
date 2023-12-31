package com.lucky.rush.ui.fragment.thirdGame

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.lucky.rush.R
import com.lucky.rush.databinding.FragmentThirdGameBinding
import com.lucky.rush.ui.core.VbFragment
import com.lucky.rush.ui.extensions.doOnBackPressed
import com.lucky.rush.ui.extensions.alpha
import com.lucky.rush.ui.extensions.safeNavigate
import com.lucky.rush.ui.extensions.revertAlpha
import com.lucky.rush.ui.extensions.applyGradientToText
import com.lucky.rush.ui.extensions.vibrator
import com.lucky.rush.ui.utils.Data
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ThirdGameFragment : VbFragment<FragmentThirdGameBinding>(
    FragmentThirdGameBinding::inflate,
) {

    private val vm by viewModels<ThirdGameViewModel>()
    private val data by lazy { Data.getInstance(requireActivity()) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        doOnBackPressed {
            findNavController().safeNavigate(
                ThirdGameFragmentDirections.actionThirdGameFragmentToGamesFragment()
            )
        }

        with(vb) {
            textViewBet.applyGradientToText(R.color.orange, R.color.yellow_2)
            buttonPlay.applyGradientToText(R.color.orange, R.color.yellow_2)

            textViewBet.text = data.betGameThird.toString()

            buttonPlay.setOnClickListener {
                val bet = textViewBet.text.toString().toLong()
                if (bet == 0L) return@setOnClickListener
                vm.play(
                    bet,
                    data.total,
                )
            }

            imageViewDecrease.setOnClickListener {
                var bet = textViewBet.text.toString().toLong() - 100L
                if (bet < 0) bet = 0
                data.betGameThird = bet
                textViewBet.text = bet.toString()
            }

            imageViewIncrease.setOnClickListener {
                var bet = textViewBet.text.toString().toLong() + 100L
                if (bet > data.total) bet = data.total
                data.betGameThird = bet
                textViewBet.text = bet.toString()
            }

            vm.total.observe(viewLifecycleOwner) { total ->
                if (total <= 0) {
                    data.total = 5000
                } else {
                    data.total = total
                }

            }

            vm.win.observe(viewLifecycleOwner) { win ->
                if (data.wingGameThird != win) {
                    vibrator()
                }
                data.wingGameThird = win
            }

            vm.items.observe(viewLifecycleOwner) { items ->
                items[0].run {
                    when {
                        appeared && animateAppearance -> {
                            if (drawableId != ThirdGameViewModel.empty) {
                                imageViewSlot1.setImageResource(drawableId)
                                imageViewSlot1.alpha()
                            } else {
                                imageViewSlot1.setImageDrawable(null)
                                vibrator(500)
                            }
                        }

                        appeared -> {
                            if (drawableId != ThirdGameViewModel.empty) {
                                imageViewSlot1.setImageResource(drawableId)
                            } else {
                                imageViewSlot1.setImageDrawable(null)
                            }
                        }

                        else -> imageViewSlot1.setImageResource(R.drawable.ic_secret)
                    }
                    imageViewSlot1.setOnClickListener {
                        vm.onItemClicked(this)
                    }
                }

                items[1].run {
                    when {
                        appeared && animateAppearance -> {
                            if (drawableId != ThirdGameViewModel.empty) {
                                imageViewSlot2.setImageResource(drawableId)
                                imageViewSlot2.alpha()
                            } else {
                                imageViewSlot2.setImageDrawable(null)
                                vibrator(500)
                            }
                        }

                        appeared -> {
                            if (drawableId != ThirdGameViewModel.empty) {
                                imageViewSlot2.setImageResource(drawableId)
                            } else {
                                imageViewSlot2.setImageDrawable(null)
                            }
                        }

                        else -> imageViewSlot2.setImageResource(R.drawable.ic_secret)
                    }
                    imageViewSlot2.setOnClickListener { vm.onItemClicked(this) }
                }

                items[2].run {
                    when {
                        appeared && animateAppearance -> {
                            if (drawableId != ThirdGameViewModel.empty) {
                                imageViewSlot3.setImageResource(drawableId)
                                imageViewSlot3.alpha()
                            } else {
                                imageViewSlot3.setImageDrawable(null)
                                vibrator(500)
                            }
                        }

                        appeared -> {
                            if (drawableId != ThirdGameViewModel.empty) {
                                imageViewSlot3.setImageResource(drawableId)
                            } else {
                                imageViewSlot3.setImageDrawable(null)
                            }
                        }

                        else -> imageViewSlot3.setImageResource(R.drawable.ic_secret)
                    }
                    imageViewSlot3.setOnClickListener { vm.onItemClicked(this) }
                }

                items[3].run {
                    when {
                        appeared && animateAppearance -> {
                            if (drawableId != ThirdGameViewModel.empty) {
                                imageViewSlot4.setImageResource(drawableId)
                                imageViewSlot4.alpha()
                            } else {
                                imageViewSlot4.setImageDrawable(null)
                                vibrator(500)
                            }
                        }

                        appeared -> {
                            if (drawableId != ThirdGameViewModel.empty) {
                                imageViewSlot4.setImageResource(drawableId)
                            } else {
                                imageViewSlot4.setImageDrawable(null)
                            }
                        }

                        else -> imageViewSlot4.setImageResource(R.drawable.ic_secret)
                    }
                    imageViewSlot4.setOnClickListener { vm.onItemClicked(this) }
                }
            }

            var finishJob: Job? = null
            vm.gameState.observe(viewLifecycleOwner) { state ->
                buttonPlay.setText(R.string.play)
                finishJob = lifecycleScope.launch {
                    when (state) {
                        ThirdGameViewModel.GameState.Finishing -> {
                            delay(2000)
                            imageViewSlot1.revertAlpha(200)
                            imageViewSlot2.revertAlpha(200)
                            imageViewSlot3.revertAlpha(200)
                            imageViewSlot4.revertAlpha(200)
                            delay(200)
                            imageViewSlot1.setImageResource(R.drawable.ic_secret)
                            imageViewSlot2.setImageResource(R.drawable.ic_secret)
                            imageViewSlot3.setImageResource(R.drawable.ic_secret)
                            imageViewSlot4.setImageResource(R.drawable.ic_secret)
                            imageViewSlot1.alpha(200)
                            imageViewSlot2.alpha(200)
                            imageViewSlot3.alpha(200)
                            imageViewSlot4.alpha(200)
                        }
                        ThirdGameViewModel.GameState.Idle -> {
                            containerSlot1.isVisible = false
                            containerSlot2.isVisible = false
                            containerSlot3.isVisible = false
                            containerSlot4.isVisible = false
                        }
                        ThirdGameViewModel.GameState.Finished -> {
                            finishJob?.cancel()
                            if (imageViewSlot1.alpha == 1f && imageViewSlot1.isVisible) {
                                return@launch
                            }
                            imageViewSlot1.alpha(200)
                            imageViewSlot2.alpha(200)
                            imageViewSlot3.alpha(200)
                            imageViewSlot4.alpha(200)
                        }
                        else -> {
                            buttonPlay.setText(R.string.claim)
                            if (containerSlot1.isVisible.not()) {
                                containerSlot1.alpha(300)
                                containerSlot2.alpha(300)
                                containerSlot3.alpha(300)
                                containerSlot4.alpha(300)
                            }
                        }
                    }
                }
            }
        }
    }
}
