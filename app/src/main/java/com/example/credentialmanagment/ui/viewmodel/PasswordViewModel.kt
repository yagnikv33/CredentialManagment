package com.example.credentialmanagment.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.credentialmanagment.data.db.Password
import com.example.credentialmanagment.data.db.PasswordDatabase
import com.example.credentialmanagment.data.repository.PasswordRepository
import kotlinx.coroutines.launch

class PasswordViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: PasswordRepository

    private val _passwords = MutableLiveData<List<Password>>()
    val passwords: LiveData<List<Password>> = _passwords

    init {
        val passwordDao = PasswordDatabase.getDatabase(application).passwordDao()
        repository = PasswordRepository(passwordDao)
        loadPasswords()
    }

    private fun loadPasswords() {
        viewModelScope.launch {
            _passwords.value = repository.getPasswords()
        }
    }

    fun addPassword(password: Password) {
        viewModelScope.launch {
            repository.addPassword(password)
            loadPasswords()
        }
    }

    fun updatePassword(password: Password) {
        viewModelScope.launch {
            repository.updatePassword(password)
            loadPasswords()
        }
    }

    fun deletePassword(password: Password) {
        viewModelScope.launch {
            repository.deletePassword(password)
            loadPasswords()
        }
    }
}
