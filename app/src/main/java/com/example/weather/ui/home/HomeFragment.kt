package com.example.weather.ui.home

import com.example.weather.databinding.FragmentHomeBinding
import com.sun.android.base.BaseFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {

    override val viewModel: HomeViewModel by viewModel()
    override fun initView() {
        // todo implement later
    }

    override fun initData() {
        // todo implement later
    }

    override fun bindData() {
        // todo implement later
    }
}
