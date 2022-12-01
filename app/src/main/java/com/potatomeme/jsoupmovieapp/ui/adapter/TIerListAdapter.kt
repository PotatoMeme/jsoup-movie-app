package com.potatomeme.jsoupmovieapp.ui.adapter


import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.potatomeme.jsoupmovieapp.data.model.MovieTier
import com.potatomeme.jsoupmovieapp.databinding.ItemTierBinding

class TIerListAdapter : ListAdapter<MovieTier, TIerListAdapter.TierViewHolder>(TierDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TierViewHolder {
        return TierViewHolder(
            ItemTierBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: TierViewHolder, position: Int) {
        val movieTier = getItem(position)
        holder.bind(movieTier!!)
    }

    interface OnItemClickListener {
        fun onItemClick(v: View, movieTier: MovieTier, pos: Int)
    }

    private var listener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }


    inner class TierViewHolder(
        private val binding: ItemTierBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(movieTier: MovieTier) {
            with(binding) {
                binding.movieTier = movieTier
            }

            val pos = absoluteAdapterPosition
            if (pos != RecyclerView.NO_POSITION) {
                itemView.setOnClickListener {
                    listener?.onItemClick(itemView, movieTier, pos)
                }
            }
        }
    }


    companion object {
        private val TierDiffCallback = object : DiffUtil.ItemCallback<MovieTier>() {
            override fun areItemsTheSame(oldItem: MovieTier, newItem: MovieTier): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: MovieTier, newItem: MovieTier): Boolean {
                return oldItem == newItem
            }
        }
    }


}