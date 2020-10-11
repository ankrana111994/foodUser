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
public class Info1Fragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public Info1Fragment() {
        // Required empty public constructor
    }

    public static Info1Fragment newInstance(String param1, String param2) {
        Info1Fragment fragment = new Info1Fragment();
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
        return inflater.inflate(R.layout.fragment_info1, container, false);
    }
}
