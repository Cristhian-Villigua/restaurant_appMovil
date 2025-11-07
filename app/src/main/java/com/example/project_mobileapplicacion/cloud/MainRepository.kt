package com.example.project_mobileapplicacion.cloud

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.project_mobileapplicacion.model.BannerModel
import com.example.project_mobileapplicacion.model.CategoryModel
import com.example.project_mobileapplicacion.model.ItemsModel
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.Query


class MainRepository {
    private val db = FirebaseDatabase.getInstance()
    fun loadBanner(): LiveData<MutableList<BannerModel>> {
        val listData = MutableLiveData<MutableList<BannerModel>>()
        val ref = db.getReference("Banner")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val bannerList = mutableListOf<BannerModel>()
                for (data in snapshot.children) {
                    val banner = data.getValue(BannerModel::class.java)
                    banner?.let {
                        bannerList.add(it)
                    }
                }
                listData.value = bannerList
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
        return listData
    }
    fun loadCategory(): LiveData<MutableList<CategoryModel>> {
        val listData = MutableLiveData<MutableList<CategoryModel>>()
        val ref = db.getReference("Category")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val bannerList = mutableListOf<CategoryModel>()
                for (data in snapshot.children) {
                    val banner = data.getValue(CategoryModel::class.java)
                    banner?.let {
                        bannerList.add(it)
                    }
                }
                listData.value = bannerList
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
        return listData
    }
    fun loadPopular(): LiveData<MutableList<ItemsModel>> {
        val listData = MutableLiveData<MutableList<ItemsModel>>()
        val ref = db.getReference("Popular")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val bannerList = mutableListOf<ItemsModel>()
                for (data in snapshot.children) {
                    val banner = data.getValue(ItemsModel::class.java)
                    banner?.let {
                        bannerList.add(it)
                    }
                }
                listData.value = bannerList
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
        return listData
    }
    fun loadItemCategory(categoryId: String): LiveData<MutableList<ItemsModel>> {
        val itemsLiveData = MutableLiveData<MutableList<ItemsModel>>()
        val ref = db.getReference("Items")
        val query: Query = ref.orderByChild("categoryId").equalTo(categoryId)
        query.addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = mutableListOf<ItemsModel>()
                for (data in snapshot.children) {
                    val item = data.getValue(ItemsModel::class.java)
                    item?.let {
                        list.add(it)
                    }
                }
                itemsLiveData.value = list
            }

            override fun onCancelled(p0: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
        return itemsLiveData
    }
}
