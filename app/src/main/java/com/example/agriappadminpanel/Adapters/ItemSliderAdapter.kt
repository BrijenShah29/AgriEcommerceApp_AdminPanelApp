package com.example.agriappadminpanel.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.agriappadminpanel.databinding.ImageItemBinding
import com.example.agriappadminpanel.databinding.ItemSliderLayoutBinding

class ItemSliderAdapter(var context: Context, var list : ArrayList<String>) : RecyclerView.Adapter<ItemSliderAdapter.SliderImageViewHolder>() {

    inner class SliderImageViewHolder(var binding : ItemSliderLayoutBinding) : RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SliderImageViewHolder {
       val binding = ItemSliderLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return SliderImageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SliderImageViewHolder, position: Int) {

       Glide.with(context).load(list[position]).centerCrop().into(holder.binding.imageSlider)
    }


    override fun getItemCount(): Int {
        return  list.size
    }
}