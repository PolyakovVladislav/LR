package com.lucky.rush.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.lucky.rush.R
import com.lucky.rush.databinding.FragmentSettingsBinding
import com.lucky.rush.ui.core.ViewBindingFragment
import com.lucky.rush.ui.extensions.addOnBackPressedCallback
import com.lucky.rush.ui.extensions.navigateSafe
import com.lucky.rush.ui.extensions.setTextGradient
import com.lucky.rush.ui.utils.Data

class SettingsFragment : ViewBindingFragment<FragmentSettingsBinding>(
    FragmentSettingsBinding::inflate,
) {

    private val data by lazy {
        Data.getInstance(requireActivity())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {

            textViewMusic.setTextGradient(R.color.red, R.color.yellow)
            textViewVibration.setTextGradient(R.color.red, R.color.yellow)
            buttonResetScore.setTextGradient(R.color.red, R.color.yellow)

            progressBarMusic.progress = data.musicVolume
            progressBarVibration.progress = data.vibratingVolume

            progressBarMusic.setOnProgressListener {
                data.musicVolume = it
            }

            progressBarVibration.setOnProgressListener {
                data.vibratingVolume = it
            }

            buttonResetScore.setOnClickListener {
                data.total = 5000
            }
        }
        addOnBackPressedCallback {
            findNavController().navigateSafe(
                SettingsFragmentDirections.actionSettingsFragmentToMainFragment()
            )
        }
    }
}
