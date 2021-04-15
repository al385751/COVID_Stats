package com.example.covidstats.insertDataActiviy

import android.view.View
import com.example.covidstats.database.Country
import com.example.covidstats.database.Region
import com.example.covidstats.database.Subregion

interface MainView {
    var countryVisible: Boolean
    var regionVisible: Boolean
    var subregionVisible: Boolean

    var progressBarVisible: Boolean

    var enableCountryOkButton: Boolean
    var enableRegionOkButton: Boolean
    var enableSubregionOkButton: Boolean

    var datesVisible: Boolean

    fun showError(message: String)

    fun showCountries(countries: List<Country>)
    fun showRegions(regions: List<Region>)
    fun showSubregions(subregions: List<Subregion>)

    fun showDatePickerFromDialog(view: View)
    fun showDatePickerToDialog(view: View)

    fun changeToDisplayCountryData(view: View)
    fun changeToDisplayRegionData(view: View)
    fun changeToDisplaySubregionData(view: View)
}