package com.example.weather.ui.favorite

import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weather.R
import com.example.weather.base.BaseFragment
import com.example.weather.data.model.entity.Weather
import com.example.weather.databinding.FragmentFavoriteBinding
import com.example.weather.ui.SharedViewModel
import com.example.weather.ui.adapter.FavoriteAdapter
import com.example.weather.ui.detail.DetailFragment
import com.example.weather.utils.Constant
import com.example.weather.utils.goBackFragment
import com.example.weather.utils.listener.OnItemClickListener
import com.example.weather.utils.replaceFragmentToActivity
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.IOException

class FavoriteFragment :
    BaseFragment<FragmentFavoriteBinding>(FragmentFavoriteBinding::inflate),
    OnItemClickListener {

    private val favoriteAdapter: FavoriteAdapter by lazy {
        FavoriteAdapter(this)
    }
    private lateinit var mWeatherList: List<Weather>

    override val viewModel: FavoriteViewModel by viewModel()
    override val sharedViewModel: SharedViewModel by activityViewModel()

    override fun initView() {
        binding.recyclerViewFavorite.adapter = favoriteAdapter
        binding.recyclerViewFavorite.layoutManager = LinearLayoutManager(context)

        val dividerItemDecoration = DividerItemDecoration(
            binding.recyclerViewFavorite.context,
            LinearLayoutManager.VERTICAL
        )
        context?.let { context ->
            ContextCompat.getDrawable(
                context,
                R.drawable.divider_white
            )
        }?.let { dividerItemDecoration.setDrawable(it) }
        binding.recyclerViewFavorite.addItemDecoration(dividerItemDecoration)

        binding.layoutHeader.icBack.setOnClickListener {
            goBackFragment()
        }
    }

    override fun initData() {
        viewModel.getAllFavorite()
    }

    override fun bindData() {
        viewModel.weatherList.observe(this) { weatherList ->
            mWeatherList = weatherList
            favoriteAdapter.updateData(mWeatherList)
            if (weatherList.isEmpty()) {
                binding.recyclerViewFavorite.visibility = View.GONE
                binding.tvError.visibility = View.VISIBLE
            } else {
                binding.recyclerViewFavorite.visibility = View.VISIBLE
                binding.tvError.visibility = View.GONE
            }
        }
    }

    override fun onItemClickListener(view: View, position: Int, action: String) {
        val id = mWeatherList[position].id
        if (action == Constant.DELETE) {
            try {
                viewModel.removeFavoriteWeather(id)
            } catch (e: IOException) {
                Log.e("FavoriteFragment", "Exception occurred", e)
            } finally {
                viewModel.getAllFavorite()
            }
        } else if (action == Constant.DETAIL) {
            activity?.let {
                (it as AppCompatActivity).replaceFragmentToActivity(
                    it.supportFragmentManager,
                    DetailFragment.newInstance(mWeatherList[position]),
                    R.id.container
                )
            }
        }
    }

    companion object {
        fun newInstance() =
            FavoriteFragment().apply {
                arguments = bundleOf()
            }
    }
}
