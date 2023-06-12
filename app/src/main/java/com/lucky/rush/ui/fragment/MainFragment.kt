package com.lucky.rush.ui.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.lucky.rush.R
import com.lucky.rush.databinding.FragmentMainBinding
import com.lucky.rush.ui.core.ViewBindingFragment
import com.lucky.rush.ui.extensions.navigateSafe
import com.lucky.rush.ui.extensions.setTextGradient

class MainFragment : ViewBindingFragment<FragmentMainBinding>(
    FragmentMainBinding::inflate,
) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            buttonPlay.setTextGradient(R.color.orange, R.color.yellow_2)
            buttonSettings.setTextGradient(R.color.orange, R.color.yellow_2)
            buttonExit.setTextGradient(R.color.orange, R.color.yellow_2)
            buttonPrivacy.setTextGradient(R.color.orange, R.color.yellow_2)

            val navController = findNavController()

            buttonPlay.setOnClickListener {
                navController.navigateSafe(
                MainFragmentDirections.actionMainFragmentToGamesFragment()
                )
            }
            buttonSettings.setOnClickListener {
                navController.navigateSafe(
                    MainFragmentDirections.actionMainFragmentToSettingsFragment()
                )
            }
            buttonExit.setOnClickListener {
                requireActivity().finish()
            }
            buttonPrivacy.setOnClickListener {
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/"))
                startActivity(browserIntent)
            }
        }
    }
}
