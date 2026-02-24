package com.sillytavern.android.di

import android.content.Context
import androidx.room.Room
import com.sillytavern.android.data.local.SillyTavernDatabase
import com.sillytavern.android.data.local.dao.CharacterDao
import com.sillytavern.android.data.local.dao.ChatDao
import com.sillytavern.android.data.local.dao.MessageDao
import com.sillytavern.android.data.local.dao.WorldInfoDao
import com.sillytavern.android.data.local.dao.WorldInfoEntryDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    
    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): SillyTavernDatabase {
        return Room.databaseBuilder(
            context,
            SillyTavernDatabase::class.java,
            "silly_tavern_db"
        )
            .fallbackToDestructiveMigration()
            .build()
    }
    
    @Provides
    fun provideCharacterDao(database: SillyTavernDatabase): CharacterDao {
        return database.characterDao()
    }
    
    @Provides
    fun provideChatDao(database: SillyTavernDatabase): ChatDao {
        return database.chatDao()
    }
    
    @Provides
    fun provideMessageDao(database: SillyTavernDatabase): MessageDao {
        return database.messageDao()
    }
    
    @Provides
    fun provideWorldInfoDao(database: SillyTavernDatabase): WorldInfoDao {
        return database.worldInfoDao()
    }
    
    @Provides
    fun provideWorldInfoEntryDao(database: SillyTavernDatabase): WorldInfoEntryDao {
        return database.worldInfoEntryDao()
    }
}
