/*
 * Copyright (c) 2019
 * Developed by Renault SW Labs, an affiliate of RENAULT S.A.S. which holds all
 * intellectual property rights. Use of this software is subject to a specific
 * license granted by RENAULT S.A.S.
 */

/**
 * Control parking sensors activation and report
 * collision detection information.
 *
 * _Ultrasonic Parking Assist  - Feature ID : 10905_
 *
 * @model com/renault/parkassist/viewmodel/sonar
 */
interface SonarMuteStateViewModel {
  /**
   * Whether Sonar Sound is muted or not
   */
  muted: boolean;

  /**
   * Mute button visibility
   */
  visible: boolean

  /**
   * Mute or Unmute Sonar Sound
   * @param muted true to mute, false to unmute
   */
   mute(muted: boolean);
}
