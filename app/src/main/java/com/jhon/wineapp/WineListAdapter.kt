package com.jhon.wineapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.jhon.wineapp.databinding.ItemWineBinding

class WineListAdapter : ListAdapter<Wine, RecyclerView.ViewHolder>(WineDiff()) {
    private  lateinit var context: Context

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        context = parent.context
        return ViewHolder(
            LayoutInflater.from(context).inflate(R.layout.item_wine, parent, false)
        )
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int
    ) {
        val wine = getItem(position)
        (holder as ViewHolder).run{
            with(binding){
                tvWine.text = wine.wine
                tvWinery.text = wine.winery
                tvLocation.text = wine.location
                rating.rating = wine.rating.average.toFloat()

                Glide.with(context)
                    .load(wine.image)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .centerCrop()
                    .into(imgWine)

            }
        }
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemWineBinding.bind(view)
    }

    private class WineDiff : DiffUtil.ItemCallback<Wine>() {
        override fun areItemsTheSame(
            oldItem: Wine,
            newItem: Wine
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: Wine,
            newItem: Wine
        ): Boolean {
            return oldItem == newItem
        }
    }
}