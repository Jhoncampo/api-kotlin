package com.jhon.wineapp

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FavouriteFragment: BaseFragment(), OnclickListener {
    private lateinit var adapter: WineFavListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupAdapter()
        setupRecyclerView()
        setupSwipeRefresh()
    }



    private fun setupAdapter() {
        adapter = WineFavListAdapter()
        adapter.setOnClickListener(this)
    }

    private fun setupRecyclerView() {
        binding.recycleView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@FavouriteFragment.adapter
        }
    }
    private fun setupSwipeRefresh() {
        binding.srlWines.setOnRefreshListener {
            adapter.submitList(listOf())
            getWines()
        }
    }
    override fun getWines() {
        lifecycleScope.launch(Dispatchers.IO) {
            //val wines = getLocalWines()
            try {
                val wines = WineApplication.database.wineDao().getAllWines()
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
    override fun onLongClick(wine: Wine) {}
    override fun onFavourite(wine: Wine) {
        TODO("Not yet implemented")
    }
}