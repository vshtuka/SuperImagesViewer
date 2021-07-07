package com.example.superimagesviewer.viewmodel

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import com.example.superimagesviewer.repository.FirebaseRepository

class FirebaseImageUploadViewModel(application: Application) : AndroidViewModel(application) {

    private val firebaseRepository = FirebaseRepository()

    public fun uploadImageToFirebase(imageUri: Uri?) {
        firebaseRepository.uploadImageToFirebase(imageUri)
    }

}