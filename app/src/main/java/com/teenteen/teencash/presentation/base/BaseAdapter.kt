package com.example.antkotlinproject.base

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

abstract class BaseAdapter : RecyclerView.Adapter<BaseViewHolder>()

abstract class BaseViewHolder(binding: ViewBinding) : RecyclerView.ViewHolder(binding.root)