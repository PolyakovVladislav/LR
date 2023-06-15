package com.lucky.rush.ui.fragment.firstGame

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.lucky.rush.R
import com.lucky.rush.databinding.FragmentFirstGameBinding
import com.lucky.rush.ui.core.VbFragment
import com.lucky.rush.ui.extensions.doOnBackPressed
import com.lucky.rush.ui.extensions.safeNavigate
import com.lucky.rush.ui.extensions.playWinSound
import com.lucky.rush.ui.extensions.setOrientationFull
import com.lucky.rush.ui.extensions.setOrientationPortrait
import com.lucky.rush.ui.extensions.applyGradientToText
import com.lucky.rush.ui.extensions.vibrator
import com.lucky.rush.ui.utils.Data

class FirstGameFragment : VbFragment<FragmentFirstGameBinding>(
    FragmentFirstGameBinding::inflate,
) {

    private val vm by viewModels<FirstGameViewModel>()
    private val data by lazy { Data.getInstance(requireActivity()) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setOrientationFull()

        doOnBackPressed {
            findNavController().safeNavigate(
                FirstGameFragmentDirections.actionFirstGameFragmentToGamesFragment(),
            )
        }

        with(vb) {
            textViewBet.applyGradientToText(R.color.orange, R.color.yellow_2)
            buttonPlay.applyGradientToText(R.color.orange, R.color.yellow_2)

            scoreViewTotal.title = getString(R.string.total)
            scoreViewWin.title = getString(R.string.win)

            scoreViewTotal.score = data.total
            scoreViewWin.score = data.wingGameFirst
            textViewBet.text = data.betGameFirst.toString()

            vm.win.observe(viewLifecycleOwner) { win ->
                if (data.wingGameFirst != win) {
                    playWinSound()
                    vibrator()
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

            imageViewIncrease.setOnClickListener {
                var bet = textViewBet.text.toString().toLong() + 100L
                if (bet > data.total) bet = data.total
                data.betGameFirst = bet
                textViewBet.text = bet.toString()
            }

            imageViewDecrease.setOnClickListener {
                var bet = textViewBet.text.toString().toLong() - 100L
                if (bet < 0) bet = 0
                data.betGameFirst = bet
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
            vb.slotView1.submitChange(it, FirstGameViewModel.VISIBLE_AMOUNT.toInt())
        }
        vm.slot2LD.observe(viewLifecycleOwner) {
            vb.slotView2.submitChange(it, FirstGameViewModel.VISIBLE_AMOUNT.toInt())
        }
        vm.slot3LD.observe(viewLifecycleOwner) {
            vb.slotView3.submitChange(it, FirstGameViewModel.VISIBLE_AMOUNT.toInt())
        }
        vm.slot4LD.observe(viewLifecycleOwner) {
            vb.slotView4.submitChange(it, FirstGameViewModel.VISIBLE_AMOUNT.toInt())
        }
    }
}
