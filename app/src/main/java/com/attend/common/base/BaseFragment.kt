package com.attend.common.base

import android.os.Bundle
import androidx.fragment.app.Fragment

open class BaseFragment : Fragment() {
    protected var container: AppContainer? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        container = AppContainer.getInstance(requireContext())
    }

    override fun onStart() {
        super.onStart()
        container?.setProgress(requireContext())
    }
}