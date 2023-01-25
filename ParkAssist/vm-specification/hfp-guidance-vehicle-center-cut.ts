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
interface HfpGuidanceVehicleCenterCutViewModel {

  /**
   * Whether the arrow curve down is visible
   */
  arrowCurveDownVisible: boolean;

  /**
   * Whether the engage left active is visible
   */
  engageLeftActiveVisible: boolean;

  /**
   * Whether the engage left not active is visible
   */
  engageLeftNotActiveVisible: boolean;

  /**
   * Whether the engage right is active
   */
  engageRightActiveVisible: boolean;

  /**
   * Whether the engage right is not active
   */
  engageRightNotActiveVisible: boolean;

}