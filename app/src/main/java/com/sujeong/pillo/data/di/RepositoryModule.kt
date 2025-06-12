package com.sujeong.pillo.data.di

import com.sujeong.pillo.data.repository.MedicineAlarmRepositoryImpl
import com.sujeong.pillo.domain.repository.MedicineAlarmRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindMedicineAlarmRepository(
        medicineAlarmRepositoryImpl: MedicineAlarmRepositoryImpl
    ): MedicineAlarmRepository
}