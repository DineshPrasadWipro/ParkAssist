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
interface HfpParkoutVehicleCenterViewModel {

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

   /**
    * Whether the arrow right side is visible
    */
   arrowRightSideVisible: boolean;

   /**
    * Whether the arrow left side is visible
    */
   arrowLeftSideVisible: boolean;

}