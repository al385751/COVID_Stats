package com.example.covidstats.model

import android.content.Context
import android.util.Log
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.covidstats.aditionalClasses.DayData
import com.example.covidstats.database.Country
import com.example.covidstats.database.Region
import com.example.covidstats.database.Subregion
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

private const val BASE_URL = "https://api.covid19tracking.narrativa.com/api"

private const val COUNTRIES = "countries"
private const val COUNTRY = "country"
private const val REGIONS = "regions"
private const val REGION = "region"
private const val SUBREGIONS = "sub_regions"
private const val SUBREGION = "sub_region"
private const val DATES = "dates"

private const val DATE_FROM = "date_from"
private const val DATE_TO = "date_to"

private const val NAME_LABEL = "name"
private const val ID_LABEL = "id"
private const val TODAY_TOTAL_CONFIRMED = "today_confirmed"
private const val TODAY_TOTAL_DEATHS = "today_deaths"
private const val TODAY_TOTAL_RECOVERED = "today_recovered"
private const val TODAY_NEW_CONFIRMED = "today_new_confirmed"
private const val TODAY_NEW_DEATHS = "today_new_deaths"
private const val TODAY_NEW_RECOVERED = "today_new_recovered"

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

    fun getCountryData(listener: Response.Listener<ArrayList<DayData>>, errorListener: Response.ErrorListener, country: Country, fromDate: String, toDate: String) {
        val url = "$BASE_URL/$COUNTRY/${country.id}?$DATE_FROM=$fromDate&$DATE_TO=$toDate"
        Log.d("MY COVID STATS", "La URL para buscar los datos del país es es $url")
        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null,
                { response -> processCountryData(response, listener, country) },
                { error -> errorListener.onErrorResponse(error) })
        queue.add(jsonObjectRequest)
    }


    fun getRegionData(listener: Response.Listener<java.util.ArrayList<DayData>>, errorListener: Response.ErrorListener, country: Country, region: Region, fromDate: String, toDate: String) {
        val url = "$BASE_URL/$COUNTRY/${region.countryId}/$REGION/${region.id}?$DATE_FROM=$fromDate&$DATE_TO=$toDate"
        Log.d("MY COVID STATS", "La URL para buscar los datos de la región es es $url")
        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null,
                { response -> processRegionData(response, listener, country, region) },
                { error -> errorListener.onErrorResponse(error) })
        queue.add(jsonObjectRequest)
    }


    fun getSubregionData(listener: Response.Listener<java.util.ArrayList<DayData>>, errorListener: Response.ErrorListener, country: Country, subregion: Subregion, fromDate: String, toDate: String) {
        val url = "$BASE_URL/$COUNTRY/${subregion.countryId}/$REGION/${subregion.regionId}/$SUBREGION/${subregion.id}?$DATE_FROM=$fromDate&$DATE_TO=$toDate"
        Log.d("MY COVID STATS", "La URL para buscar los datos de la región es es $url")
        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null,
                { response -> processSubregionData(response, listener, country, subregion) },
                { error -> errorListener.onErrorResponse(error) })
        queue.add(jsonObjectRequest)
    }

    private fun processSubregionData(response: JSONObject, listener: Response.Listener<java.util.ArrayList<DayData>>, country: Country, subregion: Subregion) {
        val subregionData = ArrayList<DayData>()
        try {
            val datesObject = response.getJSONObject(DATES)
            for (data in datesObject.keys()) {
                val dateObject : JSONObject = datesObject.getJSONObject(data)
                val countriesObject : JSONObject = dateObject.getJSONObject(COUNTRIES)
                val countryObject : JSONObject = countriesObject.getJSONObject(country.name)
                val regionsArray : JSONArray = countryObject.getJSONArray(REGIONS)
                for (i in 0 until regionsArray.length()) {
                    val regionObject : JSONObject = regionsArray[i] as JSONObject
                    if (regionObject.getString(ID_LABEL) == subregion.regionId) {
                        val subregionsArray : JSONArray = regionObject.getJSONArray(SUBREGIONS)
                        for (j in 0 until subregionsArray.length()) {
                            val subregionObject : JSONObject = subregionsArray[j] as JSONObject
                            if (subregionObject.getString(ID_LABEL) == subregion.id) {
                                val totalConfirmed = subregionObject.optString(TODAY_TOTAL_CONFIRMED, "unknown")
                                val totalDeaths = subregionObject.optString(TODAY_TOTAL_DEATHS, "unknown")
                                val totalRecovered = subregionObject.optString(TODAY_TOTAL_RECOVERED, "unknown")
                                val todayNewConfirmed = subregionObject.optString(TODAY_NEW_CONFIRMED, "unknown")
                                val todayNewDeaths = subregionObject.optString(TODAY_NEW_DEATHS, "unknown")
                                val todayNewRecovered = subregionObject.optString(TODAY_NEW_RECOVERED, "unknown")
                                subregionData.add(DayData(data, todayNewConfirmed, totalConfirmed, todayNewDeaths, totalDeaths, todayNewRecovered, totalRecovered))
                            }
                        }
                    }
                }
            }
        } catch (o: JSONException) {
            listener.onResponse(null)
        }

        subregionData.sortBy { it.date }
        listener.onResponse(subregionData)
    }

    private fun processRegionData(response: JSONObject, listener: Response.Listener<java.util.ArrayList<DayData>>, country: Country, region: Region) {
        val regionData = ArrayList<DayData>()
        try {
            val datesObject = response.getJSONObject(DATES)
            for (data in datesObject.keys()) {
                val dateObject : JSONObject = datesObject.getJSONObject(data)
                val countriesObject : JSONObject = dateObject.getJSONObject(COUNTRIES)
                val countryObject : JSONObject = countriesObject.getJSONObject(country.name)
                val regionsArray : JSONArray = countryObject.getJSONArray(REGIONS)
                for (i in 0 until regionsArray.length()) {
                    val regionObject : JSONObject = regionsArray[i] as JSONObject
                    if (regionObject.getString(ID_LABEL) == region.id) {
                        val totalConfirmed = regionObject.optString(TODAY_TOTAL_CONFIRMED, "unknown")
                        val totalDeaths = regionObject.optString(TODAY_TOTAL_DEATHS, "unknown")
                        val totalRecovered = regionObject.optString(TODAY_TOTAL_RECOVERED, "unknown")
                        val todayNewConfirmed = regionObject.optString(TODAY_NEW_CONFIRMED, "unknown")
                        val todayNewDeaths = regionObject.optString(TODAY_NEW_DEATHS, "unknown")
                        val todayNewRecovered = regionObject.optString(TODAY_NEW_RECOVERED, "unknown")
                        regionData.add(DayData(data, todayNewConfirmed, totalConfirmed, todayNewDeaths, totalDeaths, todayNewRecovered, totalRecovered))
                    }
                }
            }
        } catch (o: JSONException) {
            listener.onResponse(null)
        }

        regionData.sortBy { it.date }
        listener.onResponse(regionData)
    }

    private fun processCountryData(response: JSONObject, listener: Response.Listener<java.util.ArrayList<DayData>>, country: Country) {
        val countryData = ArrayList<DayData>()
        try {
            val datesObject = response.getJSONObject(DATES)
            for (data in datesObject.keys()) {
                val dateObject : JSONObject = datesObject.getJSONObject(data)
                val countriesObject : JSONObject = dateObject.getJSONObject(COUNTRIES)
                val countryObject : JSONObject = countriesObject.getJSONObject(country.name)
                val totalConfirmed = countryObject.optString(TODAY_TOTAL_CONFIRMED, "unknown")
                val totalDeaths = countryObject.optString(TODAY_TOTAL_DEATHS, "unknown")
                val totalRecovered = countryObject.optString(TODAY_TOTAL_RECOVERED, "unknown")
                val todayNewConfirmed = countryObject.optString(TODAY_NEW_CONFIRMED, "unknown")
                val todayNewDeaths = countryObject.optString(TODAY_NEW_DEATHS, "unknown")
                val todayNewRecovered = countryObject.optString(TODAY_NEW_RECOVERED, "unknown")
                countryData.add(DayData(data, todayNewConfirmed, totalConfirmed, todayNewDeaths, totalDeaths, todayNewRecovered, totalRecovered))
            }
        } catch (o: JSONException) {
            listener.onResponse(null)
        }

        countryData.sortBy { it.date }
        listener.onResponse(countryData)
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
