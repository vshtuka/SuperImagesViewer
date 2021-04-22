package com.example.superimagesviewer

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.lifecycle.ViewModelProvider

class InstagramPostFragment : Fragment() {

    lateinit var editTextPhotoUrl: EditText
    lateinit var uploadButton: Button
    lateinit var instagramPostViewModel: InstagramPostViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.instagram_post_fragment_, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        editTextPhotoUrl = view.findViewById(R.id.edit_text_photo_url)
        uploadButton = view.findViewById(R.id.upload_button)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        instagramPostViewModel = ViewModelProvider(this).get(InstagramPostViewModel::class.java)

        uploadButton.setOnClickListener {
            instagramPostViewModel.mosaicRepository.uploadPhotoToInstagramByUrl(editTextPhotoUrl.text.toString())
        }

    }

}