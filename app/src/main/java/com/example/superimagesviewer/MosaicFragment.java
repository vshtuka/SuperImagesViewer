package com.example.superimagesviewer;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.IOException;
import java.util.List;

public class MosaicFragment extends Fragment {
    private static final int GRID_SPAN_COUNT = 2;
    private static final int FULL_SCREEN_WIDTH_IMAGE_POSITION = 5;
    private static final int FULL_SCREEN_WIDTH_IMAGE_GRID_SPAN_COUNT = 2;
    private static final int HALF_SCREEN_WIDTH_IMAGE_GRID_SPAN_COUNT = 1;

    private RecyclerView recyclerView;
    private RecyclerAdapter adapter;
    private MosaicViewModel mosaicViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.mosaic_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        recyclerView = view.findViewById(R.id.recycler_view);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mosaicViewModel = new ViewModelProvider(this).get(MosaicViewModel.class);
        initRecyclerView();
    }

    private void initRecyclerView() {
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), GRID_SPAN_COUNT,
                GridLayoutManager.VERTICAL, false);
        layoutManager.setSpanSizeLookup(
                new GridLayoutManager.SpanSizeLookup() {
                    @Override
                    public int getSpanSize(int position) {
                        return calculateGridCount(position);
                    }
                });
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new RecyclerAdapter();
        final Observer<List<Drawable>> mosaicObserver = mosaicList -> {
            adapter.setDrawables(mosaicList);
            recyclerView.setAdapter(adapter);
        };
        mosaicViewModel.getMosaicsList().observe(getViewLifecycleOwner(), mosaicObserver);
    }

    private int calculateGridCount(int position) {
        return position % FULL_SCREEN_WIDTH_IMAGE_POSITION == 0 ?
                FULL_SCREEN_WIDTH_IMAGE_GRID_SPAN_COUNT :
                HALF_SCREEN_WIDTH_IMAGE_GRID_SPAN_COUNT;
    }

}