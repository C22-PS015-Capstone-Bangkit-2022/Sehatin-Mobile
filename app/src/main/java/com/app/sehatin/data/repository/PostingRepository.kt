package com.app.sehatin.data.repository

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.app.sehatin.data.model.Posting
import com.app.sehatin.utils.DateHelper
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.io.File
import com.app.sehatin.data.Result
import com.app.sehatin.data.paging.post.PostPagingSource
import com.app.sehatin.utils.PAGE_SIZE
import com.app.sehatin.utils.POST_COLLECTION
import com.app.sehatin.utils.POST_IMAGE_STORAGE
import com.google.firebase.firestore.Query

class PostingRepository {
    private val postRef = FirebaseFirestore.getInstance().collection(POST_COLLECTION)

    fun getPosts(queryProductsByDate: Query): LiveData<PagingData<Posting>> {
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE
            ),
            pagingSourceFactory = {
                PostPagingSource(queryProductsByDate)
            }
        ).liveData
    }

    fun uploadPost(uploadPostState: MutableLiveData<Result<Map<String, Any?>>>, postImage: File?, postDescription: String, postTags: List<String>?) {
        uploadPostState.value = Result.Loading
        val postId = postRef.document().id
        val userId = FirebaseAuth.getInstance().currentUser!!.uid
        val hasImage = postImage != null
        val post = mutableMapOf(
            "id" to postId,
            "userId" to userId,
            "createdAt" to DateHelper.getCurrentDate(),
            "hasImage" to hasImage,
            "image" to null,
            "description" to postDescription,
            "tags" to postTags,
            "likeCount" to 0,
            "commentCount" to 0,
        )

        if(hasImage) {
            val storageRef = FirebaseStorage.getInstance().getReference("$POST_IMAGE_STORAGE/$userId/${System.currentTimeMillis()}.jpg")
            val uploadTask = storageRef.putFile(Uri.fromFile(postImage))
            uploadTask.continueWithTask { task ->
                if (!task.isSuccessful) {
                    task.exception?.let {
                        throw it
                    }
                }
                return@continueWithTask storageRef.downloadUrl
            }.addOnSuccessListener {
                post["image"] = it.toString()
                savePost(uploadPostState, post)
            }.addOnFailureListener {
                it.localizedMessage?.let { msg ->
                    uploadPostState.value = Result.Error(msg)
                }
            }
        } else {
            savePost(uploadPostState, post)
        }
    }

    private fun savePost(uploadPostState: MutableLiveData<Result<Map<String, Any?>>>, postMap: Map<String, Any?>) {
        postRef.document(postMap["id"] as String).set(postMap)
            .addOnSuccessListener {
                uploadPostState.value = Result.Success(postMap)
            }
            .addOnFailureListener { e ->
                e.localizedMessage?.let { msg ->
                    uploadPostState.value = Result.Error(msg)
                }
            }
    }

}