package com.jhon.wineapp

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface WineDao {
    @Query("SELECT  * FROM WineEntity")
    fun getAllWines(): MutableList<Wine>

    @Query("SELECT * FROM WineEntity WHERE isFavourite == :isFavourite")
    fun getWinesFav(isFavourite: Boolean): MutableList<Wine>

    @Insert
    fun addWine(wine: Wine): Long

    @Update
    fun updateWine(wine: Wine): Int

    @Delete
    fun deleteWine(wine:Wine): Int
}