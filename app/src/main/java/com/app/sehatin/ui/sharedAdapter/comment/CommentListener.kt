package com.app.sehatin.ui.sharedAdapter.comment

import com.app.sehatin.data.model.User

interface CommentListener {
    fun onUserClick(user: User)
}