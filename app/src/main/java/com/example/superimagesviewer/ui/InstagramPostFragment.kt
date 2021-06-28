package com.example.superimagesviewer.ui

import android.os.Bundle
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
import kotlinx.coroutines.*

class InstagramPostFragment : Fragment() {

    private lateinit var binding: InstagramPostFragmentBinding
    private lateinit var instagramPostViewModel: InstagramPostViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.instagram_post_fragment, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.uploadButton.setOnClickListener {
            val photoUrl = binding.editTextPhotoUrl.text.toString()
            if (URLUtil.isValidUrl(photoUrl)) {
                binding.editTextPhotoUrl.isEnabled = false
                binding.uploadButton.isClickable = false
                CoroutineScope(Dispatchers.Main).launch {
                    binding.imageUploadProgressBar.visibility = View.VISIBLE
                    withContext(Dispatchers.IO) {
                        instagramPostViewModel.uploadImageToInstagram(photoUrl)
                    }
                    binding.editTextPhotoUrl.isEnabled = true
                    binding.uploadButton.isClickable = true
                    binding.imageUploadProgressBar.visibility = View.INVISIBLE
                    showDialog("Upload successful")
                }
            } else {
                showDialog("Invalid or empty url")
            }
        }
    }

    private fun showDialog(message: String) {
        val myDialogFragment = ImageUploadDialogFragment(message)
        val manager = parentFragmentManager
        myDialogFragment.show(manager, DIALOG_TAG)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        instagramPostViewModel = ViewModelProvider(this).get(InstagramPostViewModel::class.java)
    }

    companion object {
        private const val DIALOG_TAG = "imageUploadDialog"
    }
}