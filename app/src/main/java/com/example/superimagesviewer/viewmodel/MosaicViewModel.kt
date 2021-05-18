package com.example.superimagesviewer.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import android.graphics.drawable.Drawable
import com.example.superimagesviewer.repository.MosaicRepository

class MosaicViewModel(application: Application) : AndroidViewModel(application) {

    val userName: LiveData<String>
    val mosaicsList: LiveData<List<Drawable>>
    private val mosaicRepository: MosaicRepository = MosaicRepository(application)

    init {
        mosaicsList = mosaicRepository.getMosaicsList()
        userName = mosaicRepository.getUserName()
    }

}