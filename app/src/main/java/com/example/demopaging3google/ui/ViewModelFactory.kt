package com.example.demopaging3google.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.demopaging3google.data.GithubRepository

class ViewModelFactory (private val repository: GithubRepository): ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        TODO("Not yet implemented")
    }

}