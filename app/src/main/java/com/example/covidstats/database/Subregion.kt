package com.example.covidstats.database

import android.os.Parcel
import android.os.Parcelable
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

data class Subregion(val name: String, val id: String, val countryId: String, val regionId: String) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString()!!,
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
        parcel.writeString(regionId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Subregion> {
        override fun createFromParcel(parcel: Parcel): Subregion {
            return Subregion(parcel)
        }

        override fun newArray(size: Int): Array<Subregion?> {
            return arrayOfNulls(size)
        }
    }
}