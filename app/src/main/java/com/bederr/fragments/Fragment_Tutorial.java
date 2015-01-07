package com.bederr.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import pe.bederr.com.R;
import com.bederr.utils.Util_Fonts;

public class Fragment_Tutorial extends Fragment {

    protected int titulo, descripcion, imagen, color;

    public static final Fragment_Tutorial newInstance(int titulo, int descripcion, int imagen,int color) {
        Fragment_Tutorial fragment_tutorial = new Fragment_Tutorial();
        Bundle bundle = new Bundle();
        bundle.putInt("titulo", titulo);
        bundle.putInt("descripcion", descripcion);
        bundle.putInt("imagen", imagen);
        bundle.putInt("color", color);
        fragment_tutorial.setArguments(bundle);
        return fragment_tutorial;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        titulo = getArguments().getInt("titulo");
        descripcion = getArguments().getInt("descripcion");
        imagen = getArguments().getInt("imagen");
        color = getArguments().getInt("color");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tutorial, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        TextView txt_titulo = (TextView) Fragment_Tutorial.this.getView().findViewById(R.id.titulo_icono_tutorial);
        TextView txt_desc = (TextView) Fragment_Tutorial.this.getView().findViewById(R.id.desc_icono_tutorial);
        ImageView img_tutorial = (ImageView) Fragment_Tutorial.this.getView().findViewById(R.id.img_icono_tutorial);

        txt_titulo.setTypeface(Util_Fonts.setPNASemiBold(getActivity()));
        txt_desc.setTypeface(Util_Fonts.setPNALight(getActivity()));

        txt_titulo.setText(getString(titulo));
        txt_titulo.setTextColor(getResources().getColor(color));

        txt_desc.setText(getString(descripcion));
        img_tutorial.setBackgroundResource(imagen);
    }
}