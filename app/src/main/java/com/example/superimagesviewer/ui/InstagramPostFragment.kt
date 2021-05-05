package com.example.superimagesviewer.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.superimagesviewer.viewmodel.InstagramPostViewModel
import com.example.superimagesviewer.R

class InstagramPostFragment : Fragment() {

    lateinit var mosaicPageButton: Button
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
        mosaicPageButton = view.findViewById(R.id.post_to_mosaic_page_button)

        mosaicPageButton.setOnClickListener{
           view.findNavController().navigate(
               InstagramPostFragmentDirections.actionInstagramPostFragmentToMosaicFragment()
           )
        }

        uploadButton.setOnClickListener {
            instagramPostViewModel.photoUrl = editTextPhotoUrl.text.toString()
            instagramPostViewModel.uploadImageToInstagram()
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        instagramPostViewModel = ViewModelProvider(this).get(InstagramPostViewModel::class.java)
    }

}