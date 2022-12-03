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
import com.potatomeme.jsoupmovieapp.data.model.Movie
import com.potatomeme.jsoupmovieapp.databinding.ItemSavedBinding

class SavedListAdapter : ListAdapter<Movie, SavedListAdapter.SavedViewHolder>(SavedDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SavedViewHolder {
        return SavedViewHolder(
            ItemSavedBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: SavedViewHolder, position: Int) {
        val movieSaved = getItem(position)
        holder.bind(movieSaved!!)
    }

    interface OnItemClickListener {
        fun onItemClick(v: View, movie: Movie, pos: Int)
    }

    private var listener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }


    inner class SavedViewHolder(
        private val binding: ItemSavedBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(savedMovie: Movie) {
            with(binding) {
                binding.savedMovie = savedMovie
            }

            val pos = absoluteAdapterPosition
            if (pos != RecyclerView.NO_POSITION) {
                itemView.setOnClickListener {
                    listener?.onItemClick(itemView, savedMovie, pos)
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

        private val SavedDiffCallback = object : DiffUtil.ItemCallback<Movie>() {
            override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
                return oldItem == newItem
            }
        }
    }


}