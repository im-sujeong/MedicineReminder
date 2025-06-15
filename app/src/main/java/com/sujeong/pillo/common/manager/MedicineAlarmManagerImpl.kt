package com.sujeong.pillo.common.manager

import android.app.AlarmManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import com.sujeong.pillo.receiver.MedicineAlarmReceiver

class MedicineAlarmManagerImpl(
    private val context: Context,
    private val soundMediaManger: SoundMediaManger
): MedicineAlarmManager {
    override fun setAlarm(medicineId: Long, alarmDateTime: Long) {
        Log.d("PilloTAG", "setAlarm: medicineId = $medicineId, alarmDateTime = $alarmDateTime")

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val alarmIntent = getAlarmIntent(medicineId)
        val alarmClockInfo = AlarmManager.AlarmClockInfo(alarmDateTime, alarmIntent)

        alarmManager.setAlarmClock(alarmClockInfo, alarmIntent)
    }

    override fun cancelAlarm(medicineId: Long) {
        Log.d("PilloTAG", "cancelAlarm: medicineId = $medicineId")

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        alarmManager.cancel(getAlarmIntent(medicineId))
    }

    override fun clearAlarmNotification(medicineId: Long) {
        Log.d("PilloTAG", "clearAlarmNotification: medicineId = $medicineId")

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE)
                as NotificationManager

        notificationManager.cancel(medicineId.toInt())
    }

    override fun playAlarmSound() {
        soundMediaManger.play()
    }

    override fun stopAlarmSound() {
        soundMediaManger.stop()
    }

    private fun getAlarmIntent(medicineId: Long) = PendingIntent.getBroadcast(
        context,
        medicineId.toInt(),
        getAlarmReceiverIntent(medicineId),
        PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
    )

    private fun getAlarmReceiverIntent(
        medicineId: Long
    ) = Intent(context, MedicineAlarmReceiver::class.java).apply {
        putExtra(MedicineAlarmReceiver.KEY_MEDICINE_ID, medicineId)
    }
}