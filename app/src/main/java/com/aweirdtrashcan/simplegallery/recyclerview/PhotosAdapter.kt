package com.aweirdtrashcan.simplegallery.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.aweirdtrashcan.simplegallery.databinding.ListAdapterPhotosBinding
import com.aweirdtrashcan.simplegallery.models.Photos

class PhotosAdapter : ListAdapter<Photos, PhotosAdapter.PhotosViewHolder>(DiffUtilsCallback()) {

    class PhotosViewHolder(private val mBinding : ListAdapterPhotosBinding) : RecyclerView.ViewHolder(mBinding.root) {
        val binding = mBinding
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotosViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ListAdapterPhotosBinding.inflate(layoutInflater)
        return PhotosViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PhotosViewHolder, position: Int) {
        val binding = holder.binding
        val currItem = getItem(position)

        binding.ivPhotos.setImageURI(currItem.uri)
    }

}

class DiffUtilsCallback : DiffUtil.ItemCallback<Photos>() {
    override fun areItemsTheSame(oldItem: Photos, newItem: Photos): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Photos, newItem: Photos): Boolean {
        return oldItem == newItem
    }
}