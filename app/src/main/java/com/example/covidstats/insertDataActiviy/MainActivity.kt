package com.example.covidstats.insertDataActiviy

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.*
import com.example.covidstats.R
import com.example.covidstats.aditionalClasses.DatePickerFragment
import com.example.covidstats.database.Country
import com.example.covidstats.database.Region
import com.example.covidstats.database.Subregion
import com.example.covidstats.model.Model
import com.example.covidstats.showDataActiviy.ShowDataView

class MainActivity : AppCompatActivity(), MainView {
    lateinit var locationText: TextView
    lateinit var periodTimeText: TextView

    lateinit var countryText: TextView
    lateinit var countryTextView: AutoCompleteTextView
    lateinit var countryOkButton: Button

    lateinit var regionText: TextView
    lateinit var regionTextView: AutoCompleteTextView
    lateinit var regionOkButton: Button

    lateinit var subregionText: TextView
    lateinit var subregionTextView: AutoCompleteTextView
    lateinit var subregionOkButton: Button

    lateinit var progressBar: ProgressBar

    lateinit var fromText: TextView
    lateinit var fromTextDate: EditText
    lateinit var toText: TextView
    lateinit var toTextDate: EditText

    lateinit var presenter: Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        locationText = findViewById(R.id.locationText)
        periodTimeText = findViewById(R.id.periodTimeText)

        countryText = findViewById(R.id.countryText)
        countryTextView = findViewById(R.id.countryTextView)
        countryOkButton = findViewById(R.id.countryOkButton)

        regionText = findViewById(R.id.regionText)
        regionTextView = findViewById(R.id.regionTextView)
        regionOkButton = findViewById(R.id.regionOkButton)

        subregionText = findViewById(R.id.subRegionText)
        subregionTextView = findViewById(R.id.subRegionTextView)
        subregionOkButton = findViewById(R.id.subRegionOkButton)

        progressBar = findViewById(R.id.progressBar)

        fromText = findViewById(R.id.fromText)
        fromTextDate = findViewById(R.id.fromTextDate)
        toText = findViewById(R.id.toText)
        toTextDate = findViewById(R.id.toTextDate)

        val model = Model(applicationContext)
        presenter = Presenter(this, model)

        title = "COVID Stats"
    }

    override var countryVisible: Boolean
        get() = countryText.visibility == View.VISIBLE
        set(value) {
            val v = if (value) View.VISIBLE else View.GONE
            locationText.visibility = v
            countryText.visibility = v
            countryTextView.visibility = v
            countryOkButton.visibility = v
        }

    override var progressBarVisible: Boolean
        get() = progressBar.visibility == View.VISIBLE
        set(value) {
            progressBar.visibility = if (value) View.VISIBLE else View.GONE
        }

    override var regionVisible: Boolean
        get() = regionText.visibility == View.VISIBLE
        set(value) {
            val v = if (value) View.VISIBLE else View.GONE
            regionText.visibility = v
            regionTextView.visibility = v
            regionOkButton.visibility = v
        }

    override var subregionVisible: Boolean
        get() = subregionText.visibility == View.VISIBLE
        set(value) {
            val v = if (value) View.VISIBLE else View.GONE
            subregionText.visibility = v
            subregionTextView.visibility = v
            subregionOkButton.visibility = v
        }

    override var datesVisible: Boolean
        get() = periodTimeText.visibility == View.VISIBLE
        set(value) {
            val v = if (value) View.VISIBLE else View.GONE
            periodTimeText.visibility = v
            fromText.visibility = v
            fromTextDate.visibility = v
            toText.visibility = v
            toTextDate.visibility = v
        }

    override var enableCountryOkButton: Boolean
        get() = countryOkButton.isEnabled
        set(value) {
            countryOkButton.isEnabled = value
        }

    override var enableRegionOkButton: Boolean
        get() = regionOkButton.isEnabled
        set(value) {
            regionOkButton.isEnabled = value
        }

    override var enableSubregionOkButton: Boolean
        get() = subregionOkButton.isEnabled
        set(value) {
            subregionOkButton.isEnabled = value
        }

    override fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    override fun showCountries(countries: List<Country>) {
        val countriesName: ArrayList<String> = ArrayList()

        countries.forEach {
            countriesName.add(it.name)
        }

        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, countriesName)
        countryTextView.apply {
            setAdapter(adapter)
            setText("")
            addTextChangedListener(object: TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    val country = s.toString()
                    countries.binarySearch { it.name.compareTo(country) }.let {
                        if (it >= 0)
                            presenter.setChosenCountry(countries[it])
                    }
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                }

            })
        }
    }

    override fun showRegions(regions: List<Region>) {
        val regionsName: ArrayList<String> = ArrayList()

        regions.forEach {
            regionsName.add(it.name)
        }

        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, regionsName)
        regionTextView.apply {
            setAdapter(adapter)
            setText("")
            addTextChangedListener(object: TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    val region = s.toString()
                    regions.binarySearch { it.name.compareTo(region) }.let {
                        if (it >= 0)
                            presenter.setChosenRegion(regions[it])
                    }
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                }

            })
        }
    }

    override fun showSubregions(subregions: List<Subregion>) {
        val subregionsName: ArrayList<String> = ArrayList()

        subregions.forEach {
            subregionsName.add(it.name)
        }

        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, subregionsName)
        subregionTextView.apply {
            setAdapter(adapter)
            setText("")
            addTextChangedListener(object: TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    val subregion = s.toString()
                    subregions.binarySearch { it.name.compareTo(subregion) }.let {
                        if (it >= 0)
                            presenter.setChosenSubregion(subregions[it])
                    }
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                }

            })
        }
    }

    override fun showDatePickerFromDialog(view: View) {
        val newFragment = DatePickerFragment.newInstance(DatePickerDialog.OnDateSetListener { _, year, month, day ->
            val dayStr = day.twoDigits()
            val monthStr = (month + 1).twoDigits() // +1 because January is zero

            val selectedDate = "$year-$monthStr-$dayStr"
            fromTextDate.setText(selectedDate)
            presenter.updateFromDate(selectedDate)
        })

        newFragment.show(supportFragmentManager, "datePicker")
    }

    override fun showDatePickerToDialog(view: View) {
        val newFragment = DatePickerFragment.newInstance(DatePickerDialog.OnDateSetListener { _, year, month, day ->
            val dayStr = day.twoDigits()
            val monthStr = (month + 1).twoDigits() // +1 because January is zero

            val selectedDate = "$year-$monthStr-$dayStr"
            toTextDate.setText(selectedDate)
            presenter.updateToDate(selectedDate)
        })

        newFragment.show(supportFragmentManager, "datePicker")
    }

    fun Int.twoDigits() =
            if (this <= 9) "0$this" else this.toString()

    override fun changeToDisplayCountryData(view: View) {
        val intent = Intent(this, ShowDataView::class.java)
        intent.putExtra("Country", presenter.getCountry())
        intent.putExtra("FromDate", fromTextDate.text.toString())
        intent.putExtra("ToDate", toTextDate.text.toString())
        startActivity(intent)
    }

    override fun changeToDisplayRegionData(view: View) {
        val intent = Intent(this, ShowDataView::class.java)
        intent.putExtra("Country", presenter.getCountry())
        intent.putExtra("Region", presenter.getRegion())
        intent.putExtra("FromDate", fromTextDate.text.toString())
        intent.putExtra("ToDate", toTextDate.text.toString())
        startActivity(intent)
    }

    override fun changeToDisplaySubregionData(view: View) {
        val intent = Intent(this, ShowDataView::class.java)
        intent.putExtra("Country", presenter.getCountry())
        intent.putExtra("Region", presenter.getRegion())
        intent.putExtra("Subregion", presenter.getSubregion())
        intent.putExtra("FromDate", fromTextDate.text.toString())
        intent.putExtra("ToDate", toTextDate.text.toString())
        startActivity(intent)
    }
}