package com.example.doctordeni.models.responses


import com.google.gson.annotations.SerializedName

data class Source(
    @SerializedName("id")
    var id: String? = null,
    @SerializedName("name")
    var name: String? = null
)