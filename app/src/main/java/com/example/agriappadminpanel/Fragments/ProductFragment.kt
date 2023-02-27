package com.example.agriappadminpanel.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.navigation.Navigation
import com.example.agriappadminpanel.Adapters.CategoryAdapter
import com.example.agriappadminpanel.Adapters.ProductsAdapter
import com.example.agriappadminpanel.Model.AddProductModel
import com.example.agriappadminpanel.Model.CategoryModel
import com.example.agriappadminpanel.R
import com.example.agriappadminpanel.databinding.FragmentProductBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

class ProductFragment : Fragment() {

    private lateinit var binding: FragmentProductBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentProductBinding.inflate(layoutInflater)

        getProductsFromFirebase()

        binding.floatingActionButton.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_productFragment_to_addProductFragment)
        }





        return binding.root
    }


    private fun getProductsFromFirebase() {

        val list = ArrayList<AddProductModel>()

        val db = Firebase.firestore.collection("Products")

        db.get().addOnSuccessListener {
            list.clear()
            for(doc in it.documents){
                val firebaseData = doc.toObject(AddProductModel::class.java)
                list.add(firebaseData!!)
            }

            binding.recyclerView.adapter = ProductsAdapter(requireContext(),list)
        }



/*
        Firebase.firestore.collection("Categories")
            .get().addOnSuccessListener {
                list.clear()
                for(doc in it.documents){
                    val firebaseData = doc.toObject(CategoryModel::class.java)
                    list.add(firebaseData!!)
                    //Log.d("Fetched category", firebaseData.Category.toString())
                }
                binding.categoryRecycler.adapter = CategoryAdapter(requireContext(),list)
            }


    categoryList = ArrayList()

    Firebase.firestore.collection("Categories").get().addOnSuccessListener {
        categoryList.clear()
        for(doc in it.documents){
            val data = doc.toObject(CategoryModel::class.java)
            categoryList.add(data!!.Category!!)
            categoryList.add(0,"Select Category")
        }
        val arrayAdapter = ArrayAdapter(requireContext(),R.layout.dropdown_item,categoryList)
        binding.productCategoryDropdown.adapter = arrayAdapter
        binding.productSubcategoryDropdown.visibility = View.VISIBLE
    }

 */
    }
}