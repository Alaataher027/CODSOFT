package com.example.quoteofthedayapp.api.model

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class QuoteResponseItem(

	@field:SerializedName("quote")
	val quote: String? = null,

	@field:SerializedName("author")
	val author: String? = null,

	@field:SerializedName("category")
	val category: String? = null
) : Parcelable