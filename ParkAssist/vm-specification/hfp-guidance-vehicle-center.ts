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
interface HfpGuidanceVehicleCenterViewModel {

  /**
   * Whether the arrow Straight Up is visible
   */
  arrowStraightUpVisible: boolean;

  /**
   * Whether the arrow Curve Up is visible
   */
  arrowCurveUpVisible: boolean;

  /**
   * Whether the engage front is active
   */
  engageFrontActiveVisible: boolean;

  /**
   * Whether the engage front is not active
   */
  engageFrontNotActiveVisible: boolean;

  /**
   * Whether the frontright curve is active
   */
  rightCurveFrontActiveVisible: boolean;

  /**
   * Whether the frontright curve is not active
   */
  rightCurveFrontNotActiveVisible: boolean;

  /**
   * Whether the frontleft curve is active
   */
  leftCurveFrontActiveVisible: boolean;

  /**
   * Whether the frontleft curve is not active
   */
  leftCurveFrontNotActiveVisible: boolean;

  /**
   * Whether the arrow Straight Down is visible
   */
  arrowStraightDownVisible: boolean;

  /**
   * Whether the arrow Curve Down is visible
   */
  arrowCurveDownVisible: boolean;

  /**
   * Whether the engage back is active
   */
  engageBackActiveVisible: boolean;

  /**
   * Whether the engage back is not active
   */
  engageBackNotActiveVisible: boolean;

  /**
   * Whether the backright curve is active
   */
  rightCurveBackActiveVisible: boolean;

  /**
   * Whether the backright curve is not active
   */
  rightCurveBackNotActiveVisible: boolean;

  /**
   * Whether the backleft curve is active
   */
  leftCurveBackActiveVisible: boolean;

  /**
   * Whether the backleft curve is not active
   */
  leftCurveBackNotActiveVisible: boolean;

  /**
   * Whether the left double curve is visible
   */
  leftDoubleCurveVisible: boolean;

  /**
   * Whether the right double curve is visible
   */
  rightDoubleCurveVisible: boolean;

  /**
   * Whether the stop is visible
   */
  stopVisible: boolean;

}