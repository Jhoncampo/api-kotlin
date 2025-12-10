package com.jhon.wineapp

import android.R

data class Wine(
    val winery: String,
    val wine: String,
    val rating: Rating,
    val location: String,
    val image: String,
    val id: Number,
)

