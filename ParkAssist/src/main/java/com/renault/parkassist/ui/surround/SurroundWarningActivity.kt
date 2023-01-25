package com.renault.parkassist.ui.surround

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.renault.car.ui.components.RenaultDialog
import com.renault.parkassist.R
import com.renault.parkassist.utility.errorLog
import com.renault.parkassist.viewmodel.surround.SurroundWarningStateViewModelBase
import com.renault.parkassist.viewmodel.surround.SurroundWarningType
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.KoinComponent

class SurroundWarningActivity : AppCompatActivity(), KoinComponent {
    private val surroundWarningStateViewModel: SurroundWarningStateViewModelBase by viewModel()

    private var warningDialogBox: RenaultDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_warning)

        surroundWarningStateViewModel.warning.observe(this, Observer
        { warning ->
            if (warningDialogBox?.isShowing == true)
                warningDialogBox?.dismiss()

            val contentText = when (warning) {
                SurroundWarningType.SPEED_NOK ->
                    R.string.rlb_parkassist_warning_speed
                SurroundWarningType.CAMERA_MISALIGNED ->
                    R.string.rlb_parkassist_warning_camera_misaligned
                SurroundWarningType.CAMERA_SOILED ->
                    R.string.rlb_parkassist_warning_camera_soiled
                SurroundWarningType.OBSTACLE_DETECTED ->
                    R.string.rlb_parkassist_warning_obstacle_detected
                SurroundWarningType.TRAILER_ACCESS_LIMITED ->
                    R.string.rlb_parkassist_warning_access_limited
                SurroundWarningType.TRAILER_NOT_DETECTED ->
                    R.string.rlb_parkassist_warning_trailer_not_detected
                SurroundWarningType.NONE -> return@Observer
                else -> {
                    errorLog("surround", "cannot display warning: $warning")
                    return@Observer
                }
            }
            warningDialogBox = RenaultDialog.Builder(this)
                .content(contentText)
                .positiveButton(android.R.string.ok) {
                    surroundWarningStateViewModel.acknowledge()
                }
                .dismissOnButtonClick(false)
                .uxContext(getString(R.string.surround_warning_dialog_ux))
                .create()
                .apply { show() }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        warningDialogBox?.dismiss()
    }

    override fun onBackPressed() {
        // Do nothing
    }
}