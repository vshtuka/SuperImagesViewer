package com.example.superimagesviewer.repository

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
    internal fun getMosaicsList(): LiveData<List<Drawable>> {
        return mosaicsList
    }

    private lateinit var instagramUserName: String
    internal fun getUserName(): String {
        return instagramUserName
    }

    init {
        if (isNetworkConnected(application)) {
            Log.d(TAG, "Successful internet connection")
            loadInstagramUserName()
            loadImagesFromInstagram(application)
        } else {
            Log.d(TAG, "No internet connection or network problems")
        }
    }

    private fun loadInstagramUserName() {
        val token = AccessToken.getCurrentAccessToken()
        val userNameRequest = GraphRequest(
            token,
            "/$INSTAGRAM_ID?fields=username",
            null,
            HttpMethod.GET
        ) { findUserNameResponse ->
            val userNameResponse: JSONObject = findUserNameResponse.jsonObject
            instagramUserName = userNameResponse.getString("username")
        }
        val thread = Thread {
            userNameRequest.executeAndWait()
        }
        thread.start()
        thread.join()
    }

    private fun loadImagesFromInstagram(application: Application) {
        val token = AccessToken.getCurrentAccessToken()
        GraphRequest(
            token,
            "/$INSTAGRAM_ID/media",
            null,
            HttpMethod.GET
        ) { findMediasResponse ->
            findImagesByIds(findMediasResponse, application, token)
        }.executeAsync()
    }

    private fun findImagesByIds(
        findMediasResponse: GraphResponse,
        application: Application,
        token: AccessToken
    ) {
        val findDataResponse: JSONObject = findMediasResponse.jsonObject
        val data: JSONArray = findDataResponse.getJSONArray("data")
        for (id in 0 until data.length()) {
            val imageId: String = data.getJSONObject(id).getString("id")
            val findImagesByIdsRequest = GraphRequest(
                token,
                "/$imageId?fields=media_url",
                null,
                HttpMethod.GET
            ) { findImageByUrlResponse ->
                loadImageByCoil(findImageByUrlResponse, application)
            }
            findImagesByIdsAsyncTask = findImagesByIdsRequest.executeAsync()
        }
    }

    private fun loadImageByCoil(findImageByUrlResponse: GraphResponse, application: Application) {
        val imageUrlResponse: JSONObject = findImageByUrlResponse.jsonObject
        val imageUrl: String = imageUrlResponse.getString("media_url")
        CoroutineScope(Dispatchers.Main).launch {
            withContext(Dispatchers.IO) {
                val imageLoader = Coil.imageLoader(application)
                val coilRequest = GetRequest.Builder(application)
                    .data(imageUrl)
                    .build()
                val drawable = imageLoader.execute(coilRequest).drawable
                if (drawable != null) {
                    drawables.add(drawable)
                    mosaicsList.postValue(drawables)
                }
            }
        }

    }

    fun uploadPhotoToInstagramByUrl(photoUrl: String) {
        val token = AccessToken.getCurrentAccessToken()
        GraphRequest(
            token,
            "$INSTAGRAM_ID/media?image_url=$photoUrl",
            null,
            HttpMethod.POST
        ) { findMediaContainerResponse ->
            val containerIdResponse: JSONObject = findMediaContainerResponse.jsonObject
            val containerId: String = containerIdResponse.getString("id")
            GraphRequest(
                token,
                "$INSTAGRAM_ID/media_publish?creation_id=$containerId",
                null,
                HttpMethod.POST
            ).executeAsync()
        }.executeAsync()
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
        lateinit var findImagesByIdsAsyncTask: GraphRequestAsyncTask
        private const val INSTAGRAM_ID = 17841445611980036
    }

}