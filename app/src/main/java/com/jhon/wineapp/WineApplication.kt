package com.jhon.wineapp

import android.app.Application
import androidx.room.Room

class WineApplication : Application() {
    companion object {
        lateinit var database: WineDatabase
    }

    override fun onCreate() {
        super.onCreate()
        database = Room.databaseBuilder(this, WineDatabase::class.java, "WineDatabase").build()

    }
}