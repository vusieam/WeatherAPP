package co.sz.vusieam.mobileweathertest.viewmodels.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import co.sz.vusieam.mobileweathertest.R
import co.sz.vusieam.mobileweathertest.models.entities.WeatherEntity
import co.sz.vusieam.mobileweathertest.models.linkedentities.WeatherAndCityLinked
import co.sz.vusieam.mobileweathertest.views.fragments.Favourites
import com.amulyakhare.textdrawable.util.ColorGenerator

class FavouriteWeatherAdapter (private val context: Context, private val fragment: Favourites
,val adapterClickViewDetailsInterface: AdapterClickViewDetailsInterface, val adapterClickOpenMapsInterface: AdapterClickOpenMapsInterface
)
    : RecyclerView.Adapter<FavouriteWeatherAdapter.ViewHolder>(){
    private var selectedPosition:Int = -1
    private val allWeatherAndCity = ArrayList<WeatherAndCityLinked>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.single_favourite_content_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        try{
            val generator = ColorGenerator.MATERIAL // or use DEFAULT

            val color = generator.randomColor

            holder.layoutSingleViewCityTemperature.setBackgroundColor(color)

            if (selectedPosition == position){
                holder.contentLayout.setBackgroundResource(R.drawable.border_base_red)
//                holder.contentLayout.setBackgroundResource(R.drawable.border_base_red)
            }
            else
            {
//                holder.contentLayout.setBackgroundResource(R.drawable.border_base_blue)
                holder.contentLayout.setBackgroundResource(R.drawable.border_base_blue)
            }

            holder.tvSingleViewDate.text = allWeatherAndCity[position].weather.date
            holder.tvSingleViewTemperature.text = fragment.getString(R.string.degree_symbol, allWeatherAndCity[position].weather.temperature.toString())

            holder.tvSingleViewCity.text = allWeatherAndCity[position].city.cityName

            holder.btnSingleViewDetails.setOnClickListener{
                adapterClickViewDetailsInterface.onViewDetailsClick(allWeatherAndCity[position].weather)
            }

            holder.btnSingleViewGoogleMaps.setOnClickListener{
                adapterClickOpenMapsInterface.onOpenMapsClick(allWeatherAndCity[position].weather)
            }

            holder.contentLayout.setOnClickListener {

            }
        }
        catch(e: Exception){
        }
    }

    override fun getItemCount(): Int {
        return allWeatherAndCity.size
    }

    // below method is use to update our list of notes.
    fun updateList(newList: List<WeatherAndCityLinked>) {
        // on below line we are clearing
        // our notes array list
        allWeatherAndCity.clear()
        // on below line we are adding a
        // new list to our all notes list.
        allWeatherAndCity.addAll(newList)
        // on below line we are calling notify data
        // change method to notify our adapter.
        notifyDataSetChanged()
    }


    inner class ViewHolder(itemView:View) : RecyclerView.ViewHolder(itemView) {
        val contentLayout = itemView.findViewById<ConstraintLayout>(R.id.singleWeatherInfoLayout)!!
        val tvSingleViewCity = itemView.findViewById<TextView>(R.id.tvSingleViewCity)!!
        val tvSingleViewTemperature = itemView.findViewById<TextView>(R.id.tvSingleViewTemperature)!!
        val tvSingleViewDate = itemView.findViewById<TextView>(R.id.tvSingleViewDate)!!
        val btnSingleViewDetails = itemView.findViewById<View>(R.id.btnSingleViewDetails)!!
        val btnSingleViewGoogleMaps = itemView.findViewById<View>(R.id.btnSingleViewGoogleMaps)!!
        val layoutSingleViewCityTemperature = itemView.findViewById<View>(R.id.layoutSingleViewCityTemperature)!!
    }
}

interface AdapterClickOpenMapsInterface {
    // creating a method for click
    // action on delete image view.
    fun onOpenMapsClick(weather: WeatherEntity)
}

interface AdapterClickViewDetailsInterface {
    // creating a method for click action
    // on recycler view item for updating it.
    fun onViewDetailsClick(weather: WeatherEntity)
}