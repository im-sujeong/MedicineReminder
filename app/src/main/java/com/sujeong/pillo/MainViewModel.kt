package com.sujeong.pillo

import com.sujeong.pillo.common.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(): BaseViewModel<MainUiEffect>() {
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
        }

        this.medicineId = medicineId

        if(medicineId > 0) {
            onUiEffect(MainUiEffect.NavigateToAlarm(medicineId))
        } else {
            onUiEffect(MainUiEffect.NavigateToHome)
        }
    }

    private fun clearAlarm() {
        this.medicineId = -1
    }
}