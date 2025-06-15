package com.sujeong.pillo

import android.app.KeyguardManager
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.sujeong.pillo.alarm.receiver.MedicineAlarmReceiver
import com.sujeong.pillo.alarm.receiver.ScreenReceiver
import com.sujeong.pillo.navigation.AlarmRoute
import com.sujeong.pillo.navigation.HomeRoute
import com.sujeong.pillo.presentation.alarm.AlarmScreen
import com.sujeong.pillo.presentation.home.HomeScreen
import com.sujeong.pillo.ui.theme.PilloTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    private val screenReceiver by lazy {
        ScreenReceiver()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            PilloTheme {
                val navController = rememberNavController()

                LaunchedEffect(Unit) {
                    viewModel.uiEffect.collect { uiEffect ->
                        when(uiEffect) {
                            is MainUiEffect.NavigateToHome -> showWhenLocked(false)
                            is MainUiEffect.NavigateToAlarm -> {
                                registerScreenReceiver()
                                showWhenLocked(true)
                                navController.navigate(AlarmRoute(uiEffect.medicineId))
                            }
                        }
                    }
                }

                NavHost(
                    navController = navController,
                    startDestination = HomeRoute
                ) {
                    composable<HomeRoute> {
                        HomeScreen()
                    }

                    composable<AlarmRoute> {
                        AlarmScreen(
                            onGoBack = {
                                handleAlarmOnGoBack()
                                navController.navigateUp()
                            }
                        )
                    }
                }
            }
        }

        getMedicineIdFromIntent(intent)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)

        getMedicineIdFromIntent(intent)
    }

    override fun onPause() {
        super.onPause()

        if(viewModel.medicineId >= 0) {
            val intent = Intent(this, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or
                        Intent.FLAG_ACTIVITY_CLEAR_TOP or
                        Intent.FLAG_ACTIVITY_SINGLE_TOP
            }

            startActivity(intent)
        }
    }

    private fun getMedicineIdFromIntent(intent: Intent) {
        val medicineId = intent.getLongExtra(MedicineAlarmReceiver.KEY_MEDICINE_ID, -1)

        viewModel.onEvent(MainEvent.Initialize(medicineId))
    }

    private fun registerScreenReceiver() {
        registerReceiver(
            screenReceiver,
            IntentFilter().apply {
                addAction(Intent.ACTION_SCREEN_ON)
                addAction(Intent.ACTION_SCREEN_OFF)
            }
        )
    }

    private fun unregisterScreenReceiver() {
        unregisterReceiver(screenReceiver)
    }

    private fun showWhenLocked(
        showWhenLocked: Boolean = true
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(showWhenLocked)
            setTurnScreenOn(showWhenLocked)
        }

        if(showWhenLocked) {
            window.addFlags(
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON or
                        WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON or
                        WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or
                        WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
            )
        } else {
            window.clearFlags(
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON or
                        WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON or
                        WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or
                        WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
            )
        }
    }

    private fun handleAlarmOnGoBack() {
        unregisterScreenReceiver()
        showWhenLocked(false)
        showKeyGuard()

        viewModel.onEvent(MainEvent.ClearAlarm)
    }

    private fun showKeyGuard() {
        val keyguardManager = getSystemService(KEYGUARD_SERVICE) as KeyguardManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            keyguardManager.requestDismissKeyguard(this, object : KeyguardManager.KeyguardDismissCallback() {
                override fun onDismissError() { }

                override fun onDismissSucceeded() { }

                override fun onDismissCancelled() { }
            })
        }
    }
}