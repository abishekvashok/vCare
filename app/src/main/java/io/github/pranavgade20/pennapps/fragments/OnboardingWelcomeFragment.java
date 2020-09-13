package io.github.pranavgade20.pennapps.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import io.github.pranavgade20.pennapps.R;

public class OnboardingWelcomeFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_onboarding_welcome, container, false);
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        Button button = view.findViewById(R.id.button_onboardingstart);
        button.setOnClickListener(
                v -> {
                    FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                    fragmentTransaction.replace(
                            R.id.fragment_holder, new PermissionsFragment(), "PERMISSIONS_TAG");
                    fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                });
    }
}