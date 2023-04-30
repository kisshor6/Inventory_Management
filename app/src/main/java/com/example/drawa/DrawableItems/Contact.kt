package com.example.drawa.DrawableItems

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.drawa.R
import com.example.drawa.databinding.FragmentContactBinding
import com.example.drawa.databinding.FragmentProfileActivityBinding

class Contact : Fragment() {

    private var _binding : FragmentContactBinding? = null
    private val binding get() = _binding!!

    @SuppressLint("IntentReset")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentContactBinding.inflate(inflater, container, false)

        binding.submitProblem.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val problem = binding.problemEditText.text.toString()

            if (email.isNotEmpty() && problem.isNotEmpty()){

                val intent = Intent(Intent.ACTION_SEND)
                intent.type  = "text/plain"
                intent.putExtra(Intent.EXTRA_EMAIL, arrayOf("kevindrakhanal@gmail.com"))
                intent.putExtra(Intent.EXTRA_SUBJECT, "Problem report")
                intent.putExtra(Intent.EXTRA_TEXT, problem)
                startActivity(Intent.createChooser(intent, "Send email"))

            }else{
                Toast.makeText(context, "Input both Fields", Toast.LENGTH_SHORT).show()
            }
        }
        return binding.root
    }

}