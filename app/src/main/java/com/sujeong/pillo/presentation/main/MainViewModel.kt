package com.sujeong.pillo.presentation.main

import android.util.Log
import com.sujeong.pillo.AppConstant
import com.sujeong.pillo.common.base.BaseViewModel
import com.sujeong.pillo.common.manager.PreferencesManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val preferencesManager: PreferencesManager
): BaseViewModel<MainUiEffect>() {
    var medicineId: Long = -1
        private set

    fun onEvent(event: MainEvent) {
        when(event) {
            is MainEvent.Initialize -> initialize(event.medicineId)
            MainEvent.ClearAlarm -> clearAlarm()
        }
    }

    private fun initialize(medicineId: Long) {
        if(this.medicineId > -1 && medicineId == -1L) {
            return
        } else if(this.medicineId > -1 && this.medicineId == medicineId) {
            return
        }

        Log.d("PilloTAG", "medicineId: $medicineId")

        this.medicineId = if(medicineId == -1L) {
            val currentAlarmMedicineId = preferencesManager.read(
                AppConstant.PREF_KEY_CURRENT_ALARM_MEDICINE_ID,
                -1
            )

            if(currentAlarmMedicineId > 0) {
                currentAlarmMedicineId
            } else medicineId
        } else {
            medicineId
        }

        Log.d("PilloTAG", "this.medicineId: ${this.medicineId}")

        if(this.medicineId > 0) {
            preferencesManager.clear(AppConstant.PREF_KEY_CURRENT_ALARM_MEDICINE_ID)

            onUiEffect(MainUiEffect.NavigateToAlarm(this.medicineId))
        } else {
            onUiEffect(MainUiEffect.NavigateToHome)
        }
    }

    private fun clearAlarm() {
        this.medicineId = -1
    }
}