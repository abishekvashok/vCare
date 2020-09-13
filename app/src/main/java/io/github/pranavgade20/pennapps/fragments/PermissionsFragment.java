package io.github.pranavgade20.pennapps.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import io.github.pranavgade20.pennapps.OnboardingActivity;
import io.github.pranavgade20.pennapps.R;

public class PermissionsFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_permissions, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        Button b = view.findViewById(R.id.button_onboardingfinish);
        TextInputEditText age = view.findViewById(R.id.textinput_age);
        AutoCompleteTextView gender =
                view.findViewById(R.id.filled_exposed_dropdown_gender);
        String[] type = new String[] {"Male","Female","Other"};

        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(
                        this.getActivity(),
                        R.layout.dropdown_menu_popup_item,
                        type);

        gender.setAdapter(adapter);
        b.setOnClickListener(v -> {
            boolean go = true;
            int genderI = 0;
            int ageI = 0;
            if(age.getText().toString().equals("")){
                go = false;
                Snackbar.make(view, "Please enter your Age", Snackbar.LENGTH_SHORT).show();
            } else {
                ageI = Integer.parseInt(age.getText().toString());
            }
            if(gender.getText().toString().equals("")) {
                go = false;
                Snackbar.make(view, "Please select your Gender", Snackbar.LENGTH_SHORT).show();
            } else if(!(gender.getText().toString().equals("Male") || gender.getText().toString().equals("Female")
                    || gender.getText().toString().equals("Other"))){
                Snackbar.make(view, "Please select a valid Gender", Snackbar.LENGTH_SHORT).show();
            } else {
                genderI = (gender.getText().toString().equals("Male"))?1:0;
            }
            if(go){
                ((OnboardingActivity)getActivity()).onboarded(ageI, genderI);
            }
        });
    }
}