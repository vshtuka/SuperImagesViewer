package com.example.androidproject;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recyclerView);
        layoutManager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);

        ((GridLayoutManager) layoutManager).setSpanSizeLookup(
                new GridLayoutManager.SpanSizeLookup() {
                    @Override
                    public int getSpanSize(int position) {
                        return (position % 5 == 0 ? 2 : 1);
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
        String[] fileNames = getAssets().list("MyImages");
        assert fileNames != null;
        Drawable[] drawables = new Drawable[fileNames.length];
        for (int i = 0; i < fileNames.length; i++) {
            InputStream inputStream = getAssets().open("MyImages/" + fileNames[i]);
            Drawable drawable = Drawable.createFromStream(inputStream, null);
            drawables[i] = drawable;
        }

        return drawables;
    }

}
