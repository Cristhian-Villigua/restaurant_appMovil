package com.example.project_mobileapplicacion.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.project_mobileapplicacion.cloud.MainRepository
import com.example.project_mobileapplicacion.model.BannerModel
import com.example.project_mobileapplicacion.model.CategoryModel
import com.example.project_mobileapplicacion.model.ItemsModel

class MainViewModel: ViewModel() {
    private val repository = MainRepository()
    fun loadBanner(): LiveData<MutableList<BannerModel>>{
        return repository.loadBanner()
    }
    fun loadCategory(): LiveData<MutableList<CategoryModel>>{
        return repository.loadCategory()
    }
    fun loadPopular(): LiveData<MutableList<ItemsModel>>{
        return repository.loadPopular()
    }
    fun loadItems(categoryId: String): LiveData<MutableList<ItemsModel>>{
        return repository.loadItemCategory(categoryId)
    }
}