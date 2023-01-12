package com.rorono.applicationcardbin.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rorono.applicationcardbin.models.localmodels.CardBINItem

@Dao
interface CardInfoDao {
    @Query("SELECT * FROM cardBIN")
    suspend fun getALLListCardBIN(): List<CardBINItem>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCardBIN(cardBINItem: CardBINItem)
}