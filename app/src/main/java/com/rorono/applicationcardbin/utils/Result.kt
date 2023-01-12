package com.rorono.applicationcardbin.utils

import com.rorono.applicationcardbin.models.removemodels.CardInfo

sealed class Result {
    data class Success(val cardInfo: CardInfo) : Result()
    data class Error(val error: String) : Result()
}
