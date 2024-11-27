package com.metromate.Privacy;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;

public class PrivacyConsentDialog {
    private static final String PREFS_NAME = "user_preferences";
    private static final String KEY_CONSENT_GIVEN = "consent_given";

    public static boolean isConsentGiven(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return preferences.getBoolean(KEY_CONSENT_GIVEN, false);
    }

    public static void showConsentDialog(Activity activity, Runnable onConsentGranted) {
        new AlertDialog.Builder(activity)
                .setTitle("Privacy Agreement")
                .setMessage("To use this app, you need to agree to the privacy policy.")
                .setPositiveButton("Agree", (dialog, which) -> {
                    saveConsent(activity);
                    if (onConsentGranted != null) {
                        onConsentGranted.run();
                    }
                })
                .setNegativeButton("Cancel", (dialog, which) -> {
                    activity.finish(); // 앱 종료
                })
                .setCancelable(false)
                .show();
    }

    private static void saveConsent(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        preferences.edit().putBoolean(KEY_CONSENT_GIVEN, true).apply();
    }
}
