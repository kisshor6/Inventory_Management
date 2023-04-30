package com.example.drawa.DrawableItems

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.drawa.R
import com.example.drawa.databinding.FragmentHomeBinding
import com.example.drawa.databinding.FragmentProfileActivityBinding

class Home : Fragment() {

    private var _binding : FragmentHomeBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        binding.addProductItem.setOnClickListener {
            val addFragment = add_product_items()

            val fragmentManager = requireActivity().supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.frameLayout, addFragment)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }
        // Inflate the layout for this fragment
        return binding.root
    }

}