package com.example.demopaging3google.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.example.demopaging3google.api.GithubService
import com.example.demopaging3google.db.RemoteKeys
import com.example.demopaging3google.db.RepoDatabase
import com.example.demopaging3google.model.Repo
import java.io.InvalidObjectException

private const val PAGING_INDEX = 1

@ExperimentalPagingApi
class GithubRemoteMediator(
    private val query: String,
    private val service: GithubService,
    private val repoDatabase: RepoDatabase
) : RemoteMediator<Int, Repo>() {
    val repo = repoDatabase.Repo()
    override suspend fun load(loadType: LoadType, state: PagingState<Int, Repo>): MediatorResult {
        val page = when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeysCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: PAGING_INDEX
            }
            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeysForFirstItem(state)
                if (remoteKeys == null){
                    throw InvalidObjectException("Error !!!")
                }
                val preKey = remoteKeys.prevKey
                if (preKey == null){
                    return MediatorResult.Success(endOfPaginationReached = true)
                }
                remoteKeys.prevKey
            }
            LoadType.APPEND -> {

            }

        }
    }

    private suspend fun getRemoteKeysForFirstItem(state: PagingState<Int, Repo>): RemoteKeys? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull().let { repo ->
            repoDatabase.RemoteKeyDao().remoteRepoId(repo?.id)
        }
    }

    private suspend fun getRemoteKeysCurrentPosition(stage: PagingState<Int, Repo>): RemoteKeys? {
        return stage.anchorPosition?.let { postion ->
            stage.closestItemToPosition(postion)?.id.let { repoId ->
                repoDatabase.RemoteKeyDao().remoteRepoId(repoId)
            }
        }
    }
}