package com.rorono.applicationcardbin.repository

import com.rorono.applicationcardbin.database.CardBINDataBase
import com.rorono.applicationcardbin.models.localmodels.CardBINItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RepositoryDataBase(dataBase: CardBINDataBase) {
    private val cardBinDao = dataBase.cardBINDao()

    suspend fun insertCardBIN(cardBINItem: CardBINItem) {
        withContext(Dispatchers.IO) {
            cardBinDao.insertCardBIN(cardBINItem = cardBINItem)
        }
    }

    suspend fun getAllCardBIN(): ArrayList<CardBINItem> {

        val listCardBIN = arrayListOf<CardBINItem>()
        withContext(Dispatchers.IO) {
            listCardBIN.addAll(cardBinDao.getALLListCardBIN())
        }
        return listCardBIN
    }
}