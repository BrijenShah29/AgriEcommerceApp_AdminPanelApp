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
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.agriappadminpanel.Adapters.ItemSliderAdapter
import com.example.agriappadminpanel.R
import com.example.agriappadminpanel.databinding.FragmentSliderBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.util.*
import kotlin.collections.ArrayList


class SliderFragment : Fragment() {


    private lateinit var binding: FragmentSliderBinding
    private var imageUrl : Uri? = null
    private lateinit var dialog : Dialog
    private lateinit var sliderImageList : ArrayList<String>



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
        binding = FragmentSliderBinding.inflate(layoutInflater)

        dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.progress_layout)
        dialog.setCancelable(false)

        getSliderImages()

        binding.apply {
            imageView.setOnClickListener {
                val intent = Intent("android.intent.action.GET_CONTENT")
                intent.type = "image/*"
                launchGalleryActivity.launch(intent)
            }

            button5.setOnClickListener {
                if(imageUrl != null){
                    uploadImage(imageUrl!!)
                }
                else
                {
                    Toast.makeText(requireContext(), "Please select Image", Toast.LENGTH_SHORT).show()
                }

            }
        }


        return binding.root
    }

    private fun getSliderImages() {
            sliderImageList = ArrayList()
            sliderImageList.clear()
        val db = Firebase.firestore.collection("Slider")
            db.get().addOnSuccessListener {
                sliderImageList.clear()
                for(doc in it.documents){
                    val data = doc.data!!["img"].toString()
                    sliderImageList.add(data!!)
                }
                val sliderAdapter = ItemSliderAdapter(requireContext(),sliderImageList,db)
                binding.sliderRecycler.adapter = sliderAdapter
                sliderAdapter.notifyDataSetChanged()
            }
    }

    private fun uploadImage(uri: Uri) {

        dialog.show()

        val fileName = UUID.randomUUID().toString()+".jpg"

        val refStorage = FirebaseStorage.getInstance().reference.child("slider/$fileName")
        refStorage.putFile(uri)
            .addOnSuccessListener {
                it.storage.downloadUrl.addOnSuccessListener{image ->
                    storeData(image.toString())
                }
            }
            .addOnFailureListener{
                dialog.dismiss()
                Toast.makeText(requireContext(), "Something went wrong with Storage.", Toast.LENGTH_SHORT).show()
            }
    }

    private fun storeData(image: String) {
        val fileName = UUID.randomUUID().toString()


        val data = hashMapOf<String,Any>(
            "img" to image
        )
        var db = Firebase.firestore.collection("Slider").document("$fileName")

            db.set(data)
            .addOnSuccessListener{
                dialog.dismiss()
                Toast.makeText(requireContext(), "Image Updated", Toast.LENGTH_SHORT).show()
                imageUrl = null
            }
            .addOnFailureListener{
                dialog.dismiss()
                Toast.makeText(requireContext(), "Something went Wrong !!", Toast.LENGTH_SHORT).show()

            }

    }

}