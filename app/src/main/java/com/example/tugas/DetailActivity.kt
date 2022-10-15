package com.example.tugas

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView

class DetailActivity : AppCompatActivity() {

    lateinit var tvKodeResi: TextView
    lateinit var tvPenerima: TextView
    lateinit var tvKeterangan: TextView
    lateinit var tvBerat: TextView
    lateinit var tvAlamatPengirim: TextView
    lateinit var tvAlamatTujuan: TextView
    lateinit var tvNomorHP: TextView
    lateinit var tvStatus: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        tvKodeResi = findViewById(R.id.tvKodeResi)
        tvPenerima = findViewById(R.id.tvPenerimaDetail)
        tvKeterangan = findViewById(R.id.tvKeteranganDetail)
        tvBerat = findViewById(R.id.tvBeratDetail)
        tvAlamatPengirim = findViewById(R.id.tvAlamatPengirimDetail)
        tvAlamatTujuan = findViewById(R.id.tvAlamatTujuanDetail)
        tvNomorHP = findViewById(R.id.tvNomorHPDetail)
        tvStatus = findViewById(R.id.tvStatusDetail)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.option_menu, menu)
        if (menu != null) {
            menu.findItem(R.id.tambah).isVisible = false
            menu.findItem(R.id.simpan).isVisible = false
            menu.findItem(R.id.back).isVisible = true
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
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
}