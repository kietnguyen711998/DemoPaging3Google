package com.example.demopaging3google.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.demopaging3google.api.GithubService
import com.example.demopaging3google.api.IN_QUALIFIER
import com.example.demopaging3google.db.RemoteKeys
import com.example.demopaging3google.db.RepoDatabase
import com.example.demopaging3google.model.Repo
import retrofit2.HttpException
import java.io.IOException
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
                if (remoteKeys == null) {
                    throw InvalidObjectException("Error !!!")
                }
                val preKey = remoteKeys.prevKey
                if (preKey == null) {
                    return MediatorResult.Success(endOfPaginationReached = true)
                }
                remoteKeys.prevKey
            }
            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)
                if (remoteKeys == null || remoteKeys.nextKey == null) {
                    throw InvalidObjectException("Error !!")
                }
                remoteKeys.nextKey
            }

        }

        val apiQuery = query + IN_QUALIFIER

        try {
            val apiResponse = service.searchRepos(apiQuery, PAGING_INDEX, state.config.pageSize)
            val repos = apiResponse.items
            val endOfPaginationReached = repos.isEmpty()
            repoDatabase.withTransaction {
               /* repos.*/
            }

            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (exception: IOException) {
            return MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            return MediatorResult.Error(exception)
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, Repo>): RemoteKeys? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull().let { repos ->
            repoDatabase.RemoteKeyDao().remoteRepoId(repos?.id)
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