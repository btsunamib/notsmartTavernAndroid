package com.sillytavern.android.di

import android.content.Context
import androidx.room.Room
import com.sillytavern.android.data.local.SillyTavernDatabase
import com.sillytavern.android.data.local.dao.CharacterDao
import com.sillytavern.android.data.local.dao.ChatDao
import com.sillytavern.android.data.local.dao.MessageDao
import com.sillytavern.android.data.remote.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    
    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .build()
    }
    
    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.openai.com/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    
    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit, okHttpClient: OkHttpClient): ApiService {
        return ApiService(retrofit, okHttpClient)
    }
    
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): SillyTavernDatabase {
        return Room.databaseBuilder(
            context,
            SillyTavernDatabase::class.java,
            "silly_tavern_db"
        ).fallbackToDestructiveMigration().build()
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
}
