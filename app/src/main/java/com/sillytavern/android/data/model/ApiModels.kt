package com.sillytavern.android.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

enum class ApiType {
    OPENAI,
    CLAUDE,
    MISTRAL,
    GOOGLE,
    KOBOLD,
    KOBOLD_HORDE,
    NOVELAI,
    OOBABOOGA,
    TABBY,
    OPENROUTER,
    AI21,
    VLLM,
    OLLAMA,
    CUSTOM
}

@Serializable
@Parcelize
data class ApiConfig(
    @SerialName("api_type")
    val apiType: String = "openai",
    
    @SerialName("api_url")
    val apiUrl: String = "",
    
    @SerialName("api_key")
    val apiKey: String = "",
    
    @SerialName("model")
    val model: String = "",
    
    @SerialName("reverse_proxy")
    val reverseProxy: String = "",
    
    @SerialName("proxy_password")
    val proxyPassword: String = "",
    
    @SerialName("max_context")
    val maxContext: Int = 4096,
    
    @SerialName("max_tokens")
    val maxTokens: Int = 300,
    
    @SerialName("streaming")
    val streaming: Boolean = true,
    
    @SerialName("timeout")
    val timeout: Int = 120000,
    
    @SerialName("headers")
    val headers: Map<String, String> = emptyMap(),
    
    @SerialName("params")
    val params: Map<String, String> = emptyMap()
) : Parcelable

@Serializable
@Parcelize
data class OpenAIRequest(
    @SerialName("model")
    val model: String,
    
    @SerialName("messages")
    val messages: List<OpenAIMessage>,
    
    @SerialName("temperature")
    val temperature: Double? = null,
    
    @SerialName("top_p")
    val topP: Double? = null,
    
    @SerialName("max_tokens")
    val maxTokens: Int? = null,
    
    @SerialName("stream")
    val stream: Boolean = false,
    
    @SerialName("frequency_penalty")
    val frequencyPenalty: Double? = null,
    
    @SerialName("presence_penalty")
    val presencePenalty: Double? = null,
    
    @SerialName("stop")
    val stop: List<String>? = null,
    
    @SerialName("logit_bias")
    val logitBias: Map<String, Double>? = null
) : Parcelable

@Serializable
@Parcelize
data class OpenAIMessage(
    @SerialName("role")
    val role: String,
    
    @SerialName("content")
    val content: String,
    
    @SerialName("name")
    val name: String? = null
) : Parcelable

@Serializable
@Parcelize
data class OpenAIResponse(
    @SerialName("id")
    val id: String = "",
    
    @SerialName("object")
    val objectType: String = "",
    
    @SerialName("created")
    val created: Long = 0,
    
    @SerialName("model")
    val model: String = "",
    
    @SerialName("choices")
    val choices: List<OpenAIChoice> = emptyList(),
    
    @SerialName("usage")
    val usage: OpenAIUsage? = null,
    
    @SerialName("error")
    val error: OpenAIError? = null
) : Parcelable

@Serializable
@Parcelize
data class OpenAIChoice(
    @SerialName("index")
    val index: Int = 0,
    
    @SerialName("message")
    val message: OpenAIMessage? = null,
    
    @SerialName("delta")
    val delta: OpenAIMessage? = null,
    
    @SerialName("finish_reason")
    val finishReason: String? = null,
    
    @SerialName("text")
    val text: String? = null
) : Parcelable

@Serializable
@Parcelize
data class OpenAIUsage(
    @SerialName("prompt_tokens")
    val promptTokens: Int = 0,
    
    @SerialName("completion_tokens")
    val completionTokens: Int = 0,
    
    @SerialName("total_tokens")
    val totalTokens: Int = 0
) : Parcelable

@Serializable
@Parcelize
data class OpenAIError(
    @SerialName("message")
    val message: String = "",
    
    @SerialName("type")
    val type: String = "",
    
    @SerialName("code")
    val code: String? = null
) : Parcelable

@Serializable
@Parcelize
data class ClaudeRequest(
    @SerialName("model")
    val model: String,
    
    @SerialName("messages")
    val messages: List<ClaudeMessage>,
    
    @SerialName("system")
    val system: String? = null,
    
    @SerialName("max_tokens")
    val maxTokens: Int,
    
    @SerialName("temperature")
    val temperature: Double? = null,
    
    @SerialName("top_p")
    val topP: Double? = null,
    
    @SerialName("top_k")
    val topK: Int? = null,
    
    @SerialName("stream")
    val stream: Boolean = false,
    
    @SerialName("stop_sequences")
    val stopSequences: List<String>? = null
) : Parcelable

@Serializable
@Parcelize
data class ClaudeMessage(
    @SerialName("role")
    val role: String,
    
    @SerialName("content")
    val content: String
) : Parcelable

@Serializable
@Parcelize
data class ClaudeResponse(
    @SerialName("id")
    val id: String = "",
    
    @SerialName("type")
    val type: String = "",
    
    @SerialName("role")
    val role: String = "",
    
    @SerialName("content")
    val content: List<ClaudeContent> = emptyList(),
    
    @SerialName("model")
    val model: String = "",
    
    @SerialName("stop_reason")
    val stopReason: String? = null,
    
    @SerialName("usage")
    val usage: ClaudeUsage? = null,
    
    @SerialName("error")
    val error: ClaudeError? = null
) : Parcelable

@Serializable
@Parcelize
data class ClaudeContent(
    @SerialName("type")
    val type: String = "",
    
    @SerialName("text")
    val text: String = ""
) : Parcelable

@Serializable
@Parcelize
data class ClaudeUsage(
    @SerialName("input_tokens")
    val inputTokens: Int = 0,
    
    @SerialName("output_tokens")
    val outputTokens: Int = 0
) : Parcelable

@Serializable
@Parcelize
data class ClaudeError(
    @SerialName("type")
    val type: String = "",
    
    @SerialName("message")
    val message: String = ""
) : Parcelable

@Serializable
@Parcelize
data class KoboldRequest(
    @SerialName("prompt")
    val prompt: String,
    
    @SerialName("max_length")
    val maxLength: Int? = null,
    
    @SerialName("max_context_length")
    val maxContextLength: Int? = null,
    
    @SerialName("temperature")
    val temperature: Double? = null,
    
    @SerialName("top_p")
    val topP: Double? = null,
    
    @SerialName("top_k")
    val topK: Int? = null,
    
    @SerialName("top_a")
    val topA: Double? = null,
    
    @SerialName("typical")
    val typical: Double? = null,
    
    @SerialName("tfs")
    val tfs: Double? = null,
    
    @SerialName("rep_pen")
    val repPen: Double? = null,
    
    @SerialName("rep_pen_range")
    val repPenRange: Int? = null,
    
    @SerialName("rep_pen_slope")
    val repPenSlope: Double? = null,
    
    @SerialName("stop_sequence")
    val stopSequence: List<String>? = null,
    
    @SerialName("stream")
    val stream: Boolean = false,
    
    @SerialName("sampler_order")
    val samplerOrder: List<Int>? = null,
    
    @SerialName("mirostat")
    val mirostat: Int? = null,
    
    @SerialName("mirostat_tau")
    val mirostatTau: Double? = null,
    
    @SerialName("mirostat_eta")
    val mirostatEta: Double? = null,
    
    @SerialName("grammar")
    val grammar: String? = null,
    
    @SerialName("genkey")
    val genkey: String? = null
) : Parcelable

@Serializable
@Parcelize
data class KoboldResponse(
    @SerialName("results")
    val results: List<KoboldResult> = emptyList(),
    
    @SerialName("token")
    val token: String? = null,
    
    @SerialName("error")
    val error: String? = null
) : Parcelable

@Serializable
@Parcelize
data class KoboldResult(
    @SerialName("text")
    val text: String = ""
) : Parcelable
