package com.renault.parkassist.viewmodel.mvc;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
  MVC video current display mode selected
  - `front` Front view selected
  - `rear` Rear view selected
  - `left` Left view selected
  - `right` Right view selected
 */

@Retention(RetentionPolicy.SOURCE)
@IntDef({
    MvcModeSelected.FRONT,
    MvcModeSelected.REAR,
    MvcModeSelected.LEFT,
    MvcModeSelected.RIGHT
})

public @interface MvcModeSelected {
    int FRONT = 0;
    int REAR = 1;
    int LEFT = 2;
    int RIGHT = 3;
}
