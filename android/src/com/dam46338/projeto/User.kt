package com.dam46338.projeto

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class User(val email: String? = null, val moedas: Int = 0, val item: ArrayList<String>? = null)