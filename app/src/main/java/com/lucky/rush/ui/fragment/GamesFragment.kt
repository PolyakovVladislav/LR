package com.lucky.rush.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.lucky.rush.R
import com.lucky.rush.databinding.FragmentGamesBinding
import com.lucky.rush.ui.core.VbFragment
import com.lucky.rush.ui.extensions.doOnBackPressed
import com.lucky.rush.ui.extensions.safeNavigate
import com.lucky.rush.ui.extensions.applyGradientToText

class GamesFragment : VbFragment<FragmentGamesBinding>(
    FragmentGamesBinding::inflate,
) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(vb) {
            val contoller = findNavController()
            doOnBackPressed {
                contoller.safeNavigate(
                    GamesFragmentDirections.actionGamesFragmentToMainFragment()
                )
            }

            buttonPlay1.applyGradientToText(R.color.orange, R.color.yellow_2)
            buttonPlay2.applyGradientToText(R.color.orange, R.color.yellow_2)
            buttonBonusGame.applyGradientToText(R.color.orange, R.color.yellow_2)

            imageViewBack.setOnClickListener {
                contoller.safeNavigate(
                    GamesFragmentDirections.actionGamesFragmentToMainFragment()
                )
            }
            buttonPlay1.setOnClickListener {
                contoller.safeNavigate(
                    GamesFragmentDirections.actionGamesFragmentToFirstGameFragment()
                )
            }
            buttonPlay2.setOnClickListener {
                contoller.safeNavigate(
                    GamesFragmentDirections.actionGamesFragmentToSecondGameFragment()
                )
            }
            buttonBonusGame.setOnClickListener {
                contoller.safeNavigate(
                    GamesFragmentDirections.actionGamesFragmentToThirdGameFragment()
                )
            }
        }
    }
}
