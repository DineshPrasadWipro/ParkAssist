package com.renault.parkassist.ui.trailer

import android.content.res.Configuration
import androidx.fragment.app.viewModels
import com.android.car.ui.toolbar.NavButtonMode
import com.renault.car.ui.components.renaultToolbar
import com.renault.parkassist.R
import com.renault.parkassist.routing.pursuit.PursuitViewModel
import com.renault.parkassist.ui.FragmentBase
import kotlinx.android.synthetic.main.fragment_trailer.*

class TrailerFragment : FragmentBase() {

    override val layout: Int = R.layout.fragment_trailer
    private val pursuitVm: PursuitViewModel by viewModels()

    private val mBackListener = {
        pursuitVm.stop()
        true
    }

    override fun onBind() {
        with(renaultToolbar) {
            if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                title = resources.getString(R.string.rlb_parkassist_trailer)
                setNavButtonMode(NavButtonMode.BACK)
                registerBackListener(mBackListener)
            } else {
                title = resources.getString(R.string.rlb_parkassist_trailer_disclaimer)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        renaultToolbar.unregisterBackListener(mBackListener)
    }
    }
