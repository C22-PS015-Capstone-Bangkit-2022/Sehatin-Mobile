package com.app.sehatin.data.repository

import com.app.sehatin.data.model.Comment
import com.app.sehatin.data.model.Posting
import com.app.sehatin.data.remote.ApiService

class PostingRepository(private val apiService: ApiService) {

    fun getPosts(): List<Posting> {
        return posts
    }

    private val posts = arrayListOf(
        Posting(
            "1",
            "asda",
            "01 Mei 2022",
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
        )
    )

}