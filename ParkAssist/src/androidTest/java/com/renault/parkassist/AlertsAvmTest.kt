package com.renault.parkassist

import alliance.car.sonar.AllianceCarSonarManager
import alliance.car.sonar.AllianceCarSonarManager.LEVEL_HIGH_COLLISION_RISK
import alliance.car.sonar.AllianceCarSonarManager.RAEB_ALERT_1
import alliance.car.sonar.AllianceCarSonarManager.RAEB_ALERT_2
import alliance.car.sonar.AllianceCarSonarManager.RAEB_NOT_OPERATIONAL
import alliance.car.sonar.AllianceCarSonarManager.RAEB_NO_ALERT
import alliance.car.sonar.AllianceCarSonarManager.SIDE_LEFT_ALERT
import alliance.car.sonar.AllianceCarSonarManager.SIDE_LEFT_RIGHT_ALERT
import alliance.car.sonar.AllianceCarSonarManager.SIDE_RIGHT_ALERT
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.matcher.ViewMatchers.withTagValue
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.renault.parkassist.activity.OverlayActivityTest
import com.renault.parkassist.koin.KoinTestBase.ButtonsMode.ALL
import com.renault.parkassist.koin.ParkAssistHwConfig
import com.renault.parkassist.repository.surroundview.View.*
import com.renault.parkassist.ui.avm.AvmFragment
import com.renault.parkassist.ui.avm.AvmFragment.Companion.panoramicViewButtonTag
import com.renault.parkassist.ui.avm.AvmFragment.Companion.standardViewButtonTag
import com.renault.parkassist.utils.EspressoTestUtils
import com.renault.parkassist.utils.EspressoTestUtils.assertViewIsCompletelyDisplayed
import com.renault.parkassist.utils.EspressoTestUtils.assertViewIsNotDisplayed
import com.renault.parkassist.utils.EspressoTestUtils.clickOnView
import com.renault.parkassist.utils.EspressoTestUtils.setCheck
import org.hamcrest.CoreMatchers.`is`
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class AlertsAvmTest : OverlayActivityTest() {

    @Before
    fun setup() {
        setVehicleConfiguration(ParkAssistHwConfig.AVM)
        launchFullScreen()
        navigateFullscreen(R.id.avmFragment)
    }

    @After
    fun tearDown() {
        // displayCameraView()
    }

    @Test
    fun should_not_display_raeb_when_no_alert() { // ktlint-disable max-line-length
        // GIVEN RAEB is supported
        sonarRepository.raebAlertEnabled.postValue(true)
        // AND the rear gear is engaged
        displayCameraView(REAR_VIEW, ALL)
        // AND current view is Standard view
        onView(withTagValue(`is`(standardViewButtonTag)))
            .perform(setCheck(true))
        // WHEN reab alert received
        sonarRepository.raebAlertState.postValue(RAEB_NO_ALERT)
        // THEN RAEB is displayed
        assertViewIsNotDisplayed(R.id.raeb_left)
    }

    @Test
    fun should_display_raeb_when_alert_received() { // ktlint-disable max-line-length
        // GIVEN RAEB is supported
        sonarRepository.raebAlertEnabled.postValue(true)
        // AND the rear gear is engaged
        displayCameraView(REAR_VIEW, ALL)
        // AND current view is Standard view
        onView(withTagValue(`is`(standardViewButtonTag)))
            .perform(setCheck(true))
        // WHEN RAEB alert received
        sonarRepository.raebAlertState.postValue(RAEB_ALERT_1)
        // THEN RAEB is displayed
        assertViewIsCompletelyDisplayed(R.id.raeb_left)

        // WHEN reab alert received
        sonarRepository.raebAlertState.postValue(RAEB_ALERT_2)
        // THEN RAEB is displayed
        assertViewIsCompletelyDisplayed(R.id.raeb_left)

        // WHEN reab alert received
        sonarRepository.raebAlertState.postValue(RAEB_NOT_OPERATIONAL)
        // THEN RAEB is displayed
        assertViewIsNotDisplayed(R.id.raeb_left)
    }

    @Test
    fun should_display_raeb_off_when_raeb_disabled() { // ktlint-disable max-line-length
        sonarRepository.raebFeaturePresent = true
        // GIVEN RAEB is disabled
        sonarRepository.raebAlertEnabled.postValue(false)

        // AND the rear gear is engaged
        displayCameraView(REAR_VIEW, ALL)

        // AND current view is Standard view
        onView(withTagValue(`is`(AvmFragment.standardViewButtonTag)))
            .perform(setCheck(true))

        // THEN RAEB OFF is displayed
        EspressoTestUtils.assertViewHasEffectiveVisibility(R.id.elt_raeb_off)
        EspressoTestUtils.assertViewHasNoEffectiveVisibility(R.id.raeb_left)

        // WHEN RAEB alert received
        sonarRepository.raebAlertState.postValue(AllianceCarSonarManager.RAEB_ALERT_1)
        // THEN RAEB OFF is displayed
        EspressoTestUtils.assertViewHasEffectiveVisibility(R.id.elt_raeb_off)
        EspressoTestUtils.assertViewHasNoEffectiveVisibility(R.id.raeb_left)

        // WHEN reab alert received
        sonarRepository.raebAlertState.postValue(AllianceCarSonarManager.RAEB_ALERT_2)
        // THEN RAEB OFF is displayed
        EspressoTestUtils.assertViewHasEffectiveVisibility(R.id.elt_raeb_off)
        EspressoTestUtils.assertViewHasNoEffectiveVisibility(R.id.raeb_left)

        // WHEN RAEB alert not operational
        sonarRepository.raebAlertState.postValue(AllianceCarSonarManager.RAEB_NOT_OPERATIONAL)
        // THEN RAEB OFF is displayed
        EspressoTestUtils.assertViewHasEffectiveVisibility(R.id.elt_raeb_off)
        EspressoTestUtils.assertViewHasNoEffectiveVisibility(R.id.raeb_left)

        // AND current view is Panoramic view
        onView(withTagValue(`is`(panoramicViewButtonTag)))
            .perform(setCheck(true))

        // THEN RAEB OFF is displayed
        EspressoTestUtils.assertViewHasEffectiveVisibility(R.id.elt_raeb_off)
        EspressoTestUtils.assertViewHasNoEffectiveVisibility(R.id.raeb_left)

        // WHEN RAEB alert received
        sonarRepository.raebAlertState.postValue(AllianceCarSonarManager.RAEB_ALERT_1)
        // THEN RAEB OFF is displayed
        EspressoTestUtils.assertViewHasEffectiveVisibility(R.id.elt_raeb_off)
        EspressoTestUtils.assertViewHasNoEffectiveVisibility(R.id.raeb_left)

        // WHEN reab alert received
        sonarRepository.raebAlertState.postValue(AllianceCarSonarManager.RAEB_ALERT_2)
        // THEN RAEB OFF is displayed
        EspressoTestUtils.assertViewHasEffectiveVisibility(R.id.elt_raeb_off)
        EspressoTestUtils.assertViewHasNoEffectiveVisibility(R.id.raeb_left)

        // WHEN RAEB alert not operational
        sonarRepository.raebAlertState.postValue(AllianceCarSonarManager.RAEB_NOT_OPERATIONAL)
        // THEN RAEB OFF is displayed
        EspressoTestUtils.assertViewHasEffectiveVisibility(R.id.elt_raeb_off)
        EspressoTestUtils.assertViewHasNoEffectiveVisibility(R.id.raeb_left)

        // AND current view is Settings view
        clickOnView(R.id.toolbar_icon_settings)

        // THEN RAEB OFF is displayed
        EspressoTestUtils.assertViewHasEffectiveVisibility(R.id.elt_raeb_off)
        EspressoTestUtils.assertViewHasNoEffectiveVisibility(R.id.raeb_left)

        // WHEN RAEB alert received
        sonarRepository.raebAlertState.postValue(AllianceCarSonarManager.RAEB_ALERT_1)
        // THEN RAEB OFF is displayed
        EspressoTestUtils.assertViewHasEffectiveVisibility(R.id.elt_raeb_off)
        EspressoTestUtils.assertViewHasNoEffectiveVisibility(R.id.raeb_left)

        // WHEN reab alert received
        sonarRepository.raebAlertState.postValue(AllianceCarSonarManager.RAEB_ALERT_2)
        // THEN RAEB OFF is displayed
        EspressoTestUtils.assertViewHasEffectiveVisibility(R.id.elt_raeb_off)
        EspressoTestUtils.assertViewHasNoEffectiveVisibility(R.id.raeb_left)

        // GIVEN RAEB is enabled
        sonarRepository.raebAlertEnabled.postValue(true)

        // WHEN RAEB alert not operational
        sonarRepository.raebAlertState.postValue(AllianceCarSonarManager.RAEB_NOT_OPERATIONAL)
        // THEN RAEB OFF is displayed
        EspressoTestUtils.assertViewHasEffectiveVisibility(R.id.elt_raeb_off)
        EspressoTestUtils.assertViewHasNoEffectiveVisibility(R.id.raeb_left)
    }

    @Test
    fun should_not_display_raeb_when_user_deactivates_raeb_and_raeb_alert() { // ktlint-disable max-line-length
        // GIVEN RAEB is supported
        sonarRepository.raebFeaturePresent = true
        // AND the rear gear is engaged
        displayCameraView(REAR_VIEW, ALL)
        // AND current view is Standard view
        onView(withTagValue(`is`(standardViewButtonTag)))
            .perform(setCheck(true))
        // WHEN RAEB alert received
        sonarRepository.raebAlertState.postValue(RAEB_ALERT_1)
        // AND user deactivates RAEB
        sonarRepository.raebAlertEnabled.postValue(false)
        // THEN RAEB is NOT displayed
        assertViewIsNotDisplayed(R.id.raeb_left)
    }

    @Test
    fun should_display_only_left_camera_rcta_when_obstacle_detected_from_the_left_in_panoramic_mode() { // ktlint-disable max-line-length
        // GIVEN RCTA is supported
        sonarRepository.rctaFeaturePresent = true
        // AND activated by user
        sonarRepository.collisionAlertEnabled.postValue(true)
        // AND the rear gear is engaged
        displayCameraView(REAR_VIEW, ALL)
        // AND current view is Panoramic
        onView(withTagValue(`is`(panoramicViewButtonTag)))
            .perform(setCheck(true))
        displayCameraView(PANORAMIC_REAR_VIEW, ALL)
        // WHEN a coming vehicle or obstacle is detected on the left
        sonarRepository.collisionAlertSide.postValue(SIDE_LEFT_ALERT)
        sonarRepository.collisionAlertLevel.postValue(LEVEL_HIGH_COLLISION_RISK)

        // THEN the Left Rear Cross Trafic Alert is displayed in AVM camera view
        assertViewIsCompletelyDisplayed(R.id.rcta_left)
    }

    @Test
    fun should_display_only_right_camera_rcta_when_obstacle_detected_from_the_right_in_panoramic_mode() { // ktlint-disable max-line-length
        // GIVEN RCTA is supported
        sonarRepository.rctaFeaturePresent = true
        // AND activated by user
        sonarRepository.collisionAlertEnabled.postValue(true)
        // AND the rear gear is engaged
        displayCameraView(REAR_VIEW, ALL)
        // AND current view is Panoramic
        onView(withTagValue(`is`(panoramicViewButtonTag)))
            .perform(setCheck(true))
        displayCameraView(PANORAMIC_REAR_VIEW, ALL)
        // WHEN a coming vehicle or obstacle is detected on the right
        sonarRepository.collisionAlertSide.postValue(SIDE_RIGHT_ALERT)
        sonarRepository.collisionAlertLevel.postValue(LEVEL_HIGH_COLLISION_RISK)

        // THEN the Right Rear Cross Trafic Alert is displayed in AVM camera view
        assertViewIsCompletelyDisplayed(R.id.rcta_right)
    }

    @Test
    fun should_display_only_left_and_right_camera_rcta_when_obstacle_detected_from_the_left_and_the_right_in_panoramic_mode() { // ktlint-disable max-line-length
        // GIVEN RCTA is supported
        sonarRepository.rctaFeaturePresent = true
        // AND activated by user
        sonarRepository.collisionAlertEnabled.postValue(true)
        // AND the rear gear is engaged
        displayCameraView(REAR_VIEW, ALL)
        // AND current view is Panoramic
        onView(withTagValue(`is`(standardViewButtonTag)))
            .perform(setCheck(true))
        displayCameraView(PANORAMIC_REAR_VIEW, ALL)
        // WHEN a coming vehicle or obstacle is detected on the left and right
        sonarRepository.collisionAlertSide.postValue(SIDE_LEFT_RIGHT_ALERT)
        sonarRepository.collisionAlertLevel.postValue(LEVEL_HIGH_COLLISION_RISK)

        // THEN the Left and Right Rear Cross Trafic Alert is displayed in AVM camera view
        assertViewIsCompletelyDisplayed(R.id.rcta_right)
        assertViewIsCompletelyDisplayed(R.id.rcta_left)
    }

    @Test
    fun should_display_only_left_camera_rcta_when_obstacle_detected_from_the_left_in_panoramic_rear_view() { // ktlint-disable max-line-length
        // GIVEN RCTA is supported
        sonarRepository.rctaFeaturePresent = true
        // AND activated by user
        sonarRepository.collisionAlertEnabled.postValue(true)
        // AND the rear gear is engaged
        displayCameraView(REAR_VIEW, ALL)
        // AND current view is Panoramic rear
        onView(withTagValue(`is`(standardViewButtonTag)))
            .perform(setCheck(true))
        displayCameraView(PANORAMIC_REAR_VIEW, ALL)
        // WHEN a coming vehicle or obstacle is detected on the left
        sonarRepository.collisionAlertSide.postValue(SIDE_LEFT_ALERT)
        sonarRepository.collisionAlertLevel.postValue(LEVEL_HIGH_COLLISION_RISK)

        // THEN the Left Rear Cross Trafic Alert is displayed in AVM camera
        assertViewIsCompletelyDisplayed(R.id.rcta_left)
    }

    @Test
    fun should_display_only_right_camera_rcta_when_obstacle_detected_from_the_right_in_panoramic_rear_view() { // ktlint-disable max-line-length
        // GIVEN RCTA is supported
        sonarRepository.rctaFeaturePresent = true
        // AND activated by user
        sonarRepository.collisionAlertEnabled.postValue(true)
        // AND the rear gear is engaged
        displayCameraView(REAR_VIEW, ALL)
        // AND current view is panoramic rear
        onView(withTagValue(`is`(standardViewButtonTag)))
            .perform(setCheck(true))
        displayCameraView(PANORAMIC_REAR_VIEW, ALL)
        // WHEN a coming vehicle or obstacle is detected on the right
        sonarRepository.collisionAlertSide.postValue(SIDE_RIGHT_ALERT)
        sonarRepository.collisionAlertLevel.postValue(LEVEL_HIGH_COLLISION_RISK)

        // THEN the Right Rear Cross Trafic Alert is displayed in AVM sonar view
        assertViewIsCompletelyDisplayed(R.id.rcta_right)
    }

    @Test
    fun should_display_only_left_and_right_camera_rcta_when_obstacle_detected_from_the_left_and_the_right_in_panoramic_rear_view() { // ktlint-disable max-line-length
        // GIVEN RCTA is supported
        sonarRepository.rctaFeaturePresent = true
        // AND activated by user
        sonarRepository.collisionAlertEnabled.postValue(true)
        // AND the rear gear is engaged
        displayCameraView(REAR_VIEW, ALL)
        // AND current view is Panoramic
        onView(withTagValue(`is`(standardViewButtonTag)))
            .perform(setCheck(true))
        displayCameraView(PANORAMIC_REAR_VIEW, ALL)
        // WHEN a coming vehicle or obstacle is detected on the left and right
        sonarRepository.collisionAlertSide.postValue(SIDE_LEFT_RIGHT_ALERT)
        sonarRepository.collisionAlertLevel.postValue(LEVEL_HIGH_COLLISION_RISK)

        // THEN the Left and Right Rear Cross Trafic Alert is displayed in AVM camera view
        assertViewIsCompletelyDisplayed(R.id.rcta_right)
        assertViewIsCompletelyDisplayed(R.id.rcta_left)
    }

    @Test
    fun should_display_only_left_rcta_when_obstacle_detected_from_the_left_in_panoramic_rear_view() { // ktlint-disable max-line-length
        // GIVEN RCTA is supported
        sonarRepository.rctaFeaturePresent = true
        // AND activated by user
        sonarRepository.collisionAlertEnabled.postValue(true)
        // AND the rear gear is engaged
        displayCameraView(REAR_VIEW, ALL)
        // AND current view is Panoramic rear
        onView(withTagValue(`is`(standardViewButtonTag)))
            .perform(setCheck(true))
        displayCameraView(PANORAMIC_REAR_VIEW, ALL)
        // WHEN a coming vehicle or obstacle is detected on the left
        sonarRepository.collisionAlertSide.postValue(SIDE_LEFT_ALERT)
        sonarRepository.collisionAlertLevel.postValue(LEVEL_HIGH_COLLISION_RISK)
        // THEN the Left Rear Cross Trafic Alert is displayed in AVM sonar view

        assertViewIsCompletelyDisplayed(R.id.rcta_left)
    }

    @Test
    fun should_display_only_right_rcta_when_obstacle_detected_from_the_right_in_panoramic_rear_view() { // ktlint-disable max-line-length
        // GIVEN RCTA is supported
        sonarRepository.rctaFeaturePresent = true
        // AND activated by user
        sonarRepository.collisionAlertEnabled.postValue(true)
        // AND the rear gear is engaged
        displayCameraView(REAR_VIEW, ALL)
        // AND current view is Panoramic rear
        onView(withTagValue(`is`(standardViewButtonTag)))
            .perform(setCheck(true))
        displayCameraView(PANORAMIC_REAR_VIEW, ALL)
        // WHEN a coming vehicle or obstacle is detected on the right
        sonarRepository.collisionAlertSide.postValue(SIDE_RIGHT_ALERT)
        sonarRepository.collisionAlertLevel.postValue(LEVEL_HIGH_COLLISION_RISK)
        // THEN the Right Rear Cross Trafic Alert is displayed in AVM sonar view

        assertViewIsCompletelyDisplayed(R.id.rcta_right)
    }

    @Test
    fun should_display_only_left_and_right_rcta_when_obstacle_detected_from_the_left_and_the_right_in_panoramic_rear_view() { // ktlint-disable max-line-length
        // GIVEN RCTA is supported
        sonarRepository.rctaFeaturePresent = true
        // AND activated by user
        sonarRepository.collisionAlertEnabled.postValue(true)
        // AND the rear gear is engaged
        displayCameraView(REAR_VIEW, ALL)
        // AND current view is panoramic
        onView(withTagValue(`is`(standardViewButtonTag)))
            .perform(setCheck(true))
        displayCameraView(PANORAMIC_REAR_VIEW, ALL)
        // WHEN a coming vehicle or obstacle is detected on the left and right
        sonarRepository.collisionAlertSide.postValue(SIDE_LEFT_RIGHT_ALERT)
        sonarRepository.collisionAlertLevel.postValue(LEVEL_HIGH_COLLISION_RISK)
        // THEN the Left and Right Rear Cross Trafic Alert is displayed in AVM sonar view

        assertViewIsCompletelyDisplayed(R.id.rcta_left)
        assertViewIsCompletelyDisplayed(R.id.rcta_right)
    }

    @Test
    fun should_display_left_sonar_and_camera_rcta_when_obstacle_detected_from_the_left_in_standard_mode() { // ktlint-disable max-line-length
        // GIVEN RCTA is supported
        sonarRepository.rctaFeaturePresent = true
        // AND activated by user
        sonarRepository.collisionAlertEnabled.postValue(true)
        // AND the rear gear is engaged
        displayCameraView(REAR_VIEW, ALL)
        // WHEN a coming vehicle or obstacle is detected on the left
        sonarRepository.collisionAlertSide.postValue(SIDE_LEFT_ALERT)
        sonarRepository.collisionAlertLevel.postValue(LEVEL_HIGH_COLLISION_RISK)
        // THEN the Left Rear Cross Trafic Alert is displayed in both AVM camera and sonar views
        assertViewIsCompletelyDisplayed(R.id.rcta_left)
    }

    @Test
    fun should_display_right_sonar_and_camera_rcta_when_obstacle_detected_from_the_right_in_standard_mode() { // ktlint-disable max-line-length
        // GIVEN RCTA is supported
        sonarRepository.rctaFeaturePresent = true
        // AND activated by user
        sonarRepository.collisionAlertEnabled.postValue(true)
        // AND the rear gear is engaged
        displayCameraView(REAR_VIEW, ALL)
        // WHEN a coming vehicle or obstacle is detected on the right
        sonarRepository.collisionAlertSide.postValue(SIDE_RIGHT_ALERT)
        sonarRepository.collisionAlertLevel.postValue(LEVEL_HIGH_COLLISION_RISK)
        // THEN the Right Rear Cross Trafic Alert is displayed in both AVM camera and sonar views
        assertViewIsCompletelyDisplayed(R.id.rcta_right)
    }

    @Test
    fun should_display_left_and_right_sonar_and_camera_rcta_when_obstacle_detected_from_the_left_and_the_right_in_standard_mode() { // ktlint-disable max-line-length
        // GIVEN RCTA is supported
        sonarRepository.rctaFeaturePresent = true
        // AND activated by user
        sonarRepository.collisionAlertEnabled.postValue(true)
        // AND the rear gear is engaged
        displayCameraView(REAR_VIEW, ALL)
        // WHEN a coming vehicle or obstacle is detected on the left and right
        sonarRepository.collisionAlertSide.postValue(SIDE_LEFT_RIGHT_ALERT)
        sonarRepository.collisionAlertLevel.postValue(LEVEL_HIGH_COLLISION_RISK)
        // THEN the Left and Right Rear Cross Trafic Alert is displayed in both AVM camera and sonar views

        assertViewIsCompletelyDisplayed(R.id.rcta_right)
        assertViewIsCompletelyDisplayed(R.id.rcta_left)
    }

    @Test
    fun should_not_display_left_and_right_sonar_and_camera_rcta_when_obstacle_detected_from_the_left_and_the_right_AND_user_deactivates_rcta_in_standard_mode() { // ktlint-disable max-line-length
        // GIVEN RCTA is supported
        sonarRepository.rctaFeaturePresent = true
        // AND the rear gear is engaged
        displayCameraView(REAR_VIEW, ALL)
        // WHEN a coming vehicle or obstacle is detected on the left and right
        sonarRepository.collisionAlertSide.postValue(SIDE_LEFT_RIGHT_ALERT)
        sonarRepository.collisionAlertLevel.postValue(LEVEL_HIGH_COLLISION_RISK)
        // AND user deactivates RCTA
        sonarRepository.collisionAlertEnabled.postValue(false)
        // THEN left and right RCTA are NOT displayed in both AVM camera and sonar views
        assertViewIsNotDisplayed(R.id.rcta_right)
        assertViewIsNotDisplayed(R.id.rcta_left)
    }

    @Test
    fun should_not_display_left_and_right_sonar_and_camera_rcta_when_obstacle_detected_from_the_left_and_the_right_AND_no_rear_view_ongoing() { // ktlint-disable max-line-length
        // GIVEN RCTA is supported
        sonarRepository.rctaFeaturePresent = true
        // AND front view is displayed
        displayCameraView(FRONT_VIEW, ALL)
        // WHEN a coming vehicle or obstacle is detected on the left and right
        sonarRepository.collisionAlertSide.postValue(SIDE_LEFT_RIGHT_ALERT)
        sonarRepository.collisionAlertLevel.postValue(LEVEL_HIGH_COLLISION_RISK)
        // AND user activate RCTA
        sonarRepository.collisionAlertEnabled.postValue(true)
        // THEN left and right RCTA are NOT displayed in both AVM camera and sonar views
        assertViewIsNotDisplayed(R.id.rcta_right)
        assertViewIsNotDisplayed(R.id.rcta_left)
    }

    @Test
    fun should_not_display_raeb_when_obstacle_detected_from_the_left_and_the_right_AND_no_rear_view_ongoing() { // ktlint-disable max-line-length
        // GIVEN RAEB is supported
        sonarRepository.raebAlertEnabled.postValue(true)
        // AND the rear gear is engaged
        displayCameraView(FRONT_VIEW, ALL)
        // AND current view is Standard view
        onView(withTagValue(`is`(standardViewButtonTag)))
            .perform(setCheck(true))
        // WHEN reab alert received
        sonarRepository.raebAlertState.postValue(RAEB_ALERT_2)
        // THEN RAEB is not displayed
        assertViewIsNotDisplayed(R.id.raeb_left)
        assertViewIsNotDisplayed(R.id.raeb_right)
    }
}