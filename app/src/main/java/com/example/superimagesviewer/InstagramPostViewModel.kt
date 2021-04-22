package com.example.superimagesviewer

import android.app.Application
import androidx.lifecycle.AndroidViewModel

class InstagramPostViewModel(application: Application) : AndroidViewModel(application) {
    val mosaicRepository: MosaicRepository = MosaicRepository(application)
}