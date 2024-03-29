package com.example.agriappadminpanel.Fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.agriappadminpanel.Activity.AllOrderActivity
import com.example.agriappadminpanel.R
import com.example.agriappadminpanel.databinding.ActivityAllOrderBinding
import com.example.agriappadminpanel.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(layoutInflater)


        binding.button.setOnClickListener{
            findNavController().navigate(R.id.action_homeFragment_to_categoryFragment)
        }

        binding.button2.setOnClickListener{
            findNavController().navigate(R.id.action_homeFragment_to_productFragment)
        }

        binding.button3.setOnClickListener{
            findNavController().navigate(R.id.action_homeFragment_to_sliderFragment)
        }

        binding.button4.setOnClickListener{
          //  startActivity(Intent(requireContext().applicationContext, AllOrderActivity::class.java))
            findNavController().navigate(R.id.action_homeFragment_to_orderFragment)
        }
        binding.button5.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_addSubCategoryFragment)
        }


        return binding.root
    }


}