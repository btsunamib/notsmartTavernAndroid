package com.sillytavern.android.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class RegexScript(
    @SerialName("scriptName")
    val scriptName: String = "",
    
    @SerialName("findRegex")
    val findRegex: String = "",
    
    @SerialName("replaceString")
    val replaceString: String = "",
    
    @SerialName("trimStrings")
    val trimStrings: List<String> = emptyList(),
    
    @SerialName("placement")
    val placement: Int = 0,
    
    @SerialName("disabled")
    val disabled: Boolean = false,
    
    @SerialName("markdownOnly")
    val markdownOnly: Boolean = false,
    
    @SerialName("promptOnly")
    val promptOnly: Boolean = false,
    
    @SerialName("runOnEdit")
    val runOnEdit: Boolean = false,
    
    @SerialName("substituteRegex")
    val substituteRegex: String = "",
    
    @SerialName("minDepth")
    val minDepth: Int = 0,
    
    @SerialName("maxDepth")
    val maxDepth: Int = 0,
    
    @SerialName("id")
    val id: Long = System.currentTimeMillis(),
    
    @SerialName("created_at")
    val createdAt: Long = System.currentTimeMillis(),
    
    @SerialName("last_updated")
    val lastUpdated: Long = System.currentTimeMillis()
) : Parcelable {
    companion object {
        const val PLACEMENT_REPLACE = 0
        const val PLACEMENT_BEFORE = 1
        const val PLACEMENT_AFTER = 2
        const val PLACEMENT_MODIFY_OUTPUT = 3
        const val PLACEMENT_MODIFY_INPUT = 4
        const val PLACEMENT_SLASH_COMMAND = 5
    }
}

@Serializable
@Parcelize
data class RegexSettings(
    @SerialName("scripts")
    val scripts: List<RegexScript> = emptyList(),
    
    @SerialName("max_recursion")
    val maxRecursion: Int = 100,
    
    @SerialName("timeout_ms")
    val timeoutMs: Int = 5000
) : Parcelable
