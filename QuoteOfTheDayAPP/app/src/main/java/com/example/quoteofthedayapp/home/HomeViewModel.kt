package com.example.quoteofthedayapp.home

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.quoteofthedayapp.api.ApiManager
import com.example.quoteofthedayapp.api.model.QuoteResponseItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel : ViewModel() {

    fun loadQuote(
        context: Context,
        category: String,
        onDataLoaded: (QuoteResponseItem?) -> Unit,
        onError: (String) -> Unit
    ) {
        ApiManager.getApis().getQuote(category)
            .enqueue(object : Callback<List<QuoteResponseItem>> {
                override fun onResponse(
                    call: Call<List<QuoteResponseItem>>,
                    response: Response<List<QuoteResponseItem>>
                ) {
                    if (response.isSuccessful) {
                        val quoteResponse = response.body()
                        if (quoteResponse != null) {
                            if (quoteResponse.isNotEmpty()) {
                                // Assuming you want to pick a random quote from the list
                                val randomQuote = quoteResponse.random()
                                onDataLoaded(randomQuote)
                            } else {
                                onError("No quotes found")
                            }
                        } else {
                            onError("Null quote response")
                        }
                    } else {
                        val errorMessage = "Error ${response.code()}: ${response.message()}"
                        onError(errorMessage)
                    }
                }

                override fun onFailure(call: Call<List<QuoteResponseItem>>, t: Throwable) {
                    onError(t.message ?: "Unknown error")
                }
            })
    }
}