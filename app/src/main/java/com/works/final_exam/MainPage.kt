package com.works.final_exam

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.works.final_exam.databinding.ActivityMainPageBinding
import com.works.final_exam.fragments.Home
import com.works.final_exam.fragments.Profile
import com.works.final_exam.fragments.Cart
import com.works.final_exam.fragments.UpdateProfile
import com.works.final_exam.models.JWTUser

open class MainPage : AppCompatActivity() {

    private lateinit var binding:ActivityMainPageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainPageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        replaceFragment(Home())

        val userModel = intent.getSerializableExtra("user") as? JWTUser
        val bundle = Bundle()

        binding.bottomNavView.setOnItemSelectedListener {
            when(it.itemId){
                R.id.home -> replaceFragment(Home())
                R.id.cart -> {

                    bundle.putSerializable("user", userModel)
                    val cartFragment = Cart()
                    cartFragment.arguments = bundle
                    replaceFragment(cartFragment)
                }
                R.id.profile -> {
                    bundle.putSerializable("user", userModel)
                    val profileFragment = Profile()
                    profileFragment.arguments = bundle
                    replaceFragment(profileFragment)
                }
            }
            true
        }


    }

    open fun replaceFragment(fragment: Fragment){

        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout,fragment)
        fragmentTransaction.commitAllowingStateLoss()
    }
}