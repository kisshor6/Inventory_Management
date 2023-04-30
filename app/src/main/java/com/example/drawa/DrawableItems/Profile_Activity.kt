package com.example.drawa.DrawableItems

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.drawa.Daos.UserDao
import com.example.drawa.Utils.Utils.Companion.userId
import com.example.drawa.databinding.FragmentProfileActivityBinding

class Profile_Activity : Fragment() {

    private var _binding : FragmentProfileActivityBinding? = null
    private val binding get() = _binding!!
    private val userDao = UserDao()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileActivityBinding.inflate(inflater, container, false)

        userDao.getUserData(userId){user->
            Glide.with(this).load(user.imageUrl).into(binding.userProfile)
            binding.userType.text = user.userType
            binding.email.text = user.email
            binding.details.text = user.username
        }

        return binding.root
    }


}