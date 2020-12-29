package com.example.superimagesviewer;

import android.app.Application;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class MosaicViewModel extends AndroidViewModel {

    private LiveData<List<Drawable>> mosaicsList;
    private MosaicRepository mosaicRepository;

    public MosaicViewModel(@NonNull Application application) {
        super(application);
        mosaicRepository = new MosaicRepository(application);
        mosaicsList = mosaicRepository.getMosaicsList();
    }

    public LiveData<List<Drawable>> getMosaicsList() {
        return mosaicsList;
    }

}