package com.example.superimagesviewer.viewmodel

import android.app.Application
import android.content.Intent
import androidx.lifecycle.AndroidViewModel
import com.facebook.CallbackManager
import com.facebook.login.LoginManager

class FacebookLoginViewModel(application: Application) : AndroidViewModel(application) {

    var callbackManager: CallbackManager = CallbackManager.Factory.create()

    fun onResultFromActivity(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager.onActivityResult(requestCode, resultCode, data)
    }

}