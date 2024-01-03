package com.works.final_exam.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.works.final_exam.MainPage
import com.works.final_exam.R
import com.works.final_exam.adapters.ProductsListAdapter
import com.works.final_exam.databinding.FragmentProductListBinding
import com.works.final_exam.databinding.FragmentProfileBinding
import com.works.final_exam.models.JWTUser
import com.works.final_exam.models.Product
import com.works.final_exam.models.Products
import com.works.final_exam.network.ApiClient
import com.works.final_exam.network.services.DummyService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductList : Fragment() {

    private lateinit var binding: FragmentProductListBinding
    private lateinit var dummyService: DummyService
    private lateinit var urunler: List<Product>
    private lateinit var recyclerView: RecyclerView
    private lateinit var searchView: SearchView
    private lateinit var adapter: ProductsListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProductListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View,savedInstanceState: Bundle?) {
        super.onViewCreated(view,savedInstanceState)

        val selectedCategory = this.arguments?.getString("selectedCategory")

        dummyService = ApiClient.getClient().create(DummyService::class.java)

        recyclerView = binding.rvProductlist
        searchView = binding.svProductsearch

        lifecycleScope.launch(Dispatchers.IO){
            if (selectedCategory != null) {
                dummyService.getSelectedCategory(selectedCategory).enqueue(object: Callback<Products>{
                    override fun onResponse(call: Call<Products>, response: Response<Products>) {
                        if (response.isSuccessful) {
                            response.body()?.let {
                                urunler = ArrayList(it.products)
                                adapter = ProductsListAdapter(urunler, onClick = {goToDetailFragment(it)})
                                recyclerView.adapter = adapter
                            }

                            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                                override fun onQueryTextSubmit(query: String?): Boolean {
                                    return false
                                }

                                override fun onQueryTextChange(newText: String): Boolean {
                                    searchProduct(newText)
                                    return true
                                }
                            })

                        } else {
                            Toast.makeText(context, "An error has occured.", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<Products>, t: Throwable) {
                        Toast.makeText(context, "Please check your internet connection.", Toast.LENGTH_SHORT).show()
                    }

                })
            }
        }

    }

    private fun goToDetailFragment(product: Product){
        val bundle = Bundle()
        val detailFragment = ProductDetail()
        bundle.putSerializable("productDetail",product)
        detailFragment.arguments = bundle
        (activity as? MainPage)?.replaceFragment(detailFragment)
    }

    private fun searchProduct(key:String){
        dummyService.productSearch(key).enqueue(object: Callback<Products>{
            override fun onResponse(call: Call<Products>, response: Response<Products>) {
                if(response.isSuccessful){
                    val productList = response.body()?.products
                    productList.let {
                        adapter.filterProducts(it!!)
                    }
                }
            }

            override fun onFailure(call: Call<Products>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })
    }
}