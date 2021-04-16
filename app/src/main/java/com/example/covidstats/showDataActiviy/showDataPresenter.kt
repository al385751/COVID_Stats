package com.example.covidstats.showDataActiviy

import com.android.volley.Response
import com.example.covidstats.aditionalClasses.DayData
import com.example.covidstats.database.Country
import com.example.covidstats.database.Region
import com.example.covidstats.database.Subregion
import com.example.covidstats.model.Model

class showDataPresenter (val view: ShowDataView, val model: Model) {

    init {
        view.progressBarVisible = true
        view.recyclerViewVisible = false
    }

    fun getCountryData(country: Country, fromDate: String, toDate: String) {
        model.getCountryData(Response.Listener<ArrayList<DayData>> { countryData ->
            if (countryData != null) {
                view.createRecyclerView(countryData)
                view.progressBarVisible = false
                view.recyclerViewVisible = true
            } else {
                view.showError("No data found for the introduced period (POSSIBLE BAD JSON)")
            }
        }, Response.ErrorListener { error -> view.showError(error.toString()) }, country, fromDate, toDate)
    }

    fun getRegionData(country: Country, region: Region, fromDate: String, toDate: String) {
        model.getRegionData(Response.Listener<ArrayList<DayData>> { regionData ->
            if (regionData != null) {
                view.createRecyclerView(regionData)
                view.progressBarVisible = false
                view.recyclerViewVisible = true
            } else {
                view.showError("No data found for the introduced period (POSSIBLE BAD JSON)")
            }
        }, Response.ErrorListener { error -> view.showError(error.toString()) }, country, region, fromDate, toDate)
    }

    fun getSubregionData(country: Country, subregion: Subregion, fromDate: String, toDate: String) {
        model.getSubegionData(Response.Listener<ArrayList<DayData>> { subregionData ->
            if (subregionData != null) {
                view.createRecyclerView(subregionData)
                view.progressBarVisible = false
                view.recyclerViewVisible = true
            } else {
                view.showError("No data found for the introduced period (POSSIBLE BAD JSON)")
            }
        }, Response.ErrorListener { error -> view.showError(error.toString()) }, country, subregion, fromDate, toDate)
    }
}