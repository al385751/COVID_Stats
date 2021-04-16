package com.example.covidstats.database

import android.os.Parcel
import android.os.Parcelable
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

data class Region(val name: String, val id: String, val countryId: String) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString()!!,
            parcel.readString()!!,
            parcel.readString()!!) {
    }

    override fun toString(): String {
        return name
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(id)
        parcel.writeString(countryId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Region> {
        override fun createFromParcel(parcel: Parcel): Region {
            return Region(parcel)
        }

        override fun newArray(size: Int): Array<Region?> {
            return arrayOfNulls(size)
        }
    }
}