package com.lucky.rush.ui.fragment.firstGame

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.lucky.rush.R
import com.lucky.rush.databinding.FragmentFirstGameBinding
import com.lucky.rush.ui.core.ViewBindingFragment
import com.lucky.rush.ui.extensions.addOnBackPressedCallback
import com.lucky.rush.ui.extensions.navigateSafe
import com.lucky.rush.ui.extensions.playWin
import com.lucky.rush.ui.extensions.setOrientationFull
import com.lucky.rush.ui.extensions.setOrientationPortrait
import com.lucky.rush.ui.extensions.setTextGradient
import com.lucky.rush.ui.extensions.vibr
import com.lucky.rush.ui.utils.Data

class FirstGameFragment : ViewBindingFragment<FragmentFirstGameBinding>(
    FragmentFirstGameBinding::inflate,
) {

    private val vm by viewModels<FirstGameViewModel>()
    private val data by lazy { Data.getInstance(requireActivity()) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setOrientationFull()

        addOnBackPressedCallback {
            findNavController().navigateSafe(
                FirstGameFragmentDirections.actionFirstGameFragmentToGamesFragment(),
            )
        }

        with(binding) {
            textViewBet.setTextGradient(R.color.red, R.color.yellow)
            buttonPlay.setTextGradient(R.color.red, R.color.yellow)

            scoreViewTotal.title = getString(R.string.total)
            scoreViewWin.title = getString(R.string.win)

            scoreViewTotal.score = data.total
            scoreViewWin.score = data.wingGameFirst
            textViewBet.text = data.betGameFirst.toString()

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
                data.betGameFirst = bet
                textViewBet.text = bet.toString()
            }

            imageViewIncrease.setOnClickListener {
                var bet = textViewBet.text.toString().toLong() + 100L
                if (bet > data.total) bet = data.total
                data.betGameFirst = bet
                textViewBet.text = bet.toString()
            }

            vm.win.observe(viewLifecycleOwner) { win ->
                if (data.wingGameFirst != win) {
                    playWin()
                    vibr()
                }
                data.wingGameFirst = win
                scoreViewWin.score = win
            }

            vm.total.observe(viewLifecycleOwner) { total ->
                data.total = total
                if (data.total <= textViewBet.text.toString().toLong()) {
                    textViewBet.text = data.total.toString()
                }
                scoreViewTotal.score = data.total
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
            binding.slotView1.update(it, FirstGameViewModel.VISIBLE_AMOUNT.toInt())
        }
        vm.slot2LiveData.observe(viewLifecycleOwner) {
            binding.slotView2.update(it, FirstGameViewModel.VISIBLE_AMOUNT.toInt())
        }
        vm.slot3LiveData.observe(viewLifecycleOwner) {
            binding.slotView3.update(it, FirstGameViewModel.VISIBLE_AMOUNT.toInt())
        }
        vm.slot4LiveData.observe(viewLifecycleOwner) {
            binding.slotView4.update(it, FirstGameViewModel.VISIBLE_AMOUNT.toInt())
        }
    }
}
