package com.sillytavern.android.data.local

import androidx.room.TypeConverter
import java.util.Date

class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }

    @TypeConverter
    fun fromStringList(value: String?): List<String> {
        return value?.split("|||")?.filter { it.isNotEmpty() } ?: emptyList()
    }

    @TypeConverter
    fun stringListToString(list: List<String>?): String {
        return list?.joinToString("|||") ?: ""
    }

    @TypeConverter
    fun fromIntList(value: String?): List<Int> {
        return value?.split(",")?.mapNotNull { it.toIntOrNull() } ?: emptyList()
    }

    @TypeConverter
    fun intListToString(list: List<Int>?): String {
        return list?.joinToString(",") ?: ""
    }

    @TypeConverter
    fun fromDoubleList(value: String?): List<Double> {
        return value?.split(",")?.mapNotNull { it.toDoubleOrNull() } ?: emptyList()
    }

    @TypeConverter
    fun doubleListToString(list: List<Double>?): String {
        return list?.joinToString(",") ?: ""
    }

    @TypeConverter
    fun fromMap(value: String?): Map<String, String> {
        if (value.isNullOrEmpty()) return emptyMap()
        return value.split("|||")
            .filter { it.contains(":") }
            .associate {
                val (key, v) = it.split(":", limit = 2)
                key to v
            }
    }

    @TypeConverter
    fun mapToString(map: Map<String, String>?): String {
        return map?.entries?.joinToString("|||") { "${it.key}:${it.value}" } ?: ""
    }
}
