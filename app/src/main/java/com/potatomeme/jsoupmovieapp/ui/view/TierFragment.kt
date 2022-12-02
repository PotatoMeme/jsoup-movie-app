package com.potatomeme.jsoupmovieapp.ui.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.potatomeme.jsoupmovieapp.R
import com.potatomeme.jsoupmovieapp.data.model.MovieTier
import com.potatomeme.jsoupmovieapp.databinding.FragmentTierBinding
import com.potatomeme.jsoupmovieapp.ui.adapter.TIerListAdapter
import com.potatomeme.jsoupmovieapp.ui.viewmodel.MainViewModel

class TierFragment : Fragment() {

    private var _binding: FragmentTierBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: MainViewModel

    private lateinit var tierListAdapter: TIerListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_tier, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tierFragment = this
        viewModel = (activity as MainActivity).viewModel
        viewModel.searchTier(0)
        setupSpinner()
        setupRecyclerView()
        viewModel.tierList.observe(viewLifecycleOwner){
            Log.d(TAG, "tierList changed ${it}")
            tierListAdapter.submitList(it)
        }
    }

    private fun setupSpinner() {
        val sort_list = listOf("실시간 조회순", "평정순(현재 상영영화)", "평점순(모든영화)")
        val adapter_spinner =
            ArrayAdapter(context as MainActivity, R.layout.itme_dropdown, sort_list)
        binding.spinner.setAdapter(adapter_spinner)
        binding.spinner.onItemSelectedListener =  object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                Log.d(TAG, "onItemSelected: $position")
                viewModel.searchTier(position)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
    }

    private fun setupRecyclerView() {
        tierListAdapter = TIerListAdapter()
        binding.rvTierlist.apply {
            setHasFixedSize(true)
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            addItemDecoration(
                DividerItemDecoration(
                    requireContext(),
                    DividerItemDecoration.VERTICAL
                )
            )

            tierListAdapter.setOnItemClickListener( object :TIerListAdapter.OnItemClickListener{
                override fun onItemClick(v: View, movieTier: MovieTier, pos: Int) {
                    val action = TierFragmentDirections.actionFragmentTierToMovieFragment(movieTier.url)
                    findNavController().navigate(action)
                }
            })

            adapter = tierListAdapter
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