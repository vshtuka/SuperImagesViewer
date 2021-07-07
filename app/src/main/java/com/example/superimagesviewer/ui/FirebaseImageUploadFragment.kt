package com.example.superimagesviewer.ui

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.superimagesviewer.R
import com.example.superimagesviewer.databinding.FirebaseImageUploadFragmentBinding
import com.example.superimagesviewer.viewmodel.FirebaseImageUploadViewModel
import com.example.superimagesviewer.viewmodel.MosaicViewModel
import com.google.firebase.storage.FirebaseStorage


class FirebaseImageUploadFragment : Fragment() {

    private lateinit var binding: FirebaseImageUploadFragmentBinding
    private lateinit var firebaseImageUploadViewModel: FirebaseImageUploadViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.firebase_image_upload_fragment, container, false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.uploadToFirebaseButton.setOnClickListener {
            val photoPickerIntent = Intent(Intent.ACTION_PICK)
            photoPickerIntent.type = PHOTO_PICKER_INTENT_TYPE
            startActivityForResult(photoPickerIntent, GALLERY_REQUEST)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        firebaseImageUploadViewModel =
            ViewModelProvider(this).get(FirebaseImageUploadViewModel::class.java)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, imageReturnedIntent: Intent?) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent)
        if (requestCode == GALLERY_REQUEST && resultCode == RESULT_OK && null != imageReturnedIntent) {
            val imageUri = imageReturnedIntent.data
            binding.uploadedImage.setImageURI(imageUri)
            firebaseImageUploadViewModel.uploadImageToFirebase(imageUri)
        }
    }

    companion object {
        private const val GALLERY_REQUEST = 1
        private const val PHOTO_PICKER_INTENT_TYPE = "image/*"
    }
}