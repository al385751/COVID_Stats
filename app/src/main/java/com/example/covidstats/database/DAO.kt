package com.example.covidstats.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query


@Dao
interface DAO {

    @Query("SELECT * FROM Country")
    fun getCountries() : List<Country>

    @Insert
    fun insertCountries(countries: List<Country>)

    @Query("SELECT * FROM Region WHERE countryId = :country")
    fun getRegions(country: String) : List<Region>

    @Insert
    fun insertRegions(regions: List<Region>)

    @Query("SELECT * FROM Subregion WHERE regionId = :region")
    fun getSubregions(region: String) : List<Subregion>

    @Insert
    fun insertSubregions(subregions: List<Subregion>)
}