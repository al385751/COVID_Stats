package com.example.covidstats.insertDataActiviy

import android.os.Debug
import android.util.Log
import com.android.volley.Response
import com.example.covidstats.database.Country
import com.example.covidstats.database.Region
import com.example.covidstats.database.Subregion
import com.example.covidstats.model.Model

class Presenter (val view: MainView, val model: Model) {
    private var country: Country? = null
    private var region: Region? = null
    private var subregion: Subregion? = null
    private var fromDate: String? = null
    private var toDate: String? = null

    init {
        view.countryVisible = false
        view.regionVisible = false
        view.subregionVisible = false
        view.enableCountryOkButton = false
        view.enableRegionOkButton = false
        view.enableSubregionOkButton = false
        view.progressBarVisible = true
        view.datesVisible = false

        Log.d("MY COVID STATS", "Voy a llamar a model.getCountries")
        model.getCountries(Response.Listener<List<Country>> { countries ->
            if (countries != null) {
                view.showCountries(countries)
                view.progressBarVisible = false
                view.countryVisible = true
                view.datesVisible = true
            } else {
                view.showError("No countries in the returned list (possible BAD JSON)")
            }
        }, Response.ErrorListener { error -> view.showError(error.toString()) })
    }

    fun getCountry() : Country {
        return this.country!!
    }

    fun getRegion() : Region {
        return this.region!!
    }

    fun getSubregion() : Subregion {
        return this.subregion!!
    }

    fun setChosenCountry(country: Country) {
        this.country = country
        Log.d("MY COVID STATS", "The country is chosen.")

        if (this.toDate != null && this.fromDate != null){
            view.enableCountryOkButton = true
            view.showError("País correcto, puede calcular los datos")
        }

        Log.d("MY COVID STATS", "Comprobando si hay regiones...")
        model.getRegions(Response.Listener<List<Region>> { regions ->
            if (regions.isNotEmpty()) {
                Log.d("MY COVID STATS", "SI hay regiones")
                view.regionVisible = true
                getRegions()
            } else {
                this.region = null
                view.regionVisible = false
                view.subregionVisible = false
                view.enableRegionOkButton = false
                Log.d("MY COVID STATS", "NO hay regiones")
            }
        }, Response.ErrorListener { error -> view.showError(error.toString()) }, this.country!!)
    }

    fun getRegions() {
        model.getRegions(Response.Listener { regions ->
            if (regions != null) {
                view.showRegions(regions)
            } else {
                view.showError("No regions in the returned list (possible bad JSON)")
            }
        }, Response.ErrorListener { error -> view.showError(error.toString()) }, this.country!!)
    }

    fun setChosenRegion(region: Region) {
        this.region = region
        Log.d("MY COVID STATS", "The region is chosen.")

        if (this.toDate != null && this.fromDate != null){
            view.enableRegionOkButton = true
            view.showError("Región correcta, puede calcular los datos")
        }

        Log.d("MY COVID STATS", "Comprobando si hay subregiones...")
        model.getSubregions(Response.Listener { subregions ->
            if (subregions.isNotEmpty()) {
                Log.d("MY COVID STATS", "SI hay subregiones")
                view.subregionVisible = true
                getSubregions()
            } else {
                this.subregion = null
                view.subregionVisible = false
                view.enableSubregionOkButton = false
                Log.d("MY COVID STATS", "NO hay subregiones")
            }
        }, Response.ErrorListener { error -> view.showError(error.toString()) }, this.region!!)
    }

    fun getSubregions() {
        model.getSubregions(Response.Listener { subregions ->
            if (subregions != null) {
                view.showSubregions(subregions)
            } else {
                view.showError("No subregions in the returned list (possible bad JSON)")
            }
        }, Response.ErrorListener { error -> view.showError(error.toString()) }, this.region!!)
    }

    fun setChosenSubregion(subregion: Subregion) {
        this.subregion = subregion
        Log.d("MY COVID STATS", "The subregion is chosen.")

        if (this.toDate != null && this.fromDate != null){
            view.enableSubregionOkButton = true
            view.showError("Subegión correcta, puede calcular los datos")
        }
    }

    fun updateFromDate(fromDate: String) {
        this.fromDate = fromDate
        if (this.toDate != null) {
            if (fromDate.compareTo(this.toDate!!) <= 0) {
                if (this.country != null && this.region != null && this.subregion != null) {
                    view.showError("Intervalo de fechas correcto, puede calcular los datos")
                    view.enableCountryOkButton = true
                    view.enableRegionOkButton = true
                    view.enableSubregionOkButton = true
                }
                else if (this.country != null && this.region != null) {
                    view.showError("Intervalo de fechas correcto, puede calcular los datos")
                    view.enableCountryOkButton = true
                    view.enableRegionOkButton = true
                }

                else if (this.country != null) {
                    view.showError("Intervalo de fechas correcto, puede calcular los datos")
                    view.enableCountryOkButton = true
                }
            }

            else {
                view.showError("Intervalo de fechas incorrecto, introduzca otra fecha de inicio o de final")
                this.fromDate = null
                view.enableCountryOkButton = false
                view.enableRegionOkButton = false
                view.enableSubregionOkButton = false
            }
        }
    }

    fun updateToDate(toDate: String) {
        this.toDate = toDate
        if (this.fromDate != null) {
            if (toDate.compareTo(this.fromDate!!) >= 0) {
                if (this.country != null && this.region != null && this.subregion != null) {
                    view.showError("Intervalo de fechas correcto, puede calcular los datos")
                    view.enableCountryOkButton = true
                    view.enableRegionOkButton = true
                    view.enableSubregionOkButton = true
                }
                else if (this.country != null && this.region != null) {
                    view.showError("Intervalo de fechas correcto, puede calcular los datos")
                    view.enableCountryOkButton = true
                    view.enableRegionOkButton = true
                }

                else if (this.country != null) {
                    view.showError("Intervalo de fechas correcto, puede calcular los datos")
                    view.enableCountryOkButton = true
                }
            }

            else {
                view.showError("Intervalo de fechas incorrecto, introduzca otra fecha de inicio o de final")
                this.toDate = null
                view.enableCountryOkButton = false
                view.enableRegionOkButton = false
                view.enableSubregionOkButton = false
            }
        }
    }
}