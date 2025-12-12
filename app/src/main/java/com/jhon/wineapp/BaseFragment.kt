package com.jhon.wineapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.jhon.wineapp.databinding.FragmentHomeBinding

open class BaseFragment: Fragment() {
    private var _binding: FragmentHomeBinding? = null
    protected val binding get() = _binding!!
    private lateinit var adapter: WineListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupSwipeRefresh()
    }



    private fun setupSwipeRefresh() {
        binding.srlWines.setOnRefreshListener {
            adapter.submitList(listOf())
            getWines()
        }
    }
    open protected fun getWines(){

    }

    protected fun showMsg(msgRes: Int) {
        Snackbar.make(binding.root, msgRes, Snackbar.LENGTH_SHORT).show()
    }

    protected fun showRecyclerView(isVisible: Boolean) {
        binding.recycleView.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    protected fun showNoDataView(isVisible: Boolean) {
        binding.tvNoData.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    protected fun showProgress(isVisible: Boolean) {
        binding.srlWines.isRefreshing = isVisible
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}