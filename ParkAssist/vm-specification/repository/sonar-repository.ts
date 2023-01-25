/**
 * Sonar sensor state
 */
type SensorState = {
    /** @default false */
    isHwSupported: boolean;
    /** @default false */
    isHatched: boolean;
    /** @default false */
    isScanned: boolean;
    /** @default 0 */
    level: integer;
}

/**
 * Display type
 */
type DisplayType = 
  | 'none'
  | 'widget'
  | 'fullscreen';

/**
 * Sensor group state
 */
type GroupState = 
  | 'disabled'
  | 'enabled';

/**
 * UPA display request type
 */
type UpaDisplayRequestType =
  | 'no_display'
  | 'rear'
  | 'front'
  | 'rear_front'

/**
 *  FKP display request type
 */
type FkpDisplayRequestType =
  | 'no_display'
  | 'flank'

/**
 * Exposes sonar service as livedatas.
 *
 * @model com/renault/parkassist/repository/sonar
 */
interface SonarRepository {
      /**
   * Indicate whether Ultrasonic Park Assist Rear feature is part of vehicle configuration.
   * @final
   */
  upaRearFeaturePresent: boolean;

  /**
   * Indicate whether Ultrasonic Park Assist Front feature is part of vehicle configuration.
   * @final
   */
  upaFrontFeaturePresent: boolean;

  /**
   * Indicate whether FlanK Protection feature is part of vehicle configuration.
   * @final
   */
  fkpFeaturePresent: boolean;

  /**
   * Indicate whether Ultrasonic Park Assist Visual Feedback feature is part of vehicle configuration.
   * @final
   */
  upaFkpVisualFeedbackFeaturePresent: boolean;

  /**
   * Indicate whether Rear Cross Traffic Alert feature is part of vehicle configuration.
   * @final
   */
  rctaFeaturePresent: boolean;

  /**
   * Indicate whether Rear Automotive Emergency Breaking feature is part of vehicle configuration.
   * @final
   */
  raebFeaturePresent: boolean;

  /**
   * Indicate whether Ultrasonic Park Assist Visual Settings feature is part of vehicle configuration.
   * If true, 'rear' sonar can be (de)activated in the settings.
   * Else, 'rear' sonar activation cannot be change by the user
   * @final
   */
  rearUpaActivationSettingPresent: boolean;

  /**
   * Current surround view displayed screen.
   * @default 'none'
   */
  displayRequest: DisplayType;

  /**
   * UPA view requested by the service.
   * @default 'no_display'
   */
  upaDisplayRequest: UpaDisplayRequestType;

  /**
   * FKP view requested by the service.
   * @default 'no_display'
   */
  fkpDisplayRequest: FkpDisplayRequestType;

  /**
   * Obstacle detection.
   * @default false
   */
  obstacle: boolean;

  /**
   * Collision alert enabled.
   * @default false
   */
  collisionAlertEnabled: boolean;

  /**
   * Collision alert side.
   */
  collisionAlertSide: integer;

  /**
   * Collision alert level.
   */
  collisionAlertLevel: integer;

  /**
   * Rear Auto Emergency Break alert enabled.
   * @default false
   */
  raebAlertEnabled: boolean;

  /**
   * Rear Auto Emergency Break alert level.
   */
  raebAlertState: integer;

  /**
   * Close display allowed.
   */
  closeAllowed: boolean;

  /** Front sensor group */
  frontState: GroupState;
  /** Rear sensor group */
  rearState: GroupState;
  /** Flank sensor group */
  flankState: GroupState;

  /** Front left sensor */
  frontLeft: SensorState;
  /** Front center sensor */
  frontCenter: SensorState;
  /** Front right sensor */
  frontRight: SensorState;

  /** Rear left sensor */
  rearLeft: SensorState;
  /** Rear center sensor */
  rearCenter: SensorState;
  /** Rear right sensor */
  rearRight: SensorState;

  /** Left front sensor */
  leftFront: SensorState;
  /** Left front-center sensor */
  leftFrontCenter: SensorState;
  /** Left rear-center sensor */
  leftRearCenter: SensorState;
  /** Left rear sensor */
  leftRear: SensorState;

  /** Right front sensor */
  rightFront: SensorState;
  /** Right front-center sensor */
  rightFrontCenter: SensorState;
  /** Right rear-center sensor */
  rightRearCenter: SensorState;
  /** Right rear sensor */
  rightRear: SensorState;

  /**
   * Enable Collision Alert.
   * @param enable if true enable alert, disable it else.
   */
  enableCollisionAlert(enable: boolean);

  /**
   * Enable Rear Auto Emergency Break.
   * @param enable if true enable RAEB, disable it else.
   */
  enableRearAutoEmergencyBreak(enable: boolean);

  /**
   * Set sonar group.
   * @param sonarGroupId identifier of the targeted sonar group
   * @param enable if true enable the group, disable it else.
   */
  setSonarGroup(sonarGroupId: integer, enable: boolean)
}
