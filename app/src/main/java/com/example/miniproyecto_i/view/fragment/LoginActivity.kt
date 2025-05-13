package com.example.miniproyecto_i.view.fragment // Asegúrate que este es el paquete correcto

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.widget.TextView // Importar TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import com.airbnb.lottie.LottieAnimationView // Importar LottieAnimationView
import com.example.miniproyecto_i.R // Se usa para acceder a tus recursos (layout, strings, etc.)
import com.example.miniproyecto_i.view.MainActivity // Asegúrate que la ruta a tu MainActivity es correcta
import java.util.concurrent.Executor

// Este archivo LoginActivity.kt gestiona la autenticación biométrica inicial.
// Una vez autenticado, inicia la Activity principal (MainActivity) que gestiona
// la navegación mediante Navigation Components.

class LoginActivity : AppCompatActivity() {

    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // --- Criterio 1: Sin Toolbar ---
        // Asegúrate que el tema aplicado a esta Activity en AndroidManifest.xml
        // sea uno sin ActionBar (ej: Theme.AppCompat.Light.NoActionBar
        // o Theme.MaterialComponents.DayNight.NoActionBar).
        // Tu AndroidManifest.xml actual usa @style/Theme.MiniProyectoI.
        // Debes verificar en res/values/themes.xml que este tema NO incluya una ActionBar.
        setContentView(R.layout.activity_login) // Usa tu layout activity_login.xml

        executor = ContextCompat.getMainExecutor(this) // Executor para callbacks de BiometricPrompt

        setupBiometricPrompt()

        // --- Criterio 5: Listener en la imagen de huella ---
        // Usamos findViewById para obtener la referencia a la vista (desde activity_login.xml)
        // El ID 'fingerprint_lottie' DEBE existir en tu activity_login.xml
        val fingerprintLottie = findViewById<LottieAnimationView>(R.id.fingerprint_lottie)
        fingerprintLottie.setOnClickListener {
            checkBiometricSupportAndAuthenticate()
        }

        // Opcional: también en el texto (desde activity_login.xml)
        // El ID 'tap_to_auth_text' DEBE existir en tu activity_login.xml
        val tapToAuthText = findViewById<TextView>(R.id.tap_to_auth_text)
        tapToAuthText.setOnClickListener {
            checkBiometricSupportAndAuthenticate()
        }
    }

    private fun setupBiometricPrompt() {
        biometricPrompt = BiometricPrompt(this, executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    // --- Criterio 6 (Fallo/Error) ---
                    // Muestra mensaje de error usando strings de res/values/strings.xml
                    val errorMessage = when (errorCode) {
                        // Asegúrate que TODOS estos strings estén definidos en tu strings.xml
                        BiometricPrompt.ERROR_NO_BIOMETRICS -> getString(R.string.biometric_auth_error_none_enrolled)
                        BiometricPrompt.ERROR_HW_UNAVAILABLE -> getString(R.string.biometric_auth_error_hw_unavailable)
                        BiometricPrompt.ERROR_LOCKOUT -> getString(R.string.biometric_auth_error_lockout)
                        BiometricPrompt.ERROR_LOCKOUT_PERMANENT -> getString(R.string.biometric_auth_error_lockout_permanent)
                        BiometricPrompt.ERROR_USER_CANCELED -> getString(R.string.biometric_auth_error_user_canceled)
                        BiometricPrompt.ERROR_CANCELED -> getString(R.string.biometric_auth_error_user_canceled)
                        BiometricPrompt.ERROR_NEGATIVE_BUTTON -> getString(R.string.biometric_auth_error_negative_button)
                        BiometricPrompt.ERROR_TIMEOUT -> getString(R.string.biometric_auth_error_timeout)
                        BiometricPrompt.ERROR_SECURITY_UPDATE_REQUIRED -> getString(R.string.biometric_auth_error_security_update)
                        BiometricPrompt.ERROR_VENDOR -> "${getString(R.string.biometric_auth_error_vendor)}: $errString"
                        else -> "${getString(R.string.biometric_auth_error_unknown)}: $errString"
                    }
                    showToast(errorMessage)

                    // Si el error es porque no hay huellas registradas, podríamos ofrecer
                    // llevar al usuario a los ajustes.
                    if (errorCode == BiometricPrompt.ERROR_NO_BIOMETRICS) {
                        promptUserToEnroll()
                    }
                }

                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    // --- Criterio 6 (Éxito) ---
                    showToast(getString(R.string.biometric_auth_succeeded))
                    navigateToHome() // Navega a la Activity principal
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    // --- Criterio 6 (Fallo) ---
                    // Huella no reconocida (intento fallido, pero no error grave)
                    showToast(getString(R.string.biometric_auth_failed))
                }
            })

        // --- Configuración del diálogo emergente (Criterio 5) ---
        promptInfo = BiometricPrompt.PromptInfo.Builder()
            // Asegúrate que estos strings estén definidos en tu strings.xml
            .setTitle(getString(R.string.biometric_auth_title))
            .setSubtitle(getString(R.string.biometric_auth_subtitle))
            .setNegativeButtonText(getString(R.string.biometric_auth_negative_button))
            // .setAllowedAuthenticators(BIOMETRIC_STRONG or DEVICE_CREDENTIAL) // Permite huella o PIN/Patrón (si lo necesitas)
            .setAllowedAuthenticators(BIOMETRIC_STRONG) // Solo permite huella fuerte (recomendado)
            .build()
    }

    private fun checkBiometricSupportAndAuthenticate() {
        val biometricManager = BiometricManager.from(this)
        when (biometricManager.canAuthenticate(BIOMETRIC_STRONG)) {
            BiometricManager.BIOMETRIC_SUCCESS -> {
                // El hardware está disponible y hay huellas registradas. Proceder.
                biometricPrompt.authenticate(promptInfo) // Inicia el flujo de autenticación
            }
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
                showToast(getString(R.string.biometric_auth_error_hw_unavailable)) // Asegúrate que este string esté definido
                // Aquí podrías ofrecer un método de login alternativo (ej. usuario/contraseña)
            }
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
                showToast(getString(R.string.biometric_auth_error_hw_unavailable)) // Asegúrate que este string esté definido
                // Aquí podrías ofrecer un método de login alternativo
            }
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                // No hay huellas registradas.
                showToast(getString(R.string.biometric_auth_error_none_enrolled)) // Asegúrate que este string esté definido
                promptUserToEnroll() // Sugiere al usuario que registre una huella
            }
            BiometricManager.BIOMETRIC_ERROR_SECURITY_UPDATE_REQUIRED -> {
                showToast(getString(R.string.biometric_auth_error_security_update)) // Asegúrate que este string esté definido
            }
            BiometricManager.BIOMETRIC_ERROR_UNSUPPORTED -> {
                showToast(getString(R.string.biometric_auth_error_unsupported)) // Asegúrate que este string esté definido
            }
            BiometricManager.BIOMETRIC_STATUS_UNKNOWN -> {
                showToast(getString(R.string.biometric_auth_error_unknown)) // Asegúrate que este string esté definido
            }
        }
    }

    private fun navigateToHome() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    private fun promptUserToEnroll() {
        // Intenta dirigir al usuario a la configuración de seguridad para registrar huellas
        val enrollIntent = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
            Intent(Settings.ACTION_BIOMETRIC_ENROLL).apply {
                putExtra(Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED,
                    BIOMETRIC_STRONG) // O BIOMETRIC_WEAK / DEVICE_CREDENTIAL
            }
        } else if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
            // Para Android 9 y 10
            Intent(Settings.ACTION_SECURITY_SETTINGS)
        } else {
            // Versiones anteriores
            Intent(Settings.ACTION_SETTINGS)
        }

        if (enrollIntent.resolveActivity(packageManager) != null) {
            startActivity(enrollIntent)
        } else {
            // Si no se puede manejar el intent específico, ir a ajustes generales de seguridad
            showToast(getString(R.string.biometric_auth_error_cannot_enroll_directly)) // Asegúrate que este string esté definido
            startActivity(Intent(Settings.ACTION_SETTINGS))
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    // No necesitamos un REQUEST_CODE si no usamos startActivityForResult para un resultado específico.
    /*
    companion object {
        private const val REQUEST_CODE_BIOMETRIC_ENROLL = 1001 // Código para startActivityForResult
    }
    */
}