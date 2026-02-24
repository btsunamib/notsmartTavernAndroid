package com.sillytavern.android.data.repository

import com.sillytavern.android.data.model.OpenAIRequest
import com.sillytavern.android.data.remote.ApiService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SettingsRepository @Inject constructor(
    private val apiService: ApiService
) {
    private var currentApiKey: String = ""
    private var currentApiUrl: String = "https://api.openai.com/v1/chat/completions"
    private var currentApiType: String = "openai"
    
    fun setApiConfig(apiKey: String, apiUrl: String, apiType: String) {
        currentApiKey = apiKey
        currentApiUrl = apiUrl
        currentApiType = apiType
    }
    
    suspend fun generateResponse(
        request: OpenAIRequest,
        onToken: (String) -> Unit,
        onComplete: () -> Unit,
        onError: (Throwable) -> Unit
    ) {
        when (currentApiType) {
            "openai" -> {
                apiService.openAIChatCompletion(currentApiKey, request)
                    .onSuccess { response ->
                        response.choices.firstOrNull()?.message?.content?.let { content ->
                            onToken(content)
                        }
                        onComplete()
                    }
                    .onFailure { error ->
                        onError(error)
                    }
            }
            else -> {
                onError(Exception("Unsupported API type: $currentApiType"))
            }
        }
    }
}
