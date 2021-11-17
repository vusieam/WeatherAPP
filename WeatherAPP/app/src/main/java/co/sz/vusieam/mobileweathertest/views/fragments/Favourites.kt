package co.sz.vusieam.mobileweathertest.views.fragments

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import co.sz.vusieam.mobileweathertest.R
import co.sz.vusieam.mobileweathertest.models.entities.WeatherEntity
import co.sz.vusieam.mobileweathertest.viewmodels.WeatherViewModel
import co.sz.vusieam.mobileweathertest.viewmodels.adapters.AdapterClickOpenMapsInterface
import co.sz.vusieam.mobileweathertest.viewmodels.adapters.AdapterClickViewDetailsInterface
import co.sz.vusieam.mobileweathertest.viewmodels.adapters.FavouriteWeatherAdapter

class Favourites : Fragment(), AdapterClickOpenMapsInterface, AdapterClickViewDetailsInterface {

    private lateinit var viewModel: WeatherViewModel
    private lateinit var favouritesRV: RecyclerView

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_favourites, container, false)
        viewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application))
            .get(WeatherViewModel::class.java)

        favouritesRV = view.findViewById(R.id.rvSavedWeathersList)
        favouritesRV.layoutManager = LinearLayoutManager(view.context)
        val adapter = FavouriteWeatherAdapter( view.context, this, this, this )
        favouritesRV.adapter = adapter
        viewModel.allWeatherAndCity.observe(requireActivity(), Observer { list ->
            list.let {
                adapter.updateList(it)
            }
        })

        return view
    }

    override fun onOpenMapsClick(weather: WeatherEntity) {

    }

    override fun onViewDetailsClick(weather: WeatherEntity) {

    }


}