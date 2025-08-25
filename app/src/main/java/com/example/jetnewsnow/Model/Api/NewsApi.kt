package com.example.jetnewsnow.Model.Api

import com.example.jetnewsnow.Const
import com.example.jetnewsnow.Model.NewsResponse.NewsResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {

    @GET("v2/top-headlines")
    suspend fun getTopHeadlines(
        @Query("country")
        countryCode: String = "us",

        @Query("category")
        category: String? = null,

        @Query("apiKey")
        apiKey : String = Const.Api_Key
    ) : NewsResponse


    @GET("v2/everything")
    suspend fun searchForNews(
        @Query("q")
        query: String,

        @Query("apikey")
        apiKey : String = Const.Api_Key
    ) : NewsResponse

    companion object {

        fun create() : NewsApi {
            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create()).baseUrl(Const.Base_Url).build()

            return retrofit.create(NewsApi::class.java)
        }
    }
}