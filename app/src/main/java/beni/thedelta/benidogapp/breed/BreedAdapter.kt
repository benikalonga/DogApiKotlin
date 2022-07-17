package beni.thedelta.benidogapp.breed

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import beni.thedelta.benidogapp.config.ItemClickListener
import beni.thedelta.benidogapp.databinding.ItemBreedBinding

class BreedAdapter(private val inflater: LayoutInflater, private val onItemClick: (Breed) -> Unit) :
    ListAdapter<Breed, BreedHolder>(BreedDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        BreedHolder(ItemBreedBinding.inflate(inflater, parent, false), onItemClick)

    override fun onBindViewHolder(holder: BreedHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class BreedHolder(val binding: ItemBreedBinding, val onItemClick: (Breed) -> Unit) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(breed: Breed) {
        binding.breed = breed
        binding.itemClickListener = object : ItemClickListener<Breed>{
            override fun onItemClick(item: Breed) {
                onItemClick.invoke(breed)
            }
        }
    }
}

object BreedDiffCallback : DiffUtil.ItemCallback<Breed>() {
    override fun areItemsTheSame(oldItem: Breed, newItem: Breed) =
        oldItem === newItem

    override fun areContentsTheSame(oldItem: Breed, newItem: Breed) =
        oldItem.list == newItem.list &&
                oldItem.designation == newItem.designation
}