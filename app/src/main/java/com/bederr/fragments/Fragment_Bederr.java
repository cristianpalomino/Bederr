package com.bederr.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import pe.bederr.com.R;
import com.bederr.utils.Util_Fonts;

public class Fragment_Bederr extends Fragment {

    public static final Fragment_Bederr newInstance() {
        return new Fragment_Bederr();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_bederr, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((TextView) getView().findViewById(R.id.txt_que_esperas)).setTypeface(Util_Fonts.setPNASemiBold(getActivity()));
    }
}