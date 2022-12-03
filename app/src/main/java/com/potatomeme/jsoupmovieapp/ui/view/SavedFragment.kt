package com.potatomeme.jsoupmovieapp.ui.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.potatomeme.jsoupmovieapp.R
import com.potatomeme.jsoupmovieapp.data.model.Movie
import com.potatomeme.jsoupmovieapp.databinding.FragmentSavedBinding
import com.potatomeme.jsoupmovieapp.ui.adapter.SavedListAdapter
import com.potatomeme.jsoupmovieapp.ui.viewmodel.MainViewModel
import com.potatomeme.jsoupmovieapp.util.collectLatestStateFlow

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
        collectLatestStateFlow(viewModel.savedMovies){
            Log.d(TAG, "collectLatestStateFlow: $it")
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

            savedListAdapter.setOnItemClickListener( object :SavedListAdapter.OnItemClickListener{
                override fun onItemClick(v: View, movie: Movie, pos: Int) {
//                    val action = SavedFragmentDirections.actionFragmentSavedToFragmentDetail(recipe)
//                    findNavController().navigate(action)
                    Toast.makeText((context as MainActivity),movie.toString(),Toast.LENGTH_SHORT).show()
                }
            })
            adapter = savedListAdapter
        }

    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    companion object{
        private const val TAG = "StartFragment"
    }
}