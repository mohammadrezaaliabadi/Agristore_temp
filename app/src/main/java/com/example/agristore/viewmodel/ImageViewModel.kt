package com.example.agristore.viewmodel

import androidx.lifecycle.*
import com.example.agristore.data.dao.ImageDao
import com.example.agristore.data.entities.Image
import kotlinx.coroutines.launch

class ImageViewModel(private val imageDao: ImageDao):ViewModel() {

    fun insertImage(image: Image){
        viewModelScope.launch {
            imageDao.insert(image)
        }
    }
    fun getImage(imageId:String): LiveData<Image> {
        return imageDao.getImage(imageId).asLiveData()
    }

    fun deleteImage(image: Image){
        viewModelScope.launch {
            imageDao.delete(image)
        }
    }
}
class ImageViewModelFactory(private val imageDao: ImageDao):ViewModelProvider.Factory{

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ImageViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ImageViewModel(imageDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}