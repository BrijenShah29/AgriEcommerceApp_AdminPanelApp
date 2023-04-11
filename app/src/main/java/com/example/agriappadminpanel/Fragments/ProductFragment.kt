package com.example.agriappadminpanel.Fragments

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.example.agriappadminpanel.Adapters.ProductsAdapter
import com.example.agriappadminpanel.Model.AddProductModel
import com.example.agriappadminpanel.R
import com.example.agriappadminpanel.databinding.FragmentProductBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ProductFragment : Fragment() {

    private lateinit var binding: FragmentProductBinding

    private lateinit var dialog: Dialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentProductBinding.inflate(layoutInflater)



        dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.progress_layout)
        dialog.setCancelable(false)

        getProductsFromFirebase()
        binding.floatingActionButton.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_productFragment_to_addProductFragment)
        }





        return binding.root
    }


    private fun getProductsFromFirebase() {

        dialog.show()
        val list = ArrayList<AddProductModel>()

        val db = Firebase.firestore.collection("Products")

        db.get().addOnSuccessListener {
            list.clear()
            for(doc in it.documents){
                val firebaseData = doc.toObject(AddProductModel::class.java)
                list.add(firebaseData!!)
            }

            binding.recyclerView.adapter = ProductsAdapter(requireContext(),list,db,dialog)
        }

        dialog.dismiss()

    }
}