package com.submissionform.Room


import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.submissionform.Model.InputDataModel
import androidx.room.Update


@Dao
interface InputFormDao {
    @get:Query("SELECT * FROM InputDataModel")
    val all: List<InputDataModel>
    @Update
    fun update(user: InputDataModel)
//    @Query("SELECT * FROM user WHERE uid IN (:userIds)")
//    fun loadAllByIds(userIds: IntArray): List<User>

//    @Query("SELECT * FROM user WHERE first_name LIKE :first AND " + "last_name LIKE :last LIMIT 1")
//    fun findByName(first: String, last: String): InputDataModel

    @Insert
    fun insertAll(vararg users: InputDataModel)


//    @get:Query("SELECT * FROM Notes")
//    val notes: List<NewNote>

//    @Insert
//    fun insertNewNote(vararg users: NewNote)


//    @Delete
//    fun delete(user: InputDataModel)
}