package com.sujeong.pillo.common.di

import android.content.Context
import com.sujeong.pillo.alarm.manager.MedicineAlarmManager
import com.sujeong.pillo.alarm.manager.MedicineAlarmManagerImpl
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
        @ApplicationContext context: Context
    ): MedicineAlarmManager {
        return MedicineAlarmManagerImpl(context)
    }
}