package com.example.covidstats.aditionalClasses

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatDialogFragment
import com.example.covidstats.R
import com.example.covidstats.database.Country
import com.example.covidstats.database.Region
import com.example.covidstats.database.Subregion

class ExampleDialog(dayData: DayData, country: Country?, region: Region?, subregion: Subregion?) : AppCompatDialogFragment() {
    var dayDataObject = dayData
    var countryObject = country
    var regionObject = region
    var subregionObject = subregion

    lateinit var titleText : TextView
    lateinit var todayCases : TextView
    lateinit var todayDeaths : TextView
    lateinit var todayRecovered : TextView
    lateinit var todayTotalCases : TextView
    lateinit var todayTotalDeaths : TextView
    lateinit var todayTotalRecovered : TextView

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder : AlertDialog.Builder = AlertDialog.Builder(activity)
        val inflater : LayoutInflater = activity!!.layoutInflater
        val view : View = inflater.inflate(R.layout.data_dialog, null)

        titleText = view.findViewById(R.id.titleTextView)
        if (subregionObject != null) {
            val newText = subregionObject!!.name + " (" + dayDataObject.date + ")"
            titleText.setText(newText)
        }
        else {
            if (regionObject != null) {
                val newText = regionObject!!.name + " (" + dayDataObject.date + ")"
                titleText.setText(newText)
            }

            else {
                val newText = countryObject!!.name + " (" + dayDataObject.date + ")"
                titleText.setText(newText)
            }
        }

        todayCases = view.findViewById(R.id.todayNumberConfirmed)
        todayCases.setText(dayDataObject.todayCases)

        todayDeaths = view.findViewById(R.id.todayNumberDeaths)
        todayDeaths.setText(dayDataObject.todayDeaths)

        todayRecovered = view.findViewById(R.id.todayNumberRecovered)
        todayRecovered.setText(dayDataObject.todayRecovered)

        todayTotalCases = view.findViewById(R.id.totalNumberConfirmed)
        todayTotalCases.setText(dayDataObject.todayTotalCases)

        todayTotalDeaths = view.findViewById(R.id.totalNumberDeaths)
        todayTotalDeaths.setText(dayDataObject.todayTotalDeaths)

        todayTotalRecovered = view.findViewById(R.id.totalNumberRecovered)
        todayTotalRecovered.setText(dayDataObject.todayTotalRecovered)

        builder.setView(view)
                .setPositiveButton("OK") { dialog, which ->
                    dialog.cancel()
                }

        return builder.create()
    }
}