/*
 * Copyright (c) 2018
 * Developed by Renault SW Labs, an affiliate of RENAULT S.A.S. which holds all
 * intellectual property rights. Use of this software is subject to a specific
 * license granted by RENAULT S.A.S.
 */

/**
 * Handles full automatic park assist state.
 *
 * _Automatic Parking Assist  - Feature ID : _
 *
 * @model com/renault/parkassist/viewmodel/apa/fapk
 */
interface FapkViewModel {

 /**
  * The instruction the user needs to follow as a resource id.
  */
 instruction: integer;

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
  * Current maneuver stop switch button visibility state
  */
 maneuverStopSwitchButtonVisible: boolean

 /**
  * Current maneuver start switch button visibility state
  */
 maneuverStartSwitchButtonVisible: boolean

 /**
  * Current maneuver start switch button enabled state
  */
 maneuverStartSwitchButtonEnabled: boolean

 /**
  * Current maneuver start switch button selection state
  */
 maneuverStartSwitchButtonSelected: boolean

 /**
  * Current maneuver stop switch button selection state
  */
 maneuverStopSwitchButtonSelected: boolean

 /**
  * Whether we display settings icon or not
  */
 settingsVisible: boolean;

 /**
  * Whether we display parallel button
  * @final
  */
  maneuverParallelVisible: boolean;

 /**
  * Whether we display perpendicular button
  * @final
  */
  maneuverPerpendicularVisible: boolean;

 /**
  * Whether we display parkout button
  * @final
  */
  maneuverParkoutVisible: boolean;

 /**
  * Start the maneuver if all conditions boolean met
  */
 maneuverStart();

 /**
  * Stop the maneuver if all conditions boolean met
  */
 maneuverStop();

 /**
  * Switch the maneuver mode.
  * @param mode the mode to set
  * this method should take ManeuverType enum from Apa repository
  */
 setManeuver(maneuverType: integer);
}
