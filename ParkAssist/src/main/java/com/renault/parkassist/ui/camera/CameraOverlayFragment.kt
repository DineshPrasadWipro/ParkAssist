package com.renault.parkassist.ui.camera

import alliancex.renault.ui.RenaultIconView
import alliancex.renault.ui.RenaultTextView
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import com.renault.parkassist.R
import com.renault.parkassist.ui.FragmentBase
import com.renault.parkassist.utility.setPresent
import com.renault.parkassist.utility.setVisible
import com.renault.parkassist.viewmodel.camera.CameraIndication
import com.renault.parkassist.viewmodel.camera.ExtCameraViewModelBase
import org.koin.androidx.viewmodel.ext.android.viewModel

open class CameraOverlayFragment : FragmentBase() {

    companion object {
        const val LAYOUT_OVERLAY_ID = "layout_overlay_id"

        fun newInstance(layoutId: Int): CameraOverlayFragment {
            val args = Bundle()
            args.putInt(LAYOUT_OVERLAY_ID, layoutId)
            val fragment = CameraOverlayFragment()
            fragment.arguments = args
            return fragment
        }
    }

    private val cameraViewModel: ExtCameraViewModelBase by viewModel()

    private val cameraIndicatorView: CameraIndicatorView?
        get() = view?.findViewById(R.id.elt_camera_indicator)
    private val bootStatus: RenaultTextView?
        get() = view?.findViewById(R.id.elt_boot_status_info)
    private val easyParkIndicatorView: RenaultIconView?
        get() = view?.findViewById(R.id.elt_camera_easypark)
    private val raebOffView: ImageView?
        get() = view?.findViewById(R.id.elt_raeb_off)
    private val cameraMask: FrameLayout?
        get() = view?.findViewById(R.id.elt_camera_mask)

    enum class LayoutOverlay(val layoutResId: Int) {
        NONE(R.layout.fragment_ovl_none),
        RVC_STANDARD(R.layout.fragment_ovl_rvc_std),
        AVM_STANDARD(R.layout.fragment_ovl_avm_std),
        AVM_PANORAMIC(R.layout.fragment_ovl_avm_pano),
        AVM_SIDES(R.layout.fragment_ovl_avm_sides),
        AVM_3_D(R.layout.fragment_ovl_avm_3d),
        AVM_POPUP(R.layout.fragment_ovl_avm_popup),
        RVC_SETTINGS(R.layout.fragment_ovl_rvc_settings),
        AVM_APA(R.layout.fragment_ovl_avm_apa),
        RVC_APA(R.layout.fragment_ovl_rvc_apa),
        TRAILER(R.layout.fragment_ovl_trailer)
    }

    override val layout: Int = LayoutOverlay.NONE.layoutResId

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layoutResId = arguments?.getInt(LAYOUT_OVERLAY_ID, layout) ?: layout
        return inflater.inflate(layoutResId, container, false)
    }

    override fun onBind() {
        // TODO we can use a dedicated fragment to manage each of them:
        //  cameraIndication, easypark icon and boot open

        cameraViewModel.cameraIndication.observe { indication: Int ->
            // TODO put in viewmodel
            cameraIndicatorView?.direction =
                when (indication) {
                    CameraIndication.FRONT -> CameraIndicatorView.Indication.FRONT
                    CameraIndication.REAR -> CameraIndicatorView.Indication.REAR
                    CameraIndication.TRAILER -> CameraIndicatorView.Indication.TRAILER
                    CameraIndication.THREE_D -> CameraIndicatorView.Indication.THREE_D
                    else -> CameraIndicatorView.Indication.NONE
                }
        }

        cameraViewModel.cameraMaskVisible.observe {
            cameraMask?.setPresent(it)
        }

        cameraViewModel.showTailgateOpenedWarning.observe { visible: Boolean ->
            bootStatus?.setPresent(visible)
        }

        cameraViewModel.easyParkIndication.observe { visible: Boolean ->
            easyParkIndicatorView?.setPresent(visible)
        }

        cameraViewModel.raebOffVisible.observe { visible: Boolean ->
            raebOffView?.setVisible(visible)
        }
    }
}