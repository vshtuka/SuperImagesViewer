package com.example.superimagesviewer.viewmodel

import android.app.Application
import android.graphics.drawable.Drawable
import androidx.lifecycle.*
import com.example.superimagesviewer.Settings
import com.example.superimagesviewer.repository.MosaicRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MosaicViewModel(application: Application) : AndroidViewModel(application) {

    public val drawablesList: LiveData<List<Drawable>>
    public val userName: LiveData<String>
    public val isProgressVisible: LiveData<Boolean>
    private val mosaicRepository: MosaicRepository = MosaicRepository()

    init {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                if (mosaicRepository.isNetworkConnected(application)) {
                    val userName = Settings.getUserName(application)
                    if (null == userName) {
                        val newUserName = mosaicRepository.loadInstagramUserName()
                        mosaicRepository.instagramUserName.postValue(newUserName)
                        Settings.setUserName(application, newUserName)
                    } else {
                        mosaicRepository.instagramUserName.postValue(userName!!)
                        val newUserName = mosaicRepository.loadInstagramUserName()
                        if (userName != newUserName) {
                            mosaicRepository.instagramUserName.postValue(newUserName)
                            Settings.setUserName(application, newUserName)
                        }
                    }
                    mosaicRepository.loadImagesFromInstagram(application)
                    val imagesList = mosaicRepository.getInstagramImageList()
                    imagesList.sortByDescending { it.uploadDataTime }
                    mosaicRepository.drawablesList.postValue(imagesList.map { it.image })
                }
            }
        }
        drawablesList = mosaicRepository.getDrawablesList()
        userName = mosaicRepository.getUserName()
        isProgressVisible = mosaicRepository.getProgressVisibility()
    }

}