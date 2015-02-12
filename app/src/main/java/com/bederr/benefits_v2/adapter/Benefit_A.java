package com.bederr.benefits_v2.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bederr.beans_v2.Benefit_Program_DTO;
import com.bederr.beans_v2.Benefit_Program_DTO;
import com.bederr.utils.RoundedTransformation;
import com.bederr.utils.Util_Categorias;
import com.bederr.utils.Util_Fonts;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import pe.bederr.com.R;

/**
 * Created by Gantz on 5/07/14.
 */
public class Benefit_A extends BaseAdapter {

    private Context context;
    private ArrayList<Benefit_Program_DTO> benefit_program_dtos;
    private int type;
    private LayoutInflater inflater;

    public Benefit_A(Context context, ArrayList<Benefit_Program_DTO> benefit_program_dtos, int type) {
        this.context = context;
        this.benefit_program_dtos = benefit_program_dtos;
        this.inflater = LayoutInflater.from(context);
        this.type = type;
    }

    @Override
    public int getCount() {
        return benefit_program_dtos.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return benefit_program_dtos.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Holder holder = null;
        Benefit_Program_DTO benefit_program_dto = benefit_program_dtos.get(position);

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_empresa, parent, false);
            holder = new Holder();

            holder.nombreempresa = (TextView) convertView.findViewById(R.id.nombreempresa);
            holder.descipcionempresa = (TextView) convertView.findViewById(R.id.descripcionempresa);
            holder.imagenempresa = (ImageView) convertView.findViewById(R.id.imgempresa);
            holder.fondoempresa = (LinearLayout) convertView.findViewById(R.id.itemfondoempresa);

            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        holder.nombreempresa.setTag(type);
        holder.nombreempresa.setText(benefit_program_dto.getName().toUpperCase());
        holder.descipcionempresa.setText(benefit_program_dto.getDescription());
        holder.descipcionempresa.setText(benefit_program_dto.getCompany_name());
        holder.fondoempresa.setBackgroundColor(Color.parseColor("#" + benefit_program_dto.getColor()));

        String logo = benefit_program_dto.getCompany_logo();
        Picasso.with(context).
                load(logo).
                centerCrop().
                fit().
                placeholder(R.drawable.placeholder_empresa).
                transform(new RoundedTransformation(65, 0)).
                into(holder.imagenempresa);


        holder.nombreempresa.setTypeface(Util_Fonts.setPNASemiBold(context));
        holder.descipcionempresa.setTypeface(Util_Fonts.setPNALight(context));

        return convertView;
    }

    public void add(Benefit_Program_DTO benefit_Program_DTO) {
        benefit_program_dtos.add(benefit_Program_DTO);
        notifyDataSetChanged();
    }

    class Holder {
        TextView nombreempresa;
        TextView descipcionempresa;
        ImageView imagenempresa;
        LinearLayout fondoempresa;
    }
}
