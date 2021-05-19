package com.example.superimagesviewer.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import android.graphics.drawable.Drawable
import com.example.superimagesviewer.Settings
import com.example.superimagesviewer.repository.MosaicRepository

class MosaicViewModel(application: Application) : AndroidViewModel(application) {

    val mosaicsList: LiveData<List<Drawable>>
    private val mosaicRepository: MosaicRepository = MosaicRepository(application)

    init {
        mosaicsList = mosaicRepository.getMosaicsList()
        refreshReferences(application)
    }

    private fun refreshReferences(application: Application) {
        var userName = Settings.getUserName(application)
        if (userName == null) {
            userName = mosaicRepository.getUserName()
            Settings.setUserName(application, userName)
        } else if (userName != mosaicRepository.getUserName()) {
            Settings.setUserName(application, mosaicRepository.getUserName())
        }
    }

}