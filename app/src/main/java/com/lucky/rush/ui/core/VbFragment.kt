package com.lucky.rush.ui.core

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

abstract class VbFragment<VB: ViewBinding>(
    private val bindingInflater: (layoutInflater: LayoutInflater) -> VB
) : Fragment() {

    private var _vb: VB? = null
    val vb: VB
        get() = checkNotNull(_vb)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _vb = bindingInflater(inflater)
        return vb.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _vb = null
    }
}
