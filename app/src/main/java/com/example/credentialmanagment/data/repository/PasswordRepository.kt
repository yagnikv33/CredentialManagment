package com.example.credentialmanagment.data.repository

import com.example.credentialmanagment.data.db.Password
import com.example.credentialmanagment.data.db.PasswordDao

class PasswordRepository(private val passwordDao: PasswordDao) {

    suspend fun getPasswords() = passwordDao.getAllPasswords()

    suspend fun addPassword(password: Password) = passwordDao.insertPassword(password)

    suspend fun updatePassword(password: Password) = passwordDao.updatePassword(password)

    suspend fun deletePassword(password: Password) = passwordDao.deletePassword(password)
}
