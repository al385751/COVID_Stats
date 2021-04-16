package com.example.covidstats.aditionalClasses

class DayData (_date: String,
               _todayCases: String, _todayTotalCases: String,
               _todayDeaths: String, _todayTotalDeaths: String,
               _todayRecovered: String, _todayTotalRecovered: String) {
    var date: String = _date
    var todayCases = _todayCases
    var todayTotalCases = _todayTotalCases
    var todayDeaths = _todayDeaths
    var todayTotalDeaths = _todayTotalDeaths
    var todayRecovered = _todayRecovered
    var todayTotalRecovered = _todayTotalRecovered
}