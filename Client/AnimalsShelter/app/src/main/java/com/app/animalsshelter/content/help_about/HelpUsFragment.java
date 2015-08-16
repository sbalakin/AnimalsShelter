package com.app.animalsshelter.content.help_about;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.animalsshelter.BaseFragment;
import com.app.animalsshelter.R;

public class HelpUsFragment extends BaseFragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.activity_help_us, container, false);
        return root;
    }
}
