package com.example.projectFinal.activities.ui.organization

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.projectFinal.R
import com.example.projectFinal.endPoints.RequestOrganizations.RequestCreateOrganization
import kotlinx.coroutines.launch

class CreateOrgFragment : Fragment() {

    private lateinit var nameOrgText: EditText
    private lateinit var descriptionOrgText: EditText
    private lateinit var btnCreate: Button
    lateinit var v: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_create_org, container, false)

        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        nameOrgText = view.findViewById(R.id.editTextNameOrg)
        descriptionOrgText = view.findViewById(R.id.editTextDescriptionOrg)
        btnCreate = view.findViewById(R.id.id_CreateBtnOrg)

        btnCreate.setOnClickListener {
            if (!nameOrgText.text.toString().isEmpty() && !descriptionOrgText.text.toString().isEmpty()) {

                lifecycleScope.launch {
                    RequestCreateOrganization.sendRequest(nameOrgText.text.toString(), descriptionOrgText.text.toString())
                    if(RequestCreateOrganization.retunCodeCreateOrg() == "201"){
                        Toast.makeText(
                            requireActivity(),
                            "Creada con exito",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }else{
                Toast.makeText(
                    requireActivity(),
                    "Ambos campos son obligatorios",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}