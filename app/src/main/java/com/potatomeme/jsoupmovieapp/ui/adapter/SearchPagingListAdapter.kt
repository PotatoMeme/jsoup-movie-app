package com.potatomeme.jsoupmovieapp.ui.adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.potatomeme.jsoupmovieapp.data.model.SearchMovieList
import com.potatomeme.jsoupmovieapp.databinding.ItemSearchBinding

class SearchPagingListAdapter : PagingDataAdapter<SearchMovieList, SearchPagingListAdapter.SearchPagingViewHolder>(SearchDiffCallback) {

    override fun onBindViewHolder(holder: SearchPagingViewHolder, position: Int) {
        val pagedBook = getItem(position)
        pagedBook?.let { book ->
            holder.bind(book)
            holder.itemView.setOnClickListener {
                onItemClickListener?.let { it(book) }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchPagingViewHolder {
        return SearchPagingViewHolder(
            ItemSearchBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    private var onItemClickListener: ((SearchMovieList) -> Unit)? = null
    fun setOnItemClickListener(listener: (SearchMovieList) -> Unit) {
        onItemClickListener = listener
    }

    inner class SearchPagingViewHolder(
        private val binding: ItemSearchBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(searchMovie: SearchMovieList) {
            with(binding) {
                binding.searchMovie = searchMovie
            }
        }
    }


    companion object {
        @JvmStatic
        @BindingAdapter("movieImage")
        fun loadMovieImage(view: ImageView, imageUrl: String) {
            Glide.with(view.context)
                .load(imageUrl)
                .into(view)
        }

        private val SearchDiffCallback = object : DiffUtil.ItemCallback<SearchMovieList>() {
            override fun areItemsTheSame(oldItem: SearchMovieList, newItem: SearchMovieList): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: SearchMovieList, newItem: SearchMovieList): Boolean {
                return oldItem == newItem
            }
        }
    }


}