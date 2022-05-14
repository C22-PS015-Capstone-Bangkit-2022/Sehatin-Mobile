package com.app.sehatin.data.repository

import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.app.sehatin.data.model.Comment
import com.app.sehatin.data.model.Posting
import com.app.sehatin.utils.DateHelper
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.io.File
import com.app.sehatin.data.Result

const val POST_COLLECTION = "Post"
const val POST_IMAGE_STORAGE = "POST_IMAGE"

class PostingRepository() {
    private val postRef = FirebaseFirestore.getInstance().collection(POST_COLLECTION)

    fun getPosts(): List<Posting> {
        return posts
    }

    fun uploadPost(uploadPostState: MutableLiveData<Result<Posting>>, postImage: File?, postDescription: String, postTags: List<String>) {
        val postId = postRef.document().id
        val userId = FirebaseAuth.getInstance().currentUser!!.uid
        val hasImage = postImage != null
        val post = Posting(
            id = postId,
            userId = userId,
            createdAt = DateHelper.getCurrentDate(),
            hasImage = hasImage,
            image = null,
            description = postDescription,
            tags = postTags,
            likes = 0,
            comment = null,
            isLiked = false,
            isCommented = false
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
                post.image = it.toString()
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

    private fun savePost(uploadPostState: MutableLiveData<Result<Posting>>, posting: Posting) {
        posting.id?.let {
            postRef.document(it).set(posting)
                .addOnSuccessListener {
                    uploadPostState.value = Result.Success(posting)
                }
                .addOnFailureListener { e ->
                    e.localizedMessage?.let { msg ->
                        uploadPostState.value = Result.Error(msg)
                    }
                }
        }
    }

    private val posts = arrayListOf(
        Posting(
            "1",
            "asda",
            "2022-05-13T23:59:22+08:00",
            true,
            "https://i.pinimg.com/736x/e1/b6/6b/e1b66bbf48b15c026d4ee1c184455cc4.jpg",
            "asjasdn aoidlaksnd oasdkasd aslasdknasd ",
            listOf("Kanker", "Diabetes"),
            10,
            listOf(
                Comment(
                "1",
                    "asds",
                    "asdaw",
                    "asdadawda wdaw",
                    "01 Mei 2022"
                )
            ),
            isLiked = false,
            isCommented = false
        ),

        Posting(
            "2",
            "asda",
            "2022-05-13T23:59:22+08:00",
            false,
            null,
            "asjasdn aoidlaksnd oasdkasd aslasdknasd asjasdn aoidlaksnd oasdkasd aslasdknasd  asjasdn aoidlaksnd oasdkasd aslasdknasd  asjasdn aoidlaksnd oasdkasd aslasdknasd  asjasdn aoidlaksnd oasdkasd aslasdknasd  asjasdn aoidlaksnd oasdkasd aslasdknasd  asjasdn aoidlaksnd oasdkasd aslasdknasd  asjasdn aoidlaksnd oasdkasd aslasdknasd ",
            listOf("Kanker", "Diabetes"),
            10,
            listOf(
                Comment(
                    "1",
                    "asds",
                    "asdaw",
                    "asdadawda wdaw",
                    "01 Mei 2022"
                )
            ),
            isLiked = false,
            isCommented = false
        ),

        Posting(
            "3",
            "asda",
            "2022-05-13T23:59:22+07:00",
            false,
            null,
            "asjasdn aoidlaksnd oasdkasd aslasdknasd asjasdn aoidlaksnd oasdkasd aslasdknasd  asjasdn aoidlaksnd oasdkasd aslasdknasd  asjasdn aoidlaksnd oasdkasd aslasdknasd  asjasdn aoidlaksnd oasdkasd aslasdknasd  asjasdn aoidlaksnd oasdkasd aslasdknasd  asjasdn aoidlaksnd oasdkasd aslasdknasd  asjasdn aoidlaksnd oasdkasd aslasdknasd ",
            null,
            10,
            listOf(
                Comment(
                    "1",
                    "asds",
                    "asdaw",
                    "asdadawda wdaw",
                    "01 Mei 2022"
                )
            ),
            isLiked = false,
            isCommented = false
        )
    )

    private companion object {
        const val TAG = "PostingRepository"
    }

}