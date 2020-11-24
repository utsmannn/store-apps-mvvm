package com.utsman.home.ui

import android.os.Bundle
import android.view.View
import android.viewbinding.library.fragment.viewBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.utsman.abstraction.di.moduleOf
import com.utsman.abstraction.dto.listenOn
import com.utsman.abstraction.ext.bindToProgressView
import com.utsman.abstraction.ext.loge
import com.utsman.abstraction.ext.logi
import com.utsman.abstraction.listener.IResultState
import com.utsman.data.model.dto.AppsView
import com.utsman.data.model.dto.CategoryView
import com.utsman.home.R
import com.utsman.home.databinding.FragmentHomeBinding
import com.utsman.home.di.homeViewModel
import com.utsman.home.ui.adapter.CategoryAdapter

class HomeFragment : Fragment(R.layout.fragment_home) {

    private val binding: FragmentHomeBinding by viewBinding()
    private val categoryAdapter = CategoryAdapter()
    private val viewModel by moduleOf(homeViewModel)

    private val appsRandomListener = object : IResultState<List<AppsView>> {
        override fun onIdle() {
            logi("idle...")
        }

        override fun onLoading() {
            logi("loading...")
        }

        override fun onSuccess(data: List<AppsView>) {
            logi("success...")
            val defaultCategory = CategoryView.simple {
                name = "Random apps for you"
                apps = data
            }
            categoryAdapter.addItem(defaultCategory)
        }

        override fun onError(throwable: Throwable) {
            loge("error on -> \n${throwable.localizedMessage}")
            throwable.printStackTrace()
        }
    }

    private val appsCategoryListener = object : IResultState<List<CategoryView>> {
        override fun onIdle() {
            logi("idle...")
        }

        override fun onLoading() {
            logi("loading...")
        }

        override fun onSuccess(data: List<CategoryView>) {
            logi("success...")
            categoryAdapter.updateItems(data)
        }

        override fun onError(throwable: Throwable) {
            loge("error on -> \n${throwable.localizedMessage}")
            throwable.printStackTrace()
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvHome.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = categoryAdapter
        }

        viewModel.getRandomApps()
        viewModel.randomList.observe(viewLifecycleOwner, Observer { result ->
            result.listenOn(appsRandomListener)
        })

        viewModel.getCategories()
        viewModel.categories.observe(viewLifecycleOwner, Observer { result ->
            result.listenOn(appsCategoryListener)
            result.bindToProgressView(binding.progressCircular)
        })
    }
}