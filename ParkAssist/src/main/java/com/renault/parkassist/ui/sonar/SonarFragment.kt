package com.renault.parkassist.ui.sonar

import com.renault.parkassist.R
import com.renault.parkassist.ui.FragmentBase

class SonarFragment : FragmentBase() {

    override val layout: Int = R.layout.sonar_rvc

    override fun onBind() {
        // No need to bind any viewmodel here. Mute button is managed by its own fragment
    }
}