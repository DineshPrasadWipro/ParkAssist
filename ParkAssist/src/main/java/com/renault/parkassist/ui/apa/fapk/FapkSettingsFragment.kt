package com.renault.parkassist.ui.apa.fapk

import com.android.car.ui.toolbar.NavButtonMode
import com.renault.car.ui.components.renaultNavController
import com.renault.car.ui.components.renaultToolbar
import com.renault.parkassist.R
import com.renault.parkassist.ui.apa.ApaSettingsFragment
import com.renault.parkassist.viewmodel.apa.fapk.FapkSettingsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class FapkSettingsFragment : ApaSettingsFragment() {

    private val fapkSettingsViewModel: FapkSettingsViewModel by viewModel()

    private val mBackListener = {
        renaultNavController.navigate(R.id.action_to_fapkFragment)
        true
    }

    override fun onBind() {
        super.onBind()

        with(renaultToolbar) {
            title = resources.getString(R.string.rlb_parkassist_auto_park_assist)
            setNavButtonMode(NavButtonMode.BACK)
            registerBackListener(mBackListener)
        }

        fapkSettingsViewModel.shouldExitSettings.observe {
            if (it) renaultNavController.navigate(R.id.action_to_fapkFragment)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        renaultToolbar.unregisterBackListener(mBackListener)
    }
}