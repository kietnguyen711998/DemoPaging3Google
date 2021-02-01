package com.example.demopaging3google.data

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.demopaging3google.api.GithubService
import com.example.demopaging3google.db.RepoDatabase
import com.example.demopaging3google.model.Repo
import kotlinx.coroutines.flow.Flow

class GithubRepository(
    private val service: GithubService,
    private val database: RepoDatabase
) {
    @ExperimentalPagingApi
    fun getSearchResultStream(query: String): Flow<PagingData<Repo>> {
        Log.d("GithubRepository", "New query: $query")
        val dbQuery = "%${query.replace(' ', '%')}%"

        val pagingSourceFactory = { database.Repo().reposByName(dbQuery) }

        return Pager(
            config = PagingConfig(pageSize = NETWORK_PAGE_SIZE, enablePlaceholders = false),
            remoteMediator = GithubRemoteMediator(
                query,
                service,
                database
            ),
            pagingSourceFactory = pagingSourceFactory
        ).flow
    }

    companion object {
        private const val NETWORK_PAGE_SIZE = 50
    }
}