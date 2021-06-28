package com.example.superimagesviewer.viewmodel

import android.app.Application
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.lifecycle.*
import com.example.superimagesviewer.Settings
import com.example.superimagesviewer.repository.MosaicRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MosaicViewModel(application: Application) : AndroidViewModel(application) {

    private val TAG = MosaicViewModel::class.simpleName

    public val drawablesList: LiveData<List<Drawable>>
    public val userName: LiveData<String>
    private val mosaicRepository: MosaicRepository = MosaicRepository()

    init {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                if (mosaicRepository.isNetworkConnected(application)) {
                    Log.d(TAG, "Successful internet connection")
                    val userName = Settings.getUserName(application)
                    if (null == userName) {
                        mosaicRepository.loadInstagramUserName(application, userName)
                    } else {
                        mosaicRepository.instagramUserName.postValue(userName!!)
                        mosaicRepository.loadInstagramUserName(application, userName)
                    }
                    mosaicRepository.loadImagesFromInstagram(application)
                    val imagesList = mosaicRepository.getInstagramImageList()
                    imagesList.sortByDescending { it.uploadDataTime }
                    mosaicRepository.drawablesList.postValue(imagesList.map { it.image })
                } else {
                    Log.d(TAG, "No internet connection or network problems")
                }
            }
        }
        drawablesList = mosaicRepository.getDrawablesList()
        userName = mosaicRepository.getUserName()
    }

}