package com.example.credentialmanagment.ui.view.ui

import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.example.credentialmanagment.ui.view.ui.theme.CredentialManagmentTheme
import com.example.credentialmanagment.ui.viewmodel.PasswordViewModel
import java.util.concurrent.Executor

class MainActivity : FragmentActivity() {

    private val viewModel: PasswordViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CredentialManagmentTheme {
                BiometricAuthScreen()
            }
        }
    }

    @Composable
    fun BiometricAuthScreen() {
        val context = LocalContext.current
        val executor: Executor = ContextCompat.getMainExecutor(context)
        var isAuthenticated by remember { mutableStateOf(false) }
        val biometricManager = BiometricManager.from(context)
        var showSetupPrompt by remember { mutableStateOf(false) }

        val biometricPrompt = BiometricPrompt(
            context as MainActivity,
            executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    //Error
                }

                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    isAuthenticated = true
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    Toast.makeText(this@MainActivity, "Authentication failed, Try Again", Toast.LENGTH_LONG).show()
                }
            }
        )

        val biometricPromptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Biometric login for Password Manager")
            .setSubtitle("Log in using your biometric credential")
            .setNegativeButtonText("Cancel")
            .build()

        LaunchedEffect(Unit) {
            biometricPrompt.authenticate(biometricPromptInfo)
        }

        when (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG)) {
            BiometricManager.BIOMETRIC_SUCCESS -> {

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    if (isAuthenticated) {
                        PasswordManager(viewModel = viewModel, context)
                    } else {
                        Button(
                            onClick = {
                                biometricPrompt.authenticate(biometricPromptInfo)
                            },
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        ) {
                            Text("Authenticate your self...")
                        }
                    }
                }

                LaunchedEffect(Unit) {
                    biometricPrompt.authenticate(biometricPromptInfo)
                }
            }

            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
                showSetupPrompt = false
                PasswordManager(viewModel = viewModel, context)
                Toast.makeText(
                    this,
                    "No biometric hardware found",
                    Toast.LENGTH_LONG
                ).show()
            }

            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
                showSetupPrompt = false
                PasswordManager(viewModel = viewModel, context)
            }

            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                showSetupPrompt = false
                Toast.makeText(
                    this,
                    "Add biometric figure to device to make more sure for this app",
                    Toast.LENGTH_LONG
                ).show()
                PasswordManager(viewModel = viewModel, context)
            }

            else -> {
                Toast.makeText(
                    this,
                    "Try Again...",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
}
