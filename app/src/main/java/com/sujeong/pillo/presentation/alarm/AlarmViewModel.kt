package com.sujeong.pillo.presentation.alarm

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.sujeong.pillo.alarm.manager.MedicineAlarmManager
import com.sujeong.pillo.common.base.BaseViewModel
import com.sujeong.pillo.common.extension.toString
import com.sujeong.pillo.domain.model.Medicine
import com.sujeong.pillo.domain.result.onSuccess
import com.sujeong.pillo.domain.usecase.GetMedicineByIdUseCase
import com.sujeong.pillo.domain.usecase.SkipMedicineUseCase
import com.sujeong.pillo.domain.usecase.TakeMedicineUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AlarmViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getMedicineByIdUseCase: GetMedicineByIdUseCase,
    private val takeMedicineUseCase: TakeMedicineUseCase,
    private val skipMedicineUseCase: SkipMedicineUseCase,
    private val medicineAlarmManager: MedicineAlarmManager
): BaseViewModel<AlarmUiEffect>() {
    private val _state = MutableStateFlow<AlarmState>(
        AlarmState()
    )
    val state = _state.asStateFlow()

    private var medicine: Medicine? = null

    init {
        viewModelScope.launch {
            val medicineId = savedStateHandle.get<Long>("medicineId") ?: 0

            getMedicineByIdUseCase(medicineId).onSuccess {
                it?.let { medicine ->
                    this@AlarmViewModel.medicine = medicine

                    _state.value = _state.value.copy(
                        alarmTime = medicine.alarmDateTime.toString("HH:mm"),
                        title = medicine.title
                    )
                }
            }
        }
    }

    fun onEvent(event: AlarmEvent) {
        when(event) {
            AlarmEvent.TakeMedicine -> takeMedicine()
            AlarmEvent.SkipMedicine -> skipMedicine()
        }
    }

    private fun takeMedicine() {
        viewModelScope.launch {
            medicine?.let { medicine ->
                takeMedicineUseCase(medicine).onSuccess {
                    onUiEffect(AlarmUiEffect.TakenMedicine)

                    medicineAlarmManager.clearAlarmNotification(medicine.id)
                }
            }
        }
    }

    private fun skipMedicine() {
        viewModelScope.launch {
            medicine?.let { medicine ->
                skipMedicineUseCase(medicine).onSuccess {
                    onUiEffect(AlarmUiEffect.SkippedMedicine)

                    medicineAlarmManager.clearAlarmNotification(medicine.id)
                }
            }
        }
    }
}