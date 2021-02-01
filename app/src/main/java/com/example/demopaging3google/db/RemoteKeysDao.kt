package com.example.demopaging3google.db

import androidx.room.*
import com.example.demopaging3google.db.RemoteKeys

@Dao
interface RemoteKeysDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(remoteKey: List<RemoteKeys>)

    @Query("SELECT * FROM remote_keys WHERE repoId = repoId")
    suspend fun remoteRepoId(repoId: Long?) : RemoteKeys?
}