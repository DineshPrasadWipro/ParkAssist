package com.renault.parkassist

import alliance.car.sonar.AllianceCarSonarManager.*
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import com.renault.parkassist.activity.OverlayActivityTest
import com.renault.parkassist.koin.ParkAssistHwConfig
import com.renault.parkassist.repository.sonar.DisplayType.FULLSCREEN
import com.renault.parkassist.repository.sonar.DisplayType.NONE
import com.renault.parkassist.repository.surroundview.View
import com.renault.parkassist.utils.EspressoTestUtils.assertViewHasEffectiveVisibility
import com.renault.parkassist.utils.EspressoTestUtils.assertViewHasNoEffectiveVisibility
import com.renault.parkassist.utils.EspressoTestUtils.assertViewIsCompletelyDisplayed
import com.renault.parkassist.utils.EspressoTestUtils.assertViewIsNotDisplayed
import org.junit.After
import org.junit.Before
import org.junit.Test

@LargeTest
class AlertsSonarTest : OverlayActivityTest() {

    @Before
    fun setup() {
        setVehicleConfiguration(ParkAssistHwConfig.RVC)
        launchFullScreen()
        navigateFullscreen(R.id.rvcFragment)
    }

    @After
    fun teardown() {
        sonarRepository.displayRequest.postValue(NONE)
    }

    @Test
    fun should_not_display_raeb_when_no_alert() { // ktlint-disable max-line-length
        displayCameraView(View.REAR_VIEW)
        // GIVEN RAEB is supported
        sonarRepository.raebAlertEnabled.postValue(true)
        // AND rear gear is engaged
        sonarRepository.displayRequest.postValue(FULLSCREEN)
        // WHEN RAEB alert received
        sonarRepository.raebAlertState.postValue(RAEB_NO_ALERT)
        // THEN RAEB is displayed
        assertViewIsNotDisplayed(R.id.raeb_left)
    }

    @Test
    fun should_display_raeb_when_alert_received() { // ktlint-disable max-line-length
        displayCameraView(View.REAR_VIEW)
        // GIVEN RAEB is supported
        sonarRepository.raebAlertEnabled.postValue(true)
        // AND rear gear is engaged
        sonarRepository.displayRequest.postValue(FULLSCREEN)
        // WHEN RAEB alert received
        sonarRepository.raebAlertState.postValue(RAEB_ALERT_1)
        InstrumentationRegistry.getInstrumentation().waitForIdleSync()

        // THEN RAEB is displayed
        assertViewIsCompletelyDisplayed(R.id.raeb_left)
        assertViewHasEffectiveVisibility(R.id.raeb_left)

        // WHEN RAEB alert received
        sonarRepository.raebAlertState.postValue(RAEB_ALERT_2)
        // THEN RAEB is displayed
        assertViewIsCompletelyDisplayed(R.id.raeb_left)
        assertViewHasEffectiveVisibility(R.id.raeb_left)

        // WHEN RAEB alert received
        sonarRepository.raebAlertState.postValue(RAEB_NOT_OPERATIONAL)
        // THEN RAEB is not displayed
        assertViewHasNoEffectiveVisibility(R.id.raeb_left)
    }

    @Test
    fun should_not_display_raeb_not_op_when_raeb_disabled() { // ktlint-disable max-line-length
        displayCameraView(View.REAR_VIEW)
        // GIVEN RAEB is supported
        sonarRepository.raebAlertEnabled.postValue(false)
        // AND rear gear is engaged
        sonarRepository.displayRequest.postValue(FULLSCREEN)
        // WHEN RAEB alert received
        sonarRepository.raebAlertState.postValue(RAEB_NOT_OPERATIONAL)
        // THEN RAEB OFF is displayed
        assertViewHasNoEffectiveVisibility(R.id.raeb_left)
    }

    @Test
    fun should_not_display_raeb_when_user_deactivates_raeb_and_raeb_alert() { // ktlint-disable max-line-length
        displayCameraView(View.REAR_VIEW)
        // GIVEN RAEB is supported
        sonarRepository.raebFeaturePresent = true
        // AND rear gear is engaged
        sonarRepository.displayRequest.postValue(FULLSCREEN)
        // WHEN RAEB alert received
        sonarRepository.raebAlertState.postValue(RAEB_ALERT_1)
        // AND user deactivates RAEB
        sonarRepository.raebAlertEnabled.postValue(false)
        // THEN RAEB is NOT displayed
        assertViewIsNotDisplayed(R.id.raeb_left)
    }

    @Test
    fun should_display_left_rcta_when_obstacle_detected_from_the_left() { // ktlint-disable max-line-length
        displayCameraView(View.REAR_VIEW)
        // GIVEN RCTA is supported
        sonarRepository.rctaFeaturePresent = true
        // AND rear gear is engaged
        sonarRepository.displayRequest.postValue(FULLSCREEN)
        // WHEN coming obstacle detected on the left side
        sonarRepository.collisionAlertSide.postValue(SIDE_LEFT_ALERT)
        sonarRepository.collisionAlertLevel.postValue(LEVEL_HIGH_COLLISION_RISK)
        // AND RCTA is enabled
        sonarRepository.collisionAlertEnabled.postValue(true)
        // THEN left RCTA is displayed
        assertViewIsCompletelyDisplayed(R.id.rcta_left)
    }

    @Test
    fun should_display_right_rcta_when_obstacle_detected_from_the_right() { // ktlint-disable max-line-length
        displayCameraView(View.REAR_VIEW)
        // GIVEN RCTA is supported
        sonarRepository.rctaFeaturePresent = true
        // AND rear gear is engaged
        sonarRepository.displayRequest.postValue(FULLSCREEN)
        // WHEN coming obstacle detected on the right side
        sonarRepository.collisionAlertSide.postValue(SIDE_RIGHT_ALERT)
        sonarRepository.collisionAlertLevel.postValue(LEVEL_HIGH_COLLISION_RISK)
        // AND RCTA is enabled
        sonarRepository.collisionAlertEnabled.postValue(true)
        // THEN right RCTA is displayed
        assertViewIsCompletelyDisplayed(R.id.rcta_right)
    }

    @Test
    fun should_display_left_right_rcta_when_obstacle_detected_from_both_sides() { // ktlint-disable max-line-length
        displayCameraView(View.REAR_VIEW)
        // GIVEN RCTA is supported
        sonarRepository.rctaFeaturePresent = true
        // AND rear gear is engaged
        sonarRepository.displayRequest.postValue(FULLSCREEN)
        // WHEN coming obstacle detected on the right side
        sonarRepository.collisionAlertSide.postValue(SIDE_LEFT_RIGHT_ALERT)
        sonarRepository.collisionAlertLevel.postValue(LEVEL_HIGH_COLLISION_RISK)
        // AND RCTA is enabled
        sonarRepository.collisionAlertEnabled.postValue(true)
        // THEN right RCTA is displayed
        assertViewIsCompletelyDisplayed(R.id.rcta_right)
        assertViewIsCompletelyDisplayed(R.id.rcta_left)
    }

    @Test
    fun should_not_display_left_right_rcta_when_user_deactivates_rcta_and_obstacle_detected_from_both_sides() { // ktlint-disable max-line-length
        displayCameraView(View.REAR_VIEW)
        // GIVEN RCTA is supported
        sonarRepository.rctaFeaturePresent = true
        // AND rear gear is engaged
        sonarRepository.displayRequest.postValue(FULLSCREEN)
        // WHEN coming obstacle detected on the right side
        sonarRepository.collisionAlertSide.postValue(SIDE_LEFT_RIGHT_ALERT)
        sonarRepository.collisionAlertLevel.postValue(LEVEL_HIGH_COLLISION_RISK)
        // AND user deactivates RCTA
        sonarRepository.collisionAlertEnabled.postValue(false)
        // THEN left and right RCTA are NOT displayed
        assertViewIsNotDisplayed(R.id.rcta_right)
        assertViewIsNotDisplayed(R.id.rcta_left)
    }
}