package com.example.agriappadminpanel.Fragments

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.agriappadminpanel.Adapters.CategoryAdapter
import com.example.agriappadminpanel.Model.AddProductModel
import com.example.agriappadminpanel.Model.CategoryModel
import com.example.agriappadminpanel.R
import com.example.agriappadminpanel.databinding.FragmentCategoryBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.util.*
import kotlin.collections.ArrayList


class CategoryFragment : Fragment() {
    private lateinit var binding : FragmentCategoryBinding
    private var imageUrl : Uri? = null
    private lateinit var dialog : Dialog
    private val list = ArrayList<CategoryModel>()
    private lateinit var rootCategoryList : List<String>



    private var launchGalleryActivity = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){
        if(it.resultCode == Activity.RESULT_OK){
            imageUrl = it.data!!.data
            binding.imageView.setImageURI(imageUrl)
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCategoryBinding.inflate(layoutInflater)

        dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.progress_layout)
        dialog.setCancelable(false)

        rootCategoryList = resources.getStringArray(R.array.RootCategory).toList()

        getDataFromFirebase()
        setRootCategory()





        binding.apply {
            imageView.setOnClickListener {
                val intent = Intent("android.intent.action.GET_CONTENT")
                intent.type = "image/*"
                launchGalleryActivity.launch(intent)

            }
            uploadButton.setOnClickListener {
                validateData(binding.categoryName.text.toString())

            }
        }


        return binding.root
    }



    private fun setRootCategory() {
        val arrayAdapter = ArrayAdapter(requireContext(),R.layout.dropdown_item,rootCategoryList)
        binding.rootCategoryDropdown.adapter = arrayAdapter
    }

    private fun getDataFromFirebase() {

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


    }

    private fun validateData(categoryName: String) {

        if(categoryName.isEmpty())
        {
            Toast.makeText(requireContext(), "Please enter Category name", Toast.LENGTH_SHORT).show()
        }
        else if(imageUrl == null){
            Toast.makeText(requireContext(), "Please select Image", Toast.LENGTH_SHORT).show()
        }else if(binding.rootCategoryDropdown.selectedItemPosition == 0){
            Toast.makeText(requireContext(), "Please Select Root Category", Toast.LENGTH_SHORT).show()
        } else
        {
            uploadImageToFirebase(categoryName)
        }

    }

    private fun uploadImageToFirebase(categoryName: String) {
        dialog.show()

        val fileName = UUID.randomUUID().toString()+".jpg"

        val refStorage = FirebaseStorage.getInstance().reference.child("Category/$fileName")
        refStorage.putFile(imageUrl!!)
            .addOnSuccessListener {
                it.storage.downloadUrl.addOnSuccessListener{image ->
                    storeDataToFirebase(categoryName,image.toString())
                }
            }
            .addOnFailureListener{
                dialog.dismiss()
                Toast.makeText(requireContext(), "Something went wrong with Storage.", Toast.LENGTH_SHORT).show()
            }
    }

    private fun storeDataToFirebase(categoryName: String, url:String) {

        val db = Firebase.firestore

        //val data = hashMapOf<String,Any>(
         //   "Category" to categoryName,
          //  "Image" to url)

        val data = CategoryModel(
            rootCategoryList[binding.rootCategoryDropdown.selectedItemPosition],
            categoryName,
            url)

        db.collection("Categories").add(data)
            .addOnSuccessListener{
                dialog.dismiss()
                binding.imageView.setImageDrawable(resources.getDrawable(R.drawable.preview))
                binding.categoryName.text = null
                getDataFromFirebase()
                Toast.makeText(requireContext(), "Category Added", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener{
                dialog.dismiss()
                Toast.makeText(requireContext(), "Something went Wrong !!", Toast.LENGTH_SHORT).show()

            }

    }







}