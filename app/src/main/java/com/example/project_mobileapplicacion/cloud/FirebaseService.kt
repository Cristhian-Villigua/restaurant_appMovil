package com.example.project_mobileapplicacion.cloud

import android.util.Log
import com.example.project_mobileapplicacion.model.OrderHistoryItem
import com.example.project_mobileapplicacion.model.UserEntity
import com.google.firebase.firestore.FirebaseFirestore

object FirebaseService {
    private val db = FirebaseFirestore.getInstance()

    fun checkEmailExists(email: String, callback: (Boolean) -> Unit) {
        db.collection("users")
            .whereEqualTo("email", email)
            .get()
            .addOnSuccessListener { documents ->
                callback(!documents.isEmpty)
            }
            .addOnFailureListener { e ->
                Log.e("FirebaseService", "Error checking email", e)
                callback(false)
            }
    }

    fun store(user: UserEntity){
        val data = hashMapOf(
            "name" to user.name,
            "lastname" to user.lastname,
            "birthday" to user.birthday,
            "phone" to user.phone,
            "email" to user.email,
            "password" to user.password,
            "photoBase64" to user.photoBase64,
            "roleId" to user.roleId
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
                    docId = doc.id,
                    name = doc.getString("name") ?: "",
                    lastname = doc.getString("lastname") ?: "",
                    birthday = doc.getString("birthday") ?: "",
                    phone = doc.getString("phone") ?: "",
                    email = doc.getString("email") ?: "",
                    password = doc.getString("password") ?: "",
                    photoBase64 = doc.getString("photoBase64") ?: "",
                    roleId = doc.getLong("roleId")?.toInt() ?: 0
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
                            docId = document.id,
                            name = document.getString("name") ?: "",
                            lastname = document.getString("lastname") ?: "",
                            birthday = document.getString("birthday") ?: "",
                            phone = document.getString("phone") ?: "",
                            email = document.getString("email") ?: "",
                            password = document.getString("password") ?: "",
                            photoBase64 = document.getString("photoBase64") ?: "",
                            roleId = document.getLong("roleId")?.toInt() ?: 0
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
        user.docId?.let {
            db.collection("users").document(it)
                .update(
                    "name", user.name,
                    "lastname", user.lastname,
                    "birthday", user.birthday,
                    "phone", user.phone,
                    "email", user.email,
                    "password", user.password,
                    "photoBase64", user.photoBase64,
                    "roleId", user.roleId
                ).addOnSuccessListener {
                    Log.d("FirebaseService", "User successfully updated")
                }.addOnFailureListener {
                    Log.e("FirebaseService", "Error updating user")
                }
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

    // Guardar orden en Firestore
    fun storeOrder(userId: String, orderText: String) {
        val data = hashMapOf(
            "userId" to userId,
            "orderText" to orderText,
            "timestamp" to System.currentTimeMillis()
        )

        db.collection("orders").add(data)
            .addOnFailureListener { e ->
                Log.e("FirebaseService", "Error saving order", e)
            }
    }

    // Cargar historial de órdenes por userId
    fun getOrdersByUserId(userId: String, callback: (List<OrderHistoryItem>) -> Unit) {
        db.collection("orders")
            .whereEqualTo("userId", userId)
            .get()
            .addOnSuccessListener { documents ->
                val historyList = documents.mapNotNull { doc ->
                    val timestamp = doc.getLong("timestamp") ?: return@mapNotNull null
                    val orderText = doc.getString("orderText") ?: return@mapNotNull null

                    val shortId = timestamp.toString().takeLast(5)

                    OrderHistoryItem(
                        id = shortId,
                        orderText = orderText,
                        timestamp = timestamp
                    )
                }.sortedByDescending { it.timestamp }

                callback(historyList)
            }
            .addOnFailureListener { e ->
                Log.e("FirebaseService", "Error getting orders", e)
                callback(emptyList())
            }
    }
}