package com.example.covidstats.showDataActiviy

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.covidstats.R
import com.example.covidstats.aditionalClasses.DayCardAdapter
import com.example.covidstats.aditionalClasses.DayData
import com.example.covidstats.insertDataActiviy.Presenter
import com.example.covidstats.model.Model

class ShowDataView : AppCompatActivity() {
    lateinit var rvDates : RecyclerView
    lateinit var presenter: showDataPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_showdata_main)

        val selectedCountry: String = intent.getStringExtra("Country")!!
        val selectedRegion: String? = intent.getStringExtra("Region")
        val selectedSubregion: String? = intent.getStringExtra("Subregion")
        val fromDate: String = intent.getStringExtra("FromDate")!!
        val toDate: String = intent.getStringExtra("ToDate")!!

        rvDates = findViewById(R.id.dayDataRecyclerView)

        val model = Model(applicationContext)
        presenter = showDataPresenter(this, model)

        val dayDatesList = ArrayList<DayData>()

        dayDatesList.add(DayData("2020-03-09", "798575", "54", "231045"))
        dayDatesList.add(DayData("2021-04-25", "74525", "456", "230"))
        dayDatesList.add(DayData("2021-11-07", "12384", "5", "2345810"))
        dayDatesList.add(DayData("2020-10-15", "243742", "5457", "2310"))
        dayDatesList.add(DayData("2020-06-31", "289", "0", "150"))

        val adapter = DayCardAdapter(dayDatesList, this)

        rvDates.layoutManager = LinearLayoutManager(this)
        rvDates.adapter = adapter

        if (selectedSubregion != null)
            title = selectedSubregion
        else {
            if (selectedRegion != null)
                title = selectedRegion
            else
                title = selectedCountry
        }
    }
}