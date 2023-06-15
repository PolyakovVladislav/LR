package com.lucky.rush.ui.fragment.secondGame

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.lucky.rush.R
import com.lucky.rush.databinding.FragmentSecondGameBinding
import com.lucky.rush.ui.core.VbFragment
import com.lucky.rush.ui.extensions.doOnBackPressed
import com.lucky.rush.ui.extensions.safeNavigate
import com.lucky.rush.ui.extensions.playWinSound
import com.lucky.rush.ui.extensions.setOrientationFull
import com.lucky.rush.ui.extensions.setOrientationPortrait
import com.lucky.rush.ui.extensions.applyGradientToText
import com.lucky.rush.ui.extensions.vibrator
import com.lucky.rush.ui.utils.Data

class SecondGameFragment : VbFragment<FragmentSecondGameBinding>(
    FragmentSecondGameBinding::inflate,
) {

    private val vm by viewModels<SecondGameViewModel>()
    private val data by lazy { Data.getInstance(requireActivity()) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setOrientationFull()

        doOnBackPressed {
            findNavController().safeNavigate(
                SecondGameFragmentDirections.actionSecondGameFragmentToGamesFragment(),
            )
        }

        with(vb) {
            textViewBet.applyGradientToText(R.color.orange, R.color.yellow_2)
            buttonPlay.applyGradientToText(R.color.orange, R.color.yellow_2)
            textViewTotal.applyGradientToText(R.color.orange, R.color.yellow_2)
            textViewWin.applyGradientToText(R.color.orange, R.color.yellow_2)
            textViewTotalTitle.applyGradientToText(R.color.orange, R.color.yellow_2)
            textViewWinTitle.applyGradientToText(R.color.orange, R.color.yellow_2)

            textViewTotal.text = data.total.toString()
            textViewWin.text = data.wingGameSecond.toString()

            textViewBet.text = data.betGameSecond.toString()


            vm.win.observe(viewLifecycleOwner) { win ->
                if (data.wingGameSecond != win) {
                    playWinSound()
                    vibrator()
                }
                data.wingGameSecond = win
                textViewWin.text = win.toString()
            }

            vm.total.observe(viewLifecycleOwner) { total ->
                data.total = total
                if (data.total <= textViewBet.text.toString().toLong()) {
                    textViewBet.text = data.total.toString()
                }
                textViewTotal.text = total.toString()
            }

            imageViewIncrease.setOnClickListener {
                var bet = textViewBet.text.toString().toLong() + 100L
                if (bet > data.total) bet = data.total
                data.betGameSecond = bet
                textViewBet.text = bet.toString()
            }

            imageViewDecrease.setOnClickListener {
                var bet = textViewBet.text.toString().toLong() - 100L
                if (bet < 0) bet = 0
                data.betGameSecond = bet
                textViewBet.text = bet.toString()
            }

            buttonPlay.setOnClickListener {
                val bet = textViewBet.text.toString().toLong()
                if (bet == 0L) return@setOnClickListener
                vm.roll(
                    bet,
                    data,
                )
            }
        }
    }

    override fun onStop() {
        super.onStop()
        setOrientationPortrait()
    }

    override fun onResume() {
        super.onResume()
        vm.slot1LD.observe(viewLifecycleOwner) {
            vb.slotView1.submitChange(it, SecondGameViewModel.VISIBLE_AMOUNT.toInt())
        }
        vm.slot2LD.observe(viewLifecycleOwner) {
            vb.slotView2.submitChange(it, SecondGameViewModel.VISIBLE_AMOUNT.toInt())
        }
        vm.slot3LD.observe(viewLifecycleOwner) {
            vb.slotView3.submitChange(it, SecondGameViewModel.VISIBLE_AMOUNT.toInt())
        }
    }
}
