package com.sillytavern.android.data.remote

import com.sillytavern.android.data.model.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.sse.EventSource
import okhttp3.sse.EventSourceListener
import okhttp3.sse.EventSources
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface OpenAIApiService {
    @POST("v1/chat/completions")
    @Headers("Content-Type: application/json")
    suspend fun chatCompletion(@Body request: OpenAIRequest): OpenAIResponse
    
    @Streaming
    @POST("v1/chat/completions")
    @Headers("Content-Type: application/json")
    fun streamChatCompletion(@Body request: OpenAIRequest): OpenAIResponse
}

interface ClaudeApiService {
    companion object {
        fun create(client: OkHttpClient): ClaudeApiService {
            return Retrofit.Builder()
                .client(client)
                .baseUrl("https://api.anthropic.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ClaudeApiService::class.java)
        }
    }
    
    @POST("v1/messages")
    suspend fun createMessage(
        @Header("x-api-key") apiKey: String,
        @Header("anthropic-version") version: String = "2023-06-01",
        @Body request: Map<String, Any>
    ): Map<String, Any>
}

interface KoboldApiService {
    companion object {
        fun create(client: OkHttpClient): KoboldApiService {
            return Retrofit.Builder()
                .client(client)
                .baseUrl("http://127.0.0.1:5000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(KoboldApiService::class.java)
        }
    }
    
    @POST("api/v1/generate")
    suspend fun generate(@Body request: KoboldRequest): KoboldResponse
    
    @GET("api/v1/model")
    suspend fun getModel(): Map<String, String>
    
    @GET("api/v1/info/version")
    suspend fun getVersion(): Map<String, String>
}

class ApiService(
    private val openAIApiService: OpenAIApiService,
    private val claudeApiService: ClaudeApiService,
    private val koboldApiService: KoboldApiService
) {
    suspend fun openAIChatCompletion(
        apiKey: String,
        request: OpenAIRequest
    ): Result<OpenAIResponse> {
        return try {
            Result.success(openAIApiService.chatCompletion(request))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun claudeCreateMessage(
        apiKey: String,
        request: Map<String, Any>
    ): Result<Map<String, Any>> {
        return try {
            Result.success(claudeApiService.createMessage(apiKey, request = request))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun koboldGenerate(
        baseUrl: String,
        request: KoboldRequest
    ): Result<KoboldResponse> {
        return try {
            Result.success(koboldApiService.generate(request))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    fun streamOpenAIChat(
        client: OkHttpClient,
        url: String,
        apiKey: String,
        request: OpenAIRequest,
        onToken: (String) -> Unit,
        onComplete: () -> Unit,
        onError: (Throwable) -> Unit
    ): EventSource {
        val eventSourceFactory = EventSources.createFactory(client)
        val requestBody = com.google.gson.Gson().toJson(request)
            .toRequestBody("application/json".toMediaType())
        
        val httpRequest = Request.Builder()
            .url(url)
            .header("Authorization", "Bearer $apiKey")
            .header("Content-Type", "application/json")
            .post(requestBody)
            .build()
        
        val listener = object : EventSourceListener() {
            private val buffer = StringBuilder()
            
            override fun onEvent(
                eventSource: EventSource,
                id: String?,
                type: String?,
                data: String
            ) {
                if (data == "[DONE]") {
                    onComplete()
                    return
                }
                
                try {
                    val response = com.google.gson.Gson().fromJson(data, OpenAIResponse::class.java)
                    response.choices.firstOrNull()?.delta?.content?.let { content ->
                        buffer.append(content)
                        onToken(content)
                    }
                } catch (e: Exception) {
                }
            }
            
            override fun onClosed(eventSource: EventSource) {
                if (buffer.isNotEmpty()) {
                    onComplete()
                }
            }
            
            override fun onFailure(eventSource: EventSource, t: Throwable?, response: okhttp3.Response?) {
                onError(t ?: Exception("Stream failed: ${response?.message}"))
            }
        }
        
        return eventSourceFactory.newEventSource(httpRequest, listener)
    }
}
