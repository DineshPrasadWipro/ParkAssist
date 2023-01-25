/**
 * Copyright (c) 2019 Renault SW Labs
 *
 * Developed by Renault SW Labs, an affiliate of RENAULT S.A.S. which holds all
 * intellectual property rights. Use of this software is subject to a specific
 * license granted by RENAULT S.A.S.
 */
package com.renault.parkassist.repository.surroundview

import alliance.car.surroundview.GraphicLayer
import android.view.Surface

interface IExtSurroundViewRepository : ISurroundViewRepository {

    fun createGraphicLayer(surface: Surface): GraphicLayer

    fun releaseGraphicLayer(graphicLayer: GraphicLayer)
}