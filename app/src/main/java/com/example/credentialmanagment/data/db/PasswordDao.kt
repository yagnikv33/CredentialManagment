package com.example.credentialmanagment.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface PasswordDao {
    @Query("SELECT * FROM passwords")
    suspend fun getAllPasswords(): List<Password>

    @Insert
    suspend fun insertPassword(password: Password)

    @Update
    suspend fun updatePassword(password: Password)

    @Delete
    suspend fun deletePassword(password: Password)
}

