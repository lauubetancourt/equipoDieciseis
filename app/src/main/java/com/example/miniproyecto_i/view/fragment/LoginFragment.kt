package com.example.miniproyecto_i.view.fragment

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.biometric.BiometricPrompt.PromptInfo
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.airbnb.lottie.LottieAnimationView
import com.example.miniproyecto_i.R
import java.util.concurrent.Executor

class LoginFragment : Fragment() {

    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: PromptInfo
    private lateinit var fingerprintLottie: LottieAnimationView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_login, container, false)

        executor = ContextCompat.getMainExecutor(requireContext())
        fingerprintLottie = view.findViewById(R.id.fingerprint_lottie)

        setupBiometricPrompt()
        fingerprintLottie.setOnClickListener {
            checkBiometricSupportAndAuthenticate()
        }

        return view
    }

    private fun setupBiometricPrompt() {
        biometricPrompt = BiometricPrompt(this, executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    showToast(getString(R.string.biometric_auth_succeeded))
                    findNavController().navigate(R.id.action_loginFragment_to_homeAppointments)
                }

                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    showToast(getErrorMessage(errorCode, errString))
                    if (errorCode == BiometricPrompt.ERROR_NO_BIOMETRICS) promptUserToEnroll()
                }

                override fun onAuthenticationFailed() {
                    showToast(getString(R.string.biometric_auth_failed))
                }
            })

        promptInfo = PromptInfo.Builder()
            .setTitle(getString(R.string.biometric_auth_title))
            .setSubtitle(getString(R.string.biometric_auth_subtitle))
            .setNegativeButtonText(getString(R.string.biometric_auth_negative_button))
            .setAllowedAuthenticators(BIOMETRIC_STRONG)
            .build()
    }

    private fun checkBiometricSupportAndAuthenticate() {
        when (BiometricManager.from(requireContext()).canAuthenticate(BIOMETRIC_STRONG)) {
            BiometricManager.BIOMETRIC_SUCCESS -> biometricPrompt.authenticate(promptInfo)
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                showToast(getString(R.string.biometric_auth_error_none_enrolled))
                promptUserToEnroll()
            }
            else -> showToast(getString(R.string.biometric_auth_error_unsupported))
        }
    }

    private fun promptUserToEnroll() {
        val intent = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
            Intent(Settings.ACTION_BIOMETRIC_ENROLL).apply {
                putExtra(Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED, BIOMETRIC_STRONG)
            }
        } else {
            Intent(Settings.ACTION_SECURITY_SETTINGS)
        }

        if (intent.resolveActivity(requireActivity().packageManager) != null) {
            startActivity(intent)
        } else {
            startActivity(Intent(Settings.ACTION_SETTINGS))
        }
    }

    private fun getErrorMessage(code: Int, msg: CharSequence): String {
        return when (code) {
            BiometricPrompt.ERROR_NO_BIOMETRICS -> getString(R.string.biometric_auth_error_none_enrolled)
            BiometricPrompt.ERROR_HW_UNAVAILABLE -> getString(R.string.biometric_auth_error_hw_unavailable)
            else -> "${getString(R.string.biometric_auth_error_unknown)}: $msg"
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}