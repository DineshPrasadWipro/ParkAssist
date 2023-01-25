/*
 * Copyright (c) 2018 
 * Developed by Renault SW Labs, an affiliate of RENAULT S.A.S. which holds all 
 * intellectual property rights. Use of this software is subject to a specific 
 * license granted by RENAULT S.A.S.
 */

/**
 * AVM video current display mode selected
 * - `standard` Standard view avm selected
 * - `panoramic` Panoramic view selected
 * - `sides` Sides view selected
 * - `three-d` Three-d view selected
 */
type AvmModeSelected =
  | 'standard'
  | 'panoramic'
  | 'sides'
  | 'three-d';

/**
 * View mode request, asked by a user when he clicks on a bottom navigation view button in the AVM app:
 * - `standard` requests for a standard view (bird + rear/front view)
 * - `panoramic` requests for a panoramic view (rear or front panoramic view)
 * - `sides` requests for a sides view
 * - `view-3d` request for a 3D view
 * - 'settings' requests a settings view
 * - 'back-from-settings' requests a back from settings view
 */
type AvmModeRequest =
  | 'standard'
  | 'panoramic'
  | 'sides'
  | 'view-3d'
  | 'maneuver'
  | 'close'
  | 'settings'
  | 'back-from-settings';

/**
 * Arround View Monitoring (AVM) video display current state
 * @model com/renault/parkassist/viewmodel/avm
 */
interface AvmStateViewModel {
  /**
   * Current display type button selected
   * @default standard
   */
  modeSelected: AvmModeSelected;

  /**
   * Close camera view action visible
   */
  closeVisible : boolean;

  /**
   * Easypark shortcut visible
   * @final
   */
  easyparkShortcutVisible : boolean;

  /**
   * Activate maneuver view action visible
   */
  maneuverVisible : boolean;

  /**
   * Activate settings view action visible
   */
  settingsVisible : boolean;

  /**
   * Activate trailer view action visible
   */
  trailerVisible : boolean;

  /**
   * Whether or not we should add camera horizontal margin in order
   * to center it on screen for bird side view
   */
  birdSideCameraMargin : boolean;

  /**
   * Select standard view action visible
   */
  selectStandardViewVisible : boolean;

  /**
   * Back button visibility
   */
  backButtonVisible : boolean;

  /**
   * Select panoramic view action visible
   */
  selectPanoramicViewVisible : boolean;

  /**
   * Select sides view action visible
   */
  selectSidesViewVisible : boolean;

  /**
   * Select three dimension view action visible
   */
  selectThreeDimensionViewVisible : boolean;

  /**
   * three dimension info text visible
   */
  threeDimensionInfoVisible : boolean;

  /**
   * Whether buttons are enabled or not
   */
  buttonsEnabled : boolean;

  /**
   * Requests the AVM to display a specific view mode
   * @param request AVM view mode request
   */
  requestViewMode(request: AvmModeRequest);

  /**
   * This function is the entry point for Avm camera scenarios.
   * It will decide what mode to set by default.
   * To be called when lifecycle is attached to viewModels livedatas
   */
  requestView();

  /**
   * This function is the exit point for Avm camera scenarios.
   * It will decide what how to properly leave Avm.
   * To be called when lifecycle is detached to viewModels livedatas
   */
  closeView();
}
