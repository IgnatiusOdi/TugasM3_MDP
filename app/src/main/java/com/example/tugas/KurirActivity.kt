package com.example.tugas

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
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

    private var indexUser = 0
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

        indexUser = intent.getIntExtra("indexUser", 0)
        user = User.listUser[indexUser]

        tvNameKurir.text = "Hi, ${user.name} !"

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

        refreshPage()
        registerForContextMenu(tvSlot1)
        registerForContextMenu(tvSlot2)
        registerForContextMenu(tvSlot3)

        goToSearch = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            res: ActivityResult ->
            if (res.resultCode == RESULT_OK) {
                val data = res.data
                if (data != null) {
                    refreshPage()
                }
            } else if (res.resultCode == RESULT_CANCELED) {
                val data = res.data
                if (data != null) {
                    finish()
                }
            }
        }

        goToDetail = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                res: ActivityResult ->
            if (res.resultCode == RESULT_CANCELED) {
                val data = res.data
                if (data != null) {
                    finish()
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun refreshPage() {
        tvTotal.text = "${user.counterProses + user.counterTerkirim}"
        tvProses.text = "${user.counterProses}"
        tvTerkirim.text = "${user.counterTerkirim}"
        for (i in 0 until 3) {
            lateinit var data: Pengiriman
            var stringData: String
            var gravity: Int
            var indexData = -1
            if (user.pengiriman.size > i) {
                val nomorResi = user.pengiriman[i]
                for (kirim in Pengiriman.listPengiriman) {
                    if (nomorResi == kirim.nomorResi) {
                        data = kirim
                        indexData = Pengiriman.listPengiriman.indexOf(kirim)
                        break
                    }
                }
                stringData = "Kode : ${data.nomorResi}\n" +
                        "Alamat Pengirim : ${data.alamatPengirim}\n" +
                        "Alamat Penerima : ${data.alamatPenerima}\n" +
                        "Pengirim : ${data.namaPengirim}, Penerima : ${data.namaPenerima}\n" +
                        "Nomor Telepon : ${data.nomorPenerima}\n" +
                        "Keterangan : -"
                gravity = Gravity.START
            } else {
                stringData = "SLOT KOSONG"
                gravity = Gravity.CENTER
            }
            when (i) {
                0 -> {
                    tvSlot1.text = stringData
                    tvSlot1.gravity = gravity
                    if (indexData != -1) {
                        if (Pengiriman.listPengiriman[indexData].status == 1) {
                            tvSlot1.setBackgroundColor(resources.getColor(R.color.yellow))
                        } else if (Pengiriman.listPengiriman[indexData].status == 2) {
                            tvSlot1.setBackgroundColor(resources.getColor(R.color.green))
                        }
                    } else {
                        tvSlot1.setBackgroundColor(resources.getColor(R.color.gray))
                    }
                }
                1 -> {
                    tvSlot2.text = stringData
                    tvSlot2.gravity = gravity
                    if (indexData != -1) {
                        if (Pengiriman.listPengiriman[indexData].status == 1) {
                            tvSlot2.setBackgroundColor(resources.getColor(R.color.yellow))
                        } else if (Pengiriman.listPengiriman[indexData].status == 2) {
                            tvSlot2.setBackgroundColor(resources.getColor(R.color.green))
                        }
                    } else {
                        tvSlot2.setBackgroundColor(resources.getColor(R.color.gray))
                    }
                }
                else -> {
                    tvSlot3.text = stringData
                    tvSlot3.gravity = gravity
                    if (indexData != -1) {
                        if (Pengiriman.listPengiriman[indexData].status == 1) {
                            tvSlot3.setBackgroundColor(resources.getColor(R.color.yellow))
                        } else if (Pengiriman.listPengiriman[indexData].status == 2) {
                            tvSlot3.setBackgroundColor(resources.getColor(R.color.green))
                        }
                    } else {
                        tvSlot3.setBackgroundColor(resources.getColor(R.color.gray))
                    }
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

        val index = when (focusedContextView) {
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
                intent.putExtra("indexUser", indexUser)
                goToSearch.launch(intent)
                true
            }
            R.id.status->{
                // CHANGE STATUS
                if (item.title == "Finish") {
                    lateinit var nomorResi: String
                    when (focusedContextView) {
                        tvSlot1 -> {
                            nomorResi = user.pengiriman[0]
                        }
                        tvSlot2 -> {
                            nomorResi = user.pengiriman[1]
                        }
                        tvSlot3 -> {
                            nomorResi = user.pengiriman[2]
                        }
                    }
                    for (kiriman in Pengiriman.listPengiriman) {
                        if (kiriman.nomorResi == nomorResi) {
                            kiriman.status = 2
                            updatePengirim(nomorResi)
                            updateKurir()
                            break
                        }
                    }
                } else if (item.title == "Complete") {
                    when (focusedContextView) {
                        tvSlot1 -> {
                            user.pengiriman.removeAt(0)
                        }
                        tvSlot2 -> {
                            user.pengiriman.removeAt(1)
                        }
                        else -> {
                            user.pengiriman.removeAt(2)
                        }
                    }
                }

                //UPDATE DATA USER
                User.listUser[indexUser] = user

                refreshPage()
                true
            }
            R.id.detail->{
                val nomorResi = when (focusedContextView) {
                    tvSlot1 -> {
                        user.pengiriman[0]
                    }
                    tvSlot2 -> {
                        user.pengiriman[1]
                    }
                    else -> {
                        user.pengiriman[2]
                    }
                }
                val intent = Intent(this, DetailActivity::class.java)
                intent.putExtra("nomorResi", nomorResi)
                goToDetail.launch(intent)
                true
            }
            else -> {
                super.onContextItemSelected(item)
            }
        }
    }

    private fun updatePengirim(nomorResi: String) {
        lateinit var namaPengirim: String
        // CARI NAMA PENGIRIM
        for (kiriman in Pengiriman.listPengiriman) {
            if (nomorResi == kiriman.nomorResi) {
                namaPengirim = kiriman.namaPengirim
                break
            }
        }
        // EDIT TERKIRIM PENGIRIM
        for (pengirim in User.listUser) {
            if (pengirim.name == namaPengirim) {
                User.listUser[User.listUser.indexOf(pengirim)].dikirim -= 1
                User.listUser[User.listUser.indexOf(pengirim)].terkirim += 1
            }
        }
    }

    private fun updateKurir() {
        user.counterProses -= 1
        user.counterTerkirim += 1
    }
}