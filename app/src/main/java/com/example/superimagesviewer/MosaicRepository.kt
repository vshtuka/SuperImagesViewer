package com.example.superimagesviewer

import android.app.Application
import android.content.res.AssetManager
import androidx.lifecycle.MutableLiveData
import android.graphics.drawable.Drawable
import androidx.lifecycle.LiveData
import kotlinx.coroutines.*

class MosaicRepository(application: Application) {

    private val drawables = mutableListOf<Drawable>()
    private val mosaicsList = MutableLiveData<List<Drawable>>()

    init {
        CoroutineScope(Dispatchers.Main).launch {
            loadImagesFromAssets(application)
        }
    }

    fun getMosaicsList(): LiveData<List<Drawable>> {
        return mosaicsList
    }

    @Suppress("BlockingMethodInNonBlockingContext")
    private suspend fun loadImagesFromAssets(application: Application) {
        withContext(Dispatchers.IO){
            val assetManager: AssetManager = application.assets
            val fileNames = assetManager.list(IMAGES_FOLDER_NAME)
            if (fileNames != null) {
                for (fileName in fileNames) {
                    val inputStream = assetManager.open("$IMAGES_FOLDER_NAME/$fileName")
                    drawables.add(Drawable.createFromStream(inputStream, null))
                }
            }
        }
        withContext(Dispatchers.Main) {
            mosaicsList.postValue(drawables)
        }
    }

    companion object {
        private const val IMAGES_FOLDER_NAME = "MyImages"
    }

}