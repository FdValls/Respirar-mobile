package com.example.projectFinal.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.navigation.fragment.findNavController
import com.example.projectFinal.R
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import kotlin.math.log

class Fragment_login : Fragment() {

    private lateinit var buttonLogin: Button
    private lateinit var buttonRegister: Button
    private lateinit var userName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val buttonLogin = view.findViewById<Button>(R.id.loginButton)
        val buttonRegister = view.findViewById<Button>(R.id.loginRegister)
        val userName = view.findViewById<EditText>(R.id.username)
        val fingerPrint = view.findViewById<TextView>(R.id.loginBiometricButton);
        val context = activity?.applicationContext

//        buttonLogin.setOnClickListener {
//
//            parentFragmentManager.commit {
//                replace<Fragment_admin>(R.id.fragmentContainerView)
//                setReorderingAllowed(true)
////                disallowAddToBackStack()
//            }
//
//            if (userName.text.toString() == "fer"){
//                Toast.makeText(
//                    requireContext(),
//                    "true",
//                    Toast.LENGTH_SHORT
//                ).show()
//            }else{
//                Toast.makeText(
//                    requireContext(),
//                    "false",
//                    Toast.LENGTH_SHORT
//                ).show()
//            }
//
//
//        }
//
//        buttonRegister.setOnClickListener {
//            Toast.makeText(
//                requireContext(),
//                "Probando boton REGISTER!",
//                Toast.LENGTH_SHORT
//            ).show()
//        }
//
//

        buttonLogin.setOnClickListener {

            parentFragmentManager.commit {
                replace<Fragment_edit_customer>(R.id.fragmentContainerView)
                setReorderingAllowed(true)
//                disallowAddToBackStack()
            }

            if (userName.text.toString() == "fer"){
                Toast.makeText(
                    requireContext(),
                    "true",
                    Toast.LENGTH_SHORT
                ).show()
            }else{
                Toast.makeText(
                    requireContext(),
                    "false",
                    Toast.LENGTH_SHORT
                ).show()
            }


        }

        buttonRegister.setOnClickListener {
            Toast.makeText(
                requireContext(),
                "Probando boton REGISTER!",
                Toast.LENGTH_SHORT
            ).show()
        }

        var executor = ContextCompat.getMainExecutor(context!!)
        var canAuthenticate = false

        fun moveToEditCustomerFragment() {
            parentFragmentManager.commit {
                replace<Fragment_edit_customer>(R.id.fragmentContainerView)
                setReorderingAllowed(true)
            }
            Toast.makeText(
                requireContext(),
                "Login succeeded",
                Toast.LENGTH_SHORT
            ).show()
        }

        var biometricPrompt = androidx.biometric.BiometricPrompt(this, ContextCompat.getMainExecutor(context!!), object: androidx.biometric.BiometricPrompt.AuthenticationCallback(){
            override fun onAuthenticationSucceeded(result: androidx.biometric.BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                moveToEditCustomerFragment()
            }
        })

        var promptInfo = androidx.biometric.BiometricPrompt.PromptInfo.Builder()
            .setTitle("Biometric Authentication")
            .setSubtitle("Login using your finger print")
            .setNegativeButtonText("Cancel")
            .build()

        fingerPrint.setOnClickListener {
            biometricPrompt.authenticate(promptInfo)
        }
    }

}