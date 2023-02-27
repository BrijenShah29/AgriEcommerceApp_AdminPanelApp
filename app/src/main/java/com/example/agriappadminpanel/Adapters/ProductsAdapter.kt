package com.example.agriappadminpanel.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.agriappadminpanel.Model.AddProductModel
import com.example.agriappadminpanel.databinding.ItemCategoryLayoutBinding

class ProductsAdapter(val context: Context, val list : ArrayList<AddProductModel>) : RecyclerView.Adapter<ProductsAdapter.productAdapterViewHolder>() {

    inner class productAdapterViewHolder(var binding : ItemCategoryLayoutBinding) : RecyclerView.ViewHolder(binding.root){

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): productAdapterViewHolder {
        val binding = ItemCategoryLayoutBinding.inflate(LayoutInflater.from(context),parent,false)
        return productAdapterViewHolder(binding)
    }

    override fun onBindViewHolder(holder: productAdapterViewHolder, position: Int) {
        holder.binding.textView2.text = list[position].productName
        holder.binding.textView3.text = list[position].productCategory
        Glide.with(context).load(list[position].productCoverImg).centerCrop().into(holder.binding.imageView2)
    }

    override fun getItemCount(): Int {
        return list.size
    }
}