package com.example.tugas

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class User(
    val username:String,
    val name: String,
    val password: String,
    val nomortelepon: String,
):Parcelable {}