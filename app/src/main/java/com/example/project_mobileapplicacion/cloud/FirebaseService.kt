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
    fun getByEmail(email: String, callback: (UserEntity?) -> Unit) {
        db.collection("users")
            .whereEqualTo("email", email)
            .get()
            .addOnSuccessListener { documents ->
                if (documents.isEmpty) {
                    callback(null)
                } else {
                    for (document in documents) {
                        val user = UserEntity(
                            id = 0,
                            name = document.getString("name") ?: "",
                            lastname = document.getString("lastname") ?: "",
                            birthday = document.getString("birthday") ?: "",
                            phone = document.getString("phone") ?: "",
                            email = document.getString("email") ?: "",
                            password = document.getString("password") ?: ""
                        )
                        callback(user)
                    }
                }
            }
            .addOnFailureListener { exception ->
                callback(null)
            }
    }
    fun update(user: UserEntity) {
        db.collection("users").document(user.id.toString())
            .update(
                "name", user.name,
                "lastname", user.lastname,
                "birthday", user.birthday,
                "phone", user.phone,
                "email", user.email,
                "password", user.password
            ).addOnSuccessListener {
                Log.d("FirebaseService", "User successfully updated")
            }.addOnFailureListener {
                Log.e("FirebaseService", "Error updating user")
            }
    }
    fun delete(userId: String){
        db.collection("users").document(userId).delete()
            .addOnSuccessListener {
                Log.d("FirebaseService", "User successfully deleted")
            }.addOnFailureListener { e->
                Log.e("FirebaseService", "Error deleting user", e)
            }
    }
}