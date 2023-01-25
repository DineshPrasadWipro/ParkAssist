package com.renault.parkassist.ui.sonar

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import com.renault.parkassist.R
import com.renault.parkassist.ui.FragmentBase
import com.renault.parkassist.utility.setPresent
import com.renault.parkassist.viewmodel.sonar.SonarFullViewModelBase
import kotlinx.android.synthetic.main.sonar_full.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class SonarFullFragment : FragmentBase() {

    private lateinit var easyParkIndicatorView: ImageView

    private val sonarFullViewModel: SonarFullViewModelBase by sharedViewModel()

    override val layout: Int = R.layout.sonar_full

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        easyParkIndicatorView = view.findViewById(R.id.elt_sonar_easypark)
    }

    override fun onBind() {
        sonarFullViewModel.sonarAlertVisibility.observe {
            alerts_sonar_view.visibility = if (it) View.VISIBLE else View.INVISIBLE
        }

        sonarFullViewModel.easyParkIndication.observe { visible: Boolean ->
            easyParkIndicatorView.setPresent(visible)
        }
    }
}