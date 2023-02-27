package com.example.agriappadminpanel.Fragments

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.agriappadminpanel.Adapters.CategoryAdapter
import com.example.agriappadminpanel.Adapters.SubCategoryAdapter
import com.example.agriappadminpanel.Model.CategoryModel
import com.example.agriappadminpanel.Model.SubCategoryModel
import com.example.agriappadminpanel.R
import com.example.agriappadminpanel.databinding.FragmentAddSubCategoryBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.util.*
import kotlin.collections.ArrayList

class SubCategoryFragment : Fragment() {

    lateinit var binding : FragmentAddSubCategoryBinding
    private var imageUrl : Uri? = null
    private lateinit var dialog : Dialog
    private lateinit var categoryList : ArrayList<String>
    private val list = ArrayList<SubCategoryModel>()
    private lateinit var selectedCategory : String
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
        binding = FragmentAddSubCategoryBinding.inflate(layoutInflater)
            // Inflate the layout for this fragment

        dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.progress_layout)
        dialog.setCancelable(false)

        // setting up root category
        rootCategoryList = resources.getStringArray(R.array.RootCategory).toList()

        setRootCategory()

        getCategoryFromFirebase()
        getSubCategoryFromFirebase()


        binding.apply {
            imageView.setOnClickListener {
                val intent = Intent("android.intent.action.GET_CONTENT")
                intent.type = "image/*"
                launchGalleryActivity.launch(intent)

            }
        }

        binding.addSubCategoryButton.setOnClickListener {
            validateData(binding.subCategoryName.text.toString())

        }






        return binding.root
    }

    private fun setRootCategory() {

        val arrayAdapter = ArrayAdapter(requireContext(),R.layout.dropdown_item,rootCategoryList)
        binding.rootCategoryDropdown.adapter = arrayAdapter
    }

    private fun getCategoryFromFirebase() {
        categoryList = ArrayList()
        categoryList.clear()
        Firebase.firestore.collection("Categories").get().addOnSuccessListener {
            categoryList.clear()
            categoryList.add(0,"Select Category")
            for(doc in it.documents){
                val data = doc.toObject(CategoryModel::class.java)
                categoryList.add(data!!.Category!!)
            }
            val arrayAdapter = ArrayAdapter(requireContext(),R.layout.dropdown_item,categoryList)
            binding.productCategoryDropdown.adapter = arrayAdapter
        }
    }



    private fun validateData(subCategoryName: String) {

        if(subCategoryName.isEmpty())
        {
            Toast.makeText(requireContext(), "Please enter Sub-Category name", Toast.LENGTH_SHORT).show()
        }
        else if(imageUrl == null){
            Toast.makeText(requireContext(), "Please select Image", Toast.LENGTH_SHORT).show()
        }else if(binding.productCategoryDropdown.selectedItemPosition == 0){
            Toast.makeText(requireContext(), "Please select Category", Toast.LENGTH_SHORT).show()
        }

        else
        {
            uploadImageToFirebase(subCategoryName)
        }


    }

    private fun uploadImageToFirebase(subCategoryName: String) {
        dialog.show()

        val fileName = UUID.randomUUID().toString()+".jpg"

        val refStorage = FirebaseStorage.getInstance().reference.child("SubCategories/$fileName")
        refStorage.putFile(imageUrl!!)
            .addOnSuccessListener {
                it.storage.downloadUrl.addOnSuccessListener{image ->
                    storeDataToFirebase(subCategoryName,image.toString())
                }
            }
            .addOnFailureListener{
                dialog.dismiss()
                Toast.makeText(requireContext(), "Something went wrong with Storage.", Toast.LENGTH_SHORT).show()
            }
    }

    private fun storeDataToFirebase(subCategoryName: String, url:String) {

        var db = Firebase.firestore

        //val data = hashMapOf<String,Any>(
        //    "SubCategory" to subCategoryName,
        //    "Image" to url )

        val data2 = SubCategoryModel(
            rootCategoryList[binding.rootCategoryDropdown.selectedItemPosition],
            subCategoryName,
            categoryList[binding.productCategoryDropdown.selectedItemPosition],
            url
        )
        db.collection("SubCategories").add(data2)
            .addOnSuccessListener{
                dialog.dismiss()
                binding.imageView.setImageDrawable(resources.getDrawable(R.drawable.preview))
                binding.subCategoryName.text = null
                getSubCategoryFromFirebase()
                Toast.makeText(requireContext(), "Sub-Category Added", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener{
                dialog.dismiss()
                Toast.makeText(requireContext(), "Something went Wrong !!", Toast.LENGTH_SHORT).show()

            }

    }


    private fun getSubCategoryFromFirebase() {

        Firebase.firestore.collection("SubCategories")
            .get().addOnSuccessListener {
                list.clear()
                for(doc in it.documents){
                    val firebaseData = doc.toObject(SubCategoryModel::class.java)
                    list.add(firebaseData!!)
                    //Log.d("Fetched category", firebaseData.Category.toString())
                }
                binding.categoryRecycler.adapter = SubCategoryAdapter(requireContext(),list)
            }
    }

}