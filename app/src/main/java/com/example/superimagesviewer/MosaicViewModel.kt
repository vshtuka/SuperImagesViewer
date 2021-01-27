package com.example.superimagesviewer

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import android.graphics.drawable.Drawable

class MosaicViewModel(application: Application) : AndroidViewModel(application) {
    val mosaicsList: LiveData<List<Drawable>>
    private val mosaicRepository: MosaicRepository = MosaicRepository(application)

    init {
        mosaicsList = mosaicRepository.getMosaicsList()
    }
}