package com.renault.parkassist.ui.settings

import android.content.res.Configuration
import android.os.Bundle
import com.android.car.ui.toolbar.NavButtonMode
import com.renault.car.ui.components.RenaultBaseActivity
import com.renault.parkassist.R

class SettingsActivity : RenaultBaseActivity() {
    override val navigationGraphId: Int = R.navigation.settings_navigation

    override fun onCreate(savedInstanceState: Bundle?) {
        // Disable toolbar in landscape
        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setTheme(R.style.Theme_CarUi_NoToolbar)
        }
        super.onCreate(savedInstanceState)
        if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            renaultToolbar.setNavButtonMode(NavButtonMode.BACK)
            renaultToolbar.setTitle(R.string.rlb_parkassist_main_settings)
            renaultToolbar.registerBackListener {
                finish()
                true
            }
        }
    }
}
