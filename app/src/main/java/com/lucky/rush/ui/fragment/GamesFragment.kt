package com.lucky.rush.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.lucky.rush.R
import com.lucky.rush.databinding.FragmentGamesBinding
import com.lucky.rush.ui.core.ViewBindingFragment
import com.lucky.rush.ui.extensions.addOnBackPressedCallback
import com.lucky.rush.ui.extensions.navigateSafe
import com.lucky.rush.ui.extensions.setTextGradient

class GamesFragment : ViewBindingFragment<FragmentGamesBinding>(
    FragmentGamesBinding::inflate,
) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            buttonPlay1.setTextGradient(R.color.orange, R.color.yellow_2)
            buttonPlay2.setTextGradient(R.color.orange, R.color.yellow_2)
            buttonBonusGame.setTextGradient(R.color.orange, R.color.yellow_2)

            val navController = findNavController()

            addOnBackPressedCallback {
                navController.navigateSafe(
                    GamesFragmentDirections.actionGamesFragmentToMainFragment()
                )
            }
            imageViewBack.setOnClickListener {
                navController.navigateSafe(
                    GamesFragmentDirections.actionGamesFragmentToMainFragment()
                )
            }
            buttonPlay1.setOnClickListener {
                navController.navigateSafe(
                    GamesFragmentDirections.actionGamesFragmentToFirstGameFragment()
                )
            }
            buttonPlay2.setOnClickListener {
                navController.navigateSafe(
                    GamesFragmentDirections.actionGamesFragmentToSecondGameFragment()
                )
            }
            buttonBonusGame.setOnClickListener {
            }
        }
    }
}
