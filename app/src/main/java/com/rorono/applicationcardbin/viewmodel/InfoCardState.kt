package com.rorono.applicationcardbin.viewmodel

import com.rorono.applicationcardbin.models.removemodels.CardInfo

sealed class InfoCardState {
    data class Success(val data: CardInfo) : InfoCardState()
    data class Error(val messageError: String) : InfoCardState()
    object Loading : InfoCardState()
}