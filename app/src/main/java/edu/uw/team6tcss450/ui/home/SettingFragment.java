package edu.uw.team6tcss450.ui.home;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Toast;

import edu.uw.team6tcss450.MainActivityArgs;
import edu.uw.team6tcss450.R;
import edu.uw.team6tcss450.databinding.FragmentSettingBinding;
import edu.uw.team6tcss450.databinding.FragmentSignInBinding;
import edu.uw.team6tcss450.model.PushyTokenViewModel;
import edu.uw.team6tcss450.model.UserInfoViewModel;

/**
 * create an instance of this fragment.
 */
public class SettingFragment extends Fragment {

    SharedPreferences mPreferences;
    SharedPreferences mChangePreference;
    String mThemeName;
    FragmentSettingBinding binding;
    boolean stateChangeOnce;
    boolean isAutomaticallyChanged = false;
    UserInfoViewModel model;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSettingBinding.inflate(inflater);
        stateChangeOnce = false;

//        binding.switchSettingDarkmode.setOnCheckedChangeListener((buttonView, isChecked) -> {
//            System.out.println("******************* state change : "+isChecked);
//
//            if(isAutomaticallyChanged){
//                isAutomaticallyChanged = false;
//                return;
//            }
//
//            SharedPreferences preferences = getActivity().getSharedPreferences("Theme", Context.MODE_PRIVATE);
//            SharedPreferences.Editor editor = preferences.edit();
//            editor.putBoolean("themeChanged", true);
//            editor.apply();
//
//            if(isChecked){
//                System.out.println("DarkTheme");
////                isChecked = false;
//                isAutomaticallyChanged = true;
////                editor.putBoolean("DarkTheme", true);
////                editor.apply();
//                setTheme("DarkTheme");
////                binding.switchSettingDarkmode.setChecked(false);
//            }else{
//                System.out.println("Default");
////                isChecked = true;
//                isAutomaticallyChanged = true;
////                editor.putBoolean("DarkTheme", false);
////                editor.apply();
////                binding.switchSettingDarkmode.setChecked(true);
//                setTheme("Default");
//            }
//        });
//
////        binding.switchSettingDarkmode.setChecked(false);
//
        binding.buttonDarkTheme.setOnClickListener(v -> {

            SharedPreferences preferences = getActivity().getSharedPreferences("Theme", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("themeChanged", true);
            editor.apply();

            if(mThemeName.equals("DarkTheme")){
                Toast.makeText(getActivity(), "Already in Dark Mode!", Toast.LENGTH_SHORT).show();
            }else{
//                System.out.println("DarkTheme");
                setTheme("DarkTheme");
            }
        });

        binding.buttonDayTheme.setOnClickListener(v -> {

            SharedPreferences preferences = getActivity().getSharedPreferences("Theme", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("themeChanged", true);
            editor.apply();

            if(mThemeName.equals("Default")){
                Toast.makeText(getActivity(), "Already on Day Mode!", Toast.LENGTH_SHORT).show();
            }else{
//                System.out.println("DayMode");
                setTheme("Default");
            }

        });
//
//        System.out.println("At end of the onCreateView: " + mThemeName);
//
//        if (mThemeName.equalsIgnoreCase("DarkTheme")) {
//            binding.switchSettingDarkmode.setChecked(false);
//        } else  {
//            binding.switchSettingDarkmode.setChecked(true);
//        }


        // Inflate the layout for this fragment
        return binding.getRoot();
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_setting, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

//        System.out.println("Theme:" + mThemeName);
        if (mThemeName.equalsIgnoreCase("DarkTheme")) {
//            System.out.println("Night mode on");
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            getActivity().setTheme(R.style.Theme_Team6TCSS450_Night);
        } else  {
//            System.out.println("Day mode on");
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            getActivity().setTheme(R.style.Theme_Team6TCSS450);
        }

        super.onViewCreated(view, savedInstanceState);

        binding.buttonLogoutSetting.setOnClickListener(this::onClickLogOut);
        binding.buttonUpdatePassSetting.setOnClickListener(this::onClickUpdatePassword);
//        stateChangeOnce = false;

        model = new ViewModelProvider(getActivity())
                .get(UserInfoViewModel.class);
    }

    private void onClickUpdatePassword(View view) {

//        MainActivityArgs args = MainActivityArgs.fromBundle(getIntent().getExtras());


        System.out.println("Email from the setting fragment : " + model.getEmail());

        SettingFragmentDirections.ActionSettingFragment2ToUpdatePasswordFragment directions =
                SettingFragmentDirections.actionSettingFragment2ToUpdatePasswordFragment(model.getEmail());

        Navigation.findNavController(getView()).navigate(directions);
    }

    private void onClickLogOut(View v){

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        //Setting message manually and performing action on button click
        builder.setMessage("You are about to logout!!")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        SharedPreferences prefs =
                                getActivity().getSharedPreferences(
                                        getString(R.string.keys_shared_prefs),
                                        Context.MODE_PRIVATE);
                        prefs.edit().remove(getString(R.string.keys_prefs_jwt)).apply();
                        //End the app completely
                        getActivity().finishAndRemoveTask();

                        PushyTokenViewModel model = new ViewModelProvider(getActivity())
                                .get(PushyTokenViewModel.class);
                        //when we hear back from the web service quit
                        model.addResponseObserver(getActivity(), result -> getActivity().finishAndRemoveTask());
                        model.deleteTokenFromWebservice(
                                new ViewModelProvider(getActivity())
                                        .get(UserInfoViewModel.class)
                                        .getmJwt()
                        );

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //  Action for 'Cancel' Button
                        dialog.cancel();
                    }
                });
        //Creating dialog box
        AlertDialog alert = builder.create();
        //Setting the title manually
        alert.setTitle("Are You Sure ?");
        alert.show();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        mPreferences = getActivity().getSharedPreferences("Theme", Context.MODE_PRIVATE);
//        mChangePreference = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mThemeName = mPreferences.getString("ThemeName", "Default");
//        System.out.println("On Create() method: "+mThemeName);
//        mThemeName = "Default";

        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {

//        boolean isDark = mPreferences.getBoolean("DarkTheme", false);
//        boolean isChanged = mPreferences.getBoolean("themeChanged", false);

//        System.out.println("OnResume call firsst statement");
        mThemeName = mPreferences.getString("ThemeName", "Default");
//        System.out.println("Theme in onResume : "+mThemeName);
//        if(isDark && isChanged){
//            System.out.println("onCreate(): if ");
//            mPreferences.edit().remove("DarkTheme").apply();
//            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
//            getActivity().setTheme(R.style.Theme_Team6TCSS450_Night);
//        }
//        else if (!isDark && isChanged){
//            System.out.println("onCreate(): else if ");
//            mPreferences.edit().remove("DarkTheme").apply();
//            getActivity().setTheme(R.style.Theme_Team6TCSS450);
//        }

        super.onResume();
    }

    public void setTheme(String name) {
        // Create preference to store theme name
        SharedPreferences preferences = getActivity().getSharedPreferences("Theme", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("ThemeName", name);
        editor.apply();
//        System.out.println("complete set theme ! " + name);
//        return;


        if(mPreferences.getBoolean("themeChanged", false)){
//            System.out.println("theme changed statement in if statement");
            mPreferences.edit().remove("themeChanged").apply();
            getActivity().recreate();
        }


    }

}