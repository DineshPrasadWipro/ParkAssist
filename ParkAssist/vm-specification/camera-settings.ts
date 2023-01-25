/*
 * Copyright (c) 2018 
 * Developed by Renault SW Labs, an affiliate of RENAULT S.A.S. which holds all 
 * intellectual property rights. Use of this software is subject to a specific 
 * license granted by RENAULT S.A.S.
 */


/**
 * Control camera settings
 *
 * @model com/renault/parkassist/viewmodel/camera
 */
interface CameraSettingsViewModel {

  /** Auto-zoom setting availability
   * @final
   * @default true
   */
  isAutoZoomAvailable: boolean

  /** Define if auto-zoom feature is active */
  isAutoZoomActive: boolean

  /** Dynamic guidelines setting availability
   * @final
   * @default true
   */
  isDynamicGuidelinesAvailable: boolean

  /** Define if dynamic guidelines have to be displayed */
  isDynamicGuidelinesActive: boolean

  /** Static guidelines setting availability
   * @final
   * @default true
   */
  isStaticGuidelinesAvailable: boolean

  /** Define if static guidelines have to be displayed */
  isStaticGuidelinesActive: boolean

  /** Trailer guidelines setting availability
   * @final
   * @default true
   */
  isTrailerGuidelinesAvailable: boolean

  /** Define if trailer guidelines have to be displayed */
  isTrailerGuidelinesActive: boolean

  /** Current color value for the camera
   * @minimum 0
   * @maximum 100
   * @multipleOf 5
   */
  color: integer

  /** Color adjustment minimum value
   * @final
   * @default 0
   */
  colorMin: integer

  /** Color adjustment maximum value
   * @final
   * @default 20
   */
  colorMax: integer

  /** Current brightness value for the camera
   * @minimum 0
   * @maximum 100
   * @multipleOf 5
   */
  brightness: integer

  /** Brightness adjustment minimum value
   * @final
   * @default 0
   */
  brightnessMin: integer

  /** Brightness adjustment maximum value
   * @final
   * @default 20
   */
  brightnessMax: integer

  /** Current contrast value for the camera
   * @minimum 0
   * @maximum 100
   * @multipleOf 5
   */
  contrast: integer

    /** Contrast adjustment minimum value
   * @final
   * @default 0
   */
  contrastMin: integer

  /** Contrast adjustement maximum value
   * @final
   * @default 20
   */
  contrastMax: integer

  /**
   * Sets the color
   * @param color the color to set
   */
  setColor(color: integer);

  /**
   * Sets the brightness
   * @param brightness the brightness to set
   */
  setBrightness(brightness: integer);

  /**
   * Sets the contrast
   * @param contrast the contrast to set
   */
  setContrast(contrast: integer);

  /**
   * Activate or de-activate static guidelines
   * @param active true to activate, false to de-activate
   */
  setStaticGuidelines(active: boolean);

  /**
   * Activate or de-activate dynamic guidelines
   * @param active true to activate, false to de-activate
   */
  setDynamicGuidelines(active: boolean);

  /**
   * Activate or de-activate trailer guidelines
   * @param active true to activate, false to de-activate
   */
  setTrailerGuidelines(active: boolean);

  /**
   * Activate or de-activate auto-zoom feature
   * @param active true to activate, false to de-activate
   */
  setAutoZoom(active: boolean);

  /**
   * Whether toolbar is enabled or not
   */
  toolbarEnabled : boolean;

 /**
   * Whether back button in toolbar is enabled or not
   */
  backButtonVisibility : boolean;

  /**
   * Navigate back from settings request to Surround View service
   * Send request to go back to previous fragment
   */
   navigateBack();
}
