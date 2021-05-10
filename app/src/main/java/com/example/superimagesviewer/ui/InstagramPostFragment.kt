package com.example.superimagesviewer.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.URLUtil
import android.widget.Button
import android.widget.EditText
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.superimagesviewer.viewmodel.InstagramPostViewModel
import com.example.superimagesviewer.R

class InstagramPostFragment : Fragment() {

    private val TAG = InstagramPostFragment::class.simpleName

    private lateinit var mosaicPageButton: Button
    private lateinit var editTextPhotoUrl: EditText
    private lateinit var uploadButton: Button
    private lateinit var instagramPostViewModel: InstagramPostViewModel

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

        mosaicPageButton.setOnClickListener {
            view.findNavController().navigate(
                InstagramPostFragmentDirections.actionInstagramPostFragmentToMosaicFragment()
            )
        }

        uploadButton.setOnClickListener {
            val photoUrl = editTextPhotoUrl.text.toString()
            if (URLUtil.isValidUrl(photoUrl)) {
                Log.d(TAG, "Correct url")
                instagramPostViewModel.uploadImageToInstagram(photoUrl)
            } else {
                Log.d(TAG, "Invalid or empty url")
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        instagramPostViewModel = ViewModelProvider(this).get(InstagramPostViewModel::class.java)
    }

}