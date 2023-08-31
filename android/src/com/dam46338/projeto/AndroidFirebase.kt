package com.dam46338.projeto

import android.util.Log
import com.dam46338.projeto.utils.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue

class AndroidFirebase : Firebase {
    private val database: FirebaseDatabase
    private var reference: DatabaseReference
    private val auth: FirebaseAuth
    private var registerState: Int = -1
    private var loginState: Int = -1
    private var coins: Int = 0
    private var items: ArrayList<String> = arrayListOf()
    private val tag: String = "Logger"
    private val url: String = "https://cute-little-devil-default-rtdb.europe-west1.firebasedatabase.app"

    init {
        database = FirebaseDatabase.getInstance(url)
        reference = database.getReference("utilizador")
        auth = FirebaseAuth.getInstance()
    }

    override fun registerUser(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            registerState = if (task.isSuccessful) {
                reference.child(auth.currentUser!!.uid).setValue(User(email, 0, arrayListOf("Pink Monster")))
                1
            } else 0
        }
    }

    override fun loginUser(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            loginState = if (task.isSuccessful) {
                val coinsRef = reference.child(auth.currentUser!!.uid).child("moedas")
                coinsRef.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        coins = dataSnapshot.getValue<Long>()!!.toInt()
                    }

                    override fun onCancelled(error: DatabaseError) {
                    }
                })

                val itemsRef = reference.child(auth.currentUser!!.uid).child("item")
                itemsRef.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        items.clear()
                        for (postSnapshot in dataSnapshot.children) {
                            val item = postSnapshot.getValue<String>()!!
                            items.add(item)
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                    }
                })

                1
            } else 0
        }
    }

    override fun getRegisterState(): Int {
        return registerState
    }

    override fun getLoginState(): Int {
        return loginState
    }

    override fun getCurrentUserEmail(): String? {
        return auth.currentUser?.email
    }

    override fun getCoins(): Int {
        return if (loginState == 1) coins else 0
    }

    override fun getItems(): ArrayList<String> {
        return if (loginState == 1) items else arrayListOf()
    }

    override fun setCoinsInDb(value: Int) {
        if (loginState == 1) {
            val coinsRef = reference.child(auth.currentUser!!.uid).child("moedas")
            coinsRef.setValue(value)
            coins = value
        }
    }

    override fun addItemInDb(value: String) {
        if (loginState == 1) {
            items.add(value)
            val itemsRef = reference.child(auth.currentUser!!.uid).child("item")
            itemsRef.setValue(items)
        }
    }

    override fun resetStates() {
        registerState = -1
        loginState = -1
    }
}