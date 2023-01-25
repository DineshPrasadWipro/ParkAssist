package com.renault.parkassist.viewmodel.apa.fapk

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.renault.parkassist.R
import com.renault.parkassist.repository.apa.IApaRepository
import com.renault.parkassist.utility.errorLog
import com.renault.parkassist.viewmodel.apa.ApaDialogBox
import com.renault.parkassist.viewmodel.apa.ApaDialogButton
import com.renault.parkassist.viewmodel.apa.ApaViewModelMapper
import com.renault.parkassist.viewmodel.apa.ApaWarningAcknowledgment.ACK_1
import com.renault.parkassist.viewmodel.apa.ApaWarningAcknowledgment.ACK_2
import com.renault.parkassist.viewmodel.apa.ApaWarningMessage.*
import com.renault.parkassist.viewmodel.apa.IApaWarningViewModel
import org.koin.core.KoinComponent
import org.koin.core.inject

class FapkWarningViewModel : IApaWarningViewModel, KoinComponent {

    private val apaRepository: IApaRepository by inject()
    private val apaViewModelMapper: ApaViewModelMapper by inject()

    private val dialogBox: LiveData<ApaDialogBox> =
        Transformations.map(apaRepository.warningMessage) { warningId ->
            val label = apaViewModelMapper.toFapkLabelMessage(warningId)

            if (label != null && warningId != WARNING_NONE) {
                var positiveEnabled = false
                var neutralEnabled = false
                var negativeEnabled = false

                when (warningId) {
                    WARNING_MANEUVER_CANCELED, // 8
                    WARNING_MANEUVER_UNAVAILABLE_ENGINE_STOPPED, // 11
                    WARNING_FEATURE_UNAVAILABLE_REAR_GEAR_ENGAGED, // 12
                    WARNING_FEATURE_UNAVAILABLE_SPEED_TOO_HIGH, // 13
                    WARNING_FEATURE_UNAVAILABLE, // 14
                    WARNING_FEATURE_UNAVAILABLE_TRAILER_DETECTED, // 15
                    WARNING_FEATURE_UNAVAILABLE_CRUISE_CONTROL_ACTIVATED, // 16
                    WARNING_MANEUVER_CANCELED_PARKING_BRAKE, // 17
                    WARNING_MANEUVER_CANCELED_DRIVER_DOOR_OPEN, // 18
                    WARNING_MANEUVER_CANCELED_SEAT_BELT_UNFASTEN, // 19
                    WARNING_MANEUVER_UNAVAILABLE_PARKING_BRAKE, // 20
                    WARNING_MANEUVER_CANCELED_SLOPE_TOO_HIGH, // 21
                    WARNING_MANEUVER_UNAVAILABLE_SEAT_BELT_UNFASTEN, // 22
                    WARNING_MANEUVER_UNAVAILABLE_SLOPE_TOO_HIGH, // 23
                    WARNING_MANEUVER_CANCELED_BRAKING_FAILURE, // 24
                    WARNING_FEATURE_UNAVAILABLE_VEHICLE_NOT_STOPPED, // 26
                    WARNING_MANEUVER_CANCELED_TAKE_BACK_CONTROL, // 27
                    WARNING_MANEUVER_CANCELED_RELEASE_ACCELERATOR_PEDAL, // 31
                    WARNING_MANEUVER_UNAVAILABLE_H_152_ESC_OFF, // 33
                    WARNING_MANEUVER_CANCELED_ENGINE_STOPPED, // 34
                    WARNING_MANEUVER_CANCELED_GEAR_SHIFT_ACTIVATED, // 35
                    WARNING_MANEUVER_CANCELED_ESC_OFF, // 36
                    WARNING_FEATURE_UNAVAILABLE_MIRRORS_FOLDED, // 37
                    WARNING_MANEUVER_CANCELED_MIRRORS_FOLDED, // 38
                    WARNING_MANEUVER_FINISHED_NO_FRONT_OBSTACLE, // 39
                    WARNING_MANEUVER_UNAVAILABLE_SLOT_TOO_SMALL /* 40 */ -> positiveEnabled = true
                    WARNING_MANEUVER_SUSPENDED_DOOR_OPEN, // 1
                    WARNING_MANEUVER_SUSPENDED_HAND_ON_WHEEL, // 2
                    WARNING_MANEUVER_SUSPENDED, // 4
                    WARNING_MANEUVER_SUSPENDED_OBSTACLE_ON_PATH, // 6
                    WARNING_MANEUVER_UNAVAILABLE_HAND_ON_WHEEL, // 9
                    WARNING_MANEUVER_UNAVAILABLE_DOOR_OPEN, // 10
                    WARNING_BRAKE_TO_RESUME_MANEUVER, // 25
                    WARNING_MANEUVER_SUSPENDED_RELEASE_ACCELERATOR_PEDAL, // 28
                    WARNING_MANEUVER_SUSPENDED_GEAR_SHIFT_ACTIVATED, // 29
                    WARNING_SELECT_TURN_INDICATOR /* 30 */ -> neutralEnabled = true
                    WARNING_PRESS_OK_TO_START_MANEUVER /* 32  */ -> {
                        positiveEnabled = true
                        negativeEnabled = true
                    }
                    // Following messages are either unused or HFP only
                    WARNING_ASK_RESUME_MANEUVER, // 5
                    WARNING_MANEUVER_SUSPENDED_UNTIL_RESTART_ENGINE, // 3
                    WARNING_MANEUVER_SUSPENDED_BRAKE_TO_STOP /* 7 */ -> {
                        errorLog("autopark",
                            "unknown warning message, cannot show it"
                        )
                    }
                }

                ApaDialogBox(
                    label,
                    ApaDialogButton(
                        positiveEnabled,
                        R.string.rlb_ok,
                        ACK_1
                    ),
                    ApaDialogButton(
                        neutralEnabled,
                        R.string.rlb_parkassist_apa_neutral_button_label,
                        ACK_1
                    ),
                    ApaDialogButton(
                        negativeEnabled,
                        R.string.rlb_parkassist_apa_negative_button_label,
                        ACK_2
                    )
                )
            } else null
        }

    override fun getDialogBox(): LiveData<ApaDialogBox> = dialogBox

    override fun acknowledgeWarning(ack: Int) {
        apaRepository.acknowledgeWarning(ack)
    }
}