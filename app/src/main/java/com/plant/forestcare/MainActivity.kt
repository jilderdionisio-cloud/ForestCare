
package com.plant.forestcare

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.plant.forestcare.navigation.NavGraph
import com.plant.forestcare.navigation.Screen
import com.plant.forestcare.ui.theme.ForestCareTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val preferences = remember {
                getSharedPreferences(ONBOARDING_PREFS, Context.MODE_PRIVATE)
            }
            var onboardingCompleted by remember {
                mutableStateOf(preferences.getBoolean(KEY_ONBOARDING_COMPLETED, false))
            }
            var authCompleted by remember {
                mutableStateOf(preferences.getBoolean(KEY_AUTH_COMPLETED, false))
            }
            ForestCareTheme {
                NavGraph(
                    navController = rememberNavController(),
                    startDestination = if (onboardingCompleted) {
                        if (authCompleted) Screen.Dashboard.route else Screen.Login.route
                    } else {
                        Screen.Splash.route
                    },
                    onOnboardingFinished = {
                        preferences.edit().putBoolean(KEY_ONBOARDING_COMPLETED, true).apply()
                        onboardingCompleted = true
                    },
                    onAuthFinished = {
                        preferences.edit().putBoolean(KEY_AUTH_COMPLETED, true).apply()
                        authCompleted = true
                    },
                    onLogout = {
                        preferences.edit().putBoolean(KEY_AUTH_COMPLETED, false).apply()
                        authCompleted = false
                    }
                )
            }
        }
    }

    private companion object {
        private const val ONBOARDING_PREFS = "plantcare_onboarding"
        private const val KEY_ONBOARDING_COMPLETED = "completed"
        private const val KEY_AUTH_COMPLETED = "auth_completed"
    }
}

@Preview(showBackground = true)
@Composable
fun ForestCarePreview() {
    ForestCareTheme {
        NavGraph(navController = rememberNavController())
    }
}
