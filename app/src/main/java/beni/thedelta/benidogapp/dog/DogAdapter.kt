package beni.thedelta.benidogapp.dog

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import beni.thedelta.benidogapp.config.ItemClickListener
import beni.thedelta.benidogapp.databinding.ItemDogBinding

class DogAdapter(private val inflater: LayoutInflater, private val onItemClick: (View, ImageView, Dog) -> Unit) :
    ListAdapter<Dog, DogHolder>(BreedDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        DogHolder(ItemDogBinding.inflate(inflater, parent, false), onItemClick)

    override fun onBindViewHolder(holder: DogHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class DogHolder(val binding: ItemDogBinding, val onItemClick: (View, ImageView, Dog) -> Unit) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(dog: Dog) {
        binding.dog = dog
        binding.itemClickListener = object : ItemClickListener<Dog>{
            override fun onItemClick(item: Dog) {
                onItemClick(binding.cardDog, binding.imgDog, dog)
            }
        }
    }
}

object BreedDiffCallback : DiffUtil.ItemCallback<Dog>() {
    override fun areItemsTheSame(oldItem: Dog, newItem: Dog) =
        oldItem === newItem

    override fun areContentsTheSame(oldItem: Dog, newItem: Dog) =
        oldItem.imageUrl == newItem.imageUrl
}