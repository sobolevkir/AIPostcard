package com.sobolevkir.aipostcard.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.sobolevkir.aipostcard.data.db.entity.AlbumItemEntity

@Database(
    version = 1,
    entities = [AlbumItemEntity::class],
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getAlbumDao(): AlbumDao

}