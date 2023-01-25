package com.renault.parkassist.camera

import com.renault.parkassist.viewmodel.camera.AvmCameraSize
import com.renault.parkassist.viewmodel.camera.CameraSize

enum class AvmSize(val sizes: AvmCameraSize) {
    AVM_STANDARD(AvmCameraSize(CameraSize(1250, 666), CameraSize(923, 492))),
    AVM_PANORAMIC(AvmCameraSize(CameraSize(1250, 592), CameraSize(1040, 492))),
    AVM_SIDES(AvmCameraSize(CameraSize(1185, 948), CameraSize(615, 492))),
    AVM_3_D(AvmCameraSize(CameraSize(1250, 720), CameraSize(853, 492))),
    AVM_SETTINGS(AvmCameraSize(CameraSize(1250, 666), CameraSize(551, 440))),
    AVM_APA(AvmCameraSize(CameraSize(1250, 666), CameraSize(826, 440))),
    NONE(AvmCameraSize(CameraSize(0, 0), CameraSize(0, 0)))
}