package com.potatomeme.jsoupmovieapp.ui.adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.potatomeme.jsoupmovieapp.data.model.DailyBoxOffice
import com.potatomeme.jsoupmovieapp.data.model.MovieTier
import com.potatomeme.jsoupmovieapp.databinding.ItemApiTierBinding
import com.potatomeme.jsoupmovieapp.databinding.ItemTierBinding

class ApiTierListAdapter : ListAdapter<DailyBoxOffice, ApiTierListAdapter.TierViewHolder>(TierDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TierViewHolder {
        return TierViewHolder(
            ItemApiTierBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: TierViewHolder, position: Int) {
        val dailyBoxOffice = getItem(position)
        holder.bind(dailyBoxOffice!!)
    }

    interface OnItemClickListener {
        fun onItemClick(v: View, dailyBoxOffice: DailyBoxOffice, pos: Int)
    }

    private var listener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }


    inner class TierViewHolder(
        private val binding: ItemApiTierBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(dailyBoxOffice: DailyBoxOffice) {
            with(binding) {
                binding.apiTier = dailyBoxOffice
            }

            val pos = absoluteAdapterPosition
            if (pos != RecyclerView.NO_POSITION) {
                itemView.setOnClickListener {
                    listener?.onItemClick(itemView, dailyBoxOffice, pos)
                }
            }
        }
    }


    companion object {
        private val TierDiffCallback = object : DiffUtil.ItemCallback<DailyBoxOffice>() {
            override fun areItemsTheSame(oldItem: DailyBoxOffice, newItem: DailyBoxOffice): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: DailyBoxOffice, newItem: DailyBoxOffice): Boolean {
                return oldItem == newItem
            }
        }
    }


}