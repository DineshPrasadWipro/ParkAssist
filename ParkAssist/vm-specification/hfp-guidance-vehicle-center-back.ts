/*
 * Copyright (c) 2019
 * Developed by Renault SW Labs, an affiliate of RENAULT S.A.S. which holds all
 * intellectual property rights. Use of this software is subject to a specific
 * license granted by RENAULT S.A.S.
 */


/**
 * Easy Park Assist (APA) guidance state
 * @model com/renault/parkassist/viewmodel/apa/hfp
 */
interface HfpGuidanceVehicleCenterBackViewModel {
  /**
   * Whether the engage back is active
   */
  engageBackActiveVisible: boolean;

  /**
   * Whether the engage back is not active
   */
  engageBackNotActiveVisible: boolean;

  /**
   * Whether the engage back is visible
   */
  engageBackVisible: boolean;

  /**
   * Whether the stop back is visible
   */
  stopBackVisible: boolean;
}