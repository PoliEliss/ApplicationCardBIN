package com.rorono.applicationcardbin.repository

import com.rorono.applicationcardbin.network.RetrofitInstance
import com.rorono.applicationcardbin.utils.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class NetworkRepository(private val retrofit: RetrofitInstance) {
    suspend fun getCardInfo(bin: String): Result = withContext(Dispatchers.IO) {
        try {
            val response = retrofit.api.getBankCardInfo(bin = bin)
            if (response.isSuccessful) {
                return@withContext Result.Success(response.body())
            } else {
                return@withContext Result.Error("Не удалось отобразить данные")
            }
        } catch (e: Exception) {
            return@withContext Result.Error("Ошибка")
        }
    }
}