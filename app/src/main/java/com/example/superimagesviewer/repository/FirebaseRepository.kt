package com.example.superimagesviewer.repository

import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FirebaseRepository {

    private val firebaseStorage = FirebaseStorage.getInstance()
    private val storageReference = firebaseStorage.reference

    fun uploadImageToFirebase(imageUri: Uri?) {
        CoroutineScope(Dispatchers.Main).launch {
            withContext(Dispatchers.IO) {
                if (null != imageUri) {
                    val imagesReference =
                        storageReference.child("images/${imageUri.lastPathSegment}")
                    imagesReference.putFile(imageUri)
                }
            }
        }
    }
}