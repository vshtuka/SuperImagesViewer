package com.example.superimagesviewer.viewmodel

import android.app.Application
import android.graphics.drawable.Drawable
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.superimagesviewer.repository.MosaicRepository

class InstagramPostViewModel(application: Application) : AndroidViewModel(application) {

    private val mosaicRepository: MosaicRepository = MosaicRepository(application)

    fun uploadImageToInstagram(photoUrl: String){
        mosaicRepository.uploadPhotoToInstagramByUrl(photoUrl)
    }
}