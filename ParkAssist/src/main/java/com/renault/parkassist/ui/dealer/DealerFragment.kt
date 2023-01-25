package com.renault.parkassist.ui.dealer

import com.renault.parkassist.R
import com.renault.parkassist.ui.FragmentBase

class DealerFragment : FragmentBase() {
    override val layout: Int = R.layout.fragment_dealer
    override fun onBind() {
        // No need to bind any viewmodel here. This fragment is just a navigation destination with
        // static layout
    }
}