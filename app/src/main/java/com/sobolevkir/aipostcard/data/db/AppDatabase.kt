package com.sobolevkir.aipostcard.data.db

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import com.sobolevkir.aipostcard.data.db.entity.AlbumItemEntity

@Database(
    version = 2,
    entities = [AlbumItemEntity::class],
    autoMigrations = [AutoMigration(from = 1, to = 2)]
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getAlbumDao(): AlbumDao

}