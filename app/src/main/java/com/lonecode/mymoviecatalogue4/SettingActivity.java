package com.lonecode.mymoviecatalogue4;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;

import java.util.Locale;

public class SettingActivity extends AppCompatActivity {
    private Globals g = Globals.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        RadioButton rbEnglish = findViewById(R.id.rbEnglish);
        RadioButton rbIndonesia = findViewById(R.id.rbIndonesia);

        String language = g.getLanguage();

        switch (language) {
            case "en":
                rbEnglish.setChecked(true);
                break;

            case "id":
                rbIndonesia.setChecked(true);
                break;
        }

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

    public void onRadioButtonClicked(View view) {
        // is the button now checked ?
        boolean checked = ((RadioButton) view).isChecked();

        // check which radio button was clicked
        switch (view.getId()) {
            case R.id.rbEnglish:
                if (checked) {
                    setappLocale("en");
                    g.setLanguage("en");
                }

                break;

            case R.id.rbIndonesia:
                if (checked) {
                    setappLocale("id");
                    g.setLanguage("id");
                }

                break;
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

    private void setappLocale(String localeCode) {
        Resources resources = getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        Configuration config = resources.getConfiguration();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            config.setLocale(new Locale(localeCode.toLowerCase()));
        } else {
            config.locale = new Locale(localeCode.toLowerCase());
        }
        resources.updateConfiguration(config, dm);
    }
}
