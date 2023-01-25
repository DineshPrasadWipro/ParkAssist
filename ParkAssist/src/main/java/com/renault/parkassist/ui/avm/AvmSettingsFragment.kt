package com.renault.parkassist.ui.avm

import android.content.res.Configuration
import androidx.core.view.isVisible
import com.renault.car.ui.components.renaultButtonBar
import com.renault.parkassist.R
import com.renault.parkassist.ui.camera.CameraOptionsSettingsFragment

class AvmSettingsFragment : CameraOptionsSettingsFragment() {
    override val layout: Int = R.layout.fragment_avm_settings

    override fun onBind() {
        super.onBind()
        // TODO: Review with design if it's possible to use the bottom button bar here as is done
        //  in portrait and RVC, instead of the weird square of buttons
        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            renaultButtonBar.isVisible = false
        }
    }
}