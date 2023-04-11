package com.example.agriappadminpanel.Adapter.orderHistoryAdapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.agriappadminpanel.Model.OrderModel
import com.example.agriappadminpanel.R
import com.example.agriappadminpanel.databinding.LayoutOrderHistoryBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class OrderHistoryParentAdapter(val context : Context, private val parentList: ArrayList<OrderModel>) : RecyclerView.Adapter<OrderHistoryParentAdapter.OrderHistoryParentViewHolder>() {

    inner class OrderHistoryParentViewHolder(val binding : LayoutOrderHistoryBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(context: Context, parentModel: OrderModel, itemView: View)
        {
            binding.orderId.text =" Order Id : " + parentModel.orderId.toString()
            binding.orderTotal.text = "Total Payment : " + parentModel.orderAmount.toString()
            val dateFormat = SimpleDateFormat("MM-dd-yyyy hh:mm", Locale.getDefault())
            if(parentModel.orderedDate!=null){
                val date = dateFormat.format(Date(parentModel.orderedDate))
                binding.orderDate.text = "Order Date: $date"
            }

            binding.orderStatus.text = "Status :" + parentModel.shipmentStatus

            binding.orderVisibility.setOnClickListener {
                if(binding.orderItems.visibility == View.VISIBLE)
                {
                    binding.orderItems.visibility = View.GONE
                    binding.orderVisibility.setImageDrawable(context.getDrawable(R.drawable.drop_up_black))
                }
                else if(binding.orderItems.visibility == View.GONE)
                {
                    binding.orderItems.visibility = View.VISIBLE
                    binding.orderVisibility.setImageDrawable(context.getDrawable(androidx.appcompat.R.drawable.abc_ic_arrow_drop_right_black_24dp))
                }
            }

            // Set up child RecyclerView
            binding.orderItems.adapter = OrderHistoryChildAdapter(this@OrderHistoryParentAdapter.context,parentModel.orderedProducts!!)
            binding.orderItems.layoutManager = LinearLayoutManager(this.itemView.context, LinearLayoutManager.HORIZONTAL, false)

            //SETTING UP CANCEL FUNCTIONALITY
            binding.CancelButton.setOnClickListener {
                val alert = AlertDialog.Builder(context).setMessage("Are you sure about cancelling this Order?")
                    .setCancelable(false)
                    .setPositiveButton("Yes") { dialog, id ->
                        updateStatus("Cancelled", parentModel.orderId!!, binding.root)
                    }
                    .setNegativeButton("No") { dialog, id ->
                        dialog.dismiss()

                    }
                alert.create().show()

            }

            when(parentModel.shipmentStatus){
                "Ordered" -> {
                    binding.CancelButton.visibility = View.VISIBLE
                    binding.ProceedButton.isEnabled = true
                    binding.ProceedButton.text = "Dispatch Order"
                    binding.ProceedButton.setOnClickListener {
                        updateStatus("Dispatched",parentModel.orderId!!,itemView)
                        binding.ProceedButton.text = "Deliver Order"
                    }

                }
                "Dispatched" -> {
                    binding.CancelButton.visibility = View.VISIBLE
                    binding.ProceedButton.isEnabled = true
                    binding.ProceedButton.text = "Deliver Order"
                    binding.ProceedButton.setOnClickListener {
                        updateStatus("Delivered", parentModel.orderId!!,itemView)
                        binding.ProceedButton.text = "Order has been Delivered"
                    }

                }
                "Delivered" -> {
                    binding.CancelButton.visibility = View.GONE
                    binding.ProceedButton.isEnabled = false
                    binding.CancelButton.isEnabled = false
                    binding.ProceedButton.text = "Order has been Delivered"

                }
                "Cancelled" -> {
                    binding.ProceedButton.text = "Order has been Cancelled"
                    binding.CancelButton.visibility = View.GONE
                    binding.ProceedButton.isEnabled = false

                }
            }

        }


    }
    fun updateStatus(status: String, orderId: String, itemView: View){
        val data = HashMap<String,Any>()
        data["shipmentStatus"] = status

        Firebase.firestore.collection("Orders").document(orderId).update(data).addOnSuccessListener {
            Snackbar.make(itemView,"Status Updated",Snackbar.LENGTH_SHORT).show()
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderHistoryParentViewHolder {
        val view =  LayoutOrderHistoryBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return OrderHistoryParentViewHolder(view)
    }

    override fun onBindViewHolder(holder: OrderHistoryParentViewHolder, position: Int) {
        val parentModel = parentList[position]
        holder.bind(context,parentModel,holder.itemView)
    }

    override fun getItemCount(): Int = parentList.size
}