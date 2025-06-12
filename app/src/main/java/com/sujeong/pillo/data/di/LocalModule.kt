package com.sujeong.pillo.data.di

import android.content.Context
import androidx.room.Room
import com.sujeong.pillo.data.local.AppDatabase
import com.sujeong.pillo.data.local.RoomConstant
import com.sujeong.pillo.data.local.dao.MedicineAlarmDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class LocalModule {
    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ): AppDatabase = Room.databaseBuilder(
        context,
        AppDatabase::class.java,
        RoomConstant.ROOM_DB_NAME
    ).fallbackToDestructiveMigration().build()

    @Provides
    @Singleton
    fun provideMedicineAlarmDao(
        database: AppDatabase
    ): MedicineAlarmDao = database.medicineAlarmDao()
}