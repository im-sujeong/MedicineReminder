package com.sujeong.pillo.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.sujeong.pillo.data.local.dao.MedicineDao
import com.sujeong.pillo.data.local.model.MedicineEntity

@Database(
    entities = [MedicineEntity::class],
    version = RoomConstant.ROOM_VERSION
)

@TypeConverters(
    DataConverter::class
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun medicineAlarmDao(): MedicineDao
}