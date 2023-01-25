/*
 * Copyright (c) 2018 
 * Developed by Renault SW Labs, an affiliate of RENAULT S.A.S. which holds all 
 * intellectual property rights. Use of this software is subject to a specific 
 * license granted by RENAULT S.A.S.
 */


/**
 * Easy Park Assist (APA) parkout confirmation illustration
 * @model com/renault/parkassist/viewmodel/apa/hfp
 */
interface HfpParkoutViewModel {

  /**
   * Whether the background Sides is visible
   */
  backgroundSidesVisible: boolean;

  /**
   * Whether the background Parallel Left is visible
   */
  backgroundParallelLeftVisible: boolean;

  /**
   * Whether the vehicle center is visible with parallel left background
   */
  parallelLeftVehicleCenterVisible: boolean;

  /**
   * Whether the background Parallel Right is visible
   */
  backgroundParallelRightVisible: boolean;

  /**
   * Whether the vehicle center is visible with parallel right background
   */
  parallelRightVehicleCenterVisible: boolean;

  /**
   * Whether the vehicle left is visible with parallel left background
   */
  parallelLeftVehicleLeftVisible: boolean;

  /**
   * Whether the vehicle right is visible with parallel right background
   */
  parallelRightVehicleRightVisible: boolean;

}
