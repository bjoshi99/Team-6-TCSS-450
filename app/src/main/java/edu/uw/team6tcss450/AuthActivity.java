package edu.uw.team6tcss450;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

public class AuthActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        SharedPreferences Preferences = getSharedPreferences("Theme", Context.MODE_PRIVATE);
//        mChangePreference = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String themename = Preferences.getString("ThemeName", "Default");

        if (themename.equalsIgnoreCase("DarkTheme")) {
//            System.out.println("Night mode on");
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            setTheme(R.style.Theme_Team6TCSS450_Night);
        } else  {
//            System.out.println("Day mode on");
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            setTheme(R.style.Theme_Team6TCSS450);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
    }
}
