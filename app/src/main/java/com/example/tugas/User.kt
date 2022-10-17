package com.example.tugas

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class User(
    val username:String,
    val name: String,
    val password: String,
    val nomortelepon: String,
    var dikirim: Int = 0,
    var terkirim: Int = 0,

    var counterProses: Int = 0,
    var counterTerkirim: Int = 0,
    var pengiriman: ArrayList<String> = ArrayList(),
    var penolakan: ArrayList<String> = ArrayList(),
):Parcelable {
    companion object {
        var listUser = ArrayList<User>()
    }
}