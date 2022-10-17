package com.example.tugas

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class RegisterActivity : AppCompatActivity() {

    lateinit var etName: EditText
    lateinit var etUsername: EditText
    lateinit var etPassword: EditText
    lateinit var etConfirm: EditText
    lateinit var etNomorTelepon: EditText
    lateinit var btRegister: Button
    lateinit var btToLogin: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        etName = findViewById(R.id.etNameRegister)
        etUsername = findViewById(R.id.etUsernameRegister)
        etPassword = findViewById(R.id.etPasswordRegister)
        etConfirm = findViewById(R.id.etConfirmRegister)
        etNomorTelepon = findViewById(R.id.etNomorTeleponRegister)
        btRegister = findViewById(R.id.btRegister)
        btToLogin = findViewById(R.id.btToLogin)

        btRegister.setOnClickListener {
            if (registerCheck()) {
                val user = User(etName.text.toString(), etUsername.text.toString(), etPassword.text.toString(), etNomorTelepon.text.toString())
                User.listUser.add(user)
                Toast.makeText(this, "Berhasil register!", Toast.LENGTH_SHORT).show()
                etName.setText("")
                etUsername.setText("")
                etPassword.setText("")
                etConfirm.setText("")
                etNomorTelepon.setText("")
            }
        }

        btToLogin.setOnClickListener {
            finish()
        }
    }

    private fun registerCheck(): Boolean {
        if (etName.text.isBlank() || etUsername.text.isBlank() || etPassword.text.isBlank() || etConfirm.text.isBlank() || etNomorTelepon.text.isBlank()) {
            // FIELD KOSONG
            Toast.makeText(this, "Field tidak boleh kosong!", Toast.LENGTH_SHORT).show()
            return false
        } else if (etPassword.text.toString() != etConfirm.text.toString()) {
            // PASSWORD != KONFIRMASI
            Toast.makeText(this, "Password dan konfirmasi tidak sama!", Toast.LENGTH_SHORT).show()
            return false
        } else {
            // USERNAME KEMBAR
            for (user in User.listUser) {
                if (user.username == etUsername.text.toString()) {
                    Toast.makeText(this, "Username tidak boleh kembar!", Toast.LENGTH_SHORT).show()
                    return false
                }
            }
        }

        return true
    }
}