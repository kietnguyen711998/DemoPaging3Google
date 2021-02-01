package com.example.demopaging3google.data

import com.example.demopaging3google.api.GithubService
import com.example.demopaging3google.db.RepoDatabase

class GithubRepository(
    private val service: GithubService,
    private val database: RepoDatabase
) {

}