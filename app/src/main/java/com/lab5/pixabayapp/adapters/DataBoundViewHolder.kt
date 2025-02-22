package com.lab5.pixabayapp.adapters

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView


class DataBoundViewHolder<out T : ViewDataBinding> constructor(val binding: T) :
    RecyclerView.ViewHolder(binding.root)
