package com.potatomeme.jsoupmovieapp.ui.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.potatomeme.jsoupmovieapp.R
import com.potatomeme.jsoupmovieapp.databinding.FragmentMovieBinding
import com.potatomeme.jsoupmovieapp.ui.viewmodel.MainViewModel
import com.potatomeme.jsoupmovieapp.util.Constants.BASE_URL
import com.potatomeme.jsoupmovieapp.util.collectLatestStateFlow

class MovieApiFragment : Fragment() {

    private var _binding: FragmentMovieBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: MainViewModel

    private val args by navArgs<MovieApiFragmentArgs>()

    private var saved_state = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_movie, container, false)
        return binding.root
    }

    var url = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).viewModel

        viewModel.searchMovieWithNameToGetOne(args.name)
        viewModel.apiToUrl.observe(viewLifecycleOwner){
            Log.d(TAG, "onViewCreated: ${BASE_URL+it}")
            url = BASE_URL+it
            viewModel.searchMovieWithUrl(url)
        }
        viewModel.movie.observe(viewLifecycleOwner) {
            binding.movieItem = it
            Glide.with(binding.movieImage.context)
                .load(it.imgUrl)
                .into(binding.movieImage)
            saved_state = viewModel.savedMovies.value.any {  movie ->
                movie.url == url
            }
            binding.movieSave.text = if (saved_state) "delete" else "save"
        }


        collectLatestStateFlow(viewModel.savedMovies) { movies ->
            saved_state = movies.any { it.url == url }
            binding.movieSave.text = if (saved_state) "delete" else "save"
        }

        binding.movieSave.setOnClickListener {
            if (saved_state) {
                viewModel.movie.value?.let { data -> viewModel.deleteMovie(data) }
                Snackbar.make(view, "Movie has deleted", Snackbar.LENGTH_SHORT).apply {
                    setAction("Undo") {
                        viewModel.movie.value?.let { data -> viewModel.saveMovie(data) }
                    }
                }.show()
                saved_state = false
            } else {
                viewModel.movie.value?.let { data -> viewModel.saveMovie(data) }
                Snackbar.make(view, "Movie has Saved", Snackbar.LENGTH_SHORT).apply {
                    setAction("Undo") {
                        viewModel.movie.value?.let { data -> viewModel.deleteMovie(data) }
                    }
                }.show()
                saved_state = true
            }
        }
        binding.movieUpdate.setOnClickListener {
            viewModel.searchMovieWithUrl(url)
        }
        binding.movieMore.setOnClickListener {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW, Uri.parse(
                        viewModel.movie.value?.url ?: ("$BASE_URL/movie/search/")
                    )
                )
            )
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