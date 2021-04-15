package com.example.covidstats.database

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(primaryKeys = ["id", "countryId"],
        foreignKeys = [ForeignKey(
                entity = Country::class,
                parentColumns = arrayOf("id"),
                childColumns = arrayOf("countryId")
        )],
        tableName = "Region",
        indices = [androidx.room.Index("countryId")])

data class Region(val name: String, val id: String, val countryId: String) {
    override fun toString(): String {
        return name
    }
}