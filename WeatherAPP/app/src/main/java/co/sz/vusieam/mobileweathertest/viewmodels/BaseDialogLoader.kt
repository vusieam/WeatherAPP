package co.sz.vusieam.mobileweathertest.viewmodels

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.LinearLayout
import co.sz.vusieam.mobileweathertest.R

class BaseDialogLoader(context: Context) : Dialog(context) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.loader_layout)
        window?.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)
        window?.setBackgroundDrawableResource(R.color.transparent)
    }
}