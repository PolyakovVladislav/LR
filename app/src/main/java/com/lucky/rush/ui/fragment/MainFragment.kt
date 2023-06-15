package com.lucky.rush.ui.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.lucky.rush.R
import com.lucky.rush.databinding.FragmentMainBinding
import com.lucky.rush.ui.core.VbFragment
import com.lucky.rush.ui.extensions.safeNavigate
import com.lucky.rush.ui.extensions.applyGradientToText

class MainFragment : VbFragment<FragmentMainBinding>(
    FragmentMainBinding::inflate,
) {

    val contoller by lazy { findNavController() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(vb) {
            buttonPlay.applyGradientToText(R.color.orange, R.color.yellow_2)
            buttonSettings.applyGradientToText(R.color.orange, R.color.yellow_2)
            buttonExit.applyGradientToText(R.color.orange, R.color.yellow_2)
            buttonPrivacy.applyGradientToText(R.color.orange, R.color.yellow_2)

            buttonPrivacy.setOnClickListener {
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/"))
                startActivity(browserIntent)
            }

            buttonPlay.setOnClickListener {
                contoller.safeNavigate(
                MainFragmentDirections.actionMainFragmentToGamesFragment()
                )
            }
            buttonSettings.setOnClickListener {
                contoller.safeNavigate(
                    MainFragmentDirections.actionMainFragmentToSettingsFragment()
                )
            }
            buttonExit.setOnClickListener {
                requireActivity().finish()
            }
        }
    }
}
