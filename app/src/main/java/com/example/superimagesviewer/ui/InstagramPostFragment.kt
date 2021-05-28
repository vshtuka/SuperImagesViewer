package com.example.superimagesviewer.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.URLUtil
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.superimagesviewer.viewmodel.InstagramPostViewModel
import com.example.superimagesviewer.R
import com.example.superimagesviewer.databinding.InstagramPostFragmentBinding

class InstagramPostFragment : Fragment() {

    private val TAG = InstagramPostFragment::class.simpleName

    private lateinit var binding : InstagramPostFragmentBinding
    private lateinit var instagramPostViewModel: InstagramPostViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
         binding = DataBindingUtil.inflate(inflater, R.layout.instagram_post_fragment, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.uploadButton.setOnClickListener {
            val photoUrl = binding.editTextPhotoUrl.text.toString()
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