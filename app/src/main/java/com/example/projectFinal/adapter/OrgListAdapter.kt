package com.example.projectFinal.adapter

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.ProgressBar
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.projectFinal.R
import com.example.projectFinal.activities.ui.organization.OrganizationFragmentDirections
import com.example.projectFinal.data.GlobalVariables
import com.example.projectFinal.endPoints.Request.RequestListUsersWithinAnOrganization
import com.example.projectFinal.endPoints.RequestOrganizations.RequestListAllOrganization
import com.example.projectFinal.endPoints.RequestUsers.RequestListAllUser
import com.example.projectFinal.holders.OrgHolder
import com.example.projectFinal.utils.Organization
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import values.objStrings.denied
import values.objStrings.owner

class OrgListAdapter (
    private var orgList: MutableList<Organization>,
) : RecyclerView.Adapter<OrgHolder>() {

    private lateinit var view: View
    private lateinit var btnGestionar: Button
    private lateinit var btnVer: Button
    private lateinit var roleOrg: TextView
    private var isCardCheck: Boolean = false
    private var onlyButton: Boolean = true
    private lateinit var checkCard: CheckBox
    private lateinit var userDataJson: JSONObject
    private lateinit var myMap: MutableMap<String, String>
    private var keys: List<String> = listOf()

    override fun getItemCount(): Int {
        return orgList.size
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrgHolder {
        view = LayoutInflater.from(parent.context).inflate(R.layout.item_org, parent, false)

        btnGestionar = view.findViewById(R.id.id_btnGestionarOrg)
        btnVer = view.findViewById(R.id.id_btnVerOrg)
        roleOrg = view.findViewById(R.id.idRoleOrg)
        checkCard = view.findViewById(R.id.id_selectOrganization)

        return (OrgHolder(view))
    }

    override fun onBindViewHolder(holder: OrgHolder, position: Int) {
        CoroutineScope(Dispatchers.Main).launch {

            GlobalVariables.getInstance().listOrgToModify.clear() // limpia la lista con cada refresh

            // ID + ROL
            myMap = RequestListAllOrganization.returnListOnlyRoleIdOrg()

            // SOLO IDS
            keys = myMap.keys.toList()

            // OBJETO ORGANIZATION
            orgList[position].name.let { holder.setName(it) }
            orgList[position].description.let { holder.setDescription(it) }


            if(keys.contains(orgList[position].id)){
                val myRole = myMap[orgList[position].id]
                if (myRole != null) {
                    holder.setRole(myRole)
                }
            }

            if (orgList[position].image != "default") {
                holder.setGravatar(orgList[position].image)
            } else {
                orgList[position].image.let { holder.setGravatar(it) }
            }

            holder.getCardLayout().setOnClickListener {
                onlyButton = false

                val checkBox = holder.getCheckBox()
                checkBox.isChecked = !checkBox.isChecked

                if (checkBox.isChecked) {
                    isCardCheck = true
                    GlobalVariables.getInstance().listOrgToModify.add(orgList[position].id)
                    holder.getCheckBox().isEnabled = true
                    holder.getCheckBox().setTextColor(Color.BLACK)
                }
                else {
                    GlobalVariables.getInstance().listOrgToModify.remove(orgList[position].id)
                    holder.getCheckBox().setTextColor(Color.GRAY)
                }
            }

            holder.getCardButtonGestionarLayout().setOnClickListener {
                userDataJson = JSONObject(GlobalVariables.getInstance().userData)
                val userObject = userDataJson.getJSONObject("User")
                val role = userObject.getBoolean("admin")

                CoroutineScope(Dispatchers.Main).launch {
                    onlyButton = true
                    if (RequestListAllUser.sendRequest() && role && (onlyButton && myMap[orgList[position].id].toString() == owner)) {
                        val action2 =
                            OrganizationFragmentDirections.actionNavOrganizationToSwitchOwnerMemberFragment(
                                orgList[position].id
                            )
                        view.findNavController().navigate(action2)
                    } else {
                        Snackbar.make(
                            view, denied,
                            Snackbar.LENGTH_SHORT
                        ).show();
                    }
                    notifyItemChanged(position)
                    notifyDataSetChanged()
                }
            }

            holder.getCardButtonVerLayout().setOnClickListener {

                userDataJson = JSONObject(GlobalVariables.getInstance().userData)
                val userObject = userDataJson.getJSONObject("User")
                val role = userObject.getBoolean("admin")

                CoroutineScope(Dispatchers.Main).launch {
                    onlyButton = true
//                    if (onlyButton && RequestListAllUser.sendRequest()) {
                    RequestListUsersWithinAnOrganization.sendRequest(orgList[position].id)
                    val list = RequestListUsersWithinAnOrganization.returnListJsonObject()
                    if (onlyButton && list.size != 0 && role) {
                        if (myMap[orgList[position].id].toString() == owner) {
                            val action3 =
                                OrganizationFragmentDirections.actionNavOrganizationToOrganizationListUsersFragment2(
                                    orgList[position].id
                                )
                            view.findNavController().navigate(action3)
                        } else {
                            Snackbar.make(
                                view,
                                denied,
                                Snackbar.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        Snackbar.make(
                            view,
                            denied,
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }
}
