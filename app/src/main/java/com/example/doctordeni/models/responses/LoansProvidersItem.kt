package com.example.doctordeni.models.responses


import com.google.gson.annotations.SerializedName

data class LoansProvidersItem(
    @SerializedName("interestRate")
    var interestRate: Double? = 10.0,
    @SerializedName("name")
    var name: String? = "provider"
)