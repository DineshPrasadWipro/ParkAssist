/*
 * Copyright (c) 2019
 * Developed by Renault SW Labs, an affiliate of RENAULT S.A.S. which holds all
 * intellectual property rights. Use of this software is subject to a specific
 * license granted by RENAULT S.A.S.
 */

/**
 * Rear View Camera (RVC) video display current state
 * @model com/renault/parkassist/viewmodel/rvc
 */
interface RvcStateViewModel {
  /**
   * Settings camera view action visible
   */
  settingsVisible : boolean;

  /**
   * Whether toolbar is enabled or not
   */
  toolbarEnabled : boolean;

  /**
   * Requests Surround View service to navigate to RVC settings
   */
  navigateToSettings();

  /**
   * Closes the Rvc screen
   */
  close();
}
