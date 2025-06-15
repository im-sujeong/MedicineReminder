package com.sujeong.pillo.common.di

import android.content.Context
import android.content.SharedPreferences
import com.sujeong.pillo.AppConstant.PREF_NAME
import com.sujeong.pillo.common.manager.MedicineAlarmManager
import com.sujeong.pillo.common.manager.MedicineAlarmManagerImpl
import com.sujeong.pillo.common.manager.PreferencesManager
import com.sujeong.pillo.common.manager.PreferencesManagerImpl
import com.sujeong.pillo.common.manager.SoundMediaManger
import com.sujeong.pillo.common.manager.SoundMediaMangerImpl
import com.sujeong.pillo.common.provider.ResourceProvider
import com.sujeong.pillo.common.provider.ResourceProviderImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class CommonModule {
    @Singleton
    @Provides
    fun provideResourceProvider(
        @ApplicationContext context: Context
    ): ResourceProvider {
        return ResourceProviderImpl(context)
    }

    @Singleton
    @Provides
    fun provideMedicineAlarmManager(
        @ApplicationContext context: Context,
        soundMediaManger: SoundMediaManger
    ): MedicineAlarmManager {
        return MedicineAlarmManagerImpl(context, soundMediaManger)
    }

    @Singleton
    @Provides
    fun provideSharedPref(
        @ApplicationContext context: Context
    ): SharedPreferences {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    @Singleton
    @Provides
    fun providePreferencesManager(
        preferences: SharedPreferences
    ): PreferencesManager {
        return PreferencesManagerImpl(preferences)
    }

    @Singleton
    @Provides
    fun provideSoundMediaManger(
        @ApplicationContext context: Context
    ): SoundMediaManger {
        return SoundMediaMangerImpl(context)
    }
}