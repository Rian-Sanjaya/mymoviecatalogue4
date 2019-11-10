package com.lonecode.mymoviecatalogue4;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;


public class MainActivity extends AppCompatActivity {
    private Globals g = Globals.getInstance();
    private Toolbar toolbar;
    private BottomNavigationView navView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navView = findViewById(R.id.nav_view);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_movie, R.id.navigation_tvshow, R.id.navigation_favorite)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
    }

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search:
                if (navView.getSelectedItemId() == R.id.navigation_movie) {
//                    Toast.makeText(this, "Search Movie", Toast.LENGTH_LONG).show();
                    Intent searchMovieIntent = new Intent(this, SearchMovieActivity.class);
                    startActivity(searchMovieIntent);

                } else if (navView.getSelectedItemId() == R.id.navigation_tvshow) {
                    Toast.makeText(this, "Search Tv", Toast.LENGTH_LONG).show();
//                    Intent searchMovieIntent = new Intent(this, SearchMovieActivity.class);
//                    startActivity(searchMovieIntent);
                }
                return true;

            case R.id.menu_setting:
                Intent intent = new Intent(this, SettingPreferenceActivity.class);
                startActivity(intent);

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }

}
