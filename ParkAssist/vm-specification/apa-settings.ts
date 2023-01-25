/*
 * Copyright (c) 2019
 * Developed by Renault SW Labs, an affiliate of RENAULT S.A.S. which holds all
 * intellectual property rights. Use of this software is subject to a specific
 * license granted by RENAULT S.A.S.
 */

/**
 * Default maneuver enumeration.
 */
type Maneuver =
  | 'parallel'
  | 'perpendicular';

/**
 * Handles automatic park assist settings.
 *
 * _Automatic Parking Assist  - Feature ID : _
 *
 * @model com/renault/parkassist/viewmodel/apa
 */
interface ApaSettingsViewModel {

  /**
   * Display maneuver selector
   * @final
   */
  maneuverSelectorVisible: boolean

  /**
   * Different types of maneuver that can be applied
   * @final
   */
  maneuvers: integer[]

  /**
   * Currently selected default maneuver mode.
   */
  defaultManeuver: Maneuver;

  /**
   * Switch the default maneuver mode.
   * @param mode the mode to set
   */
  setDefaultManeuver(mode: Maneuver);
}
