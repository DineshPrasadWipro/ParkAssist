package com.renault.parkassist.viewmodel.apa

import alliance.car.autopark.AutoPark.ExtendedInstruction
import com.renault.parkassist.R
import com.renault.parkassist.repository.apa.Instruction

class ApaUtils {
    companion object {
        fun getInstructionResource(@ExtendedInstruction instruction: Int): Int? {
            return when (instruction) {
                Instruction.SELECT_SIDE ->
                    R.string.rlb_parkassist_apa_select_side
                Instruction.DRIVE_FORWARD,
                Instruction.DRIVE_FORWARD_TO_FIND_PARKING_SLOT,
                Instruction.DRIVE_FORWARD_SLOT_SUITABLE ->
                    R.string.rlb_parkassist_apa_drive_forward
                Instruction.STOP ->
                    R.string.rlb_parkassist_apa_stop
                Instruction.SELECT_REVERSE_GEAR_OR_PRESS_START_BUTTON ->
                    R.string.rlb_parkassist_apa_engage_rear_gear
                Instruction.SELECT_OR_ENGAGE_FORWARD_GEAR ->
                    R.string.rlb_parkassist_apa_engage_forward_gear
                Instruction.REVERSE ->
                    R.string.rlb_parkassist_apa_drive_backward
                Instruction.MANEUVER_COMPLETE_OR_FINISHED ->
                    R.string.rlb_parkassist_apa_maneuver_finished
                Instruction.GO_FORWARD_OR_REVERSE ->
                    R.string.rlb_parkassist_apa_go_forward_or_backward
                Instruction.MANEUVER_FINISHED_TAKE_BACK_CONTROL ->
                    R.string.rlb_parkassist_apa_maneuver_finished_take_back_control
                else ->
                    null
            }
        }

        fun getFakpInstructionResource(@ExtendedInstruction instruction: Int): Int? {
            return when (instruction) {
                Instruction.SELECT_SIDE ->
                    R.string.rlb_parkassist_fapk_select_side
                Instruction.DRIVE_FORWARD_TO_FIND_PARKING_SLOT ->
                    R.string.rlb_parkassist_fapk_drive_forward
                Instruction.STOP ->
                    R.string.rlb_parkassist_fapk_stop
                Instruction.SELECT_REVERSE_GEAR_OR_PRESS_START_BUTTON ->
                    R.string.rlb_parkassist_fapk_press_start_button
                Instruction.SELECT_OR_ENGAGE_FORWARD_GEAR ->
                    R.string.rlb_parkassist_fapk_engage_forward_gear
                Instruction.MANEUVER_COMPLETE_OR_FINISHED ->
                    R.string.rlb_parkassist_fapk_maneuver_finished
                Instruction.ENGAGE_REAR_GEAR ->
                    R.string.rlb_parkassist_fapk_engage_rear_gear
                Instruction.STOP_AFTER_REAR_GEAR_ENGAGED ->
                    R.string.rlb_parkassist_fapk_stop_after_rear_gear_engaged
                Instruction.ACCELERATE_AND_HOLD_THE_PEDAL_PRESSED ->
                    R.string.rlb_parkassist_fapk_accelerate_and_hold_the_pedal_pressed
                Instruction.MANEUVER_FINISHED_RELEASE_THE_ACCELERATOR_PEDAL ->
                    R.string.rlb_parkassist_fapk_maneuver_finished_release_the_accelerator_pedal
                Instruction.MANEUVER_FINISHED_TAKE_BACK_CONTROL ->
                    R.string.rlb_parkassist_fapk_maneuver_finished_take_back_control
                Instruction.HOLD_THE_ACCELERATOR_PEDAL_PRESSED ->
                    R.string.rlb_parkassist_fapk_hold_the_accelerator_pedal_pressed
                Instruction.DRIVE_FORWARD -> R.string.rlb_parkassist_fapk_drive_forward
                Instruction.DRIVE_FORWARD_SLOT_SUITABLE ->
                    R.string.rlb_parkassist_fapk_drive_forward_slightly
                else ->
                    null
            }
        }
    }
}