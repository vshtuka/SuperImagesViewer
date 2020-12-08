package com.example.superimagesviewer;

import android.app.Application;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import java.io.IOException;
import java.io.InputStream;

public class MosaicViewModel extends AndroidViewModel {

    private static final String IMAGES_FOLDER_NAME = "MyImages";
    private final Drawable[] mosaicsList;

    public MosaicViewModel(@NonNull Application application) throws IOException {
        super(application);
        mosaicsList = loadImagesFromAssets();
    }

    private Drawable[] loadImagesFromAssets() throws IOException {
        String[] fileNames = getApplication().getAssets().list(IMAGES_FOLDER_NAME);
        Drawable[] drawables = null;
        if (fileNames != null) {
            drawables = new Drawable[fileNames.length];
            for (int i = 0; i < fileNames.length; i++) {
                InputStream inputStream = getApplication().getAssets().open(IMAGES_FOLDER_NAME + "/" + fileNames[i]);
                drawables[i] = Drawable.createFromStream(inputStream, null);
            }
        }
        return drawables;
    }

    public Drawable[] getMosaicsList() {
        return mosaicsList;
    }
}