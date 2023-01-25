/*
 * Copyright (c) 2018 
 * Developed by Renault SW Labs, an affiliate of RENAULT S.A.S. which holds all 
 * intellectual property rights. Use of this software is subject to a specific 
 * license granted by RENAULT S.A.S.
 */

/**
 * Sensor level enumeration.
 */
type SensorLevel =
  | 'invisible'
  | 'greyed'
  | 'very_far'
  | 'far'
  | 'medium'
  | 'close'
  | 'very_close';

/**
 * Parking sensor state
 */
type ParkingSensor = {
  /**
   * Hatched state for a single FKP Sensor.
   */
  hatched: boolean;

  /**
   * sensor level.
   */
  level: SensorLevel;
};

/**
 * Control parking sensors activation and report
 * collision detection information.
 *
 * _Ultrasonic Parking Assist  - Feature ID : 10905_
 * 
 * @model com/renault/parkassist/viewmodel/sonar
 */
interface SonarStateViewModel {
  /** Front left UPA Sensor state */
  frontLeft: ParkingSensor;

  /** Front center UPA Sensor state */
  frontCenter: ParkingSensor;

  /** Front right UPA Sensor state */
  frontRight: ParkingSensor;

  /** Rear left UPA Sensor state */
  rearLeft: ParkingSensor;

  /** Rear center UPA Sensor state */
  rearCenter: ParkingSensor;

  /** Rear right UPA Sensor state */
  rearRight: ParkingSensor;

  /** Left front FKP Sensor state */
  leftFront: ParkingSensor;

  /** Left front-center FKP Sensor state */
  leftFrontCenter: ParkingSensor;

  /** Left rear-center FKP Sensor state */
  leftRearCenter: ParkingSensor;

  /** Left rear FKP Sensor state */
  leftRear: ParkingSensor;

  /** Right front FKP Sensor state*/
  rightFront: ParkingSensor;

  /** Right front-center FKP Sensor state */
  rightFrontCenter: ParkingSensor;

  /** Right rear-center FKP Sensor state */
  rightRearCenter: ParkingSensor;

  /** Right rear FKP Sensor state */
  rightRear: ParkingSensor;

  /** Whether an obstacle has been detected on the vehicle path */
  obstacle: boolean;

  /** Whether front sensors are enabled */
  frontEnabled: boolean;

  /** Whether rear sensors are enabled */
  rearEnabled: boolean;

  /** Whether flank sensors are enabled */
  flankEnabled: boolean;

  /** Whether car avatar should be displayed */
  avatarVisible: boolean;

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
   * Close camera view action visible
   */
  closeVisible : boolean;
}
