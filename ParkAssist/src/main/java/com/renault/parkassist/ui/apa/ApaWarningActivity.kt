package com.renault.parkassist.ui.apa

import alliancex.arch.core.logger.logD
import com.renault.car.ui.components.RenaultDialog
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.renault.parkassist.R
import com.renault.parkassist.viewmodel.apa.ApaWarningViewModelBase
import kotlinx.android.synthetic.main.custom_dialog.view.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.KoinComponent

class ApaWarningActivity : AppCompatActivity(), KoinComponent {

    private val apaWarningViewModel: ApaWarningViewModelBase by viewModel()
    private var warningDialogBox: RenaultDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_warning)

        logD { "onCreate" }

        apaWarningViewModel.dialogBox.observe(this, Observer
        { dialogBox ->
            logD { "dialogBox observer: got $dialogBox" }
            warningDialogBox?.dismiss() // Warning dialogs cannot stack up
            if (dialogBox != null) {
                // TODO: use relevant title: https://jira.dt.renault.com/browse/CCSEXT-14991
                val dialogBuilder = RenaultDialog.Builder(this)
                    .gravity(RenaultDialog.Gravity.TOP)

                val customView = getLayoutInflater().inflate(R.layout.custom_dialog, null)
                customView.apa_label.setText(dialogBox.label)

                if (dialogBox.positiveButton.enabled) dialogBuilder
                    .positiveButton(dialogBox.positiveButton.label) {
                        apaWarningViewModel.acknowledgeWarning(dialogBox.positiveButton.ack)
                    }

                if (dialogBox.neutralButton.enabled) dialogBuilder
                    .neutralButton(dialogBox.neutralButton.label) {
                        apaWarningViewModel.acknowledgeWarning(dialogBox.neutralButton.ack)
                    }

                if (dialogBox.negativeButton.enabled) dialogBuilder
                    .negativeButton(dialogBox.negativeButton.label) {
                        apaWarningViewModel.acknowledgeWarning(dialogBox.negativeButton.ack)
                    }

                warningDialogBox = dialogBuilder
                    .dismissOnButtonClick(false)
                    .uxContext(getString(R.string.apa_warning_dialog_ux))
                    .customView(customView)
                    .create()
                    .apply { show() }

                val layoutParams: WindowManager.LayoutParams? = warningDialogBox?.window?.attributes
                layoutParams?.y = resources.getDimensionPixelSize(R.dimen.rd_margin_vertical)
                warningDialogBox?.window?.attributes = layoutParams
            }
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