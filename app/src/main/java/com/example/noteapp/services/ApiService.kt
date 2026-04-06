package com.example.noteapp.services

import com.example.noteapp.models.Post
import retrofit2.http.GET

interface ApiService {
    @GET("posts")
    suspend fun getPosts(): List<Post>
}