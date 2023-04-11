package com.example.agriappadminpanel.Adapters

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.agriappadminpanel.Model.AddProductModel
import com.example.agriappadminpanel.databinding.ItemCategoryLayoutBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.CollectionReference

class ProductsAdapter(
    val context: Context,
    val list: ArrayList<AddProductModel>,
    val db: CollectionReference,
    val dialogHome: Dialog
) : RecyclerView.Adapter<ProductsAdapter.productAdapterViewHolder>() {

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
        holder.binding.deleteButton.setOnClickListener {

           val alert = AlertDialog.Builder(context).setMessage("Are you sure about Deleting this Item?")
                .setCancelable(false)
                .setPositiveButton("Yes"){dialog,id ->
                    dialogHome.show()
                    db.document(list[position].productId!!).delete()
                        .addOnSuccessListener()
                        {
                            list.remove(list[position])
                            notifyDataSetChanged()
                            dialogHome.dismiss()
                            Snackbar.make(holder.itemView,
                                "Product has been deleted",
                                Snackbar.LENGTH_SHORT).show()

                        }
                        .addOnFailureListener {
                            dialogHome.dismiss()
                            Toast.makeText(context, "Something Went Wrong !!", Toast.LENGTH_SHORT)
                                .show()
                        }

                }
                .setNegativeButton("No") { dialog, id ->
                    // Dismiss the dialog
                    dialog.dismiss()
                }

            alert.create().show()



        }
    }

    private fun deleteTheItem(itemView: View, position: Int) {

    }

    override fun getItemCount(): Int {
        return list.size
    }
}