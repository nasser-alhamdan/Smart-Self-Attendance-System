package com.attend.common.base

import android.os.Bundle
import com.attend.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

open class BaseBottomDialog : BottomSheetDialogFragment() {
    protected var container: AppContainer? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.floatingBottomSheetDialog)
        container = AppContainer.getInstance(requireContext())
    }
}