package com.example.covidstats.database

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(primaryKeys = ["id", "regionId", "countryId"],
        foreignKeys = [ForeignKey(
                entity = Region::class,
                parentColumns = arrayOf("countryId", "id"),
                childColumns = arrayOf("countryId", "regionId")
        )],
        tableName = "Subregion",
        indices = [Index(value = ["countryId", "regionId"])])

data class Subregion(val name: String, val id: String, val countryId: String, val regionId: String) {
    override fun toString(): String {
        return name
    }
}