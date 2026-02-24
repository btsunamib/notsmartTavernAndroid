package com.sillytavern.android.util

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import com.sillytavern.android.data.model.CharacterCardV2
import com.sillytavern.android.data.model.CharacterCardV3
import com.sillytavern.android.data.model.CharacterData
import com.sillytavern.android.data.model.CharacterDataExtended
import kotlinx.serialization.json.Json
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.util.zip.CRC32

object CharacterCardParser {
    private val PNG_SIGNATURE = byteArrayOf(
        0x89.toByte(), 0x50, 0x4E, 0x47, 0x0D, 0x0A, 0x1A, 0x0A
    )
    
    private const val CHUNK_CHARA = "chara"
    private const val CHUNK_CCV3 = "ccv3"
    private const val CHUNK_TEXT = "tEXt"
    private const val CHUNK_IEND = "IEND"
    
    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
        coerceInputValues = true
    }
    
    data class PngChunk(
        val type: String,
        val data: ByteArray
    )
    
    fun parseCharacterCard(inputStream: InputStream): ParseResult {
        val chunks = extractChunks(inputStream)
        
        val ccv3Chunk = chunks.find { 
            it.type == CHUNK_TEXT && extractKeyword(it.data).first.equals(CHUNK_CCV3, ignoreCase = true)
        }
        
        val charaChunk = chunks.find { 
            it.type == CHUNK_TEXT && extractKeyword(it.data).first.equals(CHUNK_CHARA, ignoreCase = true)
        }
        
        val textData = when {
            ccv3Chunk != null -> extractKeyword(ccv3Chunk.data).second
            charaChunk != null -> extractKeyword(charaChunk.data).second
            else -> throw CharacterCardException("No character data found in PNG")
        }
        
        val decodedData = try {
            Base64.decode(textData, Base64.DEFAULT).toString(Charsets.UTF_8)
        } catch (e: Exception) {
            throw CharacterCardException("Failed to decode character data: ${e.message}")
        }
        
        val characterData = parseCharacterJson(decodedData)
        
        return ParseResult(
            characterData = characterData,
            imageData = extractImageData(chunks)
        )
    }
    
    fun parseCharacterJson(jsonString: String): CharacterDataExtended {
        return try {
            val jsonObject = kotlinx.serialization.json.Json.parseToJsonElement(jsonString)
            val spec = jsonObject.jsonObject?.get("spec")?.jsonPrimitive?.content
            
            when (spec) {
                "chara_card_v3" -> {
                    json.decodeFromString<CharacterCardV3>(jsonString).data
                }
                "chara_card_v2" -> {
                    val v2Data = json.decodeFromString<CharacterCardV2>(jsonString).data
                    CharacterDataExtended(
                        name = v2Data.name,
                        description = v2Data.description,
                        personality = v2Data.personality,
                        scenario = v2Data.scenario,
                        firstMes = v2Data.firstMes,
                        mesExample = v2Data.mesExample,
                        creatorNotes = v2Data.creatorNotes,
                        systemPrompt = v2Data.systemPrompt,
                        postHistoryInstructions = v2Data.postHistoryInstructions,
                        alternateGreetings = v2Data.alternateGreetings,
                        characterBook = v2Data.characterBook,
                        tags = v2Data.tags,
                        creator = v2Data.creator,
                        characterVersion = v2Data.characterVersion,
                        extensions = v2Data.extensions
                    )
                }
                else -> {
                    val legacyData = json.decodeFromString<CharacterData>(jsonString)
                    CharacterDataExtended(
                        name = legacyData.name,
                        description = legacyData.description,
                        personality = legacyData.personality,
                        scenario = legacyData.scenario,
                        firstMes = legacyData.firstMes,
                        mesExample = legacyData.mesExample,
                        creatorNotes = legacyData.creatorNotes,
                        systemPrompt = legacyData.systemPrompt,
                        postHistoryInstructions = legacyData.postHistoryInstructions,
                        alternateGreetings = legacyData.alternateGreetings,
                        characterBook = legacyData.characterBook,
                        tags = legacyData.tags,
                        creator = legacyData.creator,
                        characterVersion = legacyData.characterVersion,
                        extensions = legacyData.extensions
                    )
                }
            }
        } catch (e: Exception) {
            throw CharacterCardException("Failed to parse character JSON: ${e.message}")
        }
    }
    
    private fun extractChunks(inputStream: InputStream): List<PngChunk> {
        val chunks = mutableListOf<PngChunk>()
        
        val signature = ByteArray(8)
        inputStream.read(signature)
        
        if (!signature.contentEquals(PNG_SIGNATURE)) {
            throw CharacterCardException("Invalid PNG signature")
        }
        
        while (true) {
            val lengthBytes = ByteArray(4)
            val bytesRead = inputStream.read(lengthBytes)
            if (bytesRead < 4) break
            
            val length = ByteBuffer.wrap(lengthBytes).order(ByteOrder.BIG_ENDIAN).int
            
            val typeBytes = ByteArray(4)
            inputStream.read(typeBytes)
            val type = String(typeBytes, Charsets.US_ASCII)
            
            val data = ByteArray(length)
            inputStream.read(data)
            
            val crcBytes = ByteArray(4)
            inputStream.read(crcBytes)
            
            chunks.add(PngChunk(type, data))
            
            if (type == CHUNK_IEND) break
        }
        
        return chunks
    }
    
    private fun extractKeyword(data: ByteArray): Pair<String, String> {
        val nullIndex = data.indexOf(0)
        if (nullIndex == -1) {
            return Pair("", String(data, Charsets.US_ASCII))
        }
        
        val keyword = String(data.copyOfRange(0, nullIndex), Charsets.US_ASCII)
        val text = String(data.copyOfRange(nullIndex + 1, data.size), Charsets.ISO_8859_1)
        
        return Pair(keyword, text)
    }
    
    private fun extractImageData(chunks: List<PngChunk>): ByteArray {
        val outputStream = ByteArrayOutputStream()
        
        outputStream.write(PNG_SIGNATURE)
        
        for (chunk in chunks) {
            val lengthBytes = ByteBuffer.allocate(4).order(ByteOrder.BIG_ENDIAN).putInt(chunk.data.size).array()
            outputStream.write(lengthBytes)
            outputStream.write(chunk.type.toByteArray(Charsets.US_ASCII))
            outputStream.write(chunk.data)
            
            val crc = CRC32()
            crc.update(chunk.type.toByteArray(Charsets.US_ASCII))
            crc.update(chunk.data)
            val crcValue = crc.value.toInt()
            val crcBytes = ByteBuffer.allocate(4).order(ByteOrder.BIG_ENDIAN).putInt(crcValue).array()
            outputStream.write(crcBytes)
        }
        
        return outputStream.toByteArray()
    }
    
    data class ParseResult(
        val characterData: CharacterDataExtended,
        val imageData: ByteArray
    )
    
    class CharacterCardException(message: String) : Exception(message)
}
