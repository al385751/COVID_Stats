package com.example.covidstats.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Country")
data class Country(val name: String, @PrimaryKey val id: String) {
    override fun toString(): String {
        return name
    }
}