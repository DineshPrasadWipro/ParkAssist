package com.renault.parkassist.injection

import alliance.car.AllianceCar
import alliance.car.autopark.AllianceCarAutoParkManager
import alliance.car.media.AllianceCarAudioManager
import alliance.car.sonar.AllianceCarSonarManager
import alliance.car.surroundview.AllianceCarSurroundViewManager
import alliance.car.windowoverlay.AllianceCarWindowOverlay
import alliance.car.windowoverlay.AllianceCarWindowOverlayManager
import android.app.Application
import android.content.Context
import android.os.UserManager
import com.renault.parkassist.UserProcess
import com.renault.parkassist.camera.*
import com.renault.parkassist.camera.EvsSurfaceTexture
import com.renault.parkassist.overlay.*
import com.renault.parkassist.repository.apa.ApaMapper
import com.renault.parkassist.repository.apa.ApaRepository
import com.renault.parkassist.repository.apa.AutoParkManagerAdapter
import com.renault.parkassist.repository.apa.IApaRepository
import com.renault.parkassist.repository.routing.*
import com.renault.parkassist.repository.settings.ISoundRepository
import com.renault.parkassist.repository.settings.SoundRepository
import com.renault.parkassist.repository.sonar.ISonarRepository
import com.renault.parkassist.repository.sonar.SonarCarManagerAdapter
import com.renault.parkassist.repository.sonar.SonarRepository
import com.renault.parkassist.repository.surroundview.*
import com.renault.parkassist.routing.*
import com.renault.parkassist.routing.policy.IPolicy
import com.renault.parkassist.routing.policy.PolicyLoader
import com.renault.parkassist.routing.pursuit.PursuitHelper
import com.renault.parkassist.routing.pursuit.PursuitManager
import com.renault.parkassist.service.CameraConnectionPolicy
import com.renault.parkassist.service.ICameraConnectionPolicy
import com.renault.parkassist.service.ShadowLauncher
import com.renault.parkassist.service.TrailerNotifier
import com.renault.parkassist.ui.apa.ManeuverButtonMapper
import com.renault.parkassist.viewmodel.apa.*
import com.renault.parkassist.viewmodel.apa.fapk.FapkSettingsViewModel
import com.renault.parkassist.viewmodel.apa.fapk.FapkViewModel
import com.renault.parkassist.viewmodel.apa.fapk.FapkViewModelBase
import com.renault.parkassist.viewmodel.apa.fapk.FapkWarningViewModel
import com.renault.parkassist.viewmodel.apa.hfp.*
import com.renault.parkassist.viewmodel.avm.*
import com.renault.parkassist.viewmodel.camera.*
import com.renault.parkassist.viewmodel.mvc.MvcMapper
import com.renault.parkassist.viewmodel.mvc.MvcStateViewModel
import com.renault.parkassist.viewmodel.mvc.MvcStateViewModelBase
import com.renault.parkassist.viewmodel.rvc.RvcStateViewModel
import com.renault.parkassist.viewmodel.rvc.RvcStateViewModelBase
import com.renault.parkassist.viewmodel.settings.MainSettingsViewModel
import com.renault.parkassist.viewmodel.settings.MainSettingsViewModelBase
import com.renault.parkassist.viewmodel.shadow.ShadowFullscreenViewModel
import com.renault.parkassist.viewmodel.sonar.*
import com.renault.parkassist.viewmodel.sound.SoundSettingsViewModel
import com.renault.parkassist.viewmodel.sound.SoundSettingsViewModelBase
import com.renault.parkassist.viewmodel.surround.SurroundWarningMapper
import com.renault.parkassist.viewmodel.surround.SurroundWarningStateViewModel
import com.renault.parkassist.viewmodel.surround.SurroundWarningStateViewModelBase
import com.renault.parkassist.viewmodel.trailer.TrailerViewModel
import com.renault.parkassist.viewmodel.trailer.TrailerViewModelBase
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    single<SonarCarManagerAdapter> {
        val allianceCar = AllianceCar.createAllianceCar(get())
        val manager = allianceCar!!.getAllianceCarManager(
            AllianceCarSonarManager.SONAR_SERVICE
        ) as AllianceCarSonarManager
        SonarCarManagerAdapter(manager)
    }
    single<SurroundViewManagerAdapter> {
        val allianceCar = AllianceCar.createAllianceCar(get())
        val manager = allianceCar!!.getAllianceCarManager(
            AllianceCarSurroundViewManager.SURROUND_VIEW_SERVICE
        ) as AllianceCarSurroundViewManager
        SurroundViewManagerAdapter(manager)
    }
    single<AllianceCarAudioManager> {
        val allianceCar = AllianceCar.createAllianceCar(get())
        allianceCar!!.getAllianceCarManager(
            AllianceCarAudioManager.ALLIANCE_CAR_AUDIO_SERVICE_NAME
        ) as AllianceCarAudioManager
    }
    single<AutoParkManagerAdapter> {
        val allianceCar = AllianceCar.createAllianceCar(get())
        val manager = allianceCar!!.getAllianceCarManager(
            AllianceCarAutoParkManager.AUTOPARK_SERVICE
        ) as AllianceCarAutoParkManager
        AutoParkManagerAdapter(manager)
    }
    single<AllianceCarWindowOverlayManager> {
        val allianceCar = AllianceCar.createAllianceCar(get())
        allianceCar!!.getAllianceCarManager(
            AllianceCarWindowOverlayManager.WINDOW_OVERLAY_SERVICE
        ) as AllianceCarWindowOverlayManager
    }

    single<IAllianceCarWindowOverlayManager> { AllianceCarWindowManagerAdapter() }
    single<LayoutParamsProvider> { LayoutParamsProvider() }
    single<ISonarRepository> { SonarRepository() }
    single<ISoundRepository> { SoundRepository() }
    single<IExtSurroundViewRepository> { SurroundViewRepository() }
    single<IApaRepository> { ApaRepository() }

    factory<ISonarRouting> { SonarRoutingBridge() }
    factory<ISurroundRouting> { SurroundRoutingBridge() }
    factory<IAutoParkRouting> { AutoParkRoutingBridge() }
    factory<PlatformRoutingBridge> { PlatformRoutingBridge() }
    factory<RouteListenerFactory> { RouteListenerFactory() }
    factory<ICameraConnectionManager> { CameraConnectionManager(get()) }
    factory<ICameraConnectionPolicy> { CameraConnectionPolicy() }

    single<IDisplayManager>(createdAtStart = true) { DisplayManager(get()) }
    single<Router> { Router() }
    single<CameraManager>(createdAtStart = true) { CameraManager() }
    single<EvsSurfaceTexture> { EvsSurfaceTexture() }
    single<INavigationRoute> { get<Router>() }
    single<IOverlayRequest> { get<Router>() }
    single<IDisplayServiceDelegate> { ServiceConnectionDelegate() }
    single<PursuitManager> { PursuitManager() }
    single<PursuitHelper> { PursuitHelper() }
    single<ShadowLauncher> { ShadowLauncher(get()) }
    single<UserProcess> { UserProcess(get()) }

    factory<ScreenOverlayManager> {
        ScreenOverlayManager(
            get()
        )
    }
    factory<SurroundDialogOverlayManager> {
        SurroundDialogOverlayManager(
            get()
        )
    }
    factory<ParkingDialogOverlayManager> {
        ParkingDialogOverlayManager(
            get()
        )
    }
    factory<AllianceCarWindowOverlay.LayoutParams> {
        AllianceCarWindowOverlay.LayoutParams(0, 0, 0, 0, 0, 0, 0, 0, 0)
    }

    single<UserManager> {
        (get() as Context).getSystemService(Application.USER_SERVICE) as UserManager
    }

    single<TrailerNotifier> { TrailerNotifier(get()) }

    factory { SurroundMapper() }
    factory { AvmMapper() }
    factory { MvcMapper() }
    factory { ApaMapper() }
    factory { SurroundWarningMapper() }
    factory { ApaViewModelMapper() }
    factory { CameraIndicationMapper() }
    factory { HfpWarningViewModel() }
    factory { FapkWarningViewModel() }
    factory { ManeuverButtonMapper() }

    factory { PolicyLoader(get(), get(), get()) }
    single<IPolicy> { get<PolicyLoader>().loadPolicy() }

    viewModel<AvmStateViewModelBase> { AvmStateViewModel(get()) }
    viewModel<MvcStateViewModelBase> { MvcStateViewModel(get()) }
    viewModel<RvcStateViewModelBase> { RvcStateViewModel(get()) }
    viewModel<SonarStateViewModelBase> { SonarStateViewModel(get()) }
    viewModel<SonarSettingsViewModelBase> { SonarSettingsViewModel(get()) }
    viewModel<SonarMuteStateViewModelBase> { SonarMuteStateViewModel(get()) }
    viewModel<MainSettingsViewModelBase> { MainSettingsViewModel(get()) }
    viewModel<SoundSettingsViewModelBase> { SoundSettingsViewModel(get()) }
    viewModel<SonarAlertsViewModelBase> { SonarAlertsViewModel(get()) }
    viewModel<CameraSettingsViewModelBase> { CameraSettingsViewModel(get()) }
    viewModel<ExtCameraViewModelBase> { CameraViewModel(get()) }
    viewModel<HfpScanningViewModelBase> { HfpScanningViewModel(get()) }
    viewModel<HfpParkoutViewModelBase> { HfpParkoutViewModel(get()) }
    viewModel<HfpGuidanceViewModelBase> { HfpGuidanceViewModel(get()) }
    viewModel<HfpGuidanceVehicleCenterBackViewModelBase> {
        HfpGuidanceVehicleCenterBackViewModel(get())
    }
    viewModel<HfpGuidanceVehicleCenterCutViewModelBase> {
        HfpGuidanceVehicleCenterCutViewModel(get())
    }
    viewModel<HfpGuidanceVehicleCenterFrontViewModelBase> {
        HfpGuidanceVehicleCenterFrontViewModel(get())
    }
    viewModel<HfpGuidanceVehicleCenterViewModelBase> {
        HfpGuidanceVehicleCenterViewModel(get())
    }
    viewModel<HfpParkoutVehicleCenterViewModelBase> {
        HfpParkoutVehicleCenterViewModel(get())
    }
    viewModel<ApaSettingsViewModelBase> { ApaSettingsViewModel(get()) }
    viewModel<FapkViewModelBase> { FapkViewModel(get()) }
    viewModel<TrailerViewModelBase> { TrailerViewModel(get()) }
    viewModel<ApaWarningViewModelBase> { ApaWarningViewModel(get()) }
    viewModel<SurroundWarningStateViewModelBase> { SurroundWarningStateViewModel(get()) }
    viewModel<SonarFullViewModelBase> { SonarFullViewModel(get()) }
    viewModel<ShadowFullscreenViewModel> { ShadowFullscreenViewModel() }
    viewModel<FapkSettingsViewModel> { FapkSettingsViewModel() }
}