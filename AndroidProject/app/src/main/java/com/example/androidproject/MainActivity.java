package com.example.androidproject;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    private static final int GRID_SPAN_COUNT = 2;
    private static final int FULL_SCREEN_WIDTH_IMAGE_POSITION = 5;
    private static final int FULL_SCREEN_WIDTH_IMAGE_GRID_SPAN_COUNT = 2;
    private static final int HALF_SCREEN_WIDTH_IMAGE_GRID_SPAN_COUNT = 1;
    private static final String IMAGES_FOLDER_NAME = "MyImages";

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recyclerView);
        layoutManager = new GridLayoutManager(this, GRID_SPAN_COUNT,
                GridLayoutManager.VERTICAL, false);

        ((GridLayoutManager) layoutManager).setSpanSizeLookup(
                new GridLayoutManager.SpanSizeLookup() {
                    @Override
                    public int getSpanSize(int position) {
                        return (position % FULL_SCREEN_WIDTH_IMAGE_POSITION == 0 ?
                                FULL_SCREEN_WIDTH_IMAGE_GRID_SPAN_COUNT :
                                HALF_SCREEN_WIDTH_IMAGE_GRID_SPAN_COUNT);
                    }
                });
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        try {
            adapter = new RecyclerAdapter(loadImagesFromAssets());
        } catch (IOException e) {
            e.printStackTrace();
        }
        recyclerView.setAdapter(adapter);
    }

    private Drawable[] loadImagesFromAssets() throws IOException {
        String[] fileNames = getAssets().list(IMAGES_FOLDER_NAME);
        Drawable[] drawables = null;
        if (fileNames != null) {
            drawables = new Drawable[fileNames.length];
            for (int i = 0; i < fileNames.length; i++) {
                InputStream inputStream = getAssets().open(IMAGES_FOLDER_NAME + "/" + fileNames[i]);
                drawables[i] = Drawable.createFromStream(inputStream, null);
            }
        }

        return drawables;
    }
}
