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
import com.google.android.material.snackbar.Snackbar
import com.potatomeme.jsoupmovieapp.R
import com.potatomeme.jsoupmovieapp.data.model.Movie
import com.potatomeme.jsoupmovieapp.databinding.FragmentMovieBinding
import com.potatomeme.jsoupmovieapp.ui.viewmodel.MainViewModel
import com.potatomeme.jsoupmovieapp.util.Constants
import com.potatomeme.jsoupmovieapp.util.collectLatestStateFlow

class SavedMovieFragment : Fragment() {

    private var _binding: FragmentMovieBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: MainViewModel

    private val args by navArgs<SavedMovieFragmentArgs>()
    private lateinit var movie : Movie

    private var saved_state = false

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
        movie = args.movie
        binding.movieItem = movie
        Glide.with(binding.movieImage.context)
            .load(movie.imgUrl)
            .into(binding.movieImage)

        viewModel.movie.observe(viewLifecycleOwner) {
            movie = it
            binding.movieItem = movie
            Glide.with(binding.movieImage.context)
                .load(it.imgUrl)
                .into(binding.movieImage)
        }

        collectLatestStateFlow(viewModel.savedMovies) { movies ->
            saved_state = movies.any { it.url == movie.url }
            binding.movieSave.text = if (saved_state) "delete" else "save"
        }


        binding.movieSave.setOnClickListener {
            if (saved_state) {
                viewModel.deleteMovie(movie)
                Snackbar.make(view, "Recipe has deleted", Snackbar.LENGTH_SHORT).apply {
                    setAction("Undo") {
                        viewModel.saveMovie(movie)
                    }
                }.show()
                saved_state = false
            } else {
                viewModel.saveMovie(movie)
                Snackbar.make(view, "Recipe has Saved", Snackbar.LENGTH_SHORT).apply {
                    setAction("Undo") {
                        viewModel.deleteMovie(movie)
                    }
                }.show()
                saved_state = true
            }
        }
        binding.movieUpdate.setOnClickListener {
            viewModel.searchMovie(movie.url)
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