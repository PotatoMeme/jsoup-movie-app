package com.potatomeme.jsoupmovieapp.ui.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.potatomeme.jsoupmovieapp.R
import com.potatomeme.jsoupmovieapp.databinding.FragmentMovieBinding
import com.potatomeme.jsoupmovieapp.ui.viewmodel.MainViewModel
import com.potatomeme.jsoupmovieapp.util.collectLatestStateFlow

class MovieFragment : Fragment() {

    private var _binding: FragmentMovieBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: MainViewModel

    private val args by navArgs<MovieFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_movie, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
        viewModel.searchMovie(args.url)
        viewModel.movie.observe(viewLifecycleOwner) {
            binding.movieItem = it
            Glide.with(binding.movieImage.context)
                .load(it.imgUrl)
                .into(binding.movieImage)
        }
        binding.movieSave.setOnClickListener {
            viewModel.movie.value?.let { data -> viewModel.saveMovie(data) }
            Log.d(TAG, "onViewCreated: ${viewModel.movie.value}")
            Log.d(TAG, "onViewCreated: ${viewModel.favoriteMovies}")
        }
        collectLatestStateFlow(viewModel.favoriteMovies){
            Log.d(TAG, "onViewCreated: $it")
        }
    }


    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    companion object {
        @JvmStatic
        @BindingAdapter("movieImage")
        fun loadMovieImage(view: ImageView, imageUrl: String) {
            Glide.with(view.context)
                .load(imageUrl)
                .into(view)
        }


        private const val TAG = "StartFragment"
    }
}