package com.sujeong.pillo.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.sujeong.pillo.common.extension.millis
import com.sujeong.pillo.common.manager.MedicineAlarmManager
import com.sujeong.pillo.domain.model.enums.AlarmStatus
import com.sujeong.pillo.domain.result.onSuccess
import com.sujeong.pillo.domain.usecase.GetMedicineByIdUseCase
import com.sujeong.pillo.receiver.MedicineAlarmReceiver
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@AndroidEntryPoint
class AlarmService : Service() {
    @Inject lateinit var medicineAlarmManager: MedicineAlarmManager
    @Inject lateinit var getMedicineByIdUseCase: GetMedicineByIdUseCase

    private var medicineId: Long = -1

    override fun onBind(intent: Intent): IBinder? = null

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        medicineId = intent.getLongExtra(MedicineAlarmReceiver.KEY_MEDICINE_ID, -1)

        Log.d("PilloTAG", "onStartCommand medicineId = $medicineId")

        return super.onStartCommand(intent, flags, startId)
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)

        Log.d("PilloTAG", "onTaskRemoved")

        CoroutineScope(Dispatchers.IO).launch {
            getMedicineByIdUseCase(medicineId).onSuccess {
                it?.let { medicine ->
                    Log.d("PilloTAG", "onTaskRemoved : medicine $medicine")

                    if(medicine.alarmStatus == AlarmStatus.SCHEDULED) {
                        //아직 예정된 알람 이라면, onTaskRemoved 시 알람을 재등록.
                        Log.d("PilloTAG", "onTaskRemoved : 알람 재등록 !")
                        medicineAlarmManager.stopAlarmSound()
                        medicineAlarmManager.setAlarm(medicine.id, LocalDateTime.now().millis())
                    }
                }
            }
        }
    }
}