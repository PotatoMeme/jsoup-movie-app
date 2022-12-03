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
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.potatomeme.jsoupmovieapp.R
import com.potatomeme.jsoupmovieapp.data.model.Movie
import com.potatomeme.jsoupmovieapp.databinding.FragmentSavedBinding
import com.potatomeme.jsoupmovieapp.ui.adapter.SavedListAdapter
import com.potatomeme.jsoupmovieapp.ui.viewmodel.MainViewModel
import com.potatomeme.jsoupmovieapp.util.collectLatestStateFlow
import kotlinx.coroutines.flow.collectLatest

class SavedFragment : Fragment() {

    private var _binding: FragmentSavedBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: MainViewModel

    private lateinit var savedListAdapter: SavedListAdapter


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_saved, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
        setupRecyclerView()
        setupTouchHelper(view)
        collectLatestStateFlow(viewModel.savedMovies) {
            Log.d(TAG, "collectLatestStateFlow in ViewCreated: $it")
            savedListAdapter.submitList(it)
        }

        binding.btnSearch.setOnClickListener {
            //searchDatabase(binding.etName.toString())
        }
    }

    var i = 0
    private fun searchDatabase(query: String) {
        viewModel.name = query
        val j = i++
        collectLatestStateFlow (viewModel.savedMovies) {
            Log.d(TAG, "searchDatabase try $j : ${it.size}")
            savedListAdapter.submitList(it)
        }
    }

    private fun setupRecyclerView() {
        savedListAdapter = SavedListAdapter()
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

            savedListAdapter.setOnItemClickListener(object : SavedListAdapter.OnItemClickListener {
                override fun onItemClick(v: View, movie: Movie, pos: Int) {
                    val action = SavedFragmentDirections.actionFragmentSavedToFragmentMovieSaved(movie)
                    findNavController().navigate(action)
                }
            })
            adapter = savedListAdapter
        }

    }

    private fun setupTouchHelper(view: View) {
        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
            0, ItemTouchHelper.LEFT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder,
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.bindingAdapterPosition
                val movie = savedListAdapter.currentList[position]
                viewModel.deleteMovie(movie)
                Snackbar.make(view, "Book has deleted", Snackbar.LENGTH_SHORT).apply {
                    setAction("Undo") {
                        viewModel.saveMovie(movie)
                    }
                }.show()
            }
        }

        ItemTouchHelper(itemTouchHelperCallback).apply {
            attachToRecyclerView(binding.rvSavedlist)
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