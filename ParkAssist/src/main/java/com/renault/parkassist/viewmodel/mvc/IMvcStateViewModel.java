package com.renault.parkassist.viewmodel.mvc;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.renault.parkassist.viewmodel.avm.AvmModeRequest;

/** Multi View Camera (MVC) video display current state */
public interface IMvcStateViewModel {


        /** Current display type button selected */
        @NonNull
        LiveData<Integer> getModeSelected();

        /** Close camera view action visible */
        @NonNull
        LiveData<Boolean> getCloseVisible();

        /** Easypark shortcut visible */
        boolean getEasyparkShortcutVisible();

        /** Activate maneuver view action visible */
        @NonNull
        LiveData<Boolean> getManeuverVisible();

        /** Activate settings view action visible */
        @NonNull
        LiveData<Boolean> getSettingsVisible();

        /** Activate trailer view action visible */
        @NonNull
        LiveData<Boolean> getTrailerVisible();

        @NonNull
        LiveData<Boolean> getSelectLeftSideViewVisible();

        /** Select sides view action visible */
        @NonNull
        LiveData<Boolean> getSelectRightSideViewVisible();

        /** Select three dimension view action visible */
        @NonNull
        LiveData<Boolean> getSelectFrontViewVisible();

        /** three dimension info text visible */
        @NonNull
        LiveData<Boolean> getRearViewInfoVisible();

        /**
         * Requests the AVM to display a specific view mode
         *
         * @param request AVM view mode request
         */
        public void requestViewMode(@MvcModeRequest int request);

        /**
         * This function is the entry point for Avm camera scenarios.
         * It will decide what mode to set by default.
         * To be called when lifecycle is attached to viewModels livedatas
         */
        public void requestView();

        /**
         * This function is the exit point for Avm camera scenarios.
         * It will decide what how to properly leave Avm.
         * To be called when lifecycle is detached to viewModels livedatas
         */
        public void closeView();
    }

