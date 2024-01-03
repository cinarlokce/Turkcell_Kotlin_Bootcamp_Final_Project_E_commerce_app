package com.works.final_exam.fragments

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.squareup.picasso.Picasso
import com.works.final_exam.databinding.FragmentProductDetailBinding
import com.works.final_exam.models.Product
import com.works.final_exam.network.ApiClient
import com.works.final_exam.network.services.DummyService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ProductDetail : Fragment() {

    private lateinit var binding: FragmentProductDetailBinding
    private lateinit var dummyService: DummyService

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProductDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View,savedInstanceState: Bundle?) {
        super.onViewCreated(view,savedInstanceState)

        val product = this.arguments?.getSerializable("productDetail") as? Product
        println(product)

        dummyService = ApiClient.getClient().create(DummyService::class.java)

        binding.txtBrandDetail.text = "Brand: "+product?.brand
        binding.txtPriceDetail.text = product?.price.toString()+"$"
        binding.txtRatingDetail.text = product?.rating.toString()+"/5 ★"
        binding.txtTitleDetail.text = product?.title
        binding.txtDescriptionDetail.text = product?.description

        binding.addCart.setOnClickListener {
            if (product != null) {
                lifecycleScope.launch(Dispatchers.IO){
                    dummyService.addProduct(product).enqueue(object: Callback<Void>{
                        override fun onResponse(call: Call<Void>, response: Response<Void>) {
                        lifecycleScope.launch(Dispatchers.Main){
                            if (response.isSuccessful){
                                val toast = Toast.makeText(requireContext(),"${product.title?.capitalize()} added to cart.",Toast.LENGTH_LONG).apply {
                                    setGravity(Gravity.BOTTOM,0,-150)
                                    show()
                                }

                            }else
                                Toast.makeText(requireContext(),"An error has occured while the product adding the cart.",Toast.LENGTH_LONG).show()
                        }
                        }

                        override fun onFailure(call: Call<Void>, t: Throwable) {
                            lifecycleScope.launch(Dispatchers.Main)
                            {
                                Toast.makeText(requireContext(), "Please check your internet connection.", Toast.LENGTH_LONG).show()
                            }
                        }

                    })
                }

            }
        }



        Picasso.get()
            .load(product?.images?.get(0))
            .centerCrop()
            .resize(400,400)
            .into(binding.imgPdetail)


    }

}