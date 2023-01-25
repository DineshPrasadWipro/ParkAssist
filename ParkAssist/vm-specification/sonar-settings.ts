/*
 * Copyright (c) 2018 
 * Developed by Renault SW Labs, an affiliate of RENAULT S.A.S. which holds all 
 * intellectual property rights. Use of this software is subject to a specific 
 * license granted by RENAULT S.A.S.
 */

/**
 * Control parking sensors activation
 *
 * _Ultrasonic Parking Assist  - Feature ID : 10905_
 * 
 * @model com/renault/parkassist/viewmodel/sonar
 */
interface SonarSettingsViewModel {

  /** Whether front sensors are enabled */
  frontEnabled: boolean;
  /** Whether front sensors are visible
   * @final
   */
  frontVisible: boolean;

  /** Whether rear sensors are enabled */
  rearEnabled: boolean;
  /** Whether rear sensors are visible
   * @final
   */
  rearVisible: boolean;
  /** Whether rear sensors are (de)activable
   * @final
   */
  rearToggleVisible: boolean;

  /** Whether flank sensors are enabled */
  flankEnabled: boolean;
  /** Whether flank sensors are visible
   * @final
   */
  flankVisible: boolean;

  /** Whether OSE is enabled */
  oseEnabled: boolean;
  /** Whether OSE is visible
   * @final
   */
  oseVisible: boolean;

  /** Whether RCTA is enabled */
  rctaEnabled: boolean;
  /** Whether RCTA option is visible
   * @final
   */
  rctaVisible: boolean;

  /** Whether R-AEB is enabled */
  raebEnabled: boolean;
  /** Whether R-AEB is visible
   * @final
   */
  raebVisible: boolean;

  /**
   * Activate or de-activate front sensors
   * @param enable true to enable, false to disable
   */
  enableFront(enable: boolean);

  /**
   * Activate or de-activate rear sensors
   * @param enable true to enable, false to disable
   */
  enableRear(enable: boolean);

  /**
   * Activate or de-activate flank sensors
   * @param enable true to enable, false to disable
   */
  enableFlank(enable: boolean);

  /**
   * Activate or de-activate OSE
   * @param enable true to enable, false to disable
   */
  enableOse(enable: boolean);

  /**
   * Activate or de-activate RCTA
   * @param enable true to enable, false to disable
   */
  enableRcta(enable: boolean);

  /**
   * Activate or de-activate R-AEB
   * @param enable true to enable, false to disable
   */
  enableRaeb(enable: boolean);
}
