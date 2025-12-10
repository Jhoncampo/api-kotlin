package com.jhon.wineapp

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.jhon.wineapp.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private lateinit var adapter: WineListAdapter
    private lateinit var binding: ActivityMainBinding
    private lateinit var service: WineService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
//        enableEdgeToEdge()
        setContentView(binding.root)

        setupAdapter()
        setupRecyclerView()
        setupRetrofit()
    }

    private fun setupAdapter() {
        adapter = WineListAdapter()
    }

    private fun setupRecyclerView() {
        binding.recycleView.apply {
            layoutManager = StaggeredGridLayoutManager(3, RecyclerView.VERTICAL)
            adapter = this@MainActivity.adapter
        }
    }

    private fun getWines() {
        lifecycleScope.launch(Dispatchers.IO) {
            //val wines = getLocalWines()
            val wines = service.getRedWines()
            withContext(Dispatchers.Main){
                adapter.submitList(wines)
            }
        }
    }


    private fun setupRetrofit ( ){
        var retrofit = Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        service = retrofit.create(WineService::class.java)
    }

    private fun getLocalWines() = listOf(
        Wine(
            "Don Mario 12",
            "Carlos Pérez",
            Rating("4.7", "389 ratings"),
            "Argentina",
            "https://picsum.photos/200",
            2
        ),

        Wine(
            "La Montaña Roja",
            "Lucía Herrera",
            Rating("4.5", "512 ratings"),
            "Chile",
            "https://picsum.photos/200",
            3
        ),

        Wine(
            "Reserva del Valle",
            "Miguel Torres",
            Rating("4.8", "620 ratings"),
            "España",
            "https://picsum.photos/200",
            4
        ),

        Wine(
            "Monte Verde Classic",
            "Ana Rodríguez",
            Rating("4.6", "301 ratings"),
            "Italia",
            "https://picsum.photos/200",
            5
        )

    )

    override fun onResume() {
        super.onResume()
        getWines()
    }
}