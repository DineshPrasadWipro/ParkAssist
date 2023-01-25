/**
 * Copyright (c) 2019 Renault SW Labs
 *
 * Developed by Renault SW Labs, an affiliate of RENAULT S.A.S. which holds all
 * intellectual property rights. Use of this software is subject to a specific
 * license granted by RENAULT S.A.S.
 */
package com.renault.parkassist.viewmodel.camera.mock

import android.app.Application
import android.graphics.SurfaceTexture
import com.renault.parkassist.viewmodel.camera.ExtCameraViewModelBase
import com.renault.parkassist.viewmodel.camera.ICameraViewModel

class ExtCameraViewModelMock(application: Application, delegate: CameraViewModelMock) :
    ExtCameraViewModelBase(application), ICameraViewModel by delegate {

    override fun provideSurface(surface: SurfaceTexture) {
    }

    override fun stopCamera(surface: SurfaceTexture) {
    }
}