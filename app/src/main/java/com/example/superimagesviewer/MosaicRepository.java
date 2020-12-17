package com.example.superimagesviewer;

import android.app.Application;
import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class MosaicRepository {

    private static final String IMAGES_FOLDER_NAME = "MyImages";

    private MutableLiveData<List<Drawable>> mosaicsList = new MutableLiveData<>();
    private final List<Drawable> drawables = new ArrayList<>();

    MosaicRepository(Application application) {
        new InsertAsyncTask(application).execute();
    }

    public LiveData<List<Drawable>> getMosaicsList() {
        return mosaicsList;
    }

    private class InsertAsyncTask extends AsyncTask<Void, Void, Void> {

        private final Application application;

        public InsertAsyncTask(Application application) {
            this.application = application;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                loadImagesFromAssets(application);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            mosaicsList.postValue(drawables);
        }

    }

    private void loadImagesFromAssets(Application application) throws IOException {
        AssetManager assets = application.getAssets();
        String[] fileNames = assets.list(IMAGES_FOLDER_NAME);
        if (fileNames != null) {
            for (String fileName : fileNames) {
                System.out.println(fileName);
                InputStream inputStream = assets.open(IMAGES_FOLDER_NAME + "/" + fileName);
                drawables.add(Drawable.createFromStream(inputStream, null));
            }
        }
    }

}
