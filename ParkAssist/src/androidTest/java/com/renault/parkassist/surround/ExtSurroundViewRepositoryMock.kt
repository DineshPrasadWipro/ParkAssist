/**
 * Copyright (c) 2019 Renault SW Labs
 *
 * Developed by Renault SW Labs, an affiliate of RENAULT S.A.S. which holds all
 * intellectual property rights. Use of this software is subject to a specific
 * license granted by RENAULT S.A.S.
 */
package com.renault.parkassist.surround

import alliance.car.surroundview.GraphicLayer
import android.app.Application
import android.view.Surface
import com.renault.parkassist.repository.surroundview.IExtSurroundViewRepository
import com.renault.parkassist.repository.surroundview.mock.SurroundViewRepositoryMock
import org.koin.core.KoinComponent
import org.koin.core.inject

class ExtSurroundViewRepositoryMock(application: Application) :
    SurroundViewRepositoryMock(application), IExtSurroundViewRepository, KoinComponent {

    private val graphicLayerMock: GraphicLayerMock by inject()

    override fun createGraphicLayer(surface: Surface): GraphicLayer = graphicLayerMock

    override fun releaseGraphicLayer(graphicLayer: GraphicLayer) {}

    fun startRendering() = graphicLayerMock.startRendering()
}