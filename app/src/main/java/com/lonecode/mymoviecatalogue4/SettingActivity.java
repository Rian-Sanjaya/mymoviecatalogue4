package com.lonecode.mymoviecatalogue4;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.lonecode.mymoviecatalogue4.services.DailyReminder;

public class SettingActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener, View.OnClickListener {
    private Globals g = Globals.getInstance();
    private Switch swDailyReminder;
    private Switch swMovieReleaseReminder;
    private DailyReminder dailyReminder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button btnChooseLanguage = findViewById(R.id.btn_choose_language);
        btnChooseLanguage.setOnClickListener(this);

        swDailyReminder = findViewById(R.id.daily_reminder);
        swDailyReminder.setOnCheckedChangeListener(this);

        swMovieReleaseReminder = findViewById(R.id.release_reminder);
        swMovieReleaseReminder.setOnCheckedChangeListener(this);

        dailyReminder = new DailyReminder();

//        String language = g.getLanguage();
//
//        switch (language) {
//            case "en":
//                rbEnglish.setChecked(true);
//                break;
//
//            case "id":
//                rbIndonesia.setChecked(true);
//                break;
//        }

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getString(R.string.setting));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
//                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // force refresh when android back button is click
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent a = new Intent(this,MainActivity.class);
            a.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(a);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

//    private void setappLocale(String localeCode) {
//        Resources resources = getResources();
//        DisplayMetrics dm = resources.getDisplayMetrics();
//        Configuration config = resources.getConfiguration();
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
//            config.setLocale(new Locale(localeCode.toLowerCase()));
//        } else {
//            config.locale = new Locale(localeCode.toLowerCase());
//        }
//        resources.updateConfiguration(config, dm);
//    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.daily_reminder:
                if (isChecked) {
//                    Toast.makeText(this, "Daily Reminder enabled", Toast.LENGTH_LONG).show();
                    dailyReminder.setRepeatingReminder(this);

                } else {
//                    Toast.makeText(this, "Daily Reminder disabled", Toast.LENGTH_LONG).show();
                    dailyReminder.cancelRepeatingReminder(this);
                }
                break;

            case R.id.release_reminder:
                if (isChecked) {
                    Toast.makeText(this, "Release Reminder enabled", Toast.LENGTH_LONG).show();

                } else {
                    Toast.makeText(this, "Release Reminder disabled", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_choose_language) {
            Intent mIntent = new Intent(Settings.ACTION_LOCALE_SETTINGS);
            startActivity(mIntent);
        }
    }
}
