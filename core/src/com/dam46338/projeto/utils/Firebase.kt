package com.dam46338.projeto.utils

interface Firebase {
    fun registerUser(email: String, password: String)
    fun loginUser(email: String, password: String)
    fun getRegisterState(): Int
    fun getLoginState(): Int
    fun getCurrentUserEmail(): String?
    fun getCoins(): Int
    fun getItems(): ArrayList<String>
    fun setCoinsInDb(value: Int)
    fun addItemInDb(value: String)
    fun resetStates()
}