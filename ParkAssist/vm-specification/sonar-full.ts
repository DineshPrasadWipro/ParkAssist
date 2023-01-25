/*
 * Copyright (c) 2019
 * Developed by Renault SW Labs, an affiliate of RENAULT S.A.S. which holds all
 * intellectual property rights. Use of this software is subject to a specific
 * license granted by RENAULT S.A.S.
 */

/**
 * Handles Sonar Alert Visibility.
 *
 * @model com/renault/parkassist/viewmodel/sonar
 */
interface SonarFullViewModel {
  /**
   * If sonar alert should be visible
   * @default false
   */
  sonarAlertVisibility : boolean

  /**
   * Change sonar alert visibility
   * @param visible true to show, false to hide
   */
  setSonarAlertVisibility(visible: boolean)

  /**
   *  EasyPark indication
   *  @default false
   */
  easyParkIndication: boolean;
}