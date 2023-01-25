/*
 * Copyright (c) 2018
 * Developed by Renault SW Labs, an affiliate of RENAULT S.A.S. which holds all
 * intellectual property rights. Use of this software is subject to a specific
 * license granted by RENAULT S.A.S.
 */

type ErrorState =
    | 'error_state_no_error'
    | 'error_state_camera_failure'

type UserAcknowledgement =
    | 'ack_ok'
    | 'ack_cancel'

type WarningState =
    | 'warning_state_none'
    | 'warning_state_speed_nok'
    | 'warning_state_camera_misaligned'
    | 'warning_state_camera_soiled'
    | 'warning_state_trailer_not_detected'
    | 'warning_state_obstacle_present'
    | 'warning_state_trailer_access_limited'

type TrunkState =
    | 'trunk_door_state_unavailable'
    | 'trunk_door_opened'
    | 'trunk_door_closed'

type TrailerPresence =
    | 'trailer_presence_unavailable'
    | 'trailer_presence_detected'
    | 'trailer_presence_not_detected'

type Action =
    | 'close_view'
    | 'activate_maneuver_view'
    | 'activate_settings_view'
    | 'activate_trailer_view'
    | 'select_panoramic_view'
    | 'select_standard_view'
    | 'select_sides_view'
    | 'select_three_dimension_view'
    | 'select_front_camera'
    | 'select_rear_camera'
    | 'back_from_settings_view'

type View =
    | 'no_display'
    | 'rear_view'
    | 'front_view'
    | 'panoramic_rear_view'
    | 'panoramic_front_view'
    | 'sides_view'
    | 'three_dimension_view'
    | 'pop_up_view'
    | 'dealer_view'
    | 'auto_zoom_rear_view'
    | 'trailer_view'
    | 'settings_rear_view'
    | 'settings_front_view'
    | 'apa_front_view'
    | 'apa_rear_view'

type Origin =
    | 'request_from_vehicle'
    | 'request_from_client'
    | 'no_request'

/**
 * Type of surround view hardware present in the vehicle.
 */
type FeatureConfig = 
  | 'rvc'
  | 'avm'
  | 'none'

/**
 * State of maneuver feature availability.
 */
type ManeuverAvailability = 
  | 'not_ready'
  | 'ready'
  | 'restricted'

/** State representation of surroundView service.
    if isRequest is true, the state is currently request, else the state is established
 */
type SurroundState = {
  view: View
  isRequest: boolean
}

/**
 * Exposes surround view service as livedatas.
 *
 * @model com/renault/parkassist/repository/surroundview
 */
interface SurroundViewRepository {
  /**
   * Hardware configuration as exposed by the service.
   * Will be one of RVC, AVM or NONE.
   * @final
   */
  featureConfig: FeatureConfig

  /** Current surround state. */
  surroundState: SurroundState

  /** Authorized actions. */
  authorizedActions: integer[]

  /** Trailer presence. */
  trailerPresence: TrailerPresence

  /** Trunk state. */
  trunkState: TrunkState

  /** Warning state. */
  warningState: WarningState

  /** Error state. */
  errorState: ErrorState

  /** Camera brightness setting. */
  brightness: integer

  /** Camera color setting. */
  color: integer

  /** Camera contrast setting. */
  contrast: integer

  /** Auto-zoom feature activation setting. */
  autoZoomRearViewActivation: boolean

  /** Dynamic guidelines activation setting. */
  dynamicGuidelinesActivation: boolean

  /** Static guidelines activation setting. */
  staticGuidelinesActivation: boolean

  /** Trailer guidelines activation setting. */
  trailerGuidelinesActivation: boolean

  /** Brightness adjustement feature availability
   * @final
   * @default true
   */
  isBrightnessSupported: boolean

  /** Brightness adjustement minimum value
   * @final
   * @default 0
   */
  brightnessMin: integer

  /** Brightness adjustement maximum value
   * @final
   * @default 20
   */
  brightnessMax: integer

  /** Color adjustement feature availability
   * @final
   * @default true
   */
  isColorSupported: boolean

  /** Color adjustement minimum value
   * @final
   * @default 0
   */
  colorMin: integer

  /** Color adjustement maximum value
   * @final
   * @default 20
   */
  colorMax: integer

  /** Contrast adjustement feature availability
   * @final
   * @default true
   */
  isContrastSupported: boolean

  /** Contrast adjustement minimum value
   * @final
   * @default 0
   */
  contrastMin: integer

  /** Contrast adjustement maximum value
   * @final
   * @default 20
   */
  contrastMax: integer

  /** Auto-zoom feature availability
   * @final
   * @default true
   */
  isAutoZoomSupported: boolean

  /** Dynamic guidelines feature availability
   * @final
   * @default true
   */
  isDynamicGuidelinesSupported: boolean

  /** Static guidelines feature availability
   * @final
   * @default true
   */
  isStaticGuidelinesSupported: boolean

  /** Trailer guidelines feature availability
   * @final
   * @default true
   */
  isTrailerGuidelinesSupported: boolean

  /** 3D view feature availability
   * @final
   * @default true
   */
  is3DViewSupported: boolean

  /** Panoramic view feature availability
   * @final
   * @default true
   */
  isPanoramicViewSupported: boolean

  /** Trailer view feature availability
   * @final
   * @default true
   */
  isTrailerViewSupported: boolean

  /** Is rear camera located on trunk door
   * @final
   * @default true
   */
  isCameraOnTrunk: boolean

  /** Is regulation applicable
   * @final
   * @default true
   */
  isRegulationApplicable: boolean

  /**
   * Set display view status. Client shall indicate its current
   * display status to the surround view component, client
   * shall set display status to NO_DISPLAY to allow view
   * state change.
   * @param View Current client displayed view (as defined in the service ViewState IntDef).
   */
  setStatus(View: View)

  /**
   * User action request to switch to another view
   * @param action Requested user action (as defined in the service Action IntDef).
   */
  request(action: Action)

  /**
   * Acknowledge a warning state. Client can provide confirmations to certain requests/states.
   * This is the case for clearing AVM warning messages or for RVC/HFP features.
   * Clients should acknowledge AVM warning states (as defined in the service UserAcknowledgement StateListener#onWarningStateChange).
   * @param userAck Client user acknowledgement (as defined in the service UserAcknowledgement IntDef).
   */
  acknowledgeWarning(userAck: UserAcknowledgement)

  /**
   * Notify surround view service about the current surface view location and
   * size in the screen in pixels. The origin of the system coordinates is the top
   * left of the screen.
   * @param x0 Position of the bottom left corner of the camera view along x axis in pixels. 
   *           Value range is from 0 to screen width - 1.
   * @param y0 Position of the bottom left corner of the camera view along y axis in pixels. 
   *           Value range is from 0 to screen width - 1.
   * @param x1 Position of the top right corner of the camera view along x axis in pixels.
   *           Value range is from 0 to screen height - 1.
   * @param y1 Position of the top right corner of the camera view along y axis in pixels.
   *           Value range is from 0 to screen height - 1.
   */
  setCameraPosition(x0: integer, y0: integer, x1: integer, y1: integer)

  /**
     * Notify surround view service about the screen press position in the screen in
     * pixels. The origin of the system coordinates in the top left of the screen.
     * Only available on 3D screen.
     * @param finger Finger touch identifier for multi-touch
     * @param x Touch screen press position along x axis in pixels (sub-pixel precision supported).
     *          Value range is from 0.0 to screen width - 1.0.
     * @param y Touch screen press position along y axis in pixels (sub-pixel precision supported).
     *          Value range is from 0.0 to screen height - 1.0.
   */
  screenPress(finger: integer, x: float, y: float)

  /**
   * Notify surround view service about screen press release. Only available on 3D
   * screen.
   * @param finger Finger touch identifier for multi-touch
   *               (as defined in the service FingerTouch IntDef).
   */
  screenRelease(finger: integer)

  /**
   * Set color setting value.
   * @param value Color setting value to set.
   */
  setColor(value: integer)

  /**
   * Set brightness setting value.
   * @param value Brightness setting value to set.
   */
  setBrightness(value: integer)

  /**
   * Set contrast setting value. Note: if not supported, the listener
   * callback is not called.
   * @param value Contrast setting value to set.
   */
  setContrast(value: integer)

  /**
   * Set Auto Zoom Rear View Activation setting.
   * @param value Set to true to activate auto zoom rear view configuration, false otherwise.
   */
  setAutoZoomRearViewActivation(value: boolean)

  /**
   * Set static guidelines activation setting.
   * @param value Set to true to activate static guidelines, false otherwise.
   */
  setStaticGuidelinesActivation(value: boolean)

  /**
   * Set dynamic guidelines activation setting.
   * @param value Set to true to activate dynamic guidelines, false otherwise.
   */
  setDynamicGuidelinesActivation(value: boolean)

  /**
   * Set trailer guidelines activation setting.
   * @param value Set to true to activate trailer guideline, false otherwise.
   */
  setTrailerGuidelinesActivation(value: boolean)
}
