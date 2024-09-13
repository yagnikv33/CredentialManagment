package com.example.credentialmanagment.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "passwords")
data class Password(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val accountName: String,
    val username: String,
    val password: String
)