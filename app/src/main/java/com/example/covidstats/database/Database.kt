package com.example.covidstats.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room.databaseBuilder
import androidx.room.RoomDatabase
import com.example.covidstats.model.SingletonHolder

@Database(
        entities = [Country::class,
                    Region::class,
                    Subregion::class],
        version = 1,
        exportSchema = false
)

abstract class AbstractDatabase : RoomDatabase() {
    abstract fun getDAO(): DAO
}

class MyDatabase private constructor(context: Context) {
    companion object: SingletonHolder<MyDatabase, Context>(::MyDatabase)

    val dao = databaseBuilder(context, AbstractDatabase::class.java, "CovidStats")
            .build()
            .getDAO()
}