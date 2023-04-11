package com.example.agriappadminpanel.Fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.agriappadminpanel.Adapter.orderHistoryAdapters.OrderHistoryParentAdapter
import com.example.agriappadminpanel.Model.OrderModel
import com.example.agriappadminpanel.databinding.FragmentOrderBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class OrderFragment : Fragment() {


    lateinit var binding : FragmentOrderBinding

    lateinit var list : ArrayList<OrderModel>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentOrderBinding.inflate(layoutInflater)


        list = ArrayList()



        // fetching all products
        list.clear()
        Firebase.firestore.collection("Orders").get().addOnSuccessListener {
            for(doc in it){
                val data =  doc.toObject(OrderModel::class.java)
                list.add(data)
                Log.d("Fetched Orders",data.orderId!!)
            }

            // fetched list
            val adapter = OrderHistoryParentAdapter(requireContext(),list)
            binding.orderList.adapter = adapter
            adapter.notifyDataSetChanged()
        }






        return binding.root
    }

}