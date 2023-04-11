package com.example.agriappadminpanel.Adapter.orderHistoryAdapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.agriappadminpanel.Model.OrderedProductsModel
import com.example.agriappadminpanel.databinding.LayoutOrderedProductsHistoryBinding

class OrderHistoryChildAdapter(val context : Context, private val childList: ArrayList<OrderedProductsModel>) : RecyclerView.Adapter<OrderHistoryChildAdapter.OrderHistoryChildViewHolder>() {

    class OrderHistoryChildViewHolder(val binding: LayoutOrderedProductsHistoryBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(context: Context, childModel: OrderedProductsModel) {
            binding.productNameTextView.text = childModel.orderedProductName
            binding.quantityTextView.text = childModel.orderedProductQuantity
            Glide.with(context).load(childModel.productImage).centerCrop().into(binding.productImageView)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):  OrderHistoryChildViewHolder {

        val view = LayoutOrderedProductsHistoryBinding.inflate(LayoutInflater.from(parent.context),parent,false)

           // LayoutInflater.from(parent.context).inflate(R.layout.layout_ordered_products_history, parent, false)
        return  OrderHistoryChildViewHolder(view)
    }

    override fun onBindViewHolder(holder:  OrderHistoryChildViewHolder, position: Int) {
        val childModel = childList[position]
        holder.bind(context,childModel)
    }

    override fun getItemCount(): Int = childList.size
}