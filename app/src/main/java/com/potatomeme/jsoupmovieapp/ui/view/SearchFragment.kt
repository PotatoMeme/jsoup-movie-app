package com.potatomeme.jsoupmovieapp.ui.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.accessibility.AccessibilityEventCompat.setAction
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.potatomeme.jsoupmovieapp.R
import com.potatomeme.jsoupmovieapp.data.model.Movie
import com.potatomeme.jsoupmovieapp.data.model.SearchMovieList
import com.potatomeme.jsoupmovieapp.databinding.FragmentSavedBinding
import com.potatomeme.jsoupmovieapp.databinding.FragmentSearchBinding
import com.potatomeme.jsoupmovieapp.ui.adapter.SavedListAdapter
import com.potatomeme.jsoupmovieapp.ui.adapter.SearchListAdapter
import com.potatomeme.jsoupmovieapp.ui.viewmodel.MainViewModel
import com.potatomeme.jsoupmovieapp.util.collectLatestStateFlow
import kotlinx.coroutines.flow.collectLatest

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: MainViewModel

    private lateinit var searchListAdapter: SearchListAdapter


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
        setupRecyclerView()

        viewModel.searchList.observe(viewLifecycleOwner){
            searchListAdapter.submitList(it)
        }

        binding.btnSearch.setOnClickListener {
           viewModel.searchMovieWithName(binding.etName.text.toString())
        }
    }



    private fun setupRecyclerView() {
        searchListAdapter = SearchListAdapter()
        binding.rvSavedlist.apply {
            setHasFixedSize(true)
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            addItemDecoration(
                DividerItemDecoration(
                    requireContext(),
                    0
                )
            )

            searchListAdapter.setOnItemClickListener(object : SearchListAdapter.OnItemClickListener {
                override fun onItemClick(v: View, movieSearch: SearchMovieList, pos: Int) {
                    val action = SearchFragmentDirections.actionFragmentSearchToFragmentMovie(movieSearch.url)
                    findNavController().navigate(action)
                }
            })
            adapter = searchListAdapter
        }

    }




    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    companion object {
        private const val TAG = "StartFragment"
    }
}