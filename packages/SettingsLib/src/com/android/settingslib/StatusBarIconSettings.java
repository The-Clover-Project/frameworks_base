/*
 * Copyright (C) 2026 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.settingslib;

import android.annotation.Nullable;
import android.app.ActivityManager;
import android.app.AppGlobals;
import android.content.Context;
import android.provider.Settings;

import com.android.settingslib.flags.Flags;

/** Helpers for the user-selectable status bar icon style. */
public final class StatusBarIconSettings {

    public static final String STATUS_BAR_CLASSIC_ICONS = "status_bar_classic_icons";
    private static Context sAppContext;

    private StatusBarIconSettings() {}

    public static void setAppContext(@Nullable Context context) {
        if (context != null) {
            sAppContext = context.getApplicationContext() != null
                    ? context.getApplicationContext()
                    : context;
        }
    }

    public static boolean useNewStatusBarIcons() {
        return useNewStatusBarIcons(null);
    }

    public static boolean useNewStatusBarIcons(@Nullable Context context) {
        return Flags.newStatusBarIcons() && !useClassicStatusBarIcons(context);
    }

    public static boolean useClassicStatusBarIcons() {
        return useClassicStatusBarIcons(null);
    }

    public static boolean useClassicStatusBarIcons(@Nullable Context context) {
        if (context != null && sAppContext == null) {
            setAppContext(context);
        }
        Context appContext = context != null
                ? (context.getApplicationContext() != null ? context.getApplicationContext() : context)
                : sAppContext;
        if (appContext == null) {
            appContext = AppGlobals.getInitialApplication();
            if (appContext != null && sAppContext == null) {
                sAppContext = appContext;
            }
        }
        if (appContext == null) {
            return false;
        }
        try {
            return Settings.Secure.getIntForUser(
                            appContext.getContentResolver(),
                            STATUS_BAR_CLASSIC_ICONS,
                            0,
                            ActivityManager.getCurrentUser())
                    != 0;
        } catch (Exception e) {
            return false;
        }
    }
}
