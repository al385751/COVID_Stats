package com.example.covidstats.model

import android.content.Context
import android.util.Log
import com.android.volley.Response
import com.example.covidstats.database.Country
import com.example.covidstats.database.MyDatabase
import com.example.covidstats.database.Region
import com.example.covidstats.database.Subregion
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class Model(context: Context) {

    private val network = Network.getInstance(context)
    private val database : MyDatabase = MyDatabase.getInstance(context)

    fun getCountries(listener: Response.Listener<List<Country>>, errorListener: Response.ErrorListener) =
            GlobalScope.launch(Dispatchers.Main) {
                val countries = withContext(Dispatchers.IO) {
                    Log.d("MY COVID STATS", "Voy a acceder a la base de datos a por los países")
                    database.dao.getCountries()
                }
                if (countries.isEmpty()) {
                    Log.d("MY COVID STATS", "No hay nada en la base de datos, guardando nuevos datos...")
                    network.getCountries(Response.Listener {
                        GlobalScope.launch {
                            database.dao.insertCountries(it)
                        }
                        listener.onResponse(it)
                    }, errorListener)
                }
                else
                    listener.onResponse(countries)
    }

    fun getRegions(listener: Response.Listener<List<Region>>, errorListener: Response.ErrorListener, country : Country) =
            GlobalScope.launch(Dispatchers.Main) {
                val regions = withContext(Dispatchers.IO) {
                    Log.d("MY COVID STATS", "Voy a acceder a la base de datos a por las regiones")
                    database.dao.getRegions(country.id)
                }
                if (regions.isEmpty()) {
                    Log.d("MY COVID STATS", "No hay nada en la base de datos, guardando nuevos datos...")
                    network.getRegions(Response.Listener {
                        GlobalScope.launch {
                            database.dao.insertRegions(it)
                        }
                        listener.onResponse(it)
                    }, errorListener, country)
                }
                else
                    listener.onResponse(regions)
            }

    fun getSubregions(listener: Response.Listener<List<Subregion>>, errorListener: Response.ErrorListener, region: Region) =
        GlobalScope.launch(Dispatchers.Main) {
            val subregions = withContext(Dispatchers.IO) {
                Log.d("MY COVID STATS", "Voy a acceder a la base de datos a por las subregiones")
                database.dao.getSubregions(region.id)
            }
            if (subregions.isEmpty()) {
                Log.d("MY COVID STATS", "No hay nada en la base de datos, guardando nuevos datos...")
                network.getSubregions(Response.Listener {
                    GlobalScope.launch {
                        database.dao.insertSubregions(it)
                    }
                    listener.onResponse(it)
                }, errorListener, region)
            }
            else
                listener.onResponse(subregions)
        }
    }