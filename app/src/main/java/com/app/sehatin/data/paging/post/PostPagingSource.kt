package com.app.sehatin.data.paging.post

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.app.sehatin.data.model.Posting
import com.app.sehatin.utils.NO_DATA
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.tasks.await

class PostPagingSource(private val queryProductsByDate: Query) : PagingSource<QuerySnapshot, Posting>() {

    override fun getRefreshKey(state: PagingState<QuerySnapshot, Posting>): QuerySnapshot? {
        return null
    }

    override suspend fun load(params: LoadParams<QuerySnapshot>): LoadResult<QuerySnapshot, Posting> {
        return try {
            Log.d(TAG, "load: ${params.loadSize}")
            val currentPage = params.key ?: queryProductsByDate.get().await()
            if(!currentPage.isEmpty) {
                val lastVisibleProduct = currentPage.documents[currentPage.size() - 1]
                val nextPage = queryProductsByDate.startAfter(lastVisibleProduct).get().await()
                LoadResult.Page(
                    data = currentPage.toObjects(Posting::class.java),
                    prevKey = null,
                    nextKey = nextPage
                )
            } else {
                LoadResult.Error(Exception(NO_DATA))
            }
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    private companion object {
        const val TAG = "PostPagingSource"
    }

}