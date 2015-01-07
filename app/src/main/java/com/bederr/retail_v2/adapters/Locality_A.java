package com.bederr.retail_v2.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bederr.beans_v2.Locality_DTO;
import pe.bederr.com.R;
import com.bederr.utils.Util_Fonts;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by Gantz on 5/07/14.
 */
public class Locality_A extends BaseAdapter {

    private Context context;
    private ArrayList<Locality_DTO> locality_dtos;
    private ArrayList<Locality_DTO> mlocality_dtos;
    private LayoutInflater inflater;

    public Locality_A(Context context, ArrayList<Locality_DTO> locality_dtos) {
        this.context = context;
        this.locality_dtos = locality_dtos;
        this.mlocality_dtos = new ArrayList<Locality_DTO>();
        this.mlocality_dtos.addAll(locality_dtos);
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return locality_dtos.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return locality_dtos.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder = null;
        Locality_DTO locality_dto = locality_dtos.get(position);

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_distrito, parent, false);
            holder = new Holder();

            holder.txtnombredistirto = (TextView) convertView.findViewById(R.id.txt_distrito_busquedas);

            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        holder.txtnombredistirto.setText(locality_dto.getName());
        holder.txtnombredistirto.setTypeface(Util_Fonts.setPNASemiBold(context));

        return convertView;
    }

    class Holder {
        TextView txtnombredistirto;
    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        locality_dtos.clear();
        if (charText.length() == 0) {
            locality_dtos.addAll(mlocality_dtos);
        } else {
            for (Locality_DTO wp : mlocality_dtos) {
                if (wp.getName().toLowerCase(Locale.getDefault()).contains(charText)) {
                    locality_dtos.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }
}
