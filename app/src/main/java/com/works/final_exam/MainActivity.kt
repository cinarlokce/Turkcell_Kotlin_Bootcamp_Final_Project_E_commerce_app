package com.works.final_exam

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.works.final_exam.databinding.ActivityMainBinding
import com.works.final_exam.models.JWTUser
import com.works.final_exam.models.UserSend
import com.works.final_exam.network.ApiClient
import com.works.final_exam.network.services.DummyService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private lateinit var dummyService: DummyService
    private lateinit var binding: ActivityMainBinding
    private lateinit var userSend: UserSend
    private lateinit var userPassword: String
    private lateinit var userName: String
    private lateinit var loginStatus: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        dummyService = ApiClient.getClient().create(DummyService::class.java)


        binding.btnLogin.setOnClickListener {
            userName = binding.editTextUserName.text.toString()
            userPassword = binding.editTextPassword.text.toString()

            if (userName.isEmpty() || userPassword.isEmpty()){
                Toast.makeText(applicationContext,"Username or password can not be empty.", Toast.LENGTH_LONG).show()
            }else{
                userSend = UserSend(userName,userPassword)
                lifecycleScope.launch(Dispatchers.IO) {

                    dummyService.login(userSend).enqueue(object : Callback<JWTUser> {
                        override fun onResponse(call: Call<JWTUser>, response: Response<JWTUser>) {
                            val statusCode = response.code()

                            if (statusCode==200){
                                loginStatus = "Login success"
                                val userModel = response.body()
                                Log.d("durumkodu", "onResponse: $userModel")
                                val i = Intent(applicationContext,MainPage::class.java)
                                i.putExtra("user",userModel)
                                startActivity(i)
                            }else{
                                loginStatus = "Username or password is wrong."
                                val userModel = response.body()
                                Log.d("durumkodu", "onResponse: $userModel")
                            }

                            Toast.makeText(applicationContext,loginStatus, Toast.LENGTH_SHORT).show()
                        }

                        override fun onFailure(call: Call<JWTUser>, t: Throwable) {
                            Toast.makeText(applicationContext, "Please check your internet connection.", Toast.LENGTH_SHORT).show()
                        }

                    })



                }
            }
        }
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.e("FCM", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }
            // Get new FCM registration token
            task.result.let {
            }
            // Log and toast
        })
    }
}
