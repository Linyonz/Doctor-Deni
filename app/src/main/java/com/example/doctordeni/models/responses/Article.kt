package com.example.doctordeni.models.responses


import com.google.gson.annotations.SerializedName

data class Article(
    @SerializedName("author")
    var author: String? = null,
    @SerializedName("content")
    var content: String? = null,
    @SerializedName("description")
    var description: String? = null,
    @SerializedName("publishedAt")
    var publishedAt: String? = null,
    @SerializedName("source")
    var source: Source? = null,
    @SerializedName("title")
    var title: String? = null,
    @SerializedName("url")
    var url: String? = null,
    @SerializedName("urlToImage")
    var urlToImage: String? = null
)