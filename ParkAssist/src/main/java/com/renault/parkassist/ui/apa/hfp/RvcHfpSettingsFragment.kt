package com.renault.parkassist.ui.apa.hfp

import com.android.car.ui.toolbar.NavButtonMode
import com.renault.car.ui.components.renaultNavController
import com.renault.car.ui.components.renaultToolbar
import com.renault.parkassist.R
import com.renault.parkassist.ui.apa.ApaSettingsFragment

class RvcHfpSettingsFragment : ApaSettingsFragment() {

    private val mBackListener = {
        renaultNavController.navigate(R.id.action_to_sonarHfpScanningFragment)
        true
    }

    override fun onBind() {
        super.onBind()

        with(renaultToolbar) {
            setTitle(R.string.rlb_parkassist_easy_park_assist)
            setNavButtonMode(NavButtonMode.BACK)
            registerBackListener(mBackListener)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        renaultToolbar.unregisterBackListener(mBackListener)
    }
}