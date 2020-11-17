package com.rndtechnosoft.fooddaily.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rndtechnosoft.fooddaily.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Info2Fragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public Info2Fragment() {
        // Required empty public constructor
    }

    public static Info2Fragment newInstance(String param1, String param2) {
        Info2Fragment fragment = new Info2Fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_info2, container, false);
    }
}
