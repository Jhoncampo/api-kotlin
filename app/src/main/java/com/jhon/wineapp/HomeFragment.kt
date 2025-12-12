package com.jhon.wineapp

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class HomeFragment : BaseFragment(), OnclickListener {

    private lateinit var adapter: WineListAdapter
    private lateinit var service: WineService


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupAdapter()
        setupRecyclerView()
        setupRetrofit()
        setupSwipeRefresh()
    }


    private fun setupAdapter() {
        adapter = WineListAdapter()
        adapter.setOnClickListener(this)
    }

    private fun setupRecyclerView() {
        binding.recycleView.apply {
            layoutManager = StaggeredGridLayoutManager(3, RecyclerView.VERTICAL)
            adapter = this@HomeFragment.adapter
        }
    }

    private fun setupSwipeRefresh() {
        binding.srlWines.setOnRefreshListener {
            adapter.submitList(listOf())
            getWines()
        }
    }

    private fun setupRetrofit() {
        var retrofit = Retrofit.Builder().baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()).build()
        service = retrofit.create(WineService::class.java)
    }

    override fun getWines() {
        lifecycleScope.launch(Dispatchers.IO) {
            //val wines = getLocalWines()
            try {

                val wines =  service.getRedWines()
                withContext(Dispatchers.Main) {
                    if (wines.isNotEmpty()) {
                        showRecyclerView(true)
                        showNoDataView(false)
                        adapter.submitList(wines)
                    } else {
                        showRecyclerView(false)
                        showNoDataView(true)
                    }

                }

            } catch (e: Exception) {
                showMsg((R.string.common_resquest_fail))
            } finally {
                showProgress(false)
            }
        }
    }


    override fun onResume() {
        super.onResume()
        showProgress(true)
        getWines()
    }


    /*
    OnClickListener
    * */
    override fun onLongClick(wine: Wine) {
        val options = resources.getStringArray(R.array.array_dialog_add_options)
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.dialog_add_fav_title)
            .setItems(options) { _, index ->
                when (index) {
                    0 -> addToFavorites(wine)
                }
            }
            .show()
    }

    private fun addToFavorites(wine: Wine) {
        lifecycleScope.launch(Dispatchers.IO) {
            wine.isFavourite = true
            val result = WineApplication.database.wineDao().addWine(wine)
            if (result != -1L) {
                showMsg(R.string.room_save_success)
            } else {
                showMsg(R.string.room_save_fail)
            }
        }

    }

    override fun onFavourite(wine: Wine) {

    }
}