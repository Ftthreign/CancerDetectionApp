package com.dicoding.asclepius.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.asclepius.data.local.entity.CancerHistoryEntity
import com.dicoding.asclepius.databinding.ItemHistoryBinding
import com.squareup.picasso.Picasso
import java.io.File

class CancerHistoryAdapter(
    private val clickListener : OnItemButtonClickListener
) : ListAdapter<CancerHistoryEntity, CancerHistoryAdapter.ViewHolder>(DIFF_CALLBACK){
    inner class ViewHolder(private val binding : ItemHistoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(cancerHistory : CancerHistoryEntity, listener: OnItemButtonClickListener) {
            binding.cancerLabel.text = cancerHistory.result
            binding.confidenceTextView.text = cancerHistory.confidenceScore
            Picasso
                .get()
                .load(File(cancerHistory.pathImage))
                .into(binding.cancerImage)
            binding.deleteHistory.setOnClickListener {
                listener.onItemButtonClick(cancerHistory)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val bind = ItemHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(bind)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val cancerHistory = getItem(position)
        holder.bind(cancerHistory, clickListener)
    }

    interface OnItemButtonClickListener {
        fun onItemButtonClick(cancerHistory: CancerHistoryEntity)
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<CancerHistoryEntity>() {
            override fun areItemsTheSame(
                oldItem: CancerHistoryEntity,
                newItem: CancerHistoryEntity
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: CancerHistoryEntity,
                newItem: CancerHistoryEntity
            ): Boolean {
                return oldItem == newItem
            }

        }
    }
}