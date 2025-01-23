package com.sobolevkir.aipostcard.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sobolevkir.aipostcard.data.db.entity.AlbumItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AlbumDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addAlbumItem(albumItemEntity: AlbumItemEntity): Long

    @Query("DELETE FROM album_items_table WHERE id = :itemId")
    suspend fun removeAlbumItem(itemId: Long): Int

    @Query("SELECT * FROM album_items_table ORDER BY time_stamp DESC")
    fun getAlbumItems(): Flow<List<AlbumItemEntity>>

    @Query("SELECT COUNT(*) FROM album_items_table WHERE uuid = :uuid")
    suspend fun isAlbumItemExists(uuid: String): Boolean

}