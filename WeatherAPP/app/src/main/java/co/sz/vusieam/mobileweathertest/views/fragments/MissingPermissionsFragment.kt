package co.sz.vusieam.mobileweathertest.views.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import co.sz.vusieam.mobileweathertest.R
import co.sz.vusieam.mobileweathertest.utils.AppBasicConstants



class MissingPermissionsFragment : Fragment() {

    private lateinit var tv_messaging: TextView
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_missing_permissions, container, false)

        tv_messaging = view.findViewById(R.id.tv_messaging)
        tv_messaging.visibility = View.GONE
        val btnGrantPermissions:View = view.findViewById(R.id.btnGrantPermissions)
        btnGrantPermissions.setOnClickListener{
            requireActivity().requestPermissions(AppBasicConstants.ARRAY_PERMISSIONS, AppBasicConstants.PERMISSION_REQUEST_CODE)
        }
        return view
    }


    //region Permissions functions for

    /**
     * Check permission to access fine location.
     */
    private fun canAccessLocation(): Boolean {
        return hasPermission(Manifest.permission.ACCESS_FINE_LOCATION)
    }

    /**
     * Check permission to access coarse location.
     */
    private fun canAccessCoreLocation(): Boolean {
        return hasPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
    }

    private fun hasPermission(perm: String): Boolean {
        return PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(requireActivity(), perm)
    }

    //endregion


    //region Override functions

    @SuppressLint("MissingPermission")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.requireActivity().onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            AppBasicConstants.PERMISSION_REQUEST_CODE ->{
                if(canAccessLocation() && canAccessCoreLocation()){
                    tv_messaging.visibility = View.VISIBLE
                    tv_messaging.text = getString(R.string.permission_granted_yes)
                    tv_messaging.setTextColor(
                        ContextCompat.getColor(
                            requireActivity(),
                            R.color.green_900
                        )
                    )
                }
                else{
                    tv_messaging.visibility = View.VISIBLE
                    tv_messaging.text = getString(R.string.permission_granted_no)
                    tv_messaging.setTextColor(
                        ContextCompat.getColor(
                            requireActivity(),
                            R.color.red_900
                        )
                    )
                }
            }
        }
    }
    //endregion

}