package com.renault.parkassist.viewmodel.apa.fapk

import alliance.car.autopark.AutoPark.*
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.test.filters.SmallTest
import com.renault.parkassist.TestUtils
import com.renault.parkassist.viewmodel.apa.ApaDialogBox
import com.renault.parkassist.viewmodel.apa.ApaWarningViewModelTest
import com.renault.parkassist.viewmodel.apa.ApaWarningViewModelTest.DialogBoxType.*
import org.junit.*

@SmallTest

class FapkWarningViewModelTest : ApaWarningViewModelTest() {

    private lateinit var fapkWarningViewModel: FapkWarningViewModel
    private lateinit var lifecycleOwner: LifecycleOwner

    private var testedDialogBox: ApaDialogBox? = null

    @Before
    override fun setup() {
        super.setup()
        lifecycleOwner = TestUtils.mockLifecycleOwner()
        fapkWarningViewModel = FapkWarningViewModel()
        fapkWarningViewModel.dialogBox.observe(lifecycleOwner, Observer {
            testedDialogBox = it
        })
    }

    @After
    override fun tearDown() {
        super.tearDown()
        fapkWarningViewModel.dialogBox.removeObservers(lifecycleOwner)
        testedDialogBox = null
    }

    @Test
    fun `should return disabled dialog when NONE message is received`() {
        val msg = MESSAGE_NONE
        apaRepositoryWarningMessage.postValue(msg)
        Assert.assertEquals(null, testedDialogBox)
    }

    @Test
    fun `should return enabled dialog with positive button when MANEUVER_CANCELED message is received`() { // ktlint-disable max-line-length
        val msg = MESSAGE_MANEUVER_CANCELED
        var expectedDialogBox = getExpectedFapkDialogBox(DIALOG_BOX_TYPE_POSITIVE, true, msg)
        apaRepositoryWarningMessage.postValue(msg)
        Assert.assertEquals(expectedDialogBox, testedDialogBox)
    }

    @Test
    fun `should return enabled dialog with positive button when FEATURE_UNAVAILABLE_SPEED_TOO_HIGH message is received`() { // ktlint-disable max-line-length
        val msg = MESSAGE_FEATURE_UNAVAILABLE_SPEED_TOO_HIGH
        var expectedDialogBox = getExpectedFapkDialogBox(DIALOG_BOX_TYPE_POSITIVE, true, msg)
        apaRepositoryWarningMessage.postValue(msg)
        Assert.assertEquals(expectedDialogBox, testedDialogBox)
    }

    @Test
    fun `should return enabled dialog with positive button when FEATURE_UNAVAILABLE message is received`() { // ktlint-disable max-line-length
        val msg = MESSAGE_FEATURE_UNAVAILABLE
        var expectedDialogBox = getExpectedFapkDialogBox(DIALOG_BOX_TYPE_POSITIVE, true, msg)
        apaRepositoryWarningMessage.postValue(msg)
        Assert.assertEquals(expectedDialogBox, testedDialogBox)
    }

    @Test
    fun `should return enabled dialog with positive button when FEATURE_UNAVAILABLE_TRAILER_DETECTED message is received`() { // ktlint-disable max-line-length
        val msg = MESSAGE_FEATURE_UNAVAILABLE_TRAILER_DETECTED
        var expectedDialogBox = getExpectedFapkDialogBox(DIALOG_BOX_TYPE_POSITIVE, true, msg)
        apaRepositoryWarningMessage.postValue(msg)
        Assert.assertEquals(expectedDialogBox, testedDialogBox)
    }

    @Test
    fun `should return enabled dialog with positive button when FEATURE_UNAVAILABLE_CRUISE_CONTROL_ACTIVATED message is received`() { // ktlint-disable max-line-length
        val msg = MESSAGE_FEATURE_UNAVAILABLE_CRUISE_CONTROL_ACTIVATED
        var expectedDialogBox = getExpectedFapkDialogBox(DIALOG_BOX_TYPE_POSITIVE, true, msg)
        apaRepositoryWarningMessage.postValue(msg)
        Assert.assertEquals(expectedDialogBox, testedDialogBox)
    }

    @Test
    fun `should return enabled dialog with positive button when MANEUVER_CANCELED_PARKING_BRAKE message is received`() { // ktlint-disable max-line-length
        val msg = MESSAGE_MANEUVER_CANCELED_PARKING_BRAKE
        var expectedDialogBox = getExpectedFapkDialogBox(DIALOG_BOX_TYPE_POSITIVE, true, msg)
        apaRepositoryWarningMessage.postValue(msg)
        Assert.assertEquals(expectedDialogBox, testedDialogBox)
    }

    @Test
    fun `should return enabled dialog with positive button when MANEUVER_CANCELED_DRIVER_DOOR_OPEN message is received`() { // ktlint-disable max-line-length
        val msg = MESSAGE_MANEUVER_CANCELED_DRIVER_DOOR_OPEN
        var expectedDialogBox = getExpectedFapkDialogBox(DIALOG_BOX_TYPE_POSITIVE, true, msg)
        apaRepositoryWarningMessage.postValue(msg)
        Assert.assertEquals(expectedDialogBox, testedDialogBox)
    }

    @Test
    fun `should return enabled dialog with positive button when MANEUVER_CANCELED_SEAT_BELT_UNFASTEN message is received`() { // ktlint-disable max-line-length
        val msg = MESSAGE_MANEUVER_CANCELED_SEAT_BELT_UNFASTEN
        var expectedDialogBox = getExpectedFapkDialogBox(DIALOG_BOX_TYPE_POSITIVE, true, msg)
        apaRepositoryWarningMessage.postValue(msg)
        Assert.assertEquals(expectedDialogBox, testedDialogBox)
    }

    @Test
    fun `should return enabled dialog with positive button when MANEUVER_UNAVAILABLE_PARKING_BRAKE message is received`() { // ktlint-disable max-line-length
        val msg = MESSAGE_MANEUVER_UNAVAILABLE_PARKING_BRAKE
        var expectedDialogBox = getExpectedFapkDialogBox(DIALOG_BOX_TYPE_POSITIVE, true, msg)
        apaRepositoryWarningMessage.postValue(msg)
        Assert.assertEquals(expectedDialogBox, testedDialogBox)
    }

    @Test
    fun `should return enabled dialog with positive button when MANEUVER_CANCELED_SLOPE_TOO_HIGH message is received`() { // ktlint-disable max-line-length
        val msg = MESSAGE_MANEUVER_CANCELED_SLOPE_TOO_HIGH
        var expectedDialogBox = getExpectedFapkDialogBox(DIALOG_BOX_TYPE_POSITIVE, true, msg)
        apaRepositoryWarningMessage.postValue(msg)
        Assert.assertEquals(expectedDialogBox, testedDialogBox)
    }

    @Test
    fun `should return enabled dialog with positive button when MANEUVER_UNAVAILABLE_SEAT_BELT_UNFASTEN message is received`() { // ktlint-disable max-line-length
        val msg = MESSAGE_MANEUVER_UNAVAILABLE_SEAT_BELT_UNFASTEN
        var expectedDialogBox = getExpectedFapkDialogBox(DIALOG_BOX_TYPE_POSITIVE, true, msg)
        apaRepositoryWarningMessage.postValue(msg)
        Assert.assertEquals(expectedDialogBox, testedDialogBox)
    }

    @Test
    fun `should return enabled dialog with positive button when MANEUVER_UNAVAILABLE_SLOPE_TOO_HIGH message is received`() { // ktlint-disable max-line-length
        val msg = MESSAGE_MANEUVER_UNAVAILABLE_SLOPE_TOO_HIGH
        var expectedDialogBox = getExpectedFapkDialogBox(DIALOG_BOX_TYPE_POSITIVE, true, msg)
        apaRepositoryWarningMessage.postValue(msg)
        Assert.assertEquals(expectedDialogBox, testedDialogBox)
    }

    @Test
    fun `should return enabled dialog with positive button when MANEUVER_CANCELED_BRAKING_FAILURE message is received`() { // ktlint-disable max-line-length
        val msg = MESSAGE_MANEUVER_CANCELED_BRAKING_FAILURE
        var expectedDialogBox = getExpectedFapkDialogBox(DIALOG_BOX_TYPE_POSITIVE, true, msg)
        apaRepositoryWarningMessage.postValue(msg)
        Assert.assertEquals(expectedDialogBox, testedDialogBox)
    }

    @Test
    fun `should return enabled dialog with positive button when FEATURE_UNAVAILABLE_VEHICLE_NOT_STOPPED message is received`() { // ktlint-disable max-line-length
        val msg = MESSAGE_FEATURE_UNAVAILABLE_VEHICLE_NOT_STOPPED
        var expectedDialogBox = getExpectedFapkDialogBox(DIALOG_BOX_TYPE_POSITIVE, true, msg)
        apaRepositoryWarningMessage.postValue(msg)
        Assert.assertEquals(expectedDialogBox, testedDialogBox)
    }

    @Test
    fun `should return enabled dialog with positive button when MANEUVER_CANCELED_TAKE_BACK_CONTROL message is received`() { // ktlint-disable max-line-length
        val msg = MESSAGE_MANEUVER_CANCELED_TAKE_BACK_CONTROL
        var expectedDialogBox = getExpectedFapkDialogBox(DIALOG_BOX_TYPE_POSITIVE, true, msg)
        apaRepositoryWarningMessage.postValue(msg)
        Assert.assertEquals(expectedDialogBox, testedDialogBox)
    }

    @Test
    fun `should return enabled dialog with positive button when MANEUVER_CANCELED_RELEASE_ACCELERATOR_PEDAL message is received`() { // ktlint-disable max-line-length
        val msg = MESSAGE_MANEUVER_CANCELED_RELEASE_ACCELERATOR_PEDAL
        var expectedDialogBox = getExpectedFapkDialogBox(DIALOG_BOX_TYPE_POSITIVE, true, msg)
        apaRepositoryWarningMessage.postValue(msg)
        Assert.assertEquals(expectedDialogBox, testedDialogBox)
    }

    @Test
    fun `should return enabled dialog with positive button when MANEUVER_UNAVAILABLE_H152_ESC_OFF message is received`() { // ktlint-disable max-line-length
        val msg = MESSAGE_MANEUVER_UNAVAILABLE_H152_ESC_OFF
        var expectedDialogBox = getExpectedFapkDialogBox(DIALOG_BOX_TYPE_POSITIVE, true, msg)
        apaRepositoryWarningMessage.postValue(msg)
        Assert.assertEquals(expectedDialogBox, testedDialogBox)
    }

    @Test
    fun `should return enabled dialog with positive button when MANEUVER_CANCELED_ESC_OFF message is received`() { // ktlint-disable max-line-length
        val msg = MESSAGE_MANEUVER_CANCELED_ESC_OFF
        var expectedDialogBox = getExpectedFapkDialogBox(DIALOG_BOX_TYPE_POSITIVE, true, msg)
        apaRepositoryWarningMessage.postValue(msg)
        Assert.assertEquals(expectedDialogBox, testedDialogBox)
    }

    @Test
    fun `should return enabled dialog with positive button when MANEUVER_FINISHED_NO_FRONT_OBSTACLE message is received`() { // ktlint-disable max-line-length
        val msg = MESSAGE_MANEUVER_FINISHED_NO_FRONT_OBSTACLE
        var expectedDialogBox = getExpectedFapkDialogBox(DIALOG_BOX_TYPE_POSITIVE, true, msg)
        apaRepositoryWarningMessage.postValue(msg)
        Assert.assertEquals(expectedDialogBox, testedDialogBox)
    }

    @Test
    fun `should return enabled dialog with positive button when MANEUVER_UNAVAILABLE_SLOT_TOO_SMALL message is received`() { // ktlint-disable max-line-length
        val msg = MESSAGE_MANEUVER_UNAVAILABLE_SLOT_TOO_SMALL
        var expectedDialogBox = getExpectedFapkDialogBox(DIALOG_BOX_TYPE_POSITIVE, true, msg)
        apaRepositoryWarningMessage.postValue(msg)
        Assert.assertEquals(expectedDialogBox, testedDialogBox)
    }

    @Test
    fun `should return enabled dialog with neutral button when MANEUVER_SUSPENDED_DOOR_OPEN message is received`() { // ktlint-disable max-line-length
        val msg = MESSAGE_MANEUVER_SUSPENDED_DOOR_OPEN
        var expectedDialogBox = getExpectedFapkDialogBox(DIALOG_BOX_TYPE_NEUTRAL, true, msg)
        apaRepositoryWarningMessage.postValue(msg)
        Assert.assertEquals(expectedDialogBox, testedDialogBox)
    }

    @Test
    fun `should return enabled dialog with neutral button when MANEUVER_SUSPENDED_HAND_ON_WHEEL message is received`() { // ktlint-disable max-line-length
        val msg = MESSAGE_MANEUVER_SUSPENDED_HAND_ON_WHEEL
        var expectedDialogBox = getExpectedFapkDialogBox(DIALOG_BOX_TYPE_NEUTRAL, true, msg)
        apaRepositoryWarningMessage.postValue(msg)
        Assert.assertEquals(expectedDialogBox, testedDialogBox)
    }

    @Test
    fun `should return enabled dialog with neutral button when MANEUVER_SUSPENDED message is received`() { // ktlint-disable max-line-length
        val msg = MESSAGE_MANEUVER_SUSPENDED
        var expectedDialogBox = getExpectedFapkDialogBox(DIALOG_BOX_TYPE_NEUTRAL, true, msg)
        apaRepositoryWarningMessage.postValue(msg)
        Assert.assertEquals(expectedDialogBox, testedDialogBox)
    }

    @Test
    fun `should return enabled dialog with neutral button when MANEUVER_SUSPENDED_OBSTACLE_ON_PATH message is received`() { // ktlint-disable max-line-length
        val msg = MESSAGE_MANEUVER_SUSPENDED_OBSTACLE_ON_PATH
        var expectedDialogBox = getExpectedFapkDialogBox(DIALOG_BOX_TYPE_NEUTRAL, true, msg)
        apaRepositoryWarningMessage.postValue(msg)
        Assert.assertEquals(expectedDialogBox, testedDialogBox)
    }

    @Test
    fun `should return enabled dialog with neutral button when MANEUVER_UNAVAILABLE_HAND_ON_WHEEL message is received`() { // ktlint-disable max-line-length
        val msg = MESSAGE_MANEUVER_SUSPENDED_OBSTACLE_ON_PATH
        var expectedDialogBox = getExpectedFapkDialogBox(DIALOG_BOX_TYPE_NEUTRAL, true, msg)
        apaRepositoryWarningMessage.postValue(msg)
        Assert.assertEquals(expectedDialogBox, testedDialogBox)
    }

    @Test
    fun `should return enabled dialog with neutral button when MANEUVER_UNAVAILABLE_DOOR_OPEN message is received`() { // ktlint-disable max-line-length
        val msg = MESSAGE_MANEUVER_UNAVAILABLE_DOOR_OPEN
        var expectedDialogBox = getExpectedFapkDialogBox(DIALOG_BOX_TYPE_NEUTRAL, true, msg)
        apaRepositoryWarningMessage.postValue(msg)
        Assert.assertEquals(expectedDialogBox, testedDialogBox)
    }

    @Test
    fun `should return enabled dialog with positive button when MANEUVER_CANCELED_ENGINE_STOPPED message is received`() { // ktlint-disable max-line-length
        val msg = MESSAGE_MANEUVER_CANCELED_ENGINE_STOPPED
        val expectedDialogBox = getExpectedFapkDialogBox(DIALOG_BOX_TYPE_POSITIVE, true, msg)
        apaRepositoryWarningMessage.postValue(msg)
        Assert.assertEquals(expectedDialogBox, testedDialogBox)
    }

    @Test
    fun `should return enabled dialog with neutral button when BRAKE_TO_RESUME_MANEUVER message is received`() { // ktlint-disable max-line-length
        val msg = MESSAGE_BRAKE_TO_RESUME_MANEUVER
        var expectedDialogBox = getExpectedFapkDialogBox(DIALOG_BOX_TYPE_NEUTRAL, true, msg)
        apaRepositoryWarningMessage.postValue(msg)
        Assert.assertEquals(expectedDialogBox, testedDialogBox)
    }

    @Test
    fun `should return enabled dialog with neutral button when MANEUVER_SUSPENDED_RELEASE_ACCELERATOR_PEDAL message is received`() { // ktlint-disable max-line-length
        val msg = MESSAGE_MANEUVER_SUSPENDED_RELEASE_ACCELERATOR_PEDAL
        var expectedDialogBox = getExpectedFapkDialogBox(DIALOG_BOX_TYPE_NEUTRAL, true, msg)
        apaRepositoryWarningMessage.postValue(msg)
        Assert.assertEquals(expectedDialogBox, testedDialogBox)
    }

    @Test
    fun `should return enabled dialog with neutral button when MANEUVER_SUSPENDED_GEAR_SHIFT_ACTIVATED message is received`() { // ktlint-disable max-line-length
        val msg = MESSAGE_MANEUVER_SUSPENDED_GEAR_SHIFT_ACTIVATED
        var expectedDialogBox = getExpectedFapkDialogBox(DIALOG_BOX_TYPE_NEUTRAL, true, msg)
        apaRepositoryWarningMessage.postValue(msg)
        Assert.assertEquals(expectedDialogBox, testedDialogBox)
    }

    @Test
    fun `should return enabled dialog with neutral button when SELECT_TURN_INDICATOR message is received`() { // ktlint-disable max-line-length
        val msg = MESSAGE_SELECT_TURN_INDICATOR
        var expectedDialogBox = getExpectedFapkDialogBox(DIALOG_BOX_TYPE_NEUTRAL, true, msg)
        apaRepositoryWarningMessage.postValue(msg)
        Assert.assertEquals(expectedDialogBox, testedDialogBox)
    }

    @Test
    fun `should return enabled dialog with neutral button when MANEUVER_CANCELED_ENGINE_STOPPED message is received`() { // ktlint-disable max-line-length
        val msg = MESSAGE_MANEUVER_CANCELED_ENGINE_STOPPED
        var expectedDialogBox = getExpectedFapkDialogBox(DIALOG_BOX_TYPE_POSITIVE, true, msg)
        apaRepositoryWarningMessage.postValue(msg)
        Assert.assertEquals(expectedDialogBox, testedDialogBox)
    }

    @Test
    fun `should return enabled dialog with positive button when MANEUVER_CANCELED_GEAR_SHIFT_ACTIVATED message is received`() { // ktlint-disable max-line-length
        val msg = MESSAGE_MANEUVER_CANCELED_GEAR_SHIFT_ACTIVATED
        var expectedDialogBox = getExpectedFapkDialogBox(DIALOG_BOX_TYPE_POSITIVE, true, msg)
        apaRepositoryWarningMessage.postValue(msg)
        Assert.assertEquals(expectedDialogBox, testedDialogBox)
    }

    @Test
    fun `should return enabled dialog with positive and negative button when PRESS_OK_TO_START_MANEUVER message is received`() { // ktlint-disable max-line-length
        val msg = MESSAGE_PRESS_OK_TO_START_MANEUVER
        var expectedDialogBox =
            getExpectedFapkDialogBox(DIALOG_BOX_TYPE_POSITIVE_NEGATIVE, true, msg)
        apaRepositoryWarningMessage.postValue(msg)
        Assert.assertEquals(expectedDialogBox, testedDialogBox)
    }

    @Test
    fun `should return negative dialog when FEATURE_UNAVAILABLE_MIRRORS_FOLDED message is received`() { // ktlint-disable max-line-length
        val msg = MESSAGE_FEATURE_UNAVAILABLE_MIRRORS_FOLDED
        var expectedDialogBox =
            getExpectedFapkDialogBox(DIALOG_BOX_TYPE_POSITIVE, true, msg)
        apaRepositoryWarningMessage.postValue(msg)
        Assert.assertEquals(expectedDialogBox, testedDialogBox)
    }

    @Test
    fun `should return negative dialog when MANEUVER_CANCELED_MIRRORS_FOLDED message is received`() { // ktlint-disable max-line-length
        val msg = MESSAGE_MANEUVER_CANCELED_MIRRORS_FOLDED
        var expectedDialogBox =
            getExpectedFapkDialogBox(DIALOG_BOX_TYPE_POSITIVE, true, msg)
        apaRepositoryWarningMessage.postValue(msg)
        Assert.assertEquals(expectedDialogBox, testedDialogBox)
    }

    @Test
    fun `should return disabled dialog when MANEUVER_SUSPENDED_UNTIL_RESTART_ENGINE message is received`() { // ktlint-disable max-line-length
        val msg = MESSAGE_MANEUVER_SUSPENDED_UNTIL_RESTART_ENGINE
        var expectedDialogBox =
            getExpectedFapkDialogBox(DIALOG_BOX_TYPE_DISABLED, false, msg)
        apaRepositoryWarningMessage.postValue(msg)
        Assert.assertEquals(expectedDialogBox, testedDialogBox)
    }

    @Test
    fun `should return disabled dialog when MANEUVER_SUSPENDED_BRAKE_TO_STOP message is received`() { // ktlint-disable max-line-length
        val msg = MESSAGE_MANEUVER_SUSPENDED_BRAKE_TO_STOP
        var expectedDialogBox =
            getExpectedFapkDialogBox(DIALOG_BOX_TYPE_DISABLED, false, msg)
        apaRepositoryWarningMessage.postValue(msg)
        Assert.assertEquals(expectedDialogBox, testedDialogBox)
    }
}