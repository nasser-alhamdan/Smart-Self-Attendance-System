package com.attend.views.activities.home.ui.qr

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ContextThemeWrapper
import androidx.fragment.app.FragmentManager
import com.attend.common.base.BaseBottomDialog
import com.attend.common.qr.QRGContents
import com.attend.common.qr.QRGEncoder
import com.attend.databinding.FragmentQrBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

fun BottomSheetDialogFragment.fragmentManager(mContext: Context): FragmentManager? {
    return when (mContext) {
        is AppCompatActivity -> mContext.supportFragmentManager
        is ContextThemeWrapper -> fragmentManager(mContext.baseContext)
        else -> null
    }
}

class QRFragment : BaseBottomDialog() {

    private var _binding: FragmentQrBinding? = null

    // This property is only valid between onCreateView and  onDestroyView.
    private val binding get() = _binding!!


    var title: String? = ""
    var id: String? = ""
    lateinit var callback: (Boolean) -> Unit

    companion object {
        fun view(context: Context, title: String, id: String, callback: (Boolean) -> Unit) {
            val dialog = QRFragment()
            dialog.title = title
            dialog.id = id
            dialog.callback = callback
            dialog.fragmentManager(context)?.let { dialog.show(it, null) }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentQrBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            txtTitle.text = title

            val qrgEncoder = QRGEncoder(id, null, QRGContents.Type.TEXT, 1000)
            barcodeImage.setImageBitmap(qrgEncoder.bitmap)

            btnOk.setOnClickListener {
                dismiss()
                callback(true)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}