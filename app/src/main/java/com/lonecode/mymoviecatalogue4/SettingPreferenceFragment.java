package com.lonecode.mymoviecatalogue4;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceFragmentCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;

import androidx.preference.Preference;
import androidx.preference.SwitchPreference;

import com.lonecode.mymoviecatalogue4.services.DailyReminder;
import com.lonecode.mymoviecatalogue4.services.ReleaseReminder;

public class SettingPreferenceFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener, Preference.OnPreferenceClickListener {
    private DailyReminder dailyReminder;
    private ReleaseReminder releaseReminder;

    private String DAILY_REMINDER;
    private String REALEASE_MOVIE_REMINDER;
    private String LOCALE_SETTING;

    private SwitchPreference swDailyReminder;
    private SwitchPreference swRealeaseMovie;
    private Preference preLocale ;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.setting);

        dailyReminder = new DailyReminder();
        releaseReminder = new ReleaseReminder();

        init();
        setSummaries();
    }

    private void init() {
        DAILY_REMINDER = getResources().getString(R.string.daily_reminder_key);
        REALEASE_MOVIE_REMINDER = getResources().getString(R.string.daily_realease_movie_key);
        LOCALE_SETTING = getResources().getString(R.string.change_language_key);

        swDailyReminder = (SwitchPreference) findPreference(DAILY_REMINDER);
        swRealeaseMovie = (SwitchPreference) findPreference(REALEASE_MOVIE_REMINDER);
        preLocale = (Preference) findPreference(LOCALE_SETTING);
        preLocale.setOnPreferenceClickListener(this);
    }

    private void setSummaries() {
        SharedPreferences sh = getPreferenceManager().getSharedPreferences();

        swDailyReminder.setChecked(sh.getBoolean(DAILY_REMINDER, false));
        swRealeaseMovie.setChecked(sh.getBoolean(REALEASE_MOVIE_REMINDER, false));
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Log.d("Preference key: ", key);
        if (key.equals(DAILY_REMINDER)) {
            swDailyReminder.setChecked(sharedPreferences.getBoolean(DAILY_REMINDER, false));

            if (swDailyReminder.isChecked()) {
                dailyReminder.setRepeatingReminder(getContext());
            } else {
                dailyReminder.cancelRepeatingReminder(getContext());
            }
        }

        if (key.equals(REALEASE_MOVIE_REMINDER)) {
            swRealeaseMovie.setChecked(sharedPreferences.getBoolean(REALEASE_MOVIE_REMINDER, false));

            if (swRealeaseMovie.isChecked()) {
                releaseReminder.setMovieReleaseReminder(getContext());
            } else {
                releaseReminder.cancelMovieReleaseReminder(getContext());
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        Log.d("Preference key: ", preference.getKey());
        if (preference.getKey().equals(LOCALE_SETTING)) {
            Intent mIntent = new Intent(Settings.ACTION_LOCALE_SETTINGS);
            startActivity(mIntent);
            return true;
        }

        return false;
    }
}
