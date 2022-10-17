package com.example.tugas

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.TextView
import android.widget.Toast

class SearchActivity : AppCompatActivity() {

    lateinit var tvSlot1: TextView
    lateinit var tvSlot2: TextView
    lateinit var tvSlot3: TextView
    lateinit var tvPage: TextView
    lateinit var btPrev: Button
    lateinit var btNext: Button

    var focusedContextView: View? = null
    var listKiriman = ArrayList<Pengiriman>()
    var totalPage = 1
    var pageNow = 1
    var namaPengirim = ""

    private var indexUser = 0
    private lateinit var user: User

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        tvSlot1 = findViewById(R.id.tvSlot1Search)
        tvSlot2 = findViewById(R.id.tvSlot2Search)
        tvSlot3 = findViewById(R.id.tvSlot3Search)
        tvPage = findViewById(R.id.tvPage)
        btPrev = findViewById(R.id.btPrev)
        btNext = findViewById(R.id.btNext)

        indexUser = intent.getIntExtra("indexUser", 0)
        user = User.listUser[indexUser]

        loadPengiriman()
        refreshPage()

        registerForContextMenu(tvSlot1)
        registerForContextMenu(tvSlot2)
        registerForContextMenu(tvSlot3)

        btPrev.setOnClickListener {
            if (pageNow > 1) {
                pageNow--
                refreshPage()
            }
        }

        btNext.setOnClickListener {
            if (pageNow < totalPage) {
                pageNow++
                refreshPage()
            }
        }
    }

    private fun loadPengiriman() {
        listKiriman = ArrayList()
        for (kirim in Pengiriman.listPengiriman) {
            // CEK STATUS 0
            if (kirim.status == 0) {
                // CEK PENOLAKAN
                var tambahkan = true
                for (penolakan in user.penolakan) {
                    if (penolakan == kirim.nomorResi) {
                        tambahkan = false
                        break
                    }
                }
                if (tambahkan) {
                    listKiriman.add(kirim)
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun refreshPage() {
        // TOTAL PAGE
        totalPage = 1
        totalPage += listKiriman.size / 3
        if (listKiriman.size % 3 == 0 && listKiriman.size != 0) {
            // KELIPATAN 3
            totalPage--
        }
        tvPage.text = "${pageNow}/${totalPage}"

        // PAGINATION
        for (i in 0 until 3) {
            // EMPTY SLOT
            var stringData = "SLOT KOSONG"
            var gravity = Gravity.CENTER

            // NOT EMPTY
            if (listKiriman.size > (pageNow-1)*3 + i) {
                val data = listKiriman[(pageNow-1)*3 + i]
                stringData = "Kode : ${data.nomorResi}\n" +
                        "Alamat Pengirim : ${data.alamatPengirim}\n" +
                        "Alamat Penerima : ${data.alamatPenerima}\n" +
                        "Pengirim : ${data.namaPengirim}, Penerima : ${data.namaPenerima}\n" +
                        "Nomor Telepon : ${data.nomorPenerima}\n" +
                        "Keterangan : -"
                gravity = Gravity.START

            }
            when (i) {
                0 -> {
                    tvSlot1.text = stringData
                    tvSlot1.gravity = gravity
                }
                1 -> {
                    tvSlot2.text = stringData
                    tvSlot2.gravity = gravity
                }
                else -> {
                    tvSlot3.text = stringData
                    tvSlot3.gravity = gravity
                }
            }
        }
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
        when (item.itemId) {
            R.id.back->{
                setResult(RESULT_OK, Intent())
                finish()
            }
            R.id.logout->{
                setResult(RESULT_CANCELED, Intent())
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
        menuInflater.inflate(R.menu.search_menu, menu)

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

        if (listKiriman.size > (pageNow-1)*3 + index) {
            menu!!.findItem(R.id.ambil).isVisible = true
            menu.findItem(R.id.tolak).isVisible = true
        } else {
            menu!!.findItem(R.id.ambil).isVisible = false
            menu.findItem(R.id.tolak).isVisible = false
        }
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.ambil -> {
                when (focusedContextView) {
                    tvSlot1 -> {
                        user.pengiriman.add(listKiriman[(pageNow-1)*3].nomorResi)
                    }
                    tvSlot2 -> {
                        user.pengiriman.add(listKiriman[(pageNow-1)*3 + 1].nomorResi)
                    }
                    else -> {
                        user.pengiriman.add(listKiriman[(pageNow-1)*3 + 2].nomorResi)
                    }
                }
                val nomorResi = user.pengiriman[user.pengiriman.size - 1]

                // UPDATE LIST PENGIRIMAN
                updateListPengiriman(nomorResi)
                updatePengirim()
                updateKurir()

                //UPDATE DATA USER
                User.listUser[indexUser] = user

                loadPengiriman()
                refreshPage()
                true
            }
            R.id.tolak -> {
                when (focusedContextView) {
                    tvSlot1 -> {
                        user.penolakan.add(listKiriman[(pageNow-1)*3].nomorResi)
                    }
                    tvSlot2 -> {
                        user.penolakan.add(listKiriman[(pageNow-1)*3 + 1].nomorResi)
                    }
                    else -> {
                        user.penolakan.add(listKiriman[(pageNow-1)*3 + 2].nomorResi)
                    }
                }

                //UPDATE DATA USER
                User.listUser[indexUser] = user

                loadPengiriman()
                refreshPage()
                true
            }
            else -> {
                super.onContextItemSelected(item)
            }
        }
    }

    private fun updateListPengiriman(nomorResi: String) {
        for (listKirim in Pengiriman.listPengiriman) {
            if (listKirim.status == 0) {
                if (listKirim.nomorResi == nomorResi) {
                    val idx = Pengiriman.listPengiriman.indexOf(listKirim)
                    Pengiriman.listPengiriman[idx].kurir = user.name
                    Pengiriman.listPengiriman[idx].status = 1
                    namaPengirim = Pengiriman.listPengiriman[idx].namaPengirim
                    break
                }
            }
        }
    }

    private fun updatePengirim() {
        for (pengirim in User.listUser) {
            if (pengirim.name == namaPengirim) {
                User.listUser[User.listUser.indexOf(pengirim)].dikirim += 1
                break
            }
        }
    }

    private fun updateKurir() {
        user.counterProses += 1
    }
}