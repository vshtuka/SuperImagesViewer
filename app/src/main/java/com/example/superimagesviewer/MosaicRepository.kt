package com.example.superimagesviewer

import android.app.Application
import android.content.Context
import android.graphics.drawable.Drawable
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import coil.Coil
import coil.request.GetRequest
import com.facebook.*
import com.facebook.AccessToken
import kotlinx.coroutines.*
import org.json.JSONArray
import org.json.JSONObject


class MosaicRepository(application: Application) {

    val TAG = MosaicRepository::class.simpleName

    private val drawables = mutableListOf<Drawable>()
    private val mosaicsList = MutableLiveData<List<Drawable>>()
    fun getMosaicsList(): LiveData<List<Drawable>> {
        return mosaicsList
    }

    init {
        if (isNetworkConnected(application)) {
            Log.d(TAG, "Successful internet connection")
            loadImagesFromInstagram(application)
        } else {
            Log.d(TAG, "No internet connection or network problems")
        }
    }

    private fun loadImagesFromInstagram(application: Application) {
        val token: AccessToken = AccessToken.getCurrentAccessToken()
        GraphRequest(
            token,
            "/$INSTAGRAM_ID/media",
            null,
            HttpMethod.GET
        ) { response ->
            findImagesByIds(response, application, token)
        }.executeAsync()
    }

    private fun findImagesByIds(
        response: GraphResponse,
        application: Application,
        token: AccessToken
    ) {
        val graphResponse: JSONObject = response.jsonObject
        val data: JSONArray = graphResponse.getJSONArray("data")
        for (id in 0 until data.length()) {
            val imageId: String = data.getJSONObject(id).getString("id")
            graphRequest = GraphRequest(
                token,
                "/$imageId?fields=media_url",
                null,
                HttpMethod.GET
            ) { response2 ->
                loadImageByCoil(response2, application)
            }
            graphRequest.executeAsync()
        }
    }

    private fun loadImageByCoil(response2: GraphResponse, application: Application) {
        val graphResponse2: JSONObject = response2.jsonObject
        val imageUrl: String = graphResponse2.getString("media_url")
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

    private fun isNetworkConnected(application: Application): Boolean {
        val connectivityManager =
            application.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork
        val networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork)
        return networkCapabilities != null &&
                networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

    companion object {
        lateinit var graphRequest: GraphRequest
        private const val INSTAGRAM_ID = 17841445611980036
    }

}


