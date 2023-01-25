package com.renault.parkassist.viewmodel.camera

import com.renault.parkassist.repository.surroundview.View
import com.renault.parkassist.utility.mapNullable

class CameraIndicationMapper {

    private val cameraIndicationMap = listOf(
        View.REAR_VIEW to CameraIndication.REAR,
        View.APA_REAR_VIEW to CameraIndication.REAR,
        View.PANORAMIC_REAR_VIEW to CameraIndication.REAR,
        View.SETTINGS_REAR_VIEW to CameraIndication.REAR,
        View.FRONT_VIEW to CameraIndication.FRONT,
        View.APA_FRONT_VIEW to CameraIndication.FRONT,
        View.PANORAMIC_FRONT_VIEW to CameraIndication.FRONT,
        View.SETTINGS_FRONT_VIEW to CameraIndication.FRONT,
        View.TRAILER_VIEW to CameraIndication.TRAILER,
        View.THREE_DIMENSION_VIEW to CameraIndication.THREE_D
    )

    @CameraIndication
    fun map(@View input: Int) =
        cameraIndicationMap.mapNullable(input) ?: CameraIndication.NONE
}