package com.example.agriappadminpanel.Fragments

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.*
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.agriappadminpanel.Adapters.AddProductImageAdapter
import com.example.agriappadminpanel.Model.AddProductModel
import com.example.agriappadminpanel.Model.CategoryModel
import com.example.agriappadminpanel.Model.SubCategoryModel
import com.example.agriappadminpanel.R
import com.example.agriappadminpanel.databinding.FragmentAddProductBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.util.*
import kotlin.collections.ArrayList


class AddProductFragment : Fragment() {

    private lateinit var binding: FragmentAddProductBinding
    private lateinit var list : ArrayList<Uri>
    private lateinit var listImages : ArrayList<String>
    private lateinit var adapter: AddProductImageAdapter
    private var coverImage : Uri ? = null
    private lateinit var dialog: Dialog
    private var coverImageUrl : String? = ""
    private lateinit var categoryList : ArrayList<String>
    private lateinit var  productOnSale : ArrayList<String>
    private lateinit var  productSubCategories : ArrayList<String>
    private var selectedOnSale : Boolean? = null
    private var selectedSubCategory : String? = null


    private var launchGalleryActivity = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){
        if(it.resultCode == Activity.RESULT_OK){
            coverImage = it.data!!.data
            binding.productCoverImage.setImageURI(coverImage)
            binding.productCoverImage.visibility = VISIBLE
        }
    }

    private var launchProductActivity = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){
        if(it.resultCode == Activity.RESULT_OK){
            val imageUrl = it.data!!.data
            list.add(imageUrl!!)
            adapter.notifyDataSetChanged()
        }
    }


    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAddProductBinding.inflate(layoutInflater)

        list = ArrayList()
        listImages = ArrayList()

        dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.progress_layout)
        dialog.setCancelable(false)

        binding.selectCoverImage.setOnClickListener {
            val intent = Intent("android.intent.action.GET_CONTENT")
            intent.type = "image/*"
            launchGalleryActivity.launch(intent)
        }

        binding.selectImageBtn.setOnClickListener {
            val intent = Intent("android.intent.action.GET_CONTENT")
            intent.type = "image/*"
            launchProductActivity.launch(intent)
        }

        binding.onSaleRadioGroup.setOnCheckedChangeListener { _, _ ->

            selectedOnSale = binding.onSaleTrue.isChecked
            if (selectedOnSale == true) {
                val tempPrice = binding.productPriceEdt.text.toString().toDouble() - (binding.productPriceEdt.text.toString().toDouble() * binding.discountPercentageEdt.text.toString().toDouble() / 100)
                binding.productSpEdt.text = String.format("%.2f",tempPrice)
            }
            else
            {
                binding.productSpEdt.text = binding.productPriceEdt.text.toString()

            }
        }

        setProductCategory()
        setProductSubCategory()

        adapter = AddProductImageAdapter(list)
        binding.productImageRecyclerview.adapter = adapter

        binding.submitProductBtn.setOnClickListener {
            validateData()
        }


        return binding.root
    }




    private fun validateData() {
        if(binding.productNameEdt.text.toString().isEmpty()){
            binding.productNameEdt.requestFocus()
            binding.productNameEdt.error = "Empty"
        } else if(binding.productDescEdt.text.toString().isEmpty()){
            binding.productDescEdt.requestFocus()
            binding.productDescEdt.error = "Empty"
        } else if(binding.productWeightEdt.text.toString().isEmpty()) {
            binding.productWeightEdt.requestFocus()
            binding.productWeightEdt.error = "Empty"
        } else if(binding.productPriceEdt.text.toString().isEmpty()) {
            binding.productPriceEdt.requestFocus()
            binding.productPriceEdt.error = "Empty"
        }else if(coverImage == null){
            Toast.makeText(requireContext(), "Please Select Cover Image", Toast.LENGTH_SHORT).show()
        }else if(list.size < 1) {
            Toast.makeText(requireContext(), "Please Select Product Image", Toast.LENGTH_SHORT)
                .show()
        }else{
            uploadImagesToFirebase()
        }

    }

    private fun uploadImagesToFirebase() {
        dialog.show()
        val fileName = UUID.randomUUID().toString()+".jpg"

        val refStorage = FirebaseStorage.getInstance().reference.child("Products/$fileName")
        refStorage.putFile(coverImage!!)
            .addOnSuccessListener {
                it.storage.downloadUrl.addOnSuccessListener{image ->
                    coverImageUrl = image.toString()

                    uploadProductImagesToFirebase()
                }
            }
            .addOnFailureListener{
                dialog.dismiss()
                Toast.makeText(requireContext(), "Something went wrong with Storage.", Toast.LENGTH_SHORT).show()
            }
    }

    var i = 0
    private fun uploadProductImagesToFirebase() {
        dialog.show()
        binding.progressBar3.visibility = VISIBLE
        val fileName = UUID.randomUUID().toString()+".jpg"

        val refStorage = FirebaseStorage.getInstance().reference.child("Products/$fileName")
        refStorage.putFile(list[i])
            .addOnSuccessListener {
                it.storage.downloadUrl.addOnSuccessListener{image ->
                    listImages.add(image!!.toString())
                    if(list.size >= listImages.size)
                    {
                    storeDataToFirebase()
                    }
                    else{
                        i += 1
                        uploadProductImagesToFirebase()
                    }
            }
            .addOnFailureListener{
                dialog.dismiss()
                binding.progressBar3.visibility = INVISIBLE
                Toast.makeText(requireContext(), "Something went wrong with Storage.", Toast.LENGTH_SHORT).show()
            }
    }
    }

    private fun storeDataToFirebase() {

        var db = Firebase.firestore.collection("Products")
        val key = db.document().id

        val data = AddProductModel(
            binding.productNameEdt.text.toString(),
            binding.productDescEdt.text.toString(),
            coverImageUrl.toString(),
            categoryList[binding.productCategoryDropdown.selectedItemPosition],
            productSubCategories[binding.productSubcategoryDropdown.selectedItemPosition],
            key,
            binding.productPriceEdt.text.toString(),
            binding.discountPercentageEdt.text.toString(),
            binding.productStockEdt.text.toString(),
            selectedOnSale!!,
            binding.productSpEdt.text.toString(),
            binding.productWeightEdt.text.toString(),
            listImages
        )

        db.document(key).set(data)
            .addOnSuccessListener {
            dialog.dismiss()
            Toast.makeText(requireContext(), "Products uploaded Successfully", Toast.LENGTH_SHORT).show()
            binding.productNameEdt.text = null
            binding.productDescEdt.text = null
            coverImageUrl = null
            listImages.clear()
            binding.productSpEdt.text = null
            binding.productWeightEdt.text = null
            binding.discountPercentageEdt.text = null
            binding.productCoverImage.setImageURI(null)
            list.clear()
            binding.progressBar3.visibility = INVISIBLE

        }
            .addOnFailureListener{
                dialog.dismiss()
                Toast.makeText(requireContext(), "Something went Wrong !", Toast.LENGTH_SHORT).show()
                binding.progressBar3.visibility = INVISIBLE


            }
    }


    private fun setProductCategory(){
        categoryList = ArrayList()

        Firebase.firestore.collection("Categories").get().addOnSuccessListener {
            categoryList.clear()
            categoryList.add(0,"Select Category")
            for(doc in it.documents){
                val data = doc.toObject(CategoryModel::class.java)
                categoryList.add(data!!.Category!!)

            }
            val arrayAdapter = ArrayAdapter(requireContext(),R.layout.dropdown_item,categoryList)
            binding.productCategoryDropdown.adapter = arrayAdapter
            binding.productSubcategoryDropdown.visibility = VISIBLE
        }
    }


    private fun setProductSubCategory() {

        productSubCategories = ArrayList()

        Firebase.firestore.collection("SubCategories").get().addOnSuccessListener {
           productSubCategories.clear()
            productSubCategories.add(0,"Select Sub Category")
            for(doc in it.documents){
                val data = doc.toObject(SubCategoryModel::class.java)
                productSubCategories.add(data!!.SubCategory!!)
            }
            val arrayAdapter = ArrayAdapter(requireContext(),R.layout.dropdown_item,productSubCategories)
            binding.productSubcategoryDropdown.adapter = arrayAdapter
            binding.productSubcategoryDropdown.visibility = VISIBLE
        }
    }

}