package com.app.sehatin.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.app.sehatin.data.local.entity.SavedPostEntity

@Dao
interface SavedPostDao {

    @Query("SELECT * FROM saved_post ORDER BY createdAt DESC")
    fun getNews(): LiveData<List<SavedPostEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPostId(savedPostEntity: SavedPostEntity)

    @Delete
    fun deletePostId(savedPostEntity: SavedPostEntity)

}