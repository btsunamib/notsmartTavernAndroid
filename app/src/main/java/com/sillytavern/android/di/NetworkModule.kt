package com.sillytavern.android.di

import com.sillytavern.android.data.remote.ApiService
import com.sillytavern.android.data.remote.OpenAIApiService
import com.sillytavern.android.data.remote.ClaudeApiService
import com.sillytavern.android.data.remote.KoboldApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    
    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .connectTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .writeTimeout(120, TimeUnit.SECONDS)
            .build()
    }
    
    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .client(client)
            .baseUrl("https://api.openai.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    
    @Provides
    @Singleton
    fun provideOpenAIApiService(retrofit: Retrofit): OpenAIApiService {
        return retrofit.create(OpenAIApiService::class.java)
    }
    
    @Provides
    @Singleton
    fun provideClaudeApiService(client: OkHttpClient): ClaudeApiService {
        return ClaudeApiService.create(client)
    }
    
    @Provides
    @Singleton
    fun provideKoboldApiService(client: OkHttpClient): KoboldApiService {
        return KoboldApiService.create(client)
    }
    
    @Provides
    @Singleton
    fun provideApiService(
        openAIApiService: OpenAIApiService,
        claudeApiService: ClaudeApiService,
        koboldApiService: KoboldApiService
    ): ApiService {
        return ApiService(openAIApiService, claudeApiService, koboldApiService)
    }
}
