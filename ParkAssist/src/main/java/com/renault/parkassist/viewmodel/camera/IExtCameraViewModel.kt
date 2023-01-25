/**
 * Copyright (c) 2019 Renault SW Labs
 *
 * Developed by Renault SW Labs, an affiliate of RENAULT S.A.S. which holds all
 * intellectual property rights. Use of this software is subject to a specific
 * license granted by RENAULT S.A.S.
 */
package com.renault.parkassist.viewmodel.camera

import android.graphics.SurfaceTexture

/** Camera component extension */
interface IExtCameraViewModel : ICameraViewModel {

    fun provideSurface(surface: SurfaceTexture)

    fun stopCamera(surface: SurfaceTexture)
}