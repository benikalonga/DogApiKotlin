package beni.thedelta.benidogapp.viewmodels

import android.widget.EditText
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import beni.thedelta.benidogapp.R
import com.bumptech.glide.Glide

@BindingAdapter("imageUrl")
fun ImageView.loadImage(url: String?) {
    url?.let {
        val into = Glide.with(context)
            .load(it)
            .placeholder(R.drawable.ic_baseline_pets_24)
            .into(this)
        into
    }
}
