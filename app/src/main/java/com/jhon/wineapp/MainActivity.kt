package com.jhon.wineapp

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.jhon.wineapp.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Dispatcher
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.random.Random

class MainActivity : AppCompatActivity(), OnclickListener {

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
        setupSwipeRefresh()
    }

    private fun setupSwipeRefresh() {
        binding.srlWines.setOnRefreshListener {
            adapter.submitList(listOf())
            getWines()
        }
    }

    private fun setupAdapter() {
        adapter = WineListAdapter()
        adapter.setOnClickListener(this)
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
            try {
                val serverOK = Random.nextBoolean()
                val wines = if (serverOK) service.getRedWines() else listOf()
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

    private fun showMsg(msgRes: Int) {
        Snackbar.make(binding.root, msgRes, Snackbar.LENGTH_SHORT).show()
    }

    private fun showRecyclerView(isVisible: Boolean) {
        binding.recycleView.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    private fun showNoDataView(isVisible: Boolean) {
        binding.tvNoData.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    private fun showProgress(isVisible: Boolean) {
        binding.srlWines.isRefreshing = isVisible
    }


    private fun setupRetrofit() {
        var retrofit = Retrofit.Builder().baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()).build()
        service = retrofit.create(WineService::class.java)
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
        MaterialAlertDialogBuilder(this)
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
            val result = WineApplication.database.wineDao().addWine(wine)
            if (result != -1L) {
                showMsg(R.string.room_save_success)
            } else {
                showMsg(R.string.room_save_fail)
            }
        }

    }
}