package com.submissionform.Room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.submissionform.Model.InputDataModel
import com.submissionform.Model.NewNote


@Database(entities = arrayOf(InputDataModel::class,NewNote::class), version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun inputDao(): InputFormDao
}