package com.lucky.rush.ui.fragment

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.lucky.rush.R
import com.lucky.rush.databinding.FragmentSettingsBinding
import com.lucky.rush.ui.core.VbFragment
import com.lucky.rush.ui.extensions.doOnBackPressed
import com.lucky.rush.ui.extensions.safeNavigate
import com.lucky.rush.ui.extensions.applyGradientToText
import com.lucky.rush.ui.utils.Data

class SettingsFragment : VbFragment<FragmentSettingsBinding>(
    FragmentSettingsBinding::inflate,
) {

    private val data by lazy {
        Data.getInstance(requireActivity())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        doOnBackPressed {
            findNavController().safeNavigate(
                SettingsFragmentDirections.actionSettingsFragmentToMainFragment()
            )
        }

        with(vb) {
            progressBarMusic.setOnProgressListener {
                data.musicVolume = it
            }

            progressBarVibration.setOnProgressListener {
                data.vibratingVolume = it
            }

            buttonResetScore.setOnClickListener {
                data.total = 5000
                Toast.makeText(
                    requireContext(),
                    R.string.score_resettled,
                    Toast.LENGTH_SHORT,
                ).show()
            }

            textViewMusic.applyGradientToText(R.color.orange, R.color.yellow_2)
            textViewVibration.applyGradientToText(R.color.orange, R.color.yellow_2)
            buttonResetScore.applyGradientToText(R.color.orange, R.color.yellow_2)

            progressBarMusic.progress = data.musicVolume
            progressBarVibration.progress = data.vibratingVolume
        }
    }
}
