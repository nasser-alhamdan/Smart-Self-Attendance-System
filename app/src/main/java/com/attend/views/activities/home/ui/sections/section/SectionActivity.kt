package com.attend.views.activities.home.ui.sections.section

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.attend.BuildConfig
import com.attend.R
import com.attend.common.base.BaseActivity
import com.attend.data.models.sections.Section
import com.attend.databinding.ActivitySectionBinding

class SectionActivity : BaseActivity() {

    private lateinit var binding: ActivitySectionBinding
    private var sectionID: String? = ""
    private var section: Section? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySectionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "Add Section"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        sectionID = intent.getStringExtra("sectionID") ?: ""

        binding.apply {
            if (sectionID!!.isNotEmpty()) {
                getById(sectionID!!)
            }
        }

        binding.btnSave.setOnClickListener {
            if (container?.validator?.isEmpty(
                    binding.txtName,
                    "Please enter Section name"
                ) == true
                || container?.validator?.isEmpty(
                    binding.txtCode,
                    "Please enter Section Code"
                ) == true
            ) return@setOnClickListener

            val name: String = binding.txtName.text.toString()
            val code: String = binding.txtCode.text.toString()

            if (section != null) {
                section?.name = name
                section?.code = code
                updateSection(section!!)
            } else {
                section = Section(
                    name = name,
                    code = code
                )
                getSectionByCode(section!!)
            }
        }

        container?.up?.showToastMsg(
            this,
            "$sectionID --- ${intent.getBooleanExtra("updatedAccount", false)}"
        )
        if (!(intent.getBooleanExtra("updatedAccount", false) && sectionID != "")) {
            binding.btnDelete.visibility = View.GONE
        } else {
            binding.btnDelete.visibility = View.VISIBLE
            binding.btnDelete.setOnClickListener {
                AlertDialog.Builder(this)
                    .setTitle("Delete")
                    .setMessage("Are you sure?")
                    .setPositiveButton(R.string.yes) { _: DialogInterface, _: Int ->
                        deleteSection(sectionID!!)
                    }
                    .setNegativeButton(R.string.cancel) { _: DialogInterface, _: Int ->
                    }.show()
            }
        }
    }


    private fun getSectionByCode(data: Section) {
        container?.repository?.getSectionByCode(data.code) {
            if (it != null) {
                binding.txtCode.error = "This Section code already exists."
                binding.txtCode.requestFocus()
                section = null
            } else
                insertSection(data)
        }
    }

    private fun insertSection(data: Section) {
        container?.repository?.insertSection(data) {
            data.id = it
            updateSection(data)
        }
    }

    private fun updateSection(data: Section) {
        container?.repository?.updateSection(data) { isSaved ->
            if (isSaved) {
                close("Saved successfully")
            }
        }
    }

    private fun deleteSection(id: String) {
        container?.repository?.deleteSection(id) { isDeleted ->
            if (isDeleted) {
                close("Deleted successfully")
            }
        }
    }

    private fun close(string: String) {
        container?.repository?.hideProgress()
        Toast.makeText(this, string, Toast.LENGTH_SHORT).show()
        finish()
    }

    private fun getById(id: String) {
        container?.repository?.getSection(id) {
            section = it
            setData()
        }
    }

    private fun setData() {
        supportActionBar?.title = "Edit Section"
        section?.let {
            binding.txtName.setText(it.name)
            binding.txtCode.setText(it.code)
        }
    }
}