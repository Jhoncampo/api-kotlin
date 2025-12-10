package com.jhon.wineapp

import retrofit2.http.GET

interface WineService {
    @GET(Constants.PATH_WINES)
    suspend fun getRedWines(): List<Wine>
}