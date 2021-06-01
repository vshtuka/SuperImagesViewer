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
import com.example.superimagesviewer.Settings
import com.facebook.*
import com.facebook.AccessToken
import kotlinx.coroutines.*
import org.json.JSONArray
import org.json.JSONObject

class MosaicRepository(application: Application) {

    private val TAG = MosaicRepository::class.simpleName

    private val drawables = mutableListOf<InstagramImage>()
    private val mosaicsList = MutableLiveData<List<Drawable>>()
    fun getMosaicsList(): LiveData<List<Drawable>> {
        return mosaicsList
    }

    private val instagramUserName = MutableLiveData<String>()
    fun getUserName(): LiveData<String> {
        return instagramUserName
    }

    init {
        if (isNetworkConnected(application)) {
            Log.d(TAG, "Successful internet connection")
            val userName = Settings.getUserName(application)
            if (userName == null) {
                loadInstagramUserName(application, userName)
            } else {
                instagramUserName.postValue(userName!!)
                loadInstagramUserName(application, userName)
            }
            loadImagesFromInstagram(application)
        } else {
            Log.d(TAG, "No internet connection or network problems")
        }
    }

    private fun loadInstagramUserName(application: Application, userName: String?) {
        val token = AccessToken.getCurrentAccessToken()
        GraphRequest(
            token,
            "/$INSTAGRAM_ID?fields=username",
            null,
            HttpMethod.GET
        ) { findUserNameResponse ->
            val userNameResponse: JSONObject = findUserNameResponse.jsonObject
            if (userName != userNameResponse.getString("username")) {
                instagramUserName.postValue(userNameResponse.getString("username"))
                Settings.setUserName(application, userNameResponse.getString("username"))
            }
        }.executeAsync()
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
                "/$imageId?fields=media_url,timestamp",
                null,
                HttpMethod.GET
            ) { findImageByUrlResponse ->
                loadImageByCoil(findImageByUrlResponse, application)
            }
            findImagesByIdsAsyncTask = findImagesByIdsRequest.executeAsync()
        }
    }

    private fun loadImageByCoil(findImageByUrlResponse: GraphResponse, application: Application) {
        val imageResponse: JSONObject = findImageByUrlResponse.jsonObject
        val imageUrl: String = imageResponse.getString("media_url")
        val imageUploadData: String = imageResponse.getString("timestamp")
        CoroutineScope(Dispatchers.Main).launch {
            withContext(Dispatchers.IO) {
                val imageLoader = Coil.imageLoader(application)
                val coilRequest = GetRequest.Builder(application)
                    .data(imageUrl)
                    .build()
                val drawable = imageLoader.execute(coilRequest).drawable
                if (drawable != null) {
                    drawables.add(InstagramImage(drawable, imageUploadData))
                    drawables.sortByDescending { it.uploadDataTime }
                    mosaicsList.postValue(drawables.map { it.image })
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

    data class InstagramImage(
        val image: Drawable,
        val uploadDataTime: String
    )

    companion object {
        lateinit var findImagesByIdsAsyncTask: GraphRequestAsyncTask
        private const val INSTAGRAM_ID = 17841445611980036
    }

}