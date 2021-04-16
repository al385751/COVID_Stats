package com.example.covidstats.aditionalClasses

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.covidstats.R

class DayCardAdapter(val dayDataList: ArrayList<DayData>, val context: Context) : RecyclerView.Adapter<DayCardAdapter.DayHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return DayHolder(layoutInflater.inflate(R.layout.item_daydata, parent, false))
    }

    override fun getItemCount(): Int {
        return dayDataList.size
    }

    override fun onBindViewHolder(holder: DayHolder, position: Int) {
        holder.render(dayDataList[position])
    }

    class DayHolder(val view: View) : RecyclerView.ViewHolder(view) {
        fun render(dayData: DayData) {
            view.findViewById<TextView>(R.id.dateTextView).text = dayData.date
            view.findViewById<TextView>(R.id.confirmedAmountTextView).text = dayData.todayTotalCases
            view.findViewById<TextView>(R.id.deathsAmountTextView).text = dayData.todayTotalDeaths
            view.findViewById<TextView>(R.id.recoveredAmountTextView).text = dayData.todayTotalRecovered
        }
    }
}