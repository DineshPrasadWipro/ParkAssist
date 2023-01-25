/*
 * Copyright (c) 2018 
 * Developed by Renault SW Labs, an affiliate of RENAULT S.A.S. which holds all 
 * intellectual property rights. Use of this software is subject to a specific 
 * license granted by RENAULT S.A.S.
 */

/**
 * Simple object that represents the possible types of sound
 */
type SoundDescriptor = {
    /** The name given to that sound */
    name: string,
    /** The id of the sound */
    id: integer
}

/**
 * Control sound settings
 *
 * _Ultrasonic Parking Assist  - Feature ID : 10905_
 * 
 * @model com/renault/parkassist/viewmodel/sound
 */
interface SoundSettingsViewModel {

  /** Whether the sound is enabled */
  soundEnabled: boolean;

  /** Defines the id of the sound to use for sonar sound notifications */
  soundId: integer;

  /** Sound volume */
  volume: integer;

  /**
   * Display sound activation switch
   * @final
   */
  soundSwitchVisible: boolean

  /**
   * Display volume selector
   * @final
   */
  volumeVisible: boolean

  /**
   * Display sound type selector
   * @final
   */
  soundTypeVisible: boolean

  /**
   * Min possible volume
   * @final
   */
  minVolume: integer

  /**
   * Max possible volume
   * @final
   */
  maxVolume: integer

  /**
   * Different types of sound that can be applied
   * @final
   */
  sounds: SoundDescriptor[]

  /**
   * Activate or de-activate sonar sound
   * @param enable true to enable, false to disable
   */
  enableSound(enable: boolean);

  /**
   * Sets the sound type
   * @param id the desired sound id
   */
  setSound(id: integer);

  /**
   * Sets the volume
   * @param volume the volume to set
   */
  setVolume(volume: integer);
}
