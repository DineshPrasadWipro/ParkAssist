/*
 * Copyright (c) 2019
 * Developed by Renault SW Labs, an affiliate of RENAULT S.A.S. which holds all
 * intellectual property rights. Use of this software is subject to a specific
 * license granted by RENAULT S.A.S.
 */

/**
 * Easy Park Assist (APA) guidance state
 * @model com/renault/parkassist/viewmodel/apa/hfp
 */
interface HfpGuidanceViewModel {
  /**
   * Whether the background Parallel Left is visible
   */
  backgroundParallelLeftVisible: boolean;

  /**
   * Whether the background Parallel Right is visible
   */
  backgroundParallelRightVisible: boolean;

  /**
   * Whether the vehicle center Cut and Parking Large is visible with parallel left background
   */
  parallelLeftVehicleCenterCutVisible: boolean;

  /**
   * Whether the vehicle center Cut and Parking Large is visible with parallel right background
   */
  parallelRightVehicleCenterCutVisible: boolean;

  /**
   * Whether the vehicle center Front is visible with parallel left background
   */
  parallelLeftVehicleCenterFrontVisible: boolean;

  /**
   * Whether the vehicle center Front is visible with parallel right background
   */
  parallelRightVehicleCenterFrontVisible: boolean;

  /**
   * Whether the vehicle center Back is visible with parallel left background
   */
  parallelLeftVehicleCenterBackVisible: boolean;

  /**
   * Whether the vehicle center Back is visible with parallel right background
   */
  parallelRightVehicleCenterBackVisible: boolean;

  /**
   * Whether the vehicle center is visible with parallel left background
   */
  parallelLeftVehicleCenterVisible: boolean;

  /**
   * Whether the vehicle center is visible with parallel right background
   */
  parallelRightVehicleCenterVisible: boolean;

  /**
   * Whether the Park finished icon is visible with parallel left background
   */
  parallelLeftParkVisible: boolean;

  /**
   * Whether the Park finished icon is visible with parallel right background
   */
  parallelRightParkVisible: boolean;

  /**
   * Whether the vehicle left is visible
   */
  parkoutLeftVehicleLeftVisible: boolean;

  /**
   * Whether the vehicle right is visible
   */
  parkoutRightVehicleRightVisible: boolean;

  /**
   * Whether the background Perpendicular Center is visible
   */
  backgroundPerpendicularCenterVisible: boolean;

  /**
   * Whether the vehicle center park is visible
   */
  perpendicularVehicleCenterParkVisible: boolean;

  /**
   * Whether the vehicle center front is visible in perpendicular background
   */
  perpendicularVehicleCenterFrontVisible: boolean;

  /**
   * Whether the vehicle center back is visible in perpendicular background
   */
  perpendicularVehicleCenterBackVisible: boolean;

  /**
   * Whether the stop back is active in vehicle center back view with perpendicular background
   */
  perpendicularVehicleCenterBackStopBackVisible: boolean;

  /**
   * Whether the stop front is active in vehicle center front view with perpendicular background
   */
  perpendicularVehicleCenterFrontStopFrontVisible: boolean;

  /**
   * Whether the vehicle center Cut and Parking Large is visible with perp left background
   */
  perpendicularLeftVehicleCenterCutVisible: boolean;

  /**
   * Whether the vehicle center Cut and Parking Large is visible with perp right background
   */
  perpendicularRightVehicleCenterCutVisible: boolean;

  /**
   * Current text resource id for apa guidance instruction
   */
  extendedInstruction: integer;

  /**
   * Current apa guidance maneuver completion
   */
  maneuverCompletion: integer;

  /**
   * Gauge shall take forward shape if true, backward shape else
   */
  isForwardGauge: boolean;

  /**
   * Whether apa guidance maneuver progress (gauge) is Visible
   */
  gaugeVisible : boolean;

  /**
   * The color of the maneuver progress bar (gauge)
   */
  gaugeColor: integer;

  /**
   *  raeb Sonar Off Visible
   *  @default false
   */
  raebSonarOffVisible: boolean;

  /**
   * Requests the camera switch On/Off
   * @param cameraOn true/false
   */
  requestCameraSwitch(cameraOn: boolean);

 /**
   * whether avm display is visible
   * @default false
   */
    isCameraVisible: boolean;
}