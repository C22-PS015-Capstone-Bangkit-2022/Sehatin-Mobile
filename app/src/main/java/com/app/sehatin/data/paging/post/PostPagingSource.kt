package com.app.sehatin.data.paging.post

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.app.sehatin.data.model.Posting
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.tasks.await

class PostPagingSource(private val queryProductsByName: Query) : PagingSource<QuerySnapshot, Posting>() {

    override fun getRefreshKey(state: PagingState<QuerySnapshot, Posting>): QuerySnapshot? {
        return null
    }

    override suspend fun load(params: LoadParams<QuerySnapshot>): LoadResult<QuerySnapshot, Posting> {
        return try {
            val currentPage = params.key ?: queryProductsByName.get().await()
            val lastVisibleProduct = currentPage.documents[currentPage.size() - 1]
            val nextPage = queryProductsByName.startAfter(lastVisibleProduct).get().await()
            LoadResult.Page(
                data = currentPage.toObjects(Posting::class.java),
                prevKey = null,
                nextKey = nextPage
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

}