package com.example.superimagesviewer.repository

import android.app.Application
import android.content.Context
import android.graphics.drawable.Drawable
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import coil.Coil
import coil.request.GetRequest
import com.example.superimagesviewer.Settings
import com.example.superimagesviewer.model.InstagramImage
import com.facebook.*
import com.facebook.AccessToken
import kotlinx.coroutines.*
import org.json.JSONArray
import org.json.JSONObject

class MosaicRepository {

    private val instagramImageList = mutableListOf<InstagramImage>()
    public fun getInstagramImageList(): MutableList<InstagramImage> {
        return instagramImageList
    }

    public val drawablesList = MutableLiveData<List<Drawable>>()
    public fun getDrawablesList(): LiveData<List<Drawable>> {
        return drawablesList
    }

    public val instagramUserName = MutableLiveData<String>()
    public fun getUserName(): LiveData<String> {
        return instagramUserName
    }

    private val isProgressVisible = MutableLiveData<Boolean>()
    public fun getProgressVisibility(): LiveData<Boolean> {
        return isProgressVisible
    }

    public fun loadInstagramUserName(): String {
        var newUserName = ""
        val token = AccessToken.getCurrentAccessToken()
        GraphRequest(
            token,
            "/$INSTAGRAM_ID?fields=username",
            null,
            HttpMethod.GET
        ) { findUserNameResponse ->
            val userNameResponse: JSONObject = findUserNameResponse.jsonObject
            newUserName = userNameResponse.getString("username")
        }.executeAndWait()
        return newUserName
    }

    public fun loadImagesFromInstagram(application: Application) {
        isProgressVisible.postValue(true)
        val token = AccessToken.getCurrentAccessToken()
        GraphRequest(
            token,
            "/$INSTAGRAM_ID/media",
            null,
            HttpMethod.GET
        ) { findMediasResponse ->
            findImagesByIds(findMediasResponse, application, token)
        }.executeAndWait()
        isProgressVisible.postValue(false)
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
            GraphRequest(
                token,
                "/$imageId?fields=media_url,timestamp",
                null,
                HttpMethod.GET
            ) { findImageByUrlResponse ->
                loadImageByCoil(findImageByUrlResponse, application)
            }.executeAndWait()
        }
    }

    private fun loadImageByCoil(findImageByUrlResponse: GraphResponse, application: Application) {
        val imageResponse: JSONObject = findImageByUrlResponse.jsonObject
        val imageUrl: String = imageResponse.getString("media_url")
        val timestamp: String = imageResponse.getString("timestamp")
        CoroutineScope(Dispatchers.IO).launch {
            withContext(Dispatchers.IO) {
                val imageLoader = Coil.imageLoader(application)
                val coilRequest = GetRequest.Builder(application)
                    .data(imageUrl)
                    .build()
                val drawable = imageLoader.execute(coilRequest).drawable
                if (drawable != null) {
                    instagramImageList.add(InstagramImage(drawable, timestamp))
                }
            }
        }
    }

    public fun uploadPhotoToInstagramByUrl(photoUrl: String) {
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
            ).executeAndWait()
        }.executeAndWait()
    }

    public fun isNetworkConnected(application: Application): Boolean {
        val connectivityManager =
            application.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork
        val networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork)
        return networkCapabilities != null &&
                networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

    companion object {
        private const val INSTAGRAM_ID = 17841445611980036
    }

}