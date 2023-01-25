package com.renault.parkassist

import alliance.car.sonar.AllianceCarSonarManager
import alliance.car.sonar.AllianceCarSonarManager.*
import com.renault.parkassist.activity.OverlayActivityTest
import com.renault.parkassist.koin.ParkAssistHwConfig
import com.renault.parkassist.repository.surroundview.View
import com.renault.parkassist.repository.surroundview.View.REAR_VIEW
import com.renault.parkassist.utils.EspressoTestUtils
import com.renault.parkassist.utils.EspressoTestUtils.assertViewHasEffectiveVisibility
import com.renault.parkassist.utils.EspressoTestUtils.assertViewHasNoEffectiveVisibility
import com.renault.parkassist.utils.EspressoTestUtils.assertViewIsCompletelyDisplayed
import com.renault.parkassist.utils.EspressoTestUtils.assertViewIsNotDisplayed
import org.junit.After
import org.junit.Before
import org.junit.Test

class AlertsRvcTest : OverlayActivityTest() {

    @Before
    fun setup() {
        setVehicleConfiguration(ParkAssistHwConfig.RVC)
        launchFullScreen()
        navigateFullscreen(R.id.rvcFragment)
    }

    @After
    fun tearDown() {
        displayCameraView()
    }

    @Test
    fun should_not_display_raeb_when_no_alert() { // ktlint-disable max-line-length
        // GIVEN RAEB is supported
        sonarRepository.raebAlertEnabled.postValue(true)
        // AND rear gear is engaged
        displayCameraView(REAR_VIEW)
        // WHEN RAEB alert received
        sonarRepository.raebAlertState.postValue(RAEB_NO_ALERT)
        // THEN RAEB is displayed
        assertViewIsNotDisplayed(R.id.raeb_left)
    }

    @Test
    fun should_display_raeb_when_alert_received() { // ktlint-disable max-line-length
        // GIVEN RAEB is supported
        sonarRepository.raebAlertEnabled.postValue(true)
        // AND rear gear is engaged
        displayCameraView(REAR_VIEW)
        // WHEN RAEB alert received
        sonarRepository.raebAlertState.postValue(RAEB_ALERT_1)
        // THEN RVC RAEB is displayed
        assertViewIsCompletelyDisplayed(R.id.raeb_left)

        // WHEN RAEB alert received
        sonarRepository.raebAlertState.postValue(RAEB_ALERT_2)
        // THEN RAEB is displayed
        assertViewIsCompletelyDisplayed(R.id.raeb_left)

        // WHEN RAEB alert received
        sonarRepository.raebAlertState.postValue(RAEB_NOT_OPERATIONAL)
        // THEN RAEB is not displayed
        assertViewIsNotDisplayed(R.id.raeb_left)
    }

    @Test
    fun should_display_raeb_off_when_raeb_disabled() { // ktlint-disable max-line-length
        sonarRepository.raebFeaturePresent = true
        // GIVEN RAEB is disabled
        sonarRepository.raebAlertEnabled.postValue(false)

        // AND rear gear is engaged
        displayCameraView(REAR_VIEW)

        // THEN RAEB OFF is displayed
        assertViewHasEffectiveVisibility(R.id.elt_raeb_off)
        EspressoTestUtils.assertViewHasNoEffectiveVisibility(R.id.raeb_left)

        // WHEN RAEB alert received
        sonarRepository.raebAlertState.postValue(AllianceCarSonarManager.RAEB_ALERT_1)
        // THEN RAEB OFF is displayed
        assertViewHasEffectiveVisibility(R.id.elt_raeb_off)
        EspressoTestUtils.assertViewHasNoEffectiveVisibility(R.id.raeb_left)

        // WHEN reab alert received
        sonarRepository.raebAlertState.postValue(AllianceCarSonarManager.RAEB_ALERT_2)
        // THEN RAEB OFF is displayed
        assertViewHasEffectiveVisibility(R.id.elt_raeb_off)
        EspressoTestUtils.assertViewHasNoEffectiveVisibility(R.id.raeb_left)

        // WHEN reab alert received
        sonarRepository.raebAlertState.postValue(AllianceCarSonarManager.RAEB_NOT_OPERATIONAL)
        // THEN RAEB OFF is displayed
        assertViewHasEffectiveVisibility(R.id.elt_raeb_off)
        EspressoTestUtils.assertViewHasNoEffectiveVisibility(R.id.raeb_left)
    }

    @Test
    fun should_not_display_raeb_when_user_deactivates_raeb_and_raeb_alert() { // ktlint-disable max-line-length
        // GIVEN RAEB is supported
        sonarRepository.raebFeaturePresent = true
        // AND rear gear is engaged
        displayCameraView(REAR_VIEW)
        // WHEN RAEB alert received
        sonarRepository.raebAlertState.postValue(RAEB_ALERT_1)
        // AND user deactivates RAEB
        sonarRepository.raebAlertEnabled.postValue(false)
        // THEN RAEB is NOT displayed
        assertViewIsNotDisplayed(R.id.raeb_left)
    }

    @Test
    fun should_display_left_rcta_when_obstacle_detected_from_the_left() { // ktlint-disable max-line-length
        // GIVEN RCTA is supported
        sonarRepository.rctaFeaturePresent = true
        // AND rear gear is engaged
        displayCameraView(REAR_VIEW)
        // WHEN coming obstacle detected on the left side
        // FIXME: two different alert levels to handle
        sonarRepository.collisionAlertSide.postValue(SIDE_LEFT_ALERT)
        sonarRepository.collisionAlertLevel.postValue(LEVEL_HIGH_COLLISION_RISK)
        // AND RCTA is enabled
        sonarRepository.collisionAlertEnabled.postValue(true)
        // THEN RVC left RCTA is displayed
        assertViewIsCompletelyDisplayed(R.id.rcta_left)
    }

    @Test
    fun should_display_right_rcta_when_obstacle_detected_from_the_right() { // ktlint-disable max-line-length
        // GIVEN RCTA is supported
        sonarRepository.rctaFeaturePresent = true
        // AND rear gear is engaged
        displayCameraView(REAR_VIEW)
        // WHEN coming obstacle detected on the right side
        // FIXME: two different alert levels to handle
        sonarRepository.collisionAlertSide.postValue(SIDE_RIGHT_ALERT)
        sonarRepository.collisionAlertLevel.postValue(LEVEL_HIGH_COLLISION_RISK)
        // AND RCTA is enabled
        sonarRepository.collisionAlertEnabled.postValue(true)
        // THEN right RCTA is displayed
        assertViewIsCompletelyDisplayed(R.id.rcta_right)
    }

    @Test
    fun should_display_left_right_rcta_when_obstacle_detected_from_both_sides() { // ktlint-disable max-line-length
        // GIVEN RCTA is supported
        sonarRepository.rctaFeaturePresent = true
        // AND rear gear is engaged
        displayCameraView(REAR_VIEW)
        // WHEN coming obstacle detected on the left and right side
        // FIXME: two different alert levels to handle
        sonarRepository.collisionAlertSide.postValue(SIDE_LEFT_RIGHT_ALERT)
        sonarRepository.collisionAlertLevel.postValue(LEVEL_HIGH_COLLISION_RISK)
        // AND RCTA is enabled
        sonarRepository.collisionAlertEnabled.postValue(true)
        // THEN RVC left and right RCTA are displayed
        assertViewIsCompletelyDisplayed(R.id.rcta_right)
        assertViewIsCompletelyDisplayed(R.id.rcta_left)
    }

    @Test
    fun should_not_display_left_right_rcta_when_user_deactivates_rcta_and_obstacle_detected_from_both_sides() { // ktlint-disable max-line-length
        // GIVEN RCTA is supported
        sonarRepository.rctaFeaturePresent = true
        // AND rear gear is engaged
        displayCameraView(REAR_VIEW)
        // WHEN coming obstacle detected on the right side
        sonarRepository.collisionAlertSide.postValue(SIDE_LEFT_RIGHT_ALERT)
        sonarRepository.collisionAlertLevel.postValue(LEVEL_HIGH_COLLISION_RISK)
        // AND user deactivates RCTA
        sonarRepository.collisionAlertEnabled.postValue(false)
        // THEN left and right RCTA are NOT displayed
        assertViewIsNotDisplayed(R.id.rcta_right)
        assertViewIsNotDisplayed(R.id.rcta_left)
    }

    @Test
    fun should_not_display_raeb_when_no_alert_in_camera_settings() { // ktlint-disable max-line-length
        // GIVEN RAEB is supported
        sonarRepository.raebAlertEnabled.postValue(true)
        // AND rear gear is engaged
        displayCameraView(REAR_VIEW)
        // AND user goes to camera settings
        gotoCameraSettings()
        // WHEN RAEB alert received
        sonarRepository.raebAlertState.postValue(RAEB_NO_ALERT)
        // THEN RAEB is displayed
        assertViewIsNotDisplayed(R.id.raeb_left)
    }

    @Test
    fun should_display_raeb_when_alert_received_in_camera_settings() { // ktlint-disable max-line-length
        // GIVEN RAEB is supported
        sonarRepository.raebAlertEnabled.postValue(true)
        // AND rear gear is engaged
        displayCameraView(REAR_VIEW)
        // AND user goes to camera settings
        gotoCameraSettings()
        // WHEN RAEB alert received
        sonarRepository.raebAlertState.postValue(RAEB_ALERT_1)
        // AND RAEB is enabled
        sonarRepository.raebAlertEnabled.postValue(true)
        // THEN RVC RAEB is displayed
        assertViewHasEffectiveVisibility(
            R.id.raeb_left,
            R.id.camera_sonar_alerts_fragment
        )

        // WHEN RAEB alert received
        sonarRepository.raebAlertState.postValue(RAEB_ALERT_2)
        // THEN RAEB is displayed
        assertViewHasEffectiveVisibility(
            R.id.raeb_left,
            R.id.camera_sonar_alerts_fragment
        )

        // WHEN RAEB alert received
        sonarRepository.raebAlertState.postValue(RAEB_NOT_OPERATIONAL)
        // THEN RAEB is displayed
        assertViewHasNoEffectiveVisibility(
            R.id.raeb_left,
            R.id.camera_sonar_alerts_fragment
        )
    }

    @Test
    fun should_not_display_raeb_when_user_deactivates_raeb_and_raeb_alert_in_camera_settings() { // ktlint-disable max-line-length
        // GIVEN RAEB is supported
        sonarRepository.raebFeaturePresent = true
        // AND rear gear is engaged
        displayCameraView(REAR_VIEW)
        // AND user goes to camera settings
        gotoCameraSettings()
        // WHEN RAEB alert received
        sonarRepository.raebAlertState.postValue(RAEB_ALERT_1)
        // AND user deactivates RAEB
        sonarRepository.raebAlertEnabled.postValue(false)
        // THEN RAEB is NOT displayed
        assertViewIsNotDisplayed(
            R.id.raeb_left,
            R.id.camera_sonar_alerts_fragment
        )
    }

    @Test
    fun should_display_left_rcta_when_obstacle_detected_from_the_left_in_camera_settings() { // ktlint-disable max-line-length
        // GIVEN RCTA is supported
        sonarRepository.rctaFeaturePresent = true
        // AND rear gear is engaged
        displayCameraView(REAR_VIEW)
        // AND user goes to camera settings
        gotoCameraSettings()
        // WHEN coming obstacle detected on the left side
        // FIXME: two different alert levels to handle
        sonarRepository.collisionAlertSide.postValue(SIDE_LEFT_ALERT)
        sonarRepository.collisionAlertLevel.postValue(LEVEL_HIGH_COLLISION_RISK)
        // AND RCTA is enabled
        sonarRepository.collisionAlertEnabled.postValue(true)
        // THEN RVC left RCTA is displayed
        assertViewHasEffectiveVisibility(
            R.id.rcta_left,
            R.id.camera_sonar_alerts_fragment
        )
    }

    @Test
    fun should_display_right_rcta_when_obstacle_detected_from_the_right_in_camera_settings() { // ktlint-disable max-line-length
        // GIVEN RCTA is supported
        sonarRepository.rctaFeaturePresent = true
        // AND rear gear is engaged
        displayCameraView(REAR_VIEW)
        // AND user goes to camera settings
        gotoCameraSettings()
        // WHEN coming obstacle detected on the right side
        // FIXME: two different alert levels to handle
        sonarRepository.collisionAlertSide.postValue(SIDE_RIGHT_ALERT)
        sonarRepository.collisionAlertLevel.postValue(LEVEL_HIGH_COLLISION_RISK)
        // AND RCTA is enabled
        sonarRepository.collisionAlertEnabled.postValue(true)
        // THEN right RCTA is displayed
        assertViewHasEffectiveVisibility(
            R.id.rcta_right,
            R.id.camera_sonar_alerts_fragment
        )
    }

    @Test
    fun should_display_left_right_rcta_when_obstacle_detected_from_both_sides_in_camera_settings() { // ktlint-disable max-line-length
        // GIVEN RCTA is supported
        sonarRepository.rctaFeaturePresent = true
        // AND rear gear is engaged
        displayCameraView(REAR_VIEW)
        // AND user goes to camera settings
        gotoCameraSettings()
        // WHEN coming obstacle detected on the right side
        // FIXME: two different alert levels to handle
        sonarRepository.collisionAlertSide.postValue(SIDE_LEFT_RIGHT_ALERT)
        sonarRepository.collisionAlertLevel.postValue(LEVEL_HIGH_COLLISION_RISK)
        // AND RCTA is enabled
        sonarRepository.collisionAlertEnabled.postValue(true)
        // THEN RVC left and right RCTA are displayed
        assertViewHasEffectiveVisibility(
            R.id.rcta_right,
            R.id.camera_sonar_alerts_fragment
        )
        assertViewHasEffectiveVisibility(
            R.id.rcta_left,
            R.id.camera_sonar_alerts_fragment
        )
    }

    @Test
    fun should_display_left_right_rcta_when_obstacle_detected_from_both_sides_and_raeb_when_alert_received_in_camera_settings() { // ktlint-disable max-line-length
        // GIVEN RCTA is supported
        sonarRepository.rctaFeaturePresent = true
        // AND RAEB is supported
        sonarRepository.raebAlertEnabled.postValue(true)
        // AND rear gear is engaged
        displayCameraView(REAR_VIEW)
        // AND user goes to camera settings
        gotoCameraSettings()
        // WHEN coming obstacle detected on the right side
        // FIXME: two different alert levels to handle
        sonarRepository.collisionAlertSide.postValue(SIDE_LEFT_RIGHT_ALERT)
        sonarRepository.collisionAlertLevel.postValue(LEVEL_HIGH_COLLISION_RISK)
        // AND RCTA is enabled
        sonarRepository.collisionAlertEnabled.postValue(true)
        // AND RAEB alert received
        sonarRepository.raebAlertState.postValue(RAEB_ALERT_1)
        // AND RAEB is enabled
        sonarRepository.raebAlertEnabled.postValue(true)
        // THEN RVC left and right RCTA are displayed
        assertViewHasEffectiveVisibility(
            R.id.rcta_right,
            R.id.camera_sonar_alerts_fragment
        )
        assertViewHasEffectiveVisibility(
            R.id.rcta_left,
            R.id.camera_sonar_alerts_fragment
        )
        // THEN RVC RAEB is displayed
        assertViewHasEffectiveVisibility(
            R.id.raeb_left,
            R.id.camera_sonar_alerts_fragment
        )
    }

    @Test
    fun should_not_display_left_right_rcta_when_user_deactivates_rcta_and_obstacle_detected_from_both_sides_in_camera_settings() { // ktlint-disable max-line-length
        // GIVEN RCTA is supported
        sonarRepository.rctaFeaturePresent = true
        // AND rear gear is engaged
        displayCameraView(REAR_VIEW)
        // AND user goes to camera settings
        gotoCameraSettings()
        // WHEN coming obstacle detected on the right side
        sonarRepository.collisionAlertSide.postValue(SIDE_LEFT_RIGHT_ALERT)
        sonarRepository.collisionAlertLevel.postValue(LEVEL_HIGH_COLLISION_RISK)
        // AND user deactivates RCTA
        sonarRepository.collisionAlertEnabled.postValue(false)
        // THEN left and right RCTA are NOT displayed
        assertViewIsNotDisplayed(
            R.id.rcta_right,
            R.id.camera_sonar_alerts_fragment
        )
        assertViewIsNotDisplayed(
            R.id.rcta_left,
            R.id.camera_sonar_alerts_fragment
        )
    }

    private fun gotoCameraSettings() {
        // WHEN the user clicks on 'settings' icon
        EspressoTestUtils.clickOnView(R.id.toolbar_icon_settings)
        navigateFullscreen(R.id.rvcSettings)
        displayCameraView(View.SETTINGS_REAR_VIEW, ButtonsMode.REGULATORY)
        EspressoTestUtils.waitForView(R.id.elt_camera_view)

        assertViewHasEffectiveVisibility(R.id.camera_settings_camera_fragment)
        assertViewHasEffectiveVisibility(R.id.camera_container)
        assertViewHasEffectiveVisibility(R.id.elt_camera_overlay_container)

        assertViewHasEffectiveVisibility(R.id.camera_ovl_rvc_settings_fragment)
        assertViewHasEffectiveVisibility(R.id.elt_camera_indicator)
        assertViewHasEffectiveVisibility(R.id.camera_sonar_alerts_fragment)
    }
}