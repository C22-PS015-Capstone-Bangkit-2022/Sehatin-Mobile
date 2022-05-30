package com.app.sehatin.data.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.app.sehatin.data.local.entity.SavedPostEntity

@Database(entities = [SavedPostEntity::class], version = 1, exportSchema = false)
abstract class SavedPostDatabase : RoomDatabase() {
    abstract fun savedPostDao(): SavedPostDao

    companion object {
        @Volatile
        private var instance: SavedPostDatabase? = null
        fun getInstance(context: Context): SavedPostDatabase =
            instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    SavedPostDatabase::class.java, "saved_post.db"
                ).build()
            }
    }
}