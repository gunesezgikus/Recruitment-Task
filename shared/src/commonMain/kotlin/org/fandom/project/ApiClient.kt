package org.fandom.project

import io.ktor.client.*
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json



class ApiClient {
    private val client = HttpClient{
        install(ContentNegotiation){
            json(Json{
                ignoreUnknownKeys = true
                prettyPrint = true
                isLenient = true
            })
        }
    }
    suspend fun fetchArticles():List<Article>{
        return try {
            val response = client.get("https://services.fandom.com/mobile-sidekick/trending/articles")
            response.body<List<Article>>()
        } catch (e: Exception){
            println("API error: ${e.message}")
            emptyList()
        }
    }


}