package com.app.hiweather.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.viewbinding.ViewBinding
import com.app.hiweather.ui.SharedViewModel

typealias FragmentInflate<T> = (LayoutInflater, ViewGroup?, Boolean) -> T

/**
 * class HomeFragment() : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {
 */
abstract class BaseFragment<VB : ViewBinding>(private val inflate: FragmentInflate<VB>) : Fragment() {
    private var _binding: VB? = null

    val binding get() = _binding!!
    abstract val viewModel: ViewModel
    abstract val sharedViewModel: SharedViewModel

    protected abstract fun initView()

    protected abstract fun initData()

    protected abstract fun bindData()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = inflate.invoke(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initData()
        bindData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
