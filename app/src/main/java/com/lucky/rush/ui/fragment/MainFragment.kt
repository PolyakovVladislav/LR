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
            buttonPlay.setTextGradient(R.color.red, R.color.yellow)
            buttonSettings.setTextGradient(R.color.red, R.color.yellow)
            buttonExit.setTextGradient(R.color.red, R.color.yellow)
            buttonPrivacy.setTextGradient(R.color.red, R.color.yellow)

            val navController = findNavController()
            buttonPlay.setOnClickListener {

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
