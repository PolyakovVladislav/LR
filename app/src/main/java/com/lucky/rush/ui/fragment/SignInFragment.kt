package com.lucky.rush.ui.fragment

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.lucky.rush.R
import com.lucky.rush.databinding.FragmentSignInBinding
import com.lucky.rush.ui.core.VbFragment
import com.lucky.rush.ui.extensions.safeNavigate
import com.lucky.rush.ui.extensions.applyGradientToText
import com.lucky.rush.ui.utils.Data

class SignInFragment : VbFragment<FragmentSignInBinding>(
    FragmentSignInBinding::inflate,
) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(vb) {

            cardViewCheck.setOnClickListener {
                imageViewCheck.isVisible = imageViewCheck.isVisible.not()
            }

            buttonPlay.setOnClickListener {
                if (imageViewCheck.isVisible) {
                    goMain()
                } else if (editText.text?.matches(Regex("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}\$")) == true) {
                    Data.getInstance(requireActivity()).isUserSigned = true
                    goMain()
                } else if (editText.text?.matches(Regex("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}\$")) == false) {
                    Toast.makeText(
                        requireContext(),
                        R.string.email_is_not_valid,
                        Toast.LENGTH_SHORT,
                    ).show()
                } else {
                    Toast.makeText(
                        requireContext(),
                        R.string.please_choose_sign_in_option,
                        Toast.LENGTH_SHORT,
                    ).show()
                }
            }
            buttonPlay.applyGradientToText(R.color.orange, R.color.yellow_2)
        }
    }

    private fun goMain() {
        findNavController().safeNavigate(
            SignInFragmentDirections.actionLoginFragmentToMainFragment()
        )
    }
}
