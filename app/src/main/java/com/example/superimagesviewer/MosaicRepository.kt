package com.example.superimagesviewer

import android.app.Application
import android.graphics.drawable.Drawable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import coil.Coil
import coil.request.GetRequest
import com.facebook.*
import kotlinx.coroutines.*
import org.json.JSONArray
import org.json.JSONObject

class MosaicRepository(application: Application) {

    private val drawables = mutableListOf<Drawable>()
    private val mosaicsList = MutableLiveData<List<Drawable>>()
    fun getMosaicsList(): LiveData<List<Drawable>> {
        return mosaicsList
    }

    init {
        loadImagesFromInstagram(application)
    }

    private fun loadImagesFromInstagram(application: Application) {
        val token: AccessToken = AccessToken.getCurrentAccessToken()
        GraphRequest(
            token,
            "/$INSTAGRAM_ID/media",
            null,
            HttpMethod.GET
        ) { response ->
            val graphResponse: JSONObject = response.jsonObject
            val data: JSONArray = graphResponse.getJSONArray("data")
            for (id in 0 until data.length()) {
                val imageId: String = data.getJSONObject(id).getString("id")
                GraphRequest(
                    token,
                    "/$imageId?fields=media_url",
                    null,
                    HttpMethod.GET
                ) { response2 ->
                    val graphResponse2: JSONObject = response2.jsonObject
                    val imageUrl: String = graphResponse2.getString("media_url")
                    loadImageByCoil(application, imageUrl)
                }.executeAsync()
            }
        }.executeAsync()
    }

    private fun loadImageByCoil(application: Application, imageUrl: String) {
        CoroutineScope(Dispatchers.Main).launch {
            withContext(Dispatchers.IO) {
                val imageLoader = Coil.imageLoader(application)
                val request = GetRequest.Builder(application)
                    .data(imageUrl)
                    .build()
                val drawable = imageLoader.execute(request).drawable
                if (drawable != null) {
                    drawables.add(drawable)
                    mosaicsList.postValue(drawables)
                }
            }
        }

    }

    companion object {
        private const val INSTAGRAM_ID = 17841445611980036
    }

}


