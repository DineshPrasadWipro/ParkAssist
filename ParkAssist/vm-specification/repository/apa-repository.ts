/*
 * Copyright (c) 2018 
 * Developed by Renault SW Labs, an affiliate of RENAULT S.A.S. which holds all 
 * intellectual property rights. Use of this software is subject to a specific 
 * license granted by RENAULT S.A.S.
 */

 /**
 * Auto Park Assist vehicle configuration
 * - `none` no auto-park assist available
 * - `hfp` hands-free parking available
 * - `hfpb` auto-break hands-free parking available
 * - `fpk`  full park ??? available ** FIXME **
 * - `fapk` full auto-park available
 */
type FeatureConfig = 
| 'none'
| 'hfp'
| 'hfpb'
| 'fpk'
| 'fapk'

 /**
 * Auto Park Assist maneuver types
 * - `parallel` park in parallel
 * - `perpendicular` park in perpendicular
 * - `angled` park in angled
 * - `parkout` park out
 * - `auto mode` apa selects automatically the maneuver
 * - `unavailable` no maneuver move
 */
type ManeuverType =
| 'parallel'
| 'perpendicular'
| 'angled'
| 'parkout'
| 'auto mode'
| 'unavailable'

 /**
 * Auto Park Assist maneuver selection
 * - `selected` maneuver not selected
 * - `not selected` maneuver selected
 * - `unavailable` maneuver unavailable
 */
type ManeuverSelection =
| 'selected'
| 'not selected'
| 'unavailable'

 /**
 * Auto Park Assist maneuver start switch
 *
 * - 'none' maneuver start switch is not displayed or is unavailable
 * - 'unusable start' maneuver start switch is displayed as start but is not enabled
 * - 'display start' maneuver start switch is enabled and displayed as start
 * - 'display start' Maneuver start switch is enabled and displayed as start (auto mode)
 * - 'display start parallel' maneuver start switch is enabled and displayed as start (parallel mode)
 * - 'display start perpendicular' maneuver start switch is enabled and displayed as start (perpendicular mode)
 * - 'display start parkout' maneuver start switch is enabled and displayed as start (park out mode)
 * - 'display cancel' maneuver start switch is enabled and displayed as cancel
 */
type ManeuverStartSwitch =
| 'none'
| 'unusable start'
| 'display start'
| 'display start auto mode'
| 'display start parallel'
| 'display start perpendicular'
| 'display start parkout'
| 'display cancel'

 /**
 * Auto Park Assist display states
 * - `display none` no display
 * - `display scanning` scanning display
 * - `display parkout confirmation` parkout display
 * - `display guidance` scanning display
 */
type DisplayState =
| 'display_none'
| 'display_scanning'
| 'display_parkout_confirmation'
| 'display_guidance'

type ScanningSide =
    | 'scanning_side_none'
    | 'scanning_side_right'
    | 'scanning_side_left'
    | 'scanning_side_unavailable'

type Instruction =
    | 'select_side'
    | 'drive_forward_to_find_parking_slot'
    | 'drive_forward_slot_suitable'
    | 'stop'
    | 'select_reverse_gear_or_press_start_button'
    | 'select_or_engage_forward_gear'
    | 'drive_forward'
    | 'reverse'
    | 'maneuver_complete_or_finished'
    | 'go_forward_or_reverse'
    | 'maneuver_finished_take_back_control'
    | 'engage_rear_gear'
    | 'stop_after_rear_gear_engaged'
    | 'accelerate_and_hold_the_pedal_pressed'
    | 'maneuver_finished_release_the_accelerator_pedal'
    | 'hold_the_accelerator_pedal_pressed'
    | 'stand_by_no_text'
    | 'unavailable'

type ManeuverMove =
    | 'first'
    | 'backward'
    | 'forward'
    | 'unavailable'

type ViewMask =
    | 'unavailable'
    | 'requested'

/**
 * Exposes automatic park assist service as livedatas.
 *
 * @model com/renault/parkassist/repository/apa
 */
interface ApaRepository {
  /**
   * APA feature configuration
   * according to vehicle configuration as exposed by the service
   * @final
   */
  featureConfiguration: FeatureConfig;

  /**
   * Supported Maneuver types
   * according to supported maneuver types as exposed by the service
   * @final
   */
  supportedManeuvers: integer[];

  /**
   * The display state is the main auto park state. It is controlled by ECU and is not managed
   * by the auto park service. It controls APA application activation and also its current display.
   * If state is DISPLAY_NONE, APA is disabled and can be activated on user request.
   * Otherwise for all other display states, APA is enabled and can be deactivated.
   */
  displayState: DisplayState;

  /**
   * Whether the left side of the car is suitable to park or not.
   */
  leftSuitable: boolean;

  /**
   * Whether the right side of the car is suitable to park or not.
   */
  rightSuitable: boolean;

  /**
   * Whether the driver selected the left side with its indicator or not.
   */
  leftSelected: boolean;

  /**
   * Whether the driver selected the right side with its indicator or not.
   */
  rightSelected: boolean;

  /**
   * Scanning side indicates which side of the vehicle the ECU is currently scanning for a
   * suitable ParkingSlot. It is a notification HMI application uses to draw
   * images and arrows on the display.
   */
  scanningSide: ScanningSide;

  /**
   * Automatic Maneuver is a boolean reporting if Status display is AUTOMATIC_MANEUVER_ON or not
   * Status Display is a notification of the current APA status IVI and cluster are supposed to
   * notify through a pictogram. On cluster it is regulatory, but not on IVI.
   * Note that if  DisplayState is DISPLAY_GUIDANCE, we already
   * know that an APA maneuver is ongoing, but status display provides more information such as
   * AUTOMATIC_MANEUVER_STANDBY which indicates that ongoing maneuver is paused.
   */
  automaticManeuver: boolean;

  /**
   * Maneuver move is an indication of the APA maneuver vehicle direction during an ongoing
   * maneuver. Depending on ManeuverMove client APA application draws different
   * indications for the gauge arrow widget.
   */
  maneuverMove: ManeuverMove;

  /**
   * During an ongoing maneuver in DISPLAY_GUIDANCE DisplayState,
   * maneuver completion monitors maneuver progress as reported by ECU. It is used by HMI client
   * for drawing a progress bar in the arrow widget.
   * Value expressed in % unit.
   * @minimum 0
   * @maximum 100
   */
  maneuverCompletion: integer;

  /**
   * When APA is activated and DisplayState is not DISPLAY_NONE,
   * ECU can provide instructions to display on client HMI application to guide user.
   * Labels corresponding to each ExtendedInstruction depend on the vehicle's FeatureConfiguration.
   */
  extendedInstruction: Instruction;

  /**
   * Warning messages are notifications sent by ECU that provide information to user about APA
   * feature. They are displayed as dialog boxes with button controls so that user can acknowledge
   * or give feedback to the ECU. Buttons are linked to acknowledgeWarning(int).
   * Note: Dialog box are to be displayed on overlay over HMI no matter the current
   * DisplayState of APA!
   */
  warningMessage: integer;

  /**
   * Notify change on default maneuver type.
   * @default 1
   */
  defaultManeuverType: integer;

  /**
   * Maneuver selection notifies of the current status for each
   * maneuver type supported in the current configuration
   */
   maneuverSelection: ManeuverSelection

  /**
   * Indicates the current selection of the parallel maneuver.
   */
   parallelManeuverSelection: ManeuverSelection

  /**
   * Indicates the current selection of the perpendicular maneuver.
   */
  perpendicularManeuverSelection: ManeuverSelection

  /**
   * Indicates the current selection of the parkout maneuver.
   */
  parkOutManeuverSelection: ManeuverSelection

  /**
   * Indicates the current maneuver switch send by the ECU.
   */
  maneuverSwitchSelection: ManeuverStartSwitch

  /**
   * View mask is a mechanism used in FAPK configuration only.
   * It controls the HMI masking of the camera part of the video stream sent by the AVM ECU.
   * Such masking can happen while scanning for parking slots when speed is comprised in a
   * certain range. This is to avoid driver distraction while still allowing for APA features.
   */
  viewMask: ViewMask

  /**
   * Request a new user maneuver type choice.
   * In APA, user can select different kinds of maneuvers that the ECU will try to perform.
   * User cannot request a new maneuver type if ECU indicates MANEUVER_CHOICE_UNAVAILABLE.
   * In that case service will raise an exception and will not perform the request.
   * It is mapped to HFP_SelectedManeuverTypeRequest. Is is automatically reset to no request by
   * VHAL after a 3000ms timeout, or before if HFP_SelectedManeuverType is changed by ECU.
   * @param maneuverType Maneuver type.
   */
  requestManeuverType(maneuverType: ManeuverType);

  /**
   * Acknowledge a warning message.
   * In autopark, user can give input to dialog boxes which are raised through warning messages.
   * If a message is not acknowledged, should it be not raised again it will clear itself.
   * On dialog boxes with one button, ack. 1 should be used. With two buttons: ack. 1 and ack. 2.
   * It is automatically reset to no ack. by VHAL after a 300ms timeout.
   * @param userAck Client user acknowledgement UserAcknowledgement.
   */
  acknowledgeWarning(userAck: integer);

  /**
   * Set default maneuver type setting.
   * This setting is applied on ECU through a dedicated signal. Service will automatically
   * set the setting on the ECU upon initialization. It will also apply it on change.
   * @param defaultManeuverType Set default maneuver type ManeuverType for autopark.
   */
  setDefaultManeuverType(maneuverType: ManeuverType);

  /**
   * Request switch maneuver start of AutoPark to ECU.
   * This is a switch activation: used both for activation and deactivation.
   * Success or failure of this operation is known later through the display state callback.
   * Autopark can only be activated by user request. This request is always available.
   * As per specification, VHAL will maintain this signal pushed for 600ms before resetting.
   */
  switchManeuverStartActivation();
}
