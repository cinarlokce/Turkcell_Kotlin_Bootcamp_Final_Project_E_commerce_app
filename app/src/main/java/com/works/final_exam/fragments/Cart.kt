package com.works.final_exam.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.works.final_exam.adapters.CartItemAdapter
import com.works.final_exam.databinding.FragmentCartBinding
import com.works.final_exam.models.Cart
import com.works.final_exam.models.CartResponse
import com.works.final_exam.models.JWTUser
import com.works.final_exam.network.ApiClient
import com.works.final_exam.network.services.DummyService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Cart : Fragment() {

    private lateinit var dummyService: DummyService
    private lateinit var binding: FragmentCartBinding
    private lateinit var cartItemAdapter: CartItemAdapter
    private lateinit var carts: List<Cart>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val user = this.arguments?.getSerializable("user") as? JWTUser
        Log.d("kullanıcı verisi", "Cart: $user")
        lifecycleScope.launch(Dispatchers.IO){

            dummyService = ApiClient.getClient().create(DummyService::class.java)

                dummyService.getUserCart(user?.id.toString()).enqueue(object : Callback<CartResponse> {
                    override fun onResponse(call: Call<CartResponse>, response: Response<CartResponse>) {
                        if (response.isSuccessful) {
                            carts = response.body()?.carts ?: emptyList()

                            for (cart in carts) {
                                cartItemAdapter = CartItemAdapter(cart.products)
                                binding.recyclerViewCart.adapter = cartItemAdapter
                                binding.recyclerViewCart.layoutManager = LinearLayoutManager(requireContext())
                            }

                        } else {
                            Toast.makeText(context, "No response from server.", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<CartResponse>, t: Throwable) {
                        Log.e("CartFragment", "API isteği başarısız oldu", t)
                        Toast.makeText(context, "Please check your internet connection.", Toast.LENGTH_SHORT).show()
                    }
                })
            }



        }
    }