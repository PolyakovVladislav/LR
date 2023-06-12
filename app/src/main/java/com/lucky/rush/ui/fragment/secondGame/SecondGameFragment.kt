package com.lucky.rush.ui.fragment.secondGame

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.lucky.rush.R
import com.lucky.rush.databinding.FragmentSecondGameBinding
import com.lucky.rush.ui.core.ViewBindingFragment
import com.lucky.rush.ui.extensions.addOnBackPressedCallback
import com.lucky.rush.ui.extensions.navigateSafe
import com.lucky.rush.ui.extensions.playWin
import com.lucky.rush.ui.extensions.setOrientationFull
import com.lucky.rush.ui.extensions.setOrientationPortrait
import com.lucky.rush.ui.extensions.setTextGradient
import com.lucky.rush.ui.extensions.vibr
import com.lucky.rush.ui.utils.Data

class SecondGameFragment : ViewBindingFragment<FragmentSecondGameBinding>(
    FragmentSecondGameBinding::inflate,
) {

    private val vm by viewModels<SecondGameViewModel>()
    private val data by lazy { Data.getInstance(requireActivity()) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setOrientationFull()

        addOnBackPressedCallback {
            findNavController().navigateSafe(
                SecondGameFragmentDirections.actionSecondGameFragmentToGamesFragment(),
            )
        }

        with(binding) {
            textViewBet.setTextGradient(R.color.orange, R.color.yellow_2)
            buttonPlay.setTextGradient(R.color.orange, R.color.yellow_2)
            textViewTotal.setTextGradient(R.color.orange, R.color.yellow_2)
            textViewWin.setTextGradient(R.color.orange, R.color.yellow_2)
            textViewTotalTitle.setTextGradient(R.color.orange, R.color.yellow_2)
            textViewWinTitle.setTextGradient(R.color.orange, R.color.yellow_2)

            textViewTotal.text = data.total.toString()
            textViewWin.text = data.wingGameSecond.toString()

            textViewBet.text = data.betGameSecond.toString()

            buttonPlay.setOnClickListener {
                val bet = textViewBet.text.toString().toLong()
                if (bet == 0L) return@setOnClickListener
                vm.play(
                    bet,
                    data,
                )
            }

            imageViewDecrease.setOnClickListener {
                var bet = textViewBet.text.toString().toLong() - 100L
                if (bet < 0) bet = 0
                data.betGameSecond = bet
                textViewBet.text = bet.toString()
            }

            imageViewIncrease.setOnClickListener {
                var bet = textViewBet.text.toString().toLong() + 100L
                if (bet > data.total) bet = data.total
                data.betGameSecond = bet
                textViewBet.text = bet.toString()
            }

            vm.win.observe(viewLifecycleOwner) { win ->
                if (data.wingGameSecond != win) {
                    playWin()
                    vibr()
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
        }
    }

    override fun onStop() {
        super.onStop()
        setOrientationPortrait()
    }

    override fun onResume() {
        super.onResume()
        vm.slot1LiveData.observe(viewLifecycleOwner) {
            binding.slotView1.update(it, SecondGameViewModel.VISIBLE_AMOUNT.toInt())
        }
        vm.slot2LiveData.observe(viewLifecycleOwner) {
            binding.slotView2.update(it, SecondGameViewModel.VISIBLE_AMOUNT.toInt())
        }
        vm.slot3LiveData.observe(viewLifecycleOwner) {
            binding.slotView3.update(it, SecondGameViewModel.VISIBLE_AMOUNT.toInt())
        }
    }
}
