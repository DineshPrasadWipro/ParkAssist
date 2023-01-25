package com.renault.parkassist.koin

import alliance.car.windowoverlay.AllianceCarWindowOverlay
import android.os.UserManager
import com.renault.parkassist.camera.CameraManager
import com.renault.parkassist.camera.EvsSurfaceTexture
import com.renault.parkassist.overlay.*
import com.renault.parkassist.repository.apa.ApaMapper
import com.renault.parkassist.repository.apa.IApaRepository
import com.renault.parkassist.repository.apa.mock.ApaRepositoryMock
import com.renault.parkassist.repository.routing.*
import com.renault.parkassist.repository.settings.ISoundRepository
import com.renault.parkassist.repository.settings.mock.SoundRepositoryMock
import com.renault.parkassist.repository.sonar.ISonarRepository
import com.renault.parkassist.repository.sonar.mock.SonarRepositoryMock
import com.renault.parkassist.repository.surroundview.IExtSurroundViewRepository
import com.renault.parkassist.repository.surroundview.ISurroundViewRepository
import com.renault.parkassist.repository.surroundview.SurroundMapper
import com.renault.parkassist.repository.surroundview.mock.SurroundViewRepositoryMock
import com.renault.parkassist.routing.*
import com.renault.parkassist.routing.mock.ApaRoutingMock
import com.renault.parkassist.routing.mock.SonarRoutingMock
import com.renault.parkassist.routing.mock.SurroundRoutingMock
import com.renault.parkassist.routing.policy.IPolicy
import com.renault.parkassist.routing.policy.PolicyLoader
import com.renault.parkassist.routing.pursuit.PursuitHelper
import com.renault.parkassist.routing.pursuit.PursuitManager
import com.renault.parkassist.routing.pursuit.PursuitViewModel
import com.renault.parkassist.service.ShadowLauncher
import com.renault.parkassist.service.TrailerNotifier
import com.renault.parkassist.surround.ExtSurroundViewRepositoryMock
import com.renault.parkassist.surround.GraphicLayerMock
import com.renault.parkassist.ui.apa.ManeuverButtonMapper
import com.renault.parkassist.viewmodel.apa.*
import com.renault.parkassist.viewmodel.apa.fapk.FapkSettingsViewModel
import com.renault.parkassist.viewmodel.apa.fapk.FapkViewModel
import com.renault.parkassist.viewmodel.apa.fapk.FapkViewModelBase
import com.renault.parkassist.viewmodel.apa.fapk.FapkWarningViewModel
import com.renault.parkassist.viewmodel.apa.hfp.*
import com.renault.parkassist.viewmodel.avm.*
import com.renault.parkassist.viewmodel.camera.*
import com.renault.parkassist.viewmodel.rvc.RvcStateViewModel
import com.renault.parkassist.viewmodel.rvc.RvcStateViewModelBase
import com.renault.parkassist.viewmodel.settings.MainSettingsViewModel
import com.renault.parkassist.viewmodel.settings.MainSettingsViewModelBase
import com.renault.parkassist.viewmodel.sonar.*
import com.renault.parkassist.viewmodel.sound.SoundSettingsViewModel
import com.renault.parkassist.viewmodel.sound.SoundSettingsViewModelBase
import com.renault.parkassist.viewmodel.surround.SurroundWarningMapper
import com.renault.parkassist.viewmodel.surround.SurroundWarningStateViewModel
import com.renault.parkassist.viewmodel.surround.SurroundWarningStateViewModelBase
import com.renault.parkassist.viewmodel.trailer.TrailerViewModel
import com.renault.parkassist.viewmodel.trailer.TrailerViewModelBase
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    single<ISonarRepository> { spyk(SonarRepositoryMock(get())) }
    single<ISoundRepository> { spyk(SoundRepositoryMock(get())) }
    single<ISurroundViewRepository> { spyk(SurroundViewRepositoryMock(get())) }
    single<IExtSurroundViewRepository> { spyk(ExtSurroundViewRepositoryMock(get())) }
    single<IApaRepository> { spyk(ApaRepositoryMock(get())) }
    single<GraphicLayerMock> { GraphicLayerMock() }

    single<ISonarRouting> { SonarRoutingMock() }
    single<ISurroundRouting> { SurroundRoutingMock() }
    single<IAutoParkRouting> { ApaRoutingMock() }
    factory<PlatformRoutingBridge> { PlatformRoutingBridge() }
    single<IDisplayManager> { DisplayManager(get()) }
    factory<RouteListenerFactory> { RouteListenerFactory() }

    factory<Router> { Router() }
    factory<INavigationRoute> { get<Router>() }
    factory<IOverlayRequest> { get<Router>() }
    factory<IDisplayServiceDelegate> { ServiceConnectionDelegate() }
    factory<PursuitManager> { PursuitManager() }
    factory<PursuitHelper> { PursuitHelper() }
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

    factory<UserManager> { mockk<UserManager>().apply {
        every { isSystemUser } returns false
    } }

    single<ShadowLauncher> { ShadowLauncher(get()) }
    factory<AllianceCarWindowOverlay.LayoutParams> {
        mockk()
    }
    single<LayoutParamsProvider> { mockk(relaxed = true) }
    single<CameraManager> { CameraManager() }
    single<EvsSurfaceTexture> { EvsSurfaceTexture() }
    single { TrailerNotifier(get()) }
    factory { SurroundMapper() }
    factory { AvmMapper() }
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
        HfpGuidanceVehicleCenterBackViewModel(get()) }
    viewModel<HfpGuidanceVehicleCenterCutViewModelBase> {
        HfpGuidanceVehicleCenterCutViewModel(get()) }
    viewModel<HfpGuidanceVehicleCenterFrontViewModelBase> {
        HfpGuidanceVehicleCenterFrontViewModel(get()) }
    viewModel<HfpGuidanceVehicleCenterViewModelBase> {
        HfpGuidanceVehicleCenterViewModel(get()) }
    viewModel<HfpParkoutVehicleCenterViewModelBase> {
        HfpParkoutVehicleCenterViewModel(get()) }
    viewModel<ApaSettingsViewModelBase> { ApaSettingsViewModel(get()) }
    viewModel<FapkViewModelBase> { FapkViewModel(get()) }
    viewModel<TrailerViewModelBase> { TrailerViewModel(get()) }
    viewModel<SurroundWarningStateViewModelBase> { SurroundWarningStateViewModel(get()) }
    viewModel<ApaWarningViewModelBase> { ApaWarningViewModel(get()) }
    viewModel<SonarFullViewModelBase> { SonarFullViewModel(get()) }
    viewModel<PursuitViewModel> { PursuitViewModel() }
    viewModel<FapkSettingsViewModel> { FapkSettingsViewModel() }
}