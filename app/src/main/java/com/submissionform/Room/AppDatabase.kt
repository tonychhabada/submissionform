package com.submissionform.Room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.submissionform.Model.InputDataModel


@Database(entities = arrayOf(InputDataModel::class), version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun inputDao(): InputFormDao
}