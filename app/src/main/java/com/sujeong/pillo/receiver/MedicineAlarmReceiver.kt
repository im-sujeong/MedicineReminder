package com.sujeong.pillo.receiver

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.sujeong.pillo.AppConstant.PREF_KEY_CURRENT_ALARM_MEDICINE_ID
import com.sujeong.pillo.presentation.main.MainActivity
import com.sujeong.pillo.R
import com.sujeong.pillo.common.manager.PreferencesManager
import com.sujeong.pillo.domain.model.Medicine
import com.sujeong.pillo.domain.result.onSuccess
import com.sujeong.pillo.domain.usecase.GetMedicineByIdUseCase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MedicineAlarmReceiver: BroadcastReceiver() {
    companion object {
        private const val NOTIFICATION_CHANNEL = "alarm_channel"
        const val KEY_MEDICINE_ID = "KEY_MEDICINE_ID"
    }

    @Inject lateinit var getMedicineByIdUseCase: GetMedicineByIdUseCase
    @Inject lateinit var preferencesManager: PreferencesManager

    override fun onReceive(context: Context, intent: Intent?) {
        val medicineId = intent?.getLongExtra(KEY_MEDICINE_ID, -1) ?: return

        Log.d("PilloTAG", "onReceive medicineId = $medicineId")

        CoroutineScope(Dispatchers.IO).launch {
            getMedicineByIdUseCase(medicineId)
                .onSuccess {
                    it?.let { medicine ->
                        preferencesManager.write(
                            PREF_KEY_CURRENT_ALARM_MEDICINE_ID,
                            medicineId
                        )

                        notification(context, medicine)
                        startActivity(context, medicine)
                    }
                }
        }
    }

    private fun notification(
        context: Context,
        medicine: Medicine
    ) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                NOTIFICATION_CHANNEL,
                "복약 알람을 알림을 위한 채널",
                NotificationManager.IMPORTANCE_HIGH
            )

            notificationManager.createNotificationChannel(channel)
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            medicine.id.toInt(),
            createIntent(context, medicine.id),
            PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL)
            .setSmallIcon(R.drawable.ic_app_logo)
            .setContentTitle(medicine.title)
            .setContentText(context.getString(R.string.notification_message_medicine_alarm))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        notificationManager.notify(medicine.id.toInt(), notification)
    }

    private fun startActivity(
        context: Context,
        medicine: Medicine
    ) {
        val intent = createIntent(context, medicine.id)

        context.startActivity(intent)
    }

    private fun createIntent(context: Context, medicineId: Long): Intent {
        return Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or
                    Intent.FLAG_ACTIVITY_CLEAR_TOP or
                    Intent.FLAG_ACTIVITY_SINGLE_TOP

            putExtra(
                KEY_MEDICINE_ID,
                medicineId
            )
        }
    }
}