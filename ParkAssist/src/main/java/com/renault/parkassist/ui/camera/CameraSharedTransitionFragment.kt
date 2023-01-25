package com.renault.parkassist.ui.camera

import android.os.Bundle
import android.transition.*
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.renault.parkassist.ui.FragmentBase

abstract class CameraSharedTransitionFragment : FragmentBase() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        sharedElementEnterTransition = TransitionSet().apply {
            addTransition(ChangeImageTransform())
            addTransition(ChangeBounds())
            addTransition(ChangeClipBounds())
            addTransition(ChangeTransform().apply { reparentWithOverlay = false })
        }
        return super.onCreateView(inflater, container, savedInstanceState)
    }
}