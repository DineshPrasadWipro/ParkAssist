/*
 * Copyright (c) 2019
 * Developed by Renault SW Labs, an affiliate of RENAULT S.A.S. which holds all
 * intellectual property rights. Use of this software is subject to a specific
 * license granted by RENAULT S.A.S.
 */

/**
 * APA Warning Messages
 */
type ApaWarningMessage =
  |'warning none'
  |'warning maneuver suspended door open'
  |'warning maneuver suspended hand on wheel'
  |'warning maneuver suspended until restart engine'
  |'warning maneuver suspended'
  |'warning ask resume maneuver'
  |'warning maneuver suspended obstacle on path'
  |'warning maneuver suspended brake to stop'
  |'warning maneuver canceled'
  |'warning maneuver unavailable hand on wheel'
  |'warning maneuver unavailable door open'
  |'warning maneuver unavailable engine stopped'
  |'warning feature unavailable rear gear engaged'
  |'warning feature unavailable speed too high'
  |'warning feature unavailable'
  |'warning feature unavailable trailer detected'
  |'warning feature unavailable cruise control activated'
  |'warning maneuver canceled parking brake'
  |'warning maneuver canceled driver door open'
  |'warning maneuver canceled seat belt unfasten'
  |'warning maneuver unavailable parking brake'
  |'warning maneuver canceled slope too high'
  |'warning maneuver unavailable seat belt unfasten'
  |'warning maneuver unavailable slope too high'
  |'warning maneuver canceled braking failure'
  |'warning brake to resume maneuver'
  |'warning feature unavailable vehicle not stopped'
  |'warning maneuver canceled take back control'
  |'warning maneuver suspended release accelerator pedal'
  |'warning maneuver suspended gear shift activated'
  |'warning select turn indicator'
  |'warning maneuver canceled release accelerator pedal'
  |'warning press ok to start maneuver'
  |'warning maneuver unavailable h152 esc off'
  |'warning maneuver canceled engine stopped'
  |'warning maneuver canceled gear shift activated'
  |'warning maneuver canceled esc off'
  |'warning feature unavailable mirrors folded'
  |'warning maneuver canceled mirrors folded'
  |'warning maneuver finished no front obstacle'
  |'warning maneuver unavailable slot too small';


type ApaWarningAcknowledgment =
    |'ack1'
    |'ack2';

type ApaDialogButton = {
    enabled: boolean
    label: integer
    ack: ApaWarningAcknowledgment
}

/**
 * DialogBox
 */
type ApaDialogBox = {
    label: integer;
    positiveButton: ApaDialogButton
    neutralButton: ApaDialogButton
    negativeButton: ApaDialogButton
}

/**
 * Easy Park Assist (APA) navigation current state
 * @model com/renault/parkassist/viewmodel/apa
 */
interface ApaWarningViewModel {
  /**
   * DialogBox to be displayed upon warning reception
   */
  dialogBox: ApaDialogBox

  /**
   * Action to acknowledge a warning
   */
  acknowledgeWarning(ack: ApaWarningAcknowledgment)
}