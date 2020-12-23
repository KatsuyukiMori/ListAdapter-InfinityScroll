package com.example.listadapter_infinityscroll

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.listadapter_infinityscroll.databinding.CardItemBinding

class MainListAdapter(
    private val viewLifecycleOwner: LifecycleOwner,
    private val viewModel: MainViewModel
) : ListAdapter<ItemCard, MainListAdapter.ItemViewHolder>(DiffCallback) {

    interface LoadMoreDispatcher {
        fun onLoadMore()
    }
    private var mDispatcher: LoadMoreDispatcher? = null
    fun setDispatcher(dispatcher: LoadMoreDispatcher) {
        mDispatcher = dispatcher
    }
    fun releaseDispatcher() {
        mDispatcher = null
    }

    companion object {
        private val TAG = this.javaClass.name + " mori"
    }

    class ItemViewHolder(private val binding: CardItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ItemCard, viewLifecycleOwner: LifecycleOwner, viewModel: MainViewModel) {
            binding.run {
                lifecycleOwner = viewLifecycleOwner
                itemCard = item
                this.viewModel = viewModel

                executePendingBindings()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ItemViewHolder(CardItemBinding.inflate(layoutInflater, parent, false))
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(getItem(position), viewLifecycleOwner, viewModel)
        Log.d(TAG, "position: $position, itemCount: $itemCount, limit: ${MainViewModel.LIMIT}")
        if (position + 1 == itemCount) {
            if (itemCount < MainViewModel.LIMIT) {
                Log.d(TAG, "call onLoadMore")
                mDispatcher?.onLoadMore()
            }
        }
    }
}

private object DiffCallback : DiffUtil.ItemCallback<ItemCard>() {
    override fun areItemsTheSame(oldItemCard: ItemCard, newItemCard: ItemCard): Boolean {
        return oldItemCard.id == newItemCard.id
    }

    override fun areContentsTheSame(oldItemCard: ItemCard, newItemCard: ItemCard): Boolean {
        return oldItemCard == newItemCard
    }
}