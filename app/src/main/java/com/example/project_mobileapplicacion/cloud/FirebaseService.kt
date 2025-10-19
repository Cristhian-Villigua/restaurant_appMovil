package com.example.project_mobileapplicacion.cloud

import android.util.Log
import com.example.project_mobileapplicacion.model.UserEntity
import com.google.firebase.firestore.FirebaseFirestore

object FirebaseService {
    private val db = FirebaseFirestore.getInstance()

    fun store(user: UserEntity){
        val data = hashMapOf(
            "name" to user.name,
            "lastname" to user.lastname,
            "birthday" to user.birthday,
            "phone" to user.phone,
            "email" to user.email,
            "password" to user.password
        )
        db.collection("users").add(data).addOnSuccessListener {
            Log.d("FirebaseService","User successfully saved to Firebase")
        }.addOnFailureListener {
            Log.e("FirebaseService","Error saving user")
        }
    }

    fun index(callback: (List<UserEntity>)-> Unit){
        db.collection("users").get().addOnSuccessListener { result->
            val list = result.map { doc->
                UserEntity(
                    id = 0,
                    name = doc.getString("name") ?: "",
                    lastname = doc.getString("lastname") ?: "",
                    birthday = doc.getString("birthday") ?: "",
                    phone = doc.getString("phone") ?: "",
                    email = doc.getString("email") ?: "",
                    password = doc.getString("password") ?: ""
                )
            }
            callback(list)
        }.addOnFailureListener { e->
            Log.e("FirebaseService", "Error getting user", e)
            callback(emptyList())
        }
    }
}