/*
 * Copyright (c) 2018 
 * Developed by Renault SW Labs, an affiliate of RENAULT S.A.S. which holds all 
 * intellectual property rights. Use of this software is subject to a specific 
 * license granted by RENAULT S.A.S.
 */

/**
 * Parking Side where slots are suitable or selected
 */
type Side =
  | 'straight'
  | 'left'
  | 'right'
  | 'leftRight';

/**
 * Handles automatic park assist state.
 *
 * _Automatic Parking Assist  - Feature ID : _
 * 
 * @model com/renault/parkassist/viewmodel/apa/hfp
 */
interface HfpScanningViewModel {

  /**
   * The instruction the user needs to follow as a resource id.
   */
  instruction: integer;

  /**
   * Whether upa sensors are disabled in apa scanning
   */
  upaDisabledApaScanning: boolean;

  /**
   * Listen to Surround State to handle avm sonar visibility when speed too high
   */
  sonarAvmVisible: boolean;

  /**
   * Listen to DisplayRequest to handle rvc sonar visibility when speed too high
   */
  sonarRvcVisible: boolean;

  /**
   * Whether we display `hfp parkout confirmation` or not
   */
  displayParkout: boolean;

  /**
   * Whether we selected left scanning
   */
  leftIndicatorSelected: boolean;

  /**
   * Whether we selected right scanning
   */
  rightIndicatorSelected: boolean;

  /**
   *  The background parking slot to display
   */
  backgroundResource: integer;

  /**
   * Whether the left Parking Slot in Parallel Maneuver is visible
   */
  leftParkingSlotParallelVisible: boolean;

  /**
   * Whether the right Parking Slot in Parallel Maneuver is visible
   */
  rightParkingSlotParallelVisible: boolean;

  /**
   * Whether the left Parking Slot in Perpendicular Maneuver is visible
   */
  leftParkingSlotPerpendicularVisible: boolean;

  /**
   * Whether the right Parking Slot in Perpendicular Maneuver is visible
   */
  rightParkingSlotPerpendicularVisible: boolean;

  /**
   * The left Parking Slot Resource to display
   */
  leftSlotResource: integer;

  /**
   * The right Parking Slot Resource to display
   */
  rightSlotResource: integer;

  /**
   * Whether we have to display arrow front car
   */
  carFrontArrowResourceVisible: boolean;

  /**
   * Whether we have to display stop front car
   */
  carFrontStopResourceVisible: boolean;

  /**
   * Whether we have to display rear Left Short Arrow
   */
  rearLeftShortArrowVisible: boolean;

  /**
   * Whether we have to display rear Right Short Arrow
   */
  rearRightShortArrowVisible: boolean;

  /**
   * Whether we have to display rear Left Long Arrow
   */
  rearLeftLongArrowVisible: boolean;

  /**
   * Whether we have to display rear Right Long Arrow
   */
  rearRightLongArrowVisible: boolean;

  /**
   * Whether we have to display rear arrow
   */
  rearArrowResourceVisible: boolean;

   /**
    * Current maneuver parallel button selection state
    */
   maneuverParallelButtonSelected: boolean

   /**
    * Current maneuver parallel button state
    */
   maneuverParallelButtonEnabled: boolean

   /**
    * Current maneuver perpendicular button selection state
    */
   maneuverPerpendicularButtonSelected: boolean

   /**
    * Current maneuver perpendicular button state
    */
   maneuverPerpendicularButtonEnabled: boolean

   /**
    * Current maneuver parkout button selection state
    */
   maneuverParkoutButtonSelected: boolean

   /**
    * Current maneuver parkout button button state
    */
   maneuverParkoutButtonEnabled: boolean

  /**
   * This function is the entry point for Apa camera scenarios.
   * It will decide what mode to set by default.
   * To be called when lifecycle is attached to viewModels livedatas
   */
  start();

  /**
   * This function is the exit point for Apa camera scenarios.
   * It will decide what how to properly leave Apa.
   * To be called when lifecycle is detached to viewModels livedatas
   */
  stop();

 /**
  * Switch the maneuver mode.
  * @param mode the mode to set
  * this method should take ManeuverType enum from Apa repository
  */
 setManeuver(maneuverType: integer);

}
