package com.example.covidstats.model

import android.content.Context
import android.util.Log
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.covidstats.database.Country
import com.example.covidstats.database.Region
import com.example.covidstats.database.Subregion
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

private const val BASE_URL = "https://api.covid19tracking.narrativa.com/api"

private const val COUNTRIES = "countries"
private const val REGIONS = "regions"
private const val SUBREGIONS = "sub_regions"

private const val NAME_LABEL = "name"
private const val ID_LABEL = "id"

class Network private constructor (context: Context) {
    companion object: SingletonHolder<Network, Context>(::Network)

    val queue = Volley.newRequestQueue(context)

    fun getCountries(listener: Response.Listener<List<Country>>, errorListener: Response.ErrorListener) {
        val url = "$BASE_URL/$COUNTRIES"
        Log.d("MY COVID STATS", "La URL para buscar countries es $url")
        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null,
            { response -> processCountries(response, listener) },
            { error -> errorListener.onErrorResponse(error) })
        queue.add(jsonObjectRequest)
    }

    fun getRegions(listener: Response.Listener<List<Region>>, errorListener: Response.ErrorListener, country: Country) {
        val url = "$BASE_URL/$COUNTRIES/${country.id}/$REGIONS"
        Log.d("MY COVID STATS", "La URL para buscar regions es $url")
        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null,
                { response -> processRegions(response, listener, country.id) },
                { error -> errorListener.onErrorResponse(error) })
        queue.add(jsonObjectRequest)
    }

    fun getSubregions(listener: Response.Listener<List<Subregion>>, errorListener: Response.ErrorListener, region: Region) {
        val url = "$BASE_URL/$COUNTRIES/${region.countryId}/$REGIONS/${region.id}/$SUBREGIONS"
        Log.d("MY COVID STATS", "La URL para buscar subregions es $url")
        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null,
                { response -> processSubregions(response, listener, region) },
                { error -> errorListener.onErrorResponse(error) })
        queue.add(jsonObjectRequest)
    }

    private fun processSubregions(response: JSONObject, listener: Response.Listener<List<Subregion>>, region: Region) {
        val subregions = ArrayList<Subregion>()
        try {
            val countryArray = response.getJSONArray(COUNTRIES)
            for (i in 0 until countryArray.length()) {
                val countryObject : JSONObject = countryArray[i] as JSONObject
                val regionArray : JSONObject = countryObject.getJSONObject(region.countryId)
                val subregionArray = regionArray.getJSONArray(region.id)
                for (j in 0 until subregionArray.length()) {
                    val subregionObject : JSONObject = subregionArray[j] as JSONObject
                    val name = subregionObject.getString(NAME_LABEL)
                    val id = subregionObject.getString(ID_LABEL)
                    subregions.add(Subregion(name, id, region.countryId, region.id))
                }
            }
        } catch (o: JSONException) {
            listener.onResponse(null)
        }

        subregions.sortBy { it.name }

        if (subregions.isNotEmpty())
            Log.d("MY COVID STATS", "Primera subregión de la región: ${subregions[0]}")

        listener.onResponse(subregions)
    }

    private fun processRegions(response: JSONObject, listener: Response.Listener<List<Region>>, countryId: String) {
        val regions = ArrayList<Region>()
        try {
            val countryArray = response.getJSONArray(COUNTRIES)
            for (i in 0 until countryArray.length()) {
                val countryObject: JSONObject = countryArray[i] as JSONObject
                val regionArray = countryObject.getJSONArray(countryId)
                for (j in 0 until regionArray.length()) {
                    val regionObject : JSONObject = regionArray[j] as JSONObject
                    val name = regionObject.getString(NAME_LABEL)
                    val id = regionObject.getString(ID_LABEL)
                    regions.add(Region(name, id, countryId))
                }
            }
        } catch (o: JSONException) {
            listener.onResponse(null)
        }

        regions.sortBy { it.name }

        if (regions.isNotEmpty())
            Log.d("MY COVID STATS", "Primera región del país: ${regions[0]}")

        listener.onResponse(regions)
    }

    private fun processCountries(response: JSONObject, listener: Response.Listener<List<Country>>) {
        val countries = ArrayList<Country>()
        try {
            val countryArray = response.getJSONArray(COUNTRIES)
            for (i in 0 until countryArray.length()) {
                val countryObject: JSONObject = countryArray[i] as JSONObject
                val name = countryObject.getString(NAME_LABEL)
                val id = countryObject.getString(ID_LABEL)
                countries.add(Country(name, id))
            }
        } catch (o: JSONException) {
             listener.onResponse(null)
        }

        countries.sortBy { it.name }
        Log.d("MY COVID STATS", "Primer país: ${countries[0]}")
        listener.onResponse(countries)
    }
}
