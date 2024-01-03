package com.works.final_exam.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import com.works.final_exam.MainPage
import com.works.final_exam.databinding.FragmentProfileBinding
import com.works.final_exam.models.JWTUser

class Profile : Fragment() {

    private lateinit var binding: FragmentProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View,savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val user = this.arguments?.getSerializable("user") as? JWTUser
        if (user != null) {
            binding.textViewEmail.text = "E-mail: "+user.email
            binding.textViewFirstName.text = "First Name: "+user.firstName
            binding.textViewLastName.text = "Last Name: "+user.lastName
            binding.textViewGender.text = "Gender: "+user.gender
            binding.textViewId.text = "User ID: "+user.id.toString()
            binding.textViewUsername.text = "Username: "+user.username

            Picasso.get()
                .load(user.image)
                .centerCrop()
                .resize(200,200)
                .into(binding.imageView)
        }

        binding.btnUpdateInfo.setOnClickListener {
            val bundle1 = Bundle()
            val receiverFragment = UpdateProfile()
            bundle1.putSerializable("user",user)
            receiverFragment.arguments = bundle1
            (activity as? MainPage)?.replaceFragment(receiverFragment)
        }
    }

}