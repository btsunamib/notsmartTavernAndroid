package com.sillytavern.android.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class Preset(
    @SerialName("name")
    val name: String = "Default",
    
    @SerialName("temp")
    val temperature: Double = 1.0,
    
    @SerialName("top_p")
    val topP: Double = 0.9,
    
    @SerialName("top_k")
    val topK: Int = 0,
    
    @SerialName("top_a")
    val topA: Double = 0.0,
    
    @SerialName("tfs")
    val tailFreeSampling: Double = 1.0,
    
    @SerialName("typical_p")
    val typicalP: Double = 1.0,
    
    @SerialName("rep_pen")
    val repetitionPenalty: Double = 1.1,
    
    @SerialName("rep_pen_range")
    val repetitionPenaltyRange: Int = 0,
    
    @SerialName("rep_pen_slope")
    val repetitionPenaltySlope: Double = 0.0,
    
    @SerialName("no_repeat_ngram_size")
    val noRepeatNgramSize: Int = 0,
    
    @SerialName("penalty_alpha")
    val penaltyAlpha: Double = 0.0,
    
    @SerialName("num_beams")
    val numBeams: Int = 1,
    
    @SerialName("length_penalty")
    val lengthPenalty: Double = 1.0,
    
    @SerialName("min_length")
    val minLength: Int = 0,
    
    @SerialName("encoder_rep_pen")
    val encoderRepetitionPenalty: Double = 1.0,
    
    @SerialName("do_sample")
    val doSample: Boolean = true,
    
    @SerialName("early_stopping")
    val earlyStopping: Boolean = false,
    
    @SerialName("seed")
    val seed: Long = -1,
    
    @SerialName("add_bos_token")
    val addBosToken: Boolean = true,
    
    @SerialName("stopping_strings")
    val stoppingStrings: List<String> = emptyList(),
    
    @SerialName("ban_eos_token")
    val banEosToken: Boolean = false,
    
    @SerialName("skip_special_tokens")
    val skipSpecialTokens: Boolean = true,
    
    @SerialName("streaming")
    val streaming: Boolean = false,
    
    @SerialName("mirostat_mode")
    val mirostatMode: Int = 0,
    
    @SerialName("mirostat_tau")
    val mirostatTau: Double = 5.0,
    
    @SerialName("mirostat_eta")
    val mirostatEta: Double = 0.1,
    
    @SerialName("guidance_scale")
    val guidanceScale: Double = 1.0,
    
    @SerialName("negative_prompt")
    val negativePrompt: String = "",
    
    @SerialName("sampler_priority")
    val samplerPriority: List<String> = listOf(
        "temperature", "dynamic_temperature", "quadratic_sampling",
        "top_k", "top_p", "typical_p", "epsilon_cutoff",
        "eta_cutoff", "tfs", "top_a", "min_p", "mirostat"
    ),
    
    @SerialName("samplers")
    val samplers: List<String> = listOf(
        "top_k", "tfs_z", "typical_p", "top_p", "min_p", "temperature"
    ),
    
    @SerialName("epsilon_cutoff")
    val epsilonCutoff: Double = 0.0,
    
    @SerialName("eta_cutoff")
    val etaCutoff: Double = 0.0,
    
    @SerialName("order")
    val order: List<Int> = listOf(6, 0, 1, 2, 3, 4, 5),
    
    @SerialName("frequency_penalty")
    val frequencyPenalty: Double = 0.0,
    
    @SerialName("presence_penalty")
    val presencePenalty: Double = 0.0,
    
    @SerialName("logit_bias")
    val logitBias: List<LogitBias> = emptyList()
) : Parcelable

@Serializable
@Parcelize
data class LogitBias(
    @SerialName("id")
    val id: String = "",
    
    @SerialName("text")
    val text: String = "",
    
    @SerialName("value")
    val value: Double = 0.0
) : Parcelable

@Serializable
@Parcelize
data class OpenAIPreset(
    @SerialName("name")
    val name: String = "Default",
    
    @SerialName("temp")
    val temperature: Double = 1.0,
    
    @SerialName("top_p")
    val topP: Double = 1.0,
    
    @SerialName("top_k")
    val topK: Int = 0,
    
    @SerialName("frequency_penalty")
    val frequencyPenalty: Double = 0.0,
    
    @SerialName("presence_penalty")
    val presencePenalty: Double = 0.0,
    
    @SerialName("max_context")
    val maxContext: Int = 4095,
    
    @SerialName("max_tokens")
    val maxTokens: Int = 300,
    
    @SerialName("stream")
    val stream: Boolean = true,
    
    @SerialName("model")
    val model: String = "gpt-4-turbo",
    
    @SerialName("prompts")
    val prompts: List<PromptConfig> = emptyList(),
    
    @SerialName("prompt_order")
    val promptOrder: List<PromptOrderConfig> = emptyList(),
    
    @SerialName("wrap_in_quotes")
    val wrapInQuotes: Boolean = false,
    
    @SerialName("send_if_empty")
    val sendIfEmpty: String = "",
    
    @SerialName("impersonation_prompt")
    val impersonationPrompt: String = "",
    
    @SerialName("new_chat_prompt")
    val newChatPrompt: String = "[Start a new Chat]",
    
    @SerialName("new_group_chat_prompt")
    val newGroupChatPrompt: String = "[Start a new group chat. Group members: {{group}}]",
    
    @SerialName("continue_nudge_prompt")
    val continueNudgePrompt: String = "[Continue your last message without repeating its original content.]"
) : Parcelable

@Serializable
@Parcelize
data class PromptConfig(
    @SerialName("identifier")
    val identifier: String = "",
    
    @SerialName("name")
    val name: String = "",
    
    @SerialName("role")
    val role: String = "system",
    
    @SerialName("content")
    val content: String = "",
    
    @SerialName("system_prompt")
    val systemPrompt: Boolean = false,
    
    @SerialName("marker")
    val marker: Boolean = false
) : Parcelable

@Serializable
@Parcelize
data class PromptOrderConfig(
    @SerialName("character_id")
    val characterId: Int = 100000,
    
    @SerialName("order")
    val order: List<PromptOrderItem> = emptyList()
) : Parcelable

@Serializable
@Parcelize
data class PromptOrderItem(
    @SerialName("identifier")
    val identifier: String = "",
    
    @SerialName("enabled")
    val enabled: Boolean = true
) : Parcelable
