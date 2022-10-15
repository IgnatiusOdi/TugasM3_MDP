package com.example.tugas

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.Toast
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class PengirimanBaruActivity : AppCompatActivity() {

    lateinit var etAlamatPengirim: EditText
    lateinit var etAlamatPenerima: EditText
    lateinit var etNamaPenerima: EditText
    lateinit var etNomorPenerima: EditText
    lateinit var etBerat: EditText
    lateinit var etKeterangan: EditText

    private lateinit var user: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pengiriman_baru)

        etAlamatPengirim = findViewById(R.id.etAlamatPengirim)
        etAlamatPenerima = findViewById(R.id.etAlamatPenerima)
        etNamaPenerima = findViewById(R.id.etNamaPenerima)
        etNomorPenerima = findViewById(R.id.etNomorPenerima)
        etBerat = findViewById(R.id.etBerat)
        etKeterangan = findViewById(R.id.etKeterangan)

        user = intent.getParcelableExtra("user")!!
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.option_menu, menu)
        if (menu != null) {
            menu.findItem(R.id.tambah).isVisible = false
            menu.findItem(R.id.simpan).isVisible = true
            menu.findItem(R.id.back).isVisible = true
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.simpan->{
                if (etAlamatPengirim.text.isBlank() || etAlamatPenerima.text.isBlank() || etNamaPenerima.text.isBlank() || etNomorPenerima.text.isBlank() || etBerat.text.isBlank() || etKeterangan.text.isBlank()) {
                    Toast.makeText(this, "Field tidak boleh kosong!", Toast.LENGTH_SHORT).show()
                } else {
                    val nomorResi = generateNomorResi()
                    Toast.makeText(this, nomorResi, Toast.LENGTH_SHORT).show()
                    Pengiriman.listPengiriman.add(Pengiriman(nomorResi, etAlamatPengirim.text.toString(), etAlamatPenerima.text.toString(), user.name, etNamaPenerima.text.toString(), etNomorPenerima.text.toString(), etBerat.text.toString().toDouble(), etKeterangan.text.toString()))
                    finish()
                }
            }
            R.id.back->{
                finish()
            }
            R.id.logout->{
                val intent = Intent()
                setResult(RESULT_CANCELED, intent)
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun generateNomorResi(): String {
        val now = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyyMMdd")
        val formatted = now.format(formatter)

        var jumlahResi = 1
        var nomorResi = "${user.name[0].uppercaseChar()}" + "${etNamaPenerima.text.toString()[0].uppercaseChar()}" + "$formatted"

        for (kiriman in Pengiriman.listPengiriman) {
            if (kiriman.nomorResi.contains(nomorResi)) {
                jumlahResi++
            }
        }

        for (i in 0 until 4 - jumlahResi.toString().length) {
            nomorResi += "0"
        }

        nomorResi += jumlahResi.toString()

        return nomorResi
    }
}