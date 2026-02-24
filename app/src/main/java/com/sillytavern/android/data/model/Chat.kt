package com.sillytavern.android.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class ChatMessage(
    @SerialName("name")
    val name: String = "",
    
    @SerialName("is_user")
    val isUser: Boolean = false,
    
    @SerialName("is_name")
    val isName: Boolean = false,
    
    @SerialName("send_date")
    val sendDate: Long = System.currentTimeMillis(),
    
    @SerialName("mes")
    val message: String = "",
    
    @SerialName("swipe_id")
    val swipeId: Int = 0,
    
    @SerialName("swipes")
    val swipes: List<Swipe> = emptyList(),
    
    @SerialName("extra")
    val extra: MessageExtra? = null,
    
    @SerialName("anchor_point")
    val anchorPoint: String? = null,
    
    @SerialName("last_anchor_point")
    val lastAnchorPoint: String? = null,
    
    @SerialName("gen_started")
    val genStarted: Long? = null,
    
    @SerialName("gen_finished")
    val genFinished: Long? = null,
    
    @SerialName("id")
    val id: Long = System.currentTimeMillis(),
    
    @SerialName("metadata")
    val metadata: MessageMetadata? = null
) : Parcelable

@Serializable
@Parcelize
data class Swipe(
    @SerialName("id")
    val id: Int = 0,
    
    @SerialName("message")
    val message: String = "",
    
    @SerialName("send_date")
    val sendDate: String = "",
    
    @SerialName("extra")
    val extra: SwipeExtra? = null
) : Parcelable

@Serializable
@Parcelize
data class SwipeExtra(
    @SerialName("gen_started")
    val genStarted: String? = null,
    
    @SerialName("gen_finished")
    val genFinished: String? = null,
    
    @SerialName("api")
    val api: String? = null,
    
    @SerialName("model")
    val model: String? = null
) : Parcelable

@Serializable
@Parcelize
data class MessageExtra(
    @SerialName("gen_started")
    val genStarted: String? = null,
    
    @SerialName("gen_finished")
    val genFinished: String? = null,
    
    @SerialName("api")
    val api: String? = null,
    
    @SerialName("model")
    val model: String? = null,
    
    @SerialName("intent")
    val intent: String? = null,
    
    @SerialName("use_mancer")
    val useMancer: Boolean? = null,
    
    @SerialName("use_aphrodite")
    val useAphrodite: Boolean? = null,
    
    @SerialName("use_extras")
    val useExtras: Boolean? = null,
    
    @SerialName("use_textgenerationwebui")
    val useTextGenerationWebUI: Boolean? = null,
    
    @SerialName("use_ollama")
    val useOllama: Boolean? = null,
    
    @SerialName("use_vllm")
    val useVllm: Boolean? = null,
    
    @SerialName("use_ai21")
    val useAi21: Boolean? = null,
    
    @SerialName("use_mistral")
    val useMistral: Boolean? = null,
    
    @SerialName("use_google")
    val useGoogle: Boolean? = null,
    
    @SerialName("use_claude")
    val useClaude: Boolean? = null,
    
    @SerialName("use_openai")
    val useOpenAI: Boolean? = null,
    
    @SerialName("use_kobold")
    val useKobold: Boolean? = null,
    
    @SerialName("use_horde")
    val useHorde: Boolean? = null,
    
    @SerialName("use_novel")
    val useNovel: Boolean? = null,
    
    @SerialName("use_ooba")
    val useOoba: Boolean? = null,
    
    @SerialName("use_tabby")
    val useTabby: Boolean? = null,
    
    @SerialName("use_cohee")
    val useCohee: Boolean? = null,
    
    @SerialName("use_perplexity")
    val usePerplexity: Boolean? = null,
    
    @SerialName("use_deepseek")
    val useDeepseek: Boolean? = null,
    
    @SerialName("use_xai")
    val useXai: Boolean? = null
) : Parcelable

@Serializable
@Parcelize
data class MessageMetadata(
    @SerialName("is_example")
    val isExample: Boolean = false,
    
    @SerialName("is_bookmark")
    val isBookmark: Boolean = false,
    
    @SerialName("is_fork")
    val isFork: Boolean = false,
    
    @SerialName("fork_id")
    val forkId: String? = null,
    
    @SerialName("bookmark_name")
    val bookmarkName: String? = null,
    
    @SerialName("bookmark_color")
    val bookmarkColor: String? = null
) : Parcelable

@Serializable
@Parcelize
data class Chat(
    @SerialName("name")
    val name: String = "",
    
    @SerialName("chat_id")
    val chatId: String = "",
    
    @SerialName("create_date")
    val createDate: Long = System.currentTimeMillis(),
    
    @SerialName("last_modified")
    val lastModified: Long = System.currentTimeMillis(),
    
    @SerialName("messages")
    val messages: List<ChatMessage> = emptyList(),
    
    @SerialName("character_id")
    val characterId: Long = 0,
    
    @SerialName("is_group")
    val isGroup: Boolean = false,
    
    @SerialName("group_id")
    val groupId: String? = null,
    
    @SerialName("metadata")
    val metadata: ChatMetadata? = null
) : Parcelable

@Serializable
@Parcelize
data class ChatMetadata(
    @SerialName("note")
    val note: String = "",
    
    @SerialName("note_position")
    val notePosition: Int = 0,
    
    @SerialName("note_depth")
    val noteDepth: Int = 4,
    
    @SerialName("target")
    val target: String = "",
    
    @SerialName("world_info")
    val worldInfo: List<String> = emptyList(),
    
    @SerialName("world_info_size")
    val worldInfoSize: Int = 0,
    
    @SerialName("world_info_depth")
    val worldInfoDepth: Int = 2,
    
    @SerialName("world_info_budget")
    val worldInfoBudget: Int = 25,
    
    @SerialName("world_info_include_names")
    val worldInfoIncludeNames: Boolean = true,
    
    @SerialName("world_info_recursive")
    val worldInfoRecursive: Boolean = true,
    
    @SerialName("world_info_overflow_alert")
    val worldInfoOverflowAlert: Boolean = false,
    
    @SerialName("world_info_case_sensitive")
    val worldInfoCaseSensitive: Boolean = false,
    
    @SerialName("world_info_match_whole_words")
    val worldInfoMatchWholeWords: Boolean = true,
    
    @SerialName("world_info_character_strategy")
    val worldInfoCharacterStrategy: Int = 1,
    
    @SerialName("world_info_budget_cap")
    val worldInfoBudgetCap: Int = 0
) : Parcelable
