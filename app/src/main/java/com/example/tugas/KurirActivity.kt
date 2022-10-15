package com.example.tugas

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts

class KurirActivity : AppCompatActivity() {

    lateinit var tvNameKurir: TextView
    lateinit var tvSlot1: TextView
    lateinit var tvSlot2: TextView
    lateinit var tvSlot3: TextView
    lateinit var tvTotal: TextView
    lateinit var tvProses: TextView
    lateinit var tvTerkirim: TextView

    lateinit var goToSearch: ActivityResultLauncher<Intent>
    lateinit var goToDetail: ActivityResultLauncher<Intent>
    var focusedContextView: View? = null
    private lateinit var user: User

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kurir)

        tvNameKurir = findViewById(R.id.tvNameKurir)
        tvSlot1 = findViewById(R.id.tvSlot1)
        tvSlot2 = findViewById(R.id.tvSlot2)
        tvSlot3 = findViewById(R.id.tvSlot3)
        tvTotal = findViewById(R.id.tvTotalKurir)
        tvProses = findViewById(R.id.tvProsesKurir)
        tvTerkirim = findViewById(R.id.tvTerkirimKurir)

        user = intent.getParcelableExtra("user")!!

        tvNameKurir.text = "Hi, ${user.name} !"
        tvTotal.text = "${user.pengiriman.size}"
        tvProses.text = "0"
        tvTerkirim.text = "0"
        for (kirim in user.pengiriman) {
            for (listkirim in Pengiriman.listPengiriman) {
                if (kirim == listkirim.nomorResi) {
                    if (listkirim.status == 1) {
                        var jumlah = tvProses.text.toString().toInt()
                        tvProses.text = "${++jumlah}"
                    } else if (listkirim.status == 2) {
                        var jumlah = tvTerkirim.text.toString().toInt()
                        tvTerkirim.text = "${++jumlah}"
                    }
                    break
                }
            }
        }

        registerForContextMenu(tvSlot1)
        registerForContextMenu(tvSlot2)
        registerForContextMenu(tvSlot3)

        goToSearch = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            res: ActivityResult ->
            if (res.resultCode == RESULT_CANCELED) {
                val data = res.data
                if (data != null) {
                    val intent = Intent()
                    intent.putExtra("user", user)
                    setResult(RESULT_OK, intent)
                    finish()
                }
            }
        }

        goToDetail = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                res: ActivityResult ->
            if (res.resultCode == RESULT_CANCELED) {
                val data = res.data
                if (data != null) {
                    val intent = Intent()
                    intent.putExtra("user", user)
                    setResult(RESULT_OK, intent)
                    finish()
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.option_menu, menu)
        if (menu != null) {
            menu.findItem(R.id.tambah).isVisible = false
            menu.findItem(R.id.simpan).isVisible = false
            menu.findItem(R.id.back).isVisible = false
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.logout->{
                val intent = Intent()
                intent.putExtra("user", user)
                setResult(RESULT_OK, intent)
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        focusedContextView = v
        menuInflater.inflate(R.menu.context_menu, menu)

        var index = -1
        index = when (focusedContextView) {
            tvSlot1 -> {
                0
            }
            tvSlot2 -> {
                1
            }
            else -> {
                2
            }
        }

        if (user.pengiriman.size > index) {
            // CARI DI LIST PENGIRIMAN
            for (listKirim in Pengiriman.listPengiriman) {
                // CEK NOMOR RESI
                if (user.pengiriman[index] == listKirim.nomorResi) {
                    if (listKirim.status == 1) {
                        menu!!.findItem(R.id.search).isVisible = false
                        menu.findItem(R.id.status).isVisible = true
                        menu.findItem(R.id.status).title = "Finish"
                        menu.findItem(R.id.detail).isVisible = true
                    } else if (listKirim.status == 2) {
                        menu!!.findItem(R.id.search).isVisible = false
                        menu.findItem(R.id.status).isVisible = true
                        menu.findItem(R.id.status).title = "Complete"
                        menu.findItem(R.id.detail).isVisible = true
                    }
                }
            }
        } else {
            menu!!.findItem(R.id.search).isVisible = true
            menu.findItem(R.id.status).isVisible = false
            menu.findItem(R.id.detail).isVisible = false
        }
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.search->{
                val intent = Intent(this, SearchActivity::class.java)
                intent.putExtra("user", user)
                goToSearch.launch(intent)
                true
            }
            R.id.detail->{
                var paket: String
                if (focusedContextView == tvSlot1) {
                    Toast.makeText(this, "tvSlot1", Toast.LENGTH_SHORT).show()
                }
//                paket = when (focusedContextView) {
//                    tvSlot1 -> {
//                        user.pengiriman[0]
//                    }
//                    tvSlot2 -> {
//                        user.pengiriman[1]
//                    }
//                    else -> {
//                        user.pengiriman[2]
//                    }
//                }
//                Toast.makeText(this, paket, Toast.LENGTH_SHORT).show()
//                val intent = Intent(this, DetailActivity::class.java)
//                intent.putExtra("data", "")
//                goToDetail.launch(intent)
                true
            }
            else -> {
                super.onContextItemSelected(item)
            }
        }
    }
}