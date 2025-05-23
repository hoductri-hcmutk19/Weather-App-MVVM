package com.app.hiweather.base

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.viewbinding.ViewBinding
import com.app.hiweather.ui.SharedViewModel

typealias ActivityInflate<T> = (LayoutInflater) -> T

abstract class BaseActivity<VB : ViewBinding>(private val inflate: ActivityInflate<VB>) : AppCompatActivity() {
    private var _binding: VB? = null

    val binding get() = _binding!!
    abstract val viewModel: ViewModel
    abstract val sharedViewModel: SharedViewModel

    protected abstract fun initialize()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = inflate.invoke(layoutInflater)
        setContentView(binding.root)
        initialize()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
