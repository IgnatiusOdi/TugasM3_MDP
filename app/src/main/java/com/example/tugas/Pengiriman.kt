package com.example.tugas

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class Pengiriman (
    val nomorResi: String,
    val alamatPengirim: String,
    val alamatPenerima: String,
    val namaPengirim: String,
    val namaPenerima: String,
    val nomorPenerima: String,
    val berat: Double,
    val keterangan: String,
    var kurir: String = "-",
    var status: Int = 0,
): Parcelable {
    companion object {
        var listPengiriman = ArrayList<Pengiriman>()
    }
}