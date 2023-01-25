package com.renault.parkassist.viewmodel.apa

import android.app.Application
import androidx.lifecycle.LiveData
import com.renault.parkassist.repository.apa.FeatureConfig
import com.renault.parkassist.repository.apa.IApaRepository
import com.renault.parkassist.utility.errorLog
import com.renault.parkassist.viewmodel.apa.fapk.FapkWarningViewModel
import com.renault.parkassist.viewmodel.apa.hfp.HfpWarningViewModel
import org.koin.core.KoinComponent
import org.koin.core.inject

class ApaWarningViewModel(application: Application) : ApaWarningViewModelBase(application),
    KoinComponent {

    private val apaRepository: IApaRepository by inject()
    private val hfpWarningViewModel: HfpWarningViewModel by inject()
    private val fapkWarningViewModel: FapkWarningViewModel by inject()
    private var vmImpl: IApaWarningViewModel

    init {
        vmImpl = when (apaRepository.featureConfiguration) {
            FeatureConfig.HFP -> hfpWarningViewModel
            FeatureConfig.FAPK -> fapkWarningViewModel
            else -> {
                errorLog(
                    "autopark",
                    "unexpected feature",
                    "falling back to HFP for warnings"
                )
                hfpWarningViewModel
            }
        }
    }

    override fun getDialogBox(): LiveData<ApaDialogBox> = vmImpl.dialogBox

    override fun acknowledgeWarning(ack: Int) = vmImpl.acknowledgeWarning(ack)
}