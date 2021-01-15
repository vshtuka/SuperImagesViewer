package com.example.superimagesviewer

import android.app.Application
import androidx.lifecycle.MutableLiveData
import android.graphics.drawable.Drawable
import androidx.lifecycle.LiveData
import android.os.AsyncTask
import java.io.IOException
import java.lang.ref.WeakReference

class MosaicRepository(application: Application) {
    val mosaicsList = MutableLiveData<List<Drawable>>()
    private val drawables = mutableListOf<Drawable>()
    fun getMosaicsList(): LiveData<List<Drawable>> {
        return mosaicsList
    }

    private inner class InsertAsyncTask(application: Application) : AsyncTask<Void, Void, Void>() {
        private val applicationWeakReference: WeakReference<Application> = WeakReference(application)
        override fun doInBackground(vararg params: Void?): Void? {
            try {
                loadImagesFromAssets(applicationWeakReference.get())
            } catch (e: IOException) {
                e.printStackTrace()
            }
            return null
        }

        override fun onPostExecute(aVoid: Void?) {
            mosaicsList.postValue(drawables)
        }

    }

    @Throws(IOException::class)
    private fun loadImagesFromAssets(application: Application?) {
        if (application != null) {
            val assets = application.assets
            val fileNames = assets.list(IMAGES_FOLDER_NAME)
            if (fileNames != null) {
                for (fileName in fileNames) {
                    val inputStream = assets.open("$IMAGES_FOLDER_NAME/$fileName")
                    drawables.add(Drawable.createFromStream(inputStream, null))
                }
            }
        }
    }

    companion object {
        private const val IMAGES_FOLDER_NAME = "MyImages"
    }

    init {
        InsertAsyncTask(application).execute()
    }
}