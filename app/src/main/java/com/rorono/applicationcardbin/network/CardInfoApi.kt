package com.rorono.applicationcardbin.network

import com.rorono.applicationcardbin.models.removemodels.CardInfo
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface CardInfoApi {
    @GET("{bin}")
    suspend fun getBankCardInfo(@Path("bin") bin:String): Response<CardInfo>
}