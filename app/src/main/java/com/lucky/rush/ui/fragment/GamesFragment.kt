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
            buttonPlay1.setTextGradient(R.color.red, R.color.yellow)
            buttonPlay2.setTextGradient(R.color.red, R.color.yellow)
            buttonBonusGame.setTextGradient(R.color.red, R.color.yellow)

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

            }
            buttonPlay2.setOnClickListener {
            }
            buttonBonusGame.setOnClickListener {
            }
        }
    }
}
