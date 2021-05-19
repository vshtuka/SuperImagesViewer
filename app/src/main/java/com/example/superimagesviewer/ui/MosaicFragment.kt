package com.example.superimagesviewer.ui

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
import com.example.superimagesviewer.R
import com.example.superimagesviewer.RecyclerAdapter
import com.example.superimagesviewer.Settings
import com.example.superimagesviewer.databinding.MosaicFragmentBinding
import com.example.superimagesviewer.repository.MosaicRepository
import com.example.superimagesviewer.viewmodel.MosaicViewModel


class MosaicFragment : Fragment() {

    private lateinit var binding: MosaicFragmentBinding
    private lateinit var adapter: RecyclerAdapter
    private lateinit var mosaicViewModel: MosaicViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.mosaic_fragment, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mosaicViewModel = ViewModelProvider(this).get(MosaicViewModel::class.java)
        setFragmentTitle()
        initRecyclerView()
    }

    private fun setFragmentTitle() {
        (requireActivity() as AppCompatActivity).supportActionBar?.title =
            Settings.getUserName(requireContext())
    }

    override fun onStop() {
        super.onStop()
        MosaicRepository.findImagesByIdsAsyncTask.cancel(true)
    }

    private fun initRecyclerView() {
        val layoutManager = GridLayoutManager(
            context, GRID_SPAN_COUNT,
            GridLayoutManager.VERTICAL, false
        )
        layoutManager.spanSizeLookup = object : SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return calculateGridCount(position)
            }
        }
        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.layoutManager = layoutManager
        adapter = RecyclerAdapter()
        val mosaicObserver = Observer { mosaicList: List<Drawable> ->
            adapter.setDrawables(mosaicList)
            binding.recyclerView.adapter = adapter
        }
        mosaicViewModel.mosaicsList.observe(viewLifecycleOwner, mosaicObserver)
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