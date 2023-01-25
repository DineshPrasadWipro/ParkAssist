package com.renault.parkassist.viewmodel.apa

import androidx.lifecycle.MutableLiveData
import com.renault.parkassist.R
import com.renault.parkassist.TestBase
import com.renault.parkassist.repository.apa.ApaMapper
import com.renault.parkassist.repository.apa.ApaRepository
import com.renault.parkassist.repository.apa.IApaRepository
import io.mockk.every
import io.mockk.mockk
import org.junit.Before
import org.koin.dsl.module

abstract class ApaWarningViewModelTest : TestBase() {

    private val apaViewModelMapper = ApaViewModelMapper()
    private val apaMapper = ApaMapper()
    private val apaRepository = mockk<ApaRepository>(relaxed = true)
    protected val apaRepositoryWarningMessage = MutableLiveData<Int>()

    override val koinModule = module {
        single<IApaRepository>(override = true) { apaRepository }
        factory { apaViewModelMapper }
        factory { apaMapper }
    }

    @Before
    override fun setup() {
        super.setup()
        every { apaRepository.warningMessage } returns apaRepositoryWarningMessage
    }

    enum class DialogBoxType {
        DIALOG_BOX_TYPE_POSITIVE,
        DIALOG_BOX_TYPE_NEUTRAL,
        DIALOG_BOX_TYPE_POSITIVE_NEGATIVE,
        DIALOG_BOX_TYPE_DISABLED
    }

    private fun getHfpLabel(apaRepositoryMessage: Int) = apaViewModelMapper.toHfpLabelMessage(
        apaMapper.mapApaViewModelWarningId(apaRepositoryMessage)
    ) ?: -1

    private fun getFapkLabel(apaRepositoryMessage: Int) = apaViewModelMapper.toFapkLabelMessage(
        apaMapper.mapApaViewModelWarningId(apaRepositoryMessage)
    ) ?: -1

    protected fun getExpectedHfpDialogBox(
        type: DialogBoxType,
        visible: Boolean,
        apaRepositoryMessage: Int
    ): ApaDialogBox? = getExpectedDialogBox(type, visible, apaRepositoryMessage, true)

    protected fun getExpectedFapkDialogBox(
        type: DialogBoxType,
        visible: Boolean,
        apaRepositoryMessage: Int
    ): ApaDialogBox? = getExpectedDialogBox(type, visible, apaRepositoryMessage, false)

    private fun getExpectedDialogBox(
        type: DialogBoxType,
        visible: Boolean,
        apaRepositoryMessage: Int,
        isHfp: Boolean
    ): ApaDialogBox? {

        if (!visible) return null
        val label = if (isHfp)
            getHfpLabel(apaRepositoryMessage)
        else
            getFapkLabel(apaRepositoryMessage)

        var positiveEnabled = false
        var neutralEnabled = false
        var negativeEnabled = false

        when (type) {
            DialogBoxType.DIALOG_BOX_TYPE_POSITIVE -> positiveEnabled = true
            DialogBoxType.DIALOG_BOX_TYPE_NEUTRAL -> neutralEnabled = true
            DialogBoxType.DIALOG_BOX_TYPE_POSITIVE_NEGATIVE -> {
                positiveEnabled = true
                negativeEnabled = true
            }
            DialogBoxType.DIALOG_BOX_TYPE_DISABLED -> {
                positiveEnabled = false
                neutralEnabled = false
                negativeEnabled = false
            }
        }
        return ApaDialogBox(
            label,
            ApaDialogButton(
                positiveEnabled,
                R.string.rlb_ok,
                ApaWarningAcknowledgment.ACK_1
            ),
            ApaDialogButton(
                neutralEnabled,
                R.string.rlb_parkassist_apa_neutral_button_label,
                ApaWarningAcknowledgment.ACK_1
            ),
            ApaDialogButton(
                negativeEnabled,
                R.string.rlb_parkassist_apa_negative_button_label,
                ApaWarningAcknowledgment.ACK_2
            )
        )
    }
}