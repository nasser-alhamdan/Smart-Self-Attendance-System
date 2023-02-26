package com.attend.views.activities.home.ui.users.admin

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.attend.BuildConfig
import com.attend.R
import com.attend.common.base.BaseActivity
import com.attend.common.util.PasswordValidator
import com.attend.data.models.users.Admin
import com.attend.databinding.ActivityAdminBinding

class AdminActivity : BaseActivity() {

    private lateinit var binding: ActivityAdminBinding
    private var adminID: String? = ""
    private var admin: Admin? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "Add Admin"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        adminID = intent.getStringExtra("adminID") ?: ""

        binding.apply {
            if (adminID!!.isNotEmpty()) {
                getById(adminID!!)
                binding.checkboxPassword.visibility = View.VISIBLE
                binding.checkboxPassword.setOnCheckedChangeListener { _, isChecked ->
                    binding.inputPassword.visibility = if (isChecked) View.VISIBLE else View.GONE
                    binding.inputConfirmPassword.visibility =
                        if (isChecked) View.VISIBLE else View.GONE
                }
                binding.inputPassword.visibility = View.GONE
                binding.inputConfirmPassword.visibility = View.GONE

            } else {
                binding.checkboxPassword.visibility = View.GONE
                binding.txtPassword.visibility = View.VISIBLE
                binding.inputConfirmPassword.visibility = View.VISIBLE
            }
        }

        binding.btnSave.setOnClickListener {
            if (container?.validator?.isEmpty(binding.txtName, "Please enter your name") == true
                || container?.validator?.isEmpty(
                    binding.txtPhone,
                    "Please enter your phone"
                ) == true
                || container?.validator?.isPhoneNotValid(
                    binding.txtPhone,
                    "Please enter valid phone"
                ) == true
            ) return@setOnClickListener

            val name: String = binding.txtName.text.toString()
            val phone: String = binding.txtPhone.text.toString()
            val password: String = binding.txtPassword.text.toString()

            if (binding.checkboxPassword.isChecked || adminID == "") {
                if (container?.validator?.isEmpty(
                        binding.txtPassword,
                        "Please enter your password"
                    ) == true
                )
                    return@setOnClickListener

                if (!PasswordValidator.isValid(password)) {
                    binding.txtPassword.error =
                        "Password must be 8 characters and contain Capital and Small letters, Numbers and Special Characters!"
                    binding.txtPassword.requestFocus()
                    return@setOnClickListener
                }

                if (container?.validator?.isNotEqual(
                        binding.txtConfirmPassword,
                        password,
                        "Password not equals."
                    ) == true
                ) return@setOnClickListener
            }

            if (admin != null) {
                admin?.name = name
                admin?.phone = phone
                if (binding.checkboxPassword.isChecked)
                    admin?.password = password

                container?.repository?.getAdminByPhone(admin?.phone!!) {
                    if (it != null) {
                        if (it.phone == admin?.phone) {
                            updateAdmin(admin!!)
                        } else {
                            binding.txtPhone.error = "This Phone number already exists."
                            binding.txtPhone.requestFocus()
                            admin = null
                        }
                    } else
                        updateAdmin(admin!!)
                }
            } else {
                admin = Admin(
                    name = name,
                    phone = phone,
                    password = password
                )
                getAdminByPhone(admin!!)
            }
        }

        container?.up?.showToastMsg(
            this,
            "$adminID --- ${intent.getBooleanExtra("updatedAccount", false)}"
        )
        if (!(intent.getBooleanExtra("updatedAccount", false) && adminID != "")) {
            binding.btnDelete.visibility = View.GONE
        } else {
            binding.btnDelete.visibility = View.VISIBLE
            binding.btnDelete.setOnClickListener {
                AlertDialog.Builder(this)
                    .setTitle("Delete")
                    .setMessage("Are you sure?")
                    .setPositiveButton(R.string.yes) { _: DialogInterface, _: Int ->
                        deleteAdmin(adminID!!)
                    }
                    .setNegativeButton(R.string.cancel) { _: DialogInterface, _: Int ->
                    }.show()
            }
        }
    }


    private fun getAdminByPhone(data: Admin) {
        container?.repository?.getAdminByPhone(data.phone) {
            if (it != null) {
                binding.txtPhone.error = "This Phone number already exists."
                binding.txtPhone.requestFocus()
                admin = null
            } else
                addAdmin(data)
        }
    }

    private fun addAdmin(data: Admin) {
        container?.repository?.insertAdmin(data) {
            data.id = it
            updateAdmin(data)
        }
    }

    private fun updateAdmin(data: Admin) {
        container?.repository?.updateAdmin(data) { isSaved ->
            if (isSaved) {
                container?.up?.saveUser(data)
                close("Saved successfully")
            }
        }
    }

    private fun deleteAdmin(id: String) {
        container?.repository?.deleteAdmin(id) { isDeleted ->
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
        container?.repository?.getAdmin(id) {
            admin = it
            setData()
        }
    }

    private fun setData() {
        supportActionBar?.title = "Edit Account"
        admin?.let {
            binding.txtName.setText(it.name)
            binding.txtPhone.setText(it.phone)
        }
    }
}