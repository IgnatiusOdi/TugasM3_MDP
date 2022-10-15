package com.example.tugas

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.TextView

class SearchActivity : AppCompatActivity() {

    lateinit var tvSlot1: TextView
    lateinit var tvSlot2: TextView
    lateinit var tvSlot3: TextView
    lateinit var tvPage: TextView
    lateinit var btPrev: Button
    lateinit var btNext: Button

    var focusedContextView: View? = null
    lateinit var user: User
    var totalPage: Int = 1
    var pageNow: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        tvSlot1 = findViewById(R.id.tvSlot1Search)
        tvSlot2 = findViewById(R.id.tvSlot2Search)
        tvSlot3 = findViewById(R.id.tvSlot3Search)
        tvPage = findViewById(R.id.tvPage)
        btPrev = findViewById(R.id.btPrev)
        btNext = findViewById(R.id.btNext)

        user = intent.getParcelableExtra("user")!!

        registerForContextMenu(tvSlot1)
        registerForContextMenu(tvSlot2)
        registerForContextMenu(tvSlot3)

        refreshPage()
    }

    private fun refreshPage() {
        // TOTAL PAGE
        totalPage += Pengiriman.listPengiriman.size / 3
        if (Pengiriman.listPengiriman.size % 3 == 0) {
            // KELIPATAN 3
            totalPage--
        }

        // FILL SLOT
        for (i in 0 until 3) {

        }

        // COUNT PENGIRIMAN NO STATUS
        var countNoStatus = 0
        for (kirim in Pengiriman.listPengiriman) {
            if (kirim.status == 0) {
                countNoStatus++
            }
        }

        pageNow += countNoStatus / 3
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

    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        focusedContextView = v
        menuInflater.inflate(R.menu.search_menu, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.ambil -> {
                user.pengiriman.add(focusedContext.nomorResi)
                true
            }
            R.id.tolak -> {
//                user.penolakan.add(focusedContext.nomorResi)
                true
            }
            else -> {
                super.onContextItemSelected(item)
            }
        }
    }
}