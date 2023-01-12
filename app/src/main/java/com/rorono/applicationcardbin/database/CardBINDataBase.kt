package com.rorono.applicationcardbin.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.rorono.applicationcardbin.models.localmodels.CardBINItem


@Database(entities = [CardBINItem::class], version = 1)
abstract class CardBINDataBase : RoomDatabase() {
    abstract fun cardBINDao(): CardInfoDao

    companion object {
        @Volatile
        private var database: CardBINDataBase? = null

        @Synchronized
        fun getInstance(context: Context): CardBINDataBase {
            return if (database == null) {
                database = Room.databaseBuilder(
                    context, CardBINDataBase::class.java,
                    "database"
                ).build()
                database as CardBINDataBase
            } else database as CardBINDataBase
        }
    }

}