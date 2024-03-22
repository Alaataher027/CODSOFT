package com.example.quoteofthedayapp.api

import com.example.quoteofthedayapp.api.model.QuoteResponseItem
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface WebServices {
    @GET("/v1/quotes")
    @Headers("X-Api-Key:QFPa90kc44H4O5K2RF+gzQ==qrY7NxSTRod6fY0N")
    fun getQuote(
        @Query("category") category: String
    ): Call<List<QuoteResponseItem>>
}
