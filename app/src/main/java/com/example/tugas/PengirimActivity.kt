package com.example.tugas

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity

class PengirimActivity : AppCompatActivity() {

    lateinit var tvName: TextView
    lateinit var tvTotal: TextView
    lateinit var tvDikirim: TextView
    lateinit var tvTerkirim: TextView
    lateinit var etNomorResi: EditText
    lateinit var tvKurir: TextView
    lateinit var tvKeterangan: TextView
    lateinit var tvBerat: TextView
    lateinit var tvAlamatTujuan: TextView
    lateinit var tvNomorHP: TextView
    lateinit var tvStatus: TextView
    lateinit var btCheck: Button

    private lateinit var goToPengirimanBaru: ActivityResultLauncher<Intent>
    private var indexUser = 0

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pengirim)

        tvName = findViewById(R.id.tvNamePengirim)
        tvTotal = findViewById(R.id.tvTotalPengirim)
        tvDikirim = findViewById(R.id.tvDikirimPengirim)
        tvTerkirim = findViewById(R.id.tvTerkirimPengirim)
        etNomorResi = findViewById(R.id.etNomorResi)
        tvKurir = findViewById(R.id.tvKurir)
        tvKeterangan = findViewById(R.id.tvKeterangan)
        tvBerat = findViewById(R.id.tvBerat)
        tvAlamatTujuan = findViewById(R.id.tvAlamatTujuan)
        tvNomorHP = findViewById(R.id.tvNomorHP)
        tvStatus = findViewById(R.id.tvStatus)
        btCheck = findViewById(R.id.btCheck)

        indexUser = intent.getIntExtra("indexUser", 0)
        val user = User.listUser[indexUser]

        tvName.text = "Hi, ${user.name} !"
        tvTotal.text = "${user.dikirim + user.terkirim}"
        tvDikirim.text = "${user.dikirim}"
        tvTerkirim.text = "${user.terkirim}"

        goToPengirimanBaru = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            res: ActivityResult ->
            if (res.resultCode == RESULT_CANCELED) {
                val data = res.data
                if (data != null) {
                    finish()
                }
            }
        }

        btCheck.setOnClickListener {
            var indexResi = -1
            for (kiriman in Pengiriman.listPengiriman) {
                if (kiriman.nomorResi == etNomorResi.text.toString() && kiriman.namaPengirim == user.name) {
                    indexResi = Pengiriman.listPengiriman.indexOf(kiriman)
                    tvKurir.text = "Kurir : ${kiriman.kurir}"
                    tvKeterangan.text = kiriman.keterangan
                    tvBerat.text = "Berat : ${kiriman.berat}"
                    tvAlamatTujuan.text = kiriman.alamatPenerima
                    tvNomorHP.text = "Nomor HP : ${kiriman.nomorPenerima}"
                    when (kiriman.status) {
                        1 -> {
                            tvStatus.text = "Status : Sedang dikirim"
                        }
                        2 -> {
                            tvStatus.text = "Status : Terkirim"
                        }
                        else -> {
                            tvStatus.text = "Status : -"
                        }
                    }
                }
            }
            
            if (indexResi == -1) {
                Toast.makeText(this, "Resi tidak ditemukan!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.option_menu, menu)
        if (menu != null) {
            menu.findItem(R.id.tambah).isVisible = true
            menu.findItem(R.id.simpan).isVisible = false
            menu.findItem(R.id.back).isVisible = false
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.tambah->{
                val intent = Intent(this, PengirimanBaruActivity::class.java)
                intent.putExtra("indexUser", indexUser)
                goToPengirimanBaru.launch(intent)
            }
            R.id.logout->{
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}