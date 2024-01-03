package com.works.final_exam.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.works.final_exam.databinding.FragmentUpdateProfileBinding
import com.works.final_exam.models.JWTUser
import com.works.final_exam.models.UpdateUserRequest
import com.works.final_exam.network.ApiClient
import com.works.final_exam.network.services.DummyService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UpdateProfile : Fragment() {


    private lateinit var binding: FragmentUpdateProfileBinding
    private lateinit var dummyService: DummyService

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUpdateProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View,savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val user = this.arguments?.getSerializable("user") as? JWTUser

        dummyService = ApiClient.getClient().create(DummyService::class.java)

            val emailRegex = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}\$"

            fun isValidEmail(email: String): Boolean {
                return email.matches(emailRegex.toRegex())
            }

        binding.btnUpdate.setOnClickListener {
            var email = binding.txtEmail.text.toString()
            var username = binding.txtUsername.text.toString()
            var firstname = binding.txtFirstname.text.toString()
            var lastname = binding.txtLastname.text.toString()

            if (user != null){
                if(email.isEmpty() or email.isBlank())
                    email=user.email


                if(username.isEmpty() or username.isBlank())
                    username=user.username


                if(firstname.isEmpty() or firstname.isBlank())
                    firstname=user.firstName


                if(lastname.isEmpty() or lastname.isBlank())
                    lastname=user.lastName


                if (isValidEmail(email)) {
                    val body = UpdateUserRequest(firstname, lastname, email,username)

                    dummyService.updateUser(user.id.toString(), body).enqueue(object: Callback<JWTUser>{
                        override fun onResponse(call: Call<JWTUser>, response: Response<JWTUser>) {
                            if (response.isSuccessful){
                                Toast.makeText(context,"User informations has been updated successfully.",Toast.LENGTH_SHORT).show()
                            }else{
                                Toast.makeText(context,"An error has occured when user informations had been updating.",Toast.LENGTH_SHORT).show()
                            }
                        }

                        override fun onFailure(call: Call<JWTUser>, t: Throwable) {
                            Toast.makeText(context,"Please check your internet connection",Toast.LENGTH_SHORT).show()
                        }

                    })

                }
                else Toast.makeText(this.requireContext(),"Invalid email address.",Toast.LENGTH_LONG).show()
            }

        }
    }


    }