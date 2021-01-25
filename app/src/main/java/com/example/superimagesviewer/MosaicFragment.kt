package com.example.superimagesviewer

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
import android.graphics.drawable.Drawable
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer

class MosaicFragment : Fragment() {
    private var recyclerView: RecyclerView? = null
    private var adapter: RecyclerAdapter? = null
    private var mosaicViewModel: MosaicViewModel? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.mosaic_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recyclerView = view.findViewById(R.id.recycler_view)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mosaicViewModel = ViewModelProvider(this).get(MosaicViewModel::class.java)
        initRecyclerView()
    }

    private fun initRecyclerView() {
        val layoutManager = GridLayoutManager(context, GRID_SPAN_COUNT,
                GridLayoutManager.VERTICAL, false)
        layoutManager.spanSizeLookup = object : SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return calculateGridCount(position)
            }
        }
        recyclerView?.setHasFixedSize(true)
        recyclerView?.layoutManager = layoutManager
        adapter = RecyclerAdapter()
        val mosaicObserver = Observer { mosaicList: List<Drawable> ->
            adapter?.setDrawables(mosaicList)
            recyclerView?.adapter = adapter
        }
        mosaicViewModel?.mosaicsList?.observe(viewLifecycleOwner, mosaicObserver)
    }

    private fun calculateGridCount(position: Int): Int {
        return if (position % FULL_SCREEN_WIDTH_IMAGE_POSITION == 0) FULL_SCREEN_WIDTH_IMAGE_GRID_SPAN_COUNT
        else HALF_SCREEN_WIDTH_IMAGE_GRID_SPAN_COUNT
    }

    companion object {
        private const val GRID_SPAN_COUNT = 2
        private const val FULL_SCREEN_WIDTH_IMAGE_POSITION = 5
        private const val FULL_SCREEN_WIDTH_IMAGE_GRID_SPAN_COUNT = 2
        private const val HALF_SCREEN_WIDTH_IMAGE_GRID_SPAN_COUNT = 1
    }
}