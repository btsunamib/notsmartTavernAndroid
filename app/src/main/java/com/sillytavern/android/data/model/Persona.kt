package com.sillytavern.android.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class Persona(
    @SerialName("name")
    val name: String = "",
    
    @SerialName("description")
    val description: String = "",
    
    @SerialName("avatar")
    val avatar: String = "",
    
    @SerialName("id")
    val id: Long = System.currentTimeMillis(),
    
    @SerialName("created_at")
    val createdAt: Long = System.currentTimeMillis(),
    
    @SerialName("last_updated")
    val lastUpdated: Long = System.currentTimeMillis(),
    
    @SerialName("is_default")
    val isDefault: Boolean = false,
    
    @SerialName("extensions")
    val extensions: Map<String, String> = emptyMap()
) : Parcelable

@Serializable
@Parcelize
data class PersonaSettings(
    @SerialName("personas")
    val personas: Map<String, Persona> = emptyMap(),
    
    @SerialName("default_persona")
    val defaultPersona: String? = null,
    
    @SerialName("persona_descriptions")
    val personaDescriptions: Map<String, String> = emptyMap(),
    
    @SerialName("current_persona")
    val currentPersona: String? = null
) : Parcelable
