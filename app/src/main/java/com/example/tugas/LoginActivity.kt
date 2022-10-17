package com.example.tugas

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts

class LoginActivity : AppCompatActivity() {

    lateinit var etUsername: EditText
    lateinit var etPassword: EditText
    lateinit var rbPengirim: RadioButton
    lateinit var rbKurir: RadioButton
    lateinit var btLogin: Button
    lateinit var btToRegister: Button

    private var indexUser = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        etUsername = findViewById(R.id.etUsernameLogin)
        etPassword = findViewById(R.id.etPasswordLogin)
        rbPengirim = findViewById(R.id.rbPengirim)
        rbKurir = findViewById(R.id.rbKurir)
        btLogin = findViewById(R.id.btLogin)
        btToRegister = findViewById(R.id.btToRegister)

        User.listUser.add(User("a", "a", "a", "12345678"))
        etUsername.setText("a")
        etPassword.setText("a")
        rbKurir.isChecked = true

        btLogin.setOnClickListener {
            indexUser = loginCheck()
            if (indexUser != -1) {
                if (rbPengirim.isChecked) {
                    // PENGIRIM
                    val intent = Intent(this, PengirimActivity::class.java)
                    intent.putExtra("indexUser", indexUser)
                    startActivity(intent)
                } else if (rbKurir.isChecked) {
                    // KURIR
                    val intent = Intent(this, KurirActivity::class.java)
                    intent.putExtra("indexUser", indexUser)
                    startActivity(intent)
                }
            }
        }

        btToRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun loginCheck(): Int {
        if (etUsername.text.isBlank() || etPassword.text.isBlank() || (!rbPengirim.isChecked && !rbKurir.isChecked)) {
            // FIELD KOSONG
            Toast.makeText(this, "Field tidak boleh kosong!", Toast.LENGTH_SHORT).show()
        } else {
            // CEK USER
            for (user in User.listUser) {
                if (user.username == etUsername.text.toString()) {
                    // CEK PASSWORD
                    if (user.password == etPassword.text.toString()) {
                        return User.listUser.indexOf(user)
                    } else {
                        Toast.makeText(this, "Password salah!", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            // USER TIDAK DITEMUKAN
            Toast.makeText(this, "User tidak ditemukan!", Toast.LENGTH_SHORT).show()
        }
        return -1
    }
}