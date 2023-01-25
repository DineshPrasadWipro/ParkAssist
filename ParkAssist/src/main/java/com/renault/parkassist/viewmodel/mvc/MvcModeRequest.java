package com.renault.parkassist.viewmodel.mvc;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * View mode request, asked by a user when he clicks on a bottom navigation view button in the MVC app:
 * - `front` requests for a front view (front view)
 * - `rear` requests for a rear view (rear)
 * - `left` requests for a left sides view
 * - `right` request for a right sides view
 * - 'settings' requests a settings view
 * - 'back-from-settings' requests a back from settings view
 */

@Retention(RetentionPolicy.SOURCE)
@IntDef({
    MvcModeRequest.FRONT,
    MvcModeRequest.REAR,
    MvcModeRequest.LEFT,
    MvcModeRequest.RIGHT,
    MvcModeRequest.CLOSE,
    MvcModeRequest.SETTINGS,
    MvcModeRequest.BACK_FROM_SETTINGS
})

public @interface MvcModeRequest {
    int FRONT = 0;
    int REAR = 1;
    int LEFT = 2;
    int RIGHT = 3;
    int CLOSE = 4;
    int SETTINGS = 5;
    int BACK_FROM_SETTINGS = 6;
}
