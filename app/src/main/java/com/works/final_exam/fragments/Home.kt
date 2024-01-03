package com.works.final_exam.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.works.final_exam.MainPage
import com.works.final_exam.adapters.CategoryAdapter
import com.works.final_exam.databinding.FragmentHomeBinding
import com.works.final_exam.network.ApiClient
import com.works.final_exam.network.services.DummyService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Home : Fragment() {

    private lateinit var dummyService: DummyService
    private lateinit var categories: List<String>
    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dummyService = ApiClient.getClient().create(DummyService::class.java)

        dummyService.getCategories().enqueue(object : Callback<List<String>> {
            override fun onResponse(call: Call<List<String>>, response: Response<List<String>>) {
                if (response.isSuccessful) {
                    categories = response.body()!!
                    showCategories(categories.map { it.capitalize() })

                } else {
                    Toast.makeText(context, "No response from server.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<String>>, t: Throwable) {
                Toast.makeText(context, "Please check your internet connection.", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun showCategories(categories: List<String>?) {
        categories?.let {
            val adapter = CategoryAdapter(categories) { selectedCategory ->
                val productListFragment = ProductList()
                val bundle = Bundle()
                bundle.putString("selectedCategory", selectedCategory)
                productListFragment.arguments = bundle
                (activity as? MainPage)?.replaceFragment(productListFragment)
            }

            binding.rvCategories.layoutManager = LinearLayoutManager(context)
            binding.rvCategories.adapter = adapter
        }
    }
}