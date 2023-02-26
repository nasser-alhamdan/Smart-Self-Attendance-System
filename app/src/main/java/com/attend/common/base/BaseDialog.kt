package com.attend.common.base

import android.os.Bundle
import androidx.fragment.app.DialogFragment

class BaseDialog : DialogFragment() {
    protected var container: AppContainer? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //        setStyle(DialogFragment.STYLE_NORMAL, R.style.floatingBottomSheetDialog);
        container = AppContainer.getInstance(requireContext())
    }
}