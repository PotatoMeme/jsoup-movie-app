package com.potatomeme.jsoupmovieapp.ui.adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.potatomeme.jsoupmovieapp.data.model.SearchMovieList
import com.potatomeme.jsoupmovieapp.databinding.ItemSearchBinding

class SearchListAdapter : ListAdapter<SearchMovieList, SearchListAdapter.SearchViewHolder>(SearchDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        return SearchViewHolder(
            ItemSearchBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        val movieSearch = getItem(position)
        holder.bind(movieSearch!!)
    }

    interface OnItemClickListener {
        fun onItemClick(v: View, movieSearch: SearchMovieList, pos: Int)
    }

    private var listener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }


    inner class SearchViewHolder(
        private val binding: ItemSearchBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(searchMovie: SearchMovieList) {
            with(binding) {
                binding.searchMovie = searchMovie
            }

            val pos = absoluteAdapterPosition
            if (pos != RecyclerView.NO_POSITION) {
                itemView.setOnClickListener {
                    listener?.onItemClick(itemView, searchMovie, pos)
                }
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