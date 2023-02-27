package com.example.agriappadminpanel.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.agriappadminpanel.Model.CategoryModel
import com.example.agriappadminpanel.R
import com.example.agriappadminpanel.databinding.ItemCategoryLayoutBinding

class CategoryAdapter(var context: Context, var list : ArrayList<CategoryModel>) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>()  {

    inner class CategoryViewHolder(view : View) : RecyclerView.ViewHolder(view){
        var binding = ItemCategoryLayoutBinding.bind(view)
        var txtBox = view.findViewById<TextView>(R.id.textView2)
        var rootCat = view.findViewById<TextView>(R.id.textView3)
        var img = view.findViewById<ImageView>(R.id.imageView2)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        return CategoryViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_category_layout, parent, false))
    }


    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {

        holder.txtBox.text = list[position].Category
        holder.rootCat.text = list[position].Root
        Glide.with(context).load(list[position].Image).centerCrop().into(holder.img)

    }


    override fun getItemCount(): Int {
        return list.size
    }
}