/**
 * Camera overlay:
 * - `none` requests for an empty overlay view (i.e. dealer view)
 * - `rvc/avm_standard` requests for a standard view (bird + rear/front view)
 * - `avm_panoramic` requests for a panoramic view (rear or front panoramic view)
 * - `avm_sides` requests for a sides view
 * - `avm_3d` request for a 3D view
 * - `avm_popup` requests for a bird view (from top)
 * - 'settings' requests a settings view
 * - 'avm_apa' for an avm apa view (bird + rear/front view)
 * - 'rvc_apa' for an rvc apa view
 * - 'trailer' requests a trailer view
 * - 'error' requests an error view
 */
type CameraOverlay =
  | 'none'
  | 'rvc_standard'
  | 'rvc_settings'
  | 'avm_standard'
  | 'avm_panoramic'
  | 'avm_sides'
  | 'avm_3d'
  | 'avm_popup'
  | 'avm_settings'
  | 'avm_apa'
  | 'rvc_apa'
  | 'trailer';

/**
 * Camera direction indication:
 * - `none` no camera direction indication
 * - `front` front camera indication
 * - `rear` rear camera indication
 */
type CameraIndication =
  | 'none'
  | 'front'
  | 'rear'
  | 'trailer'
  | 'three_d';

/**
 * Finger identification:
 * - `first` 1st finger identifier
 * - `second` 2nd finger identifier
 */
type Finger =
  | 'first'
  | 'second';

/**
 * Camera preview size
 */
type CameraSize = {
  width: integer
  height: integer
}

/**
 * Avm Camera preview sizes
 */
type AvmCameraSize = {
  port: CameraSize
  land: CameraSize
}

/**
 * Camera component reused in various views (RVC, AVM, APA...)
 * @model com/renault/parkassist/viewmodel/camera
 */
interface CameraViewModel {
  /**
   *  Whether tailgate opened warning message is displayed or not
   *  @default false
   */
  showTailgateOpenedWarning: boolean;

  /**
   *  Camera indication
   */
  cameraIndication : CameraIndication;

  /**
   *  EasyPark indication
   *  @default false
   */
  easyParkIndication: boolean;

  /**
   *  raeb Off Visible
   *  @default false
   */
  raebOffVisible: boolean;

  /**
   *  Camera preview size in pixels
   */
  cameraSize : AvmCameraSize;

  /**
   *  Camera overlay
   */
  cameraOverlay : CameraOverlay;

  /**
   *  Camera gravity
   */
  cameraGravity : integer;

  /**
   *  Show camera stream if true, else hide it
   */
  cameraVisible : boolean;

  /**
   *  Whether or not the camera is in error state
   */
  cameraError : boolean;

  /**
   *  Whether or not the camera view position has to be updated
   */
  updateCameraViewPosition : boolean;

   /**
    * Whether we display camera mask or not
    */
   cameraMaskVisible: boolean;

  /**
   * To be called when we have new coordinates for a finger on the screen.
   * @param finger identifier of the finger
   * @param x position of the finger on the x-axis
   * @param y position of the finger on the y-axis
   */
  screenPress(finger: Finger, x: float, y: float);

  /**
   * To be called when a finger left the screen.
   * @param finger identifier of the finger
   */
  screenRelease(finger: Finger);

  /**
   * To be called to notify the service that the camera view position has changed on screen.
   * @param x0 left side x position
   * @param y0 top side y position
   * @param x1 right side x position
   * @param y1 bottom side y position
   */
  setCameraViewPosition(x0: integer, y0: integer, x1: integer, y1: integer);
}
