package com.sillytavern.android.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class Extension(
    @SerialName("id")
    val id: String = "",
    
    @SerialName("name")
    val name: String = "",
    
    @SerialName("description")
    val description: String = "",
    
    @SerialName("version")
    val version: String = "1.0.0",
    
    @SerialName("author")
    val author: String = "",
    
    @SerialName("enabled")
    val enabled: Boolean = false,
    
    @SerialName("scriptPath")
    val scriptPath: String = "",
    
    @SerialName("config")
    val config: Map<String, String> = emptyMap(),
    
    @SerialName("dependencies")
    val dependencies: List<String> = emptyList(),
    
    @SerialName("permissions")
    val permissions: List<String> = emptyList(),
    
    @SerialName("installedAt")
    val installedAt: Long = System.currentTimeMillis()
) : Parcelable

@Serializable
@Parcelize
data class ExtensionManifest(
    @SerialName("id")
    val id: String,
    
    @SerialName("name")
    val name: String,
    
    @SerialName("description")
    val description: String = "",
    
    @SerialName("version")
    val version: String = "1.0.0",
    
    @SerialName("author")
    val author: String = "",
    
    @SerialName("minAppVersion")
    val minAppVersion: String = "1.0.0",
    
    @SerialName("dependencies")
    val dependencies: List<String> = emptyList(),
    
    @SerialName("permissions")
    val permissions: List<String> = emptyList(),
    
    @SerialName("main")
    val main: String = "index.js",
    
    @SerialName("keywords")
    val keywords: List<String> = emptyList()
) : Parcelable

object ExtensionConstants {
    const val PERMISSION_INTERNET = "internet"
    const val PERMISSION_STORAGE = "storage"
    const val PERMISSION_NOTIFICATIONS = "notifications"
    const val PERMISSION_AUDIO = "audio"
    const val PERMISSION_CAMERA = "camera"
    
    val BUILT_IN_EXTENSIONS = listOf(
        Extension(
            id = "caption",
            name = "Caption",
            description = "Auto-generate captions for uploaded character images",
            version = "1.0.0",
            author = "SillyTavern Team",
            enabled = true,
            scriptPath = "extensions/caption/index.js"
        ),
        Extension(
            id = "translation",
            name = "Translation",
            description = "Translate messages between languages",
            version = "1.0.0",
            author = "SillyTavern Team",
            enabled = false,
            scriptPath = "extensions/translation/index.js"
        ),
        Extension(
            id = "expressions",
            name = "Expressions",
            description = "Character facial expressions based on emotions",
            version = "1.0.0",
            author = "SillyTavern Team",
            enabled = false,
            scriptPath = "extensions/expressions/index.js"
        ),
        Extension(
            id = "quiet",
            name = "Quiet",
            description = "Auto-silence when character is typing",
            version = "1.0.0",
            author = "SillyTavern Team",
            enabled = false,
            scriptPath = "extensions/quiet/index.js"
        ),
        Extension(
            id = "upload-images",
            name = "Upload Images",
            description = "Upload and share images in chat",
            version = "1.0.0",
            author = "SillyTavern Team",
            enabled = false,
            scriptPath = "extensions/upload-images/index.js"
        ),
        Extension(
            id = "context",
            name = "Context",
            description = "Context management for conversations",
            version = "1.0.0",
            author = "SillyTavern Team",
            enabled = true,
            scriptPath = "extensions/context/index.js"
        ),
        Extension(
            id = "randomizer",
            name = "Randomizer",
            description = "Randomize character responses",
            version = "1.0.0",
            author = "SillyTavern Team",
            enabled = false,
            scriptPath = "extensions/randomizer/index.js"
        ),
        Extension(
            id = "tts",
            name = "Text-to-Speech",
            description = "Convert AI responses to speech",
            version = "1.0.0",
            author = "SillyTavern Team",
            enabled = false,
            scriptPath = "extensions/tts/index.js"
        ),
        Extension(
            id = "st-script",
            name = "ST Script",
            description = "Script-based automation",
            version = "1.0.0",
            author = "SillyTavern Team",
            enabled = false,
            scriptPath = "extensions/st-script/index.js"
        ),
        Extension(
            id = "websearch",
            name = "Web Search",
            description = "Search the web for information",
            version = "1.0.0",
            author = "SillyTavern Team",
            enabled = false,
            scriptPath = "extensions/websearch/index.js"
        ),
        Extension(
            id = "compare",
            name = "Compare",
            description = "Compare AI responses",
            version = "1.0.0",
            author = "SillyTavern Team",
            enabled = false,
            scriptPath = "extensions/compare/index.js"
        ),
        Extension(
            id = "chromadb",
            name = "ChromaDB",
            description = "Vector database for memories",
            version = "1.0.0",
            author = "SillyTavern Team",
            enabled = false,
            scriptPath = "extensions/chromadb/index.js"
        ),
        Extension(
            id = "piggy",
            name = "Piggy",
            description = "Piggy back on other AI conversations",
            version = "1.0.0",
            author = "SillyTavern Team",
            enabled = false,
            scriptPath = "extensions/piggy/index.js"
        ),
        Extension(
            id = "voice",
            name = "Voice Input",
            description = "Voice recognition for input",
            version = "1.0.0",
            author = "SillyTavern Team",
            enabled = false,
            scriptPath = "extensions/voice/index.js"
        ),
        Extension(
            id = "sd-api",
            name = "Stable Diffusion API",
            description = "Image generation via Stable Diffusion",
            version = "1.0.0",
            author = "SillyTavern Team",
            enabled = false,
            scriptPath = "extensions/sd-api/index.js"
        )
    )
}
