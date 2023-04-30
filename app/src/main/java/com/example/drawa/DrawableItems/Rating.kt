package com.example.drawa.DrawableItems

import android.content.res.ColorStateList
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.drawa.R
import com.example.drawa.Utils.Utils.Companion.database
import com.example.drawa.Utils.Utils.Companion.userId
import com.example.drawa.databinding.FragmentProfileActivityBinding
import com.example.drawa.databinding.FragmentRatingBinding
import com.example.drawa.modal.RatingModal

class Rating : Fragment() {

    private var _binding : FragmentRatingBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRatingBinding.inflate(inflater, container, false)

        binding.ratingBar.setOnRatingBarChangeListener { ratingBar, rating, fromUser ->

            val progressTint = ColorStateList.valueOf(resources.getColor(R.color.yello))
            ratingBar.progressTintList = progressTint

            val secondaryProgressTint = ColorStateList.valueOf(resources.getColor(R.color.white))
            ratingBar.secondaryProgressTintList = secondaryProgressTint
        }

        binding.rateButton.setOnClickListener {
            when (binding.ratingBar.rating) {
                1.0f -> {
                    sendRating(1)
                }
                2.0f -> {
                    // Perform action for 2 star rating
                    sendRating(2)
                }
                3.0f -> {
                    // Perform action for 3 star rating
                    sendRating(3)
                }
                4.0f -> {
                    // Perform action for 4 star rating
                    sendRating(4)
                }
                5.0f -> {
                    // Perform action for 5 star rating
                    sendRating(5)
                }
            }
        }

        return binding.root
    }

    private fun sendRating(i: Int) {
        val ratingModal = RatingModal(userId, i.toString())
        database.reference.child("rating")
            .child(userId)
            .setValue(ratingModal)
            .addOnSuccessListener {
                Toast.makeText(context, "You have given $i Rating", Toast.LENGTH_LONG).show()
            }
            .addOnFailureListener {
                Toast.makeText(context, "Failed to given Rating", Toast.LENGTH_LONG).show()

            }
    }

}