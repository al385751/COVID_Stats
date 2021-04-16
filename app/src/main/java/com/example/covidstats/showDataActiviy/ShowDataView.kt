package com.example.covidstats.showDataActiviy

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.covidstats.R
import com.example.covidstats.aditionalClasses.DayCardAdapter
import com.example.covidstats.aditionalClasses.DayData
import com.example.covidstats.aditionalClasses.ExampleDialog
import com.example.covidstats.database.Country
import com.example.covidstats.database.Region
import com.example.covidstats.database.Subregion
import com.example.covidstats.model.Model


class ShowDataView : AppCompatActivity() {
    var country : Country? = null
    var region: Region? = null
    var subregion: Subregion? = null

    lateinit var rvDates : RecyclerView
    lateinit var progressBar: ProgressBar

    lateinit var presenter: showDataPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_showdata_main)

        country = intent.getParcelableExtra("Country")
        region = intent.getParcelableExtra("Region")
        subregion = intent.getParcelableExtra("Subregion")
        val fromDate: String = intent.getStringExtra("FromDate")!!
        val toDate: String = intent.getStringExtra("ToDate")!!

        rvDates = findViewById(R.id.dayDataRecyclerView)
        progressBar = findViewById(R.id.progressBar2)

        val model = Model(applicationContext)
        presenter = showDataPresenter(this, model)

        if (subregion != null) {
            title = subregion!!.name
            presenter.getSubregionData(country!!, subregion!!, fromDate, toDate)
        }
        else {
            if (region != null) {
                title = region!!.name
                presenter.getRegionData(country!!, region!!, fromDate, toDate)
            }

            else {
                title = country!!.name
                presenter.getCountryData(country!!, fromDate, toDate)
            }
        }
    }

    var progressBarVisible: Boolean
        get() = progressBar.visibility == View.VISIBLE
        set(value) {
            progressBar.visibility = if (value) View.VISIBLE else View.GONE
        }

    var recyclerViewVisible: Boolean
        get() = rvDates.visibility == View.VISIBLE
        set(value) {
            rvDates.visibility = if (value) View.VISIBLE else View.GONE
        }

    fun createRecyclerView(dayDatesList: ArrayList<DayData>) {
        val adapter = DayCardAdapter(dayDatesList) {dayData ->
            dayDatesList.forEach {
                if (dayData.date == it.date) {
                    showData(it, country, region, subregion)
                }
            }
        }
        rvDates.layoutManager = LinearLayoutManager(this)
        rvDates.adapter = adapter
    }

    fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    fun showData(dayData: DayData, country: Country?, region: Region?, subregion: Subregion?) {
        val exampleDialog  = ExampleDialog(dayData, country, region, subregion)
        exampleDialog.show(supportFragmentManager, "example dialog")
    }

}
