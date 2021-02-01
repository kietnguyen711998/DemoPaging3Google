package com.example.demopaging3google

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import com.example.demopaging3google.api.GithubService
import com.example.demopaging3google.data.GithubRepository
import com.example.demopaging3google.db.RepoDatabase
import com.example.demopaging3google.ui.ViewModelFactory

object Injection {
    private fun providerGithubResponsive(context: Context): GithubRepository {
        return GithubRepository(GithubService.create(), RepoDatabase.getInstance(context))
    }

    fun providerViewModelFactory(context: Context): ViewModelProvider.Factory {
        return ViewModelFactory(providerGithubResponsive(context))
    }

}