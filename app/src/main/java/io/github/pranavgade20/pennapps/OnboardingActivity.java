package io.github.pranavgade20.pennapps;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import io.github.pranavgade20.pennapps.fragments.OnboardingWelcomeFragment;
import io.github.pranavgade20.pennapps.fragments.PermissionsFragment;

public class OnboardingActivity extends AppCompatActivity {

    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);
        sharedPref = this.getSharedPreferences(getString(R.string.user_data_file), Context.MODE_PRIVATE);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(
                R.id.fragment_holder, new OnboardingWelcomeFragment(), "PERMISSIONS_TAG");
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
    public void onboarded(int age, int gender) {
        //call this after all data has been saved
        SharedPreferences sharedPref = this.getSharedPreferences(getString(R.string.user_data_file), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(getString(R.string.is_user_onboarded), true);
        editor.putInt("AGE", age);
        editor.putInt("GENDER", gender);

        editor.apply();

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
