package com.lab5.pixabayapp.binding

import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.lab5.pixabayapp.R


object BindingAdapters {
    @JvmStatic
    @BindingAdapter("visibleGone")
    fun showHide(view: View, show: Boolean) {
        view.visibility = if (show) View.VISIBLE else View.GONE
    }

    @JvmStatic
    @BindingAdapter("imageUrl")
    fun showPhoto (imageView: ImageView, url : String) {
        if (url.startsWith("http://"))
            url.replace("http://", "https://");
        Glide.with(imageView.context)
            .load(url)
            .placeholder(R.drawable.ic_image_placeholder)
            .into(imageView)
    }
}