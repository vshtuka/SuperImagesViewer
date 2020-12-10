package com.example.superimagesviewer;

import android.app.Application;
import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class MosaicViewModel extends AndroidViewModel {

    private static final String IMAGES_FOLDER_NAME = "MyImages";
    private final MutableLiveData<List<Drawable>> mosaicsList =  new MutableLiveData<>();
    private final List<Drawable> drawables = new ArrayList<>();

    public MosaicViewModel(@NonNull Application application) throws IOException {
        super(application);
        loadImagesFromAssets();
        mosaicsList.postValue(drawables);
    }

    private void loadImagesFromAssets() throws IOException {
        AssetManager assets = getApplication().getAssets();
        String[] fileNames = assets.list(IMAGES_FOLDER_NAME);
        if (fileNames != null) {
            for (String fileName : fileNames) {
                InputStream inputStream = assets.open(IMAGES_FOLDER_NAME + "/" + fileName);
                drawables.add(Drawable.createFromStream(inputStream, null));
            }
        }
    }

    public LiveData<List<Drawable>> getMosaicsList() { return mosaicsList; }

}