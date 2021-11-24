package com.example.superimagesviewer.ui

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.superimagesviewer.R
import com.example.superimagesviewer.databinding.FacebookLoginFragmentBinding
import com.example.superimagesviewer.viewmodel.FacebookLoginViewModel
import com.facebook.login.LoginManager

class FacebookLoginFragment : Fragment() {

    private lateinit var binding: FacebookLoginFragmentBinding
    private lateinit var facebookLoginViewModel: FacebookLoginViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.facebook_login_fragment, container, false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.loginButton.setOnClickListener {
            LoginManager.getInstance().logInWithReadPermissions(
                this, listOf(
                    "public_profile",
                    "email",
                    "instagram_basic",
                    "pages_show_list",
                    "instagram_manage_comments",
                    "instagram_manage_insights",
                    "instagram_content_publish",
                    "pages_read_engagement",
                    "business_management"
                )
            )
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        facebookLoginViewModel = ViewModelProvider(this).get(FacebookLoginViewModel::class.java)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        facebookLoginViewModel.onResultFromActivity(requestCode, resultCode, data)
    }

}