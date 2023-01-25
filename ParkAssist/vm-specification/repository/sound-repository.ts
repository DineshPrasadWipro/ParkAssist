/**
 * Sound type
 */
type SoundType = {
  /** Sound identifier */
  id: integer
  /** Sound name */
  name: string
}

/**
 * Exposes sound service as livedatas.
 *
 * @model com/renault/parkassist/repository/settings
 */
interface SoundRepository {
  /**
   * Is sound enabled ?
   */
  soundEnabled: boolean;

  /**
   * Occupant Seat Exit enabled ?
   */
  oseEnabled: boolean;

  /**
   * Selected sound type
   */
  soundType: integer;

  /**
   * Current volume
   */
  volume: integer;

  /**
   * Is sound muted ?
   */
  muted: boolean;

  /**
   * Sonar audio activation control feature present
   * @final
   */
  soundActivationControlPresence: boolean;

  /**
   * Sonar sound type selection control feature present
   * @final
   */
  soundSelectionControlPresence: boolean;
  
  /**
   * Available sound types
   * @final
   */
  soundTypes: SoundType[];

  /**
   * Sonar volume control feature present
   * @final
   */
  volumeControlPresence: boolean;

  /**
   * Minimum volume
   * @final
   */
  minVolume: integer;

  /**
   * Maximum volume
   * @final
   */
  maxVolume: integer;

  /**
   * Sonar mute control feature present
   * @final
   */
  temporaryMuteControlPresence: boolean;

  /**
   * Occupant Seat Exit feature present
   * @final
   */
  oseControlPresence: boolean;

  /**
   * Enable/disable sound.
   * @param enable if true enable the sound, disable it else.
   */
  enableSound(enable: boolean)

  /**
   * Set sound type.
   * @param id identifier of the targeted sound type
   */
  setSoundType(id: integer)

  /**
   * Set volume.
   * @param volume value to set.
   */
  setVolume(volume: integer)

  /**
   * Mute/unmute sound.
   * @param muted if true mute the group, unmute it else.
   */
  mute(muted: boolean)

  /**
   * Enable/disable Occupant Seat Exit feature.
   * @param enable if true enable the group, disable it else.
   */
  enableOse(enable: boolean)
}
