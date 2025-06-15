package com.sujeong.pillo.data.di

import com.sujeong.pillo.data.repository.MedicineRepositoryImpl
import com.sujeong.pillo.domain.repository.MedicineRepository
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
        medicineRepositoryImpl: MedicineRepositoryImpl
    ): MedicineRepository
}