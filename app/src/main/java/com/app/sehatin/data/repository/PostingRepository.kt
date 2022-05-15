package com.app.sehatin.data.repository

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.app.sehatin.data.model.Posting
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.io.File
import com.app.sehatin.data.Result
import com.app.sehatin.data.model.Like
import com.app.sehatin.data.paging.post.PostPagingSource
import com.app.sehatin.utils.*
import com.google.firebase.firestore.FieldValue
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

    fun getTrendingPost(trendingPostState: MutableLiveData<Result<List<Posting>>>, size : Long) {
        trendingPostState.value = Result.Loading
        postRef
            .orderBy(LIKE_COUNT)
            .limit(size)
            .get()
            .addOnSuccessListener { docs ->
                val listPost = mutableListOf<Posting>()
                for(doc in docs) {
                    val post = doc.toObject(Posting::class.java)
                    listPost.add(post)
                }
                trendingPostState.value = Result.Success(listPost)
            }.addOnFailureListener {
                it.localizedMessage?.let { msg ->
                    trendingPostState.value = Result.Error(msg)
                }
            }
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
                createPost(uploadPostState, post)
            }.addOnFailureListener {
                it.localizedMessage?.let { msg ->
                    uploadPostState.value = Result.Error(msg)
                }
            }
        } else {
            createPost(uploadPostState, post)
        }
    }

    fun togglePostLike(posting: Posting, isLike: Boolean) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        userId?.let {
            if(isLike) {
                likePost(posting, it)
            } else {
                unlikePost(posting, it)
            }
        }
    }

    private fun likePost(posting: Posting, userId: String) {
        posting.id?.let { postId ->
            //increment post's likeCount
            postRef.document(postId)
                .update(LIKE_COUNT, FieldValue.increment(1))

            //add current user to post's likes collection
            val like = Like(
                userId,
                postId,
                DateHelper.getCurrentDate()
            )
            postRef.document(postId).collection(LIKES_COLLECTION).document(userId).set(like)
        }
    }

    private fun unlikePost(posting: Posting, userId: String) {
        posting.id?.let { postId ->
            //decrement post's likeCount
            postRef.document(postId)
                .get()
                .addOnSuccessListener {
                    val post = it.toObject(Posting::class.java)
                    if(post != null) {
                        postRef.document(postId)
                            .update(LIKE_COUNT, (post.likeCount-1))
                    }
                }

            //remove current user from posts' likes collection
            postRef.document(postId).collection(LIKES_COLLECTION).document(userId).delete()
        }
    }

    private fun createPost(uploadPostState: MutableLiveData<Result<Map<String, Any?>>>, postMap: Map<String, Any?>) {
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