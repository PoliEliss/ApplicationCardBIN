package com.rorono.applicationcardbin.models.localmodels

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cardBIN")
data class CardBINItem(
    @PrimaryKey val cardBIN: String,
)
