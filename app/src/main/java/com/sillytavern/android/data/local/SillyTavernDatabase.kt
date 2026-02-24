package com.sillytavern.android.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.sillytavern.android.data.local.dao.CharacterDao
import com.sillytavern.android.data.local.dao.ChatDao
import com.sillytavern.android.data.local.dao.MessageDao
import com.sillytavern.android.data.local.dao.WorldInfoDao
import com.sillytavern.android.data.local.dao.WorldInfoEntryDao
import com.sillytavern.android.data.local.entity.CharacterEntity
import com.sillytavern.android.data.local.entity.ChatEntity
import com.sillytavern.android.data.local.entity.MessageEntity
import com.sillytavern.android.data.local.entity.WorldInfoEntity
import com.sillytavern.android.data.local.entity.WorldInfoEntryEntity

@Database(
    entities = [
        CharacterEntity::class,
        ChatEntity::class,
        MessageEntity::class,
        WorldInfoEntity::class,
        WorldInfoEntryEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class SillyTavernDatabase : RoomDatabase() {
    abstract fun characterDao(): CharacterDao
    abstract fun chatDao(): ChatDao
    abstract fun messageDao(): MessageDao
    abstract fun worldInfoDao(): WorldInfoDao
    abstract fun worldInfoEntryDao(): WorldInfoEntryDao
}
