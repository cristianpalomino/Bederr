package com.bederr.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bederr.beans.Distrito_DTO;

import pe.bederr.com.R;
import com.bederr.utils.Util_Fonts;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by Gantz on 5/07/14.
 */
public class Adapter_Distrito extends BaseAdapter {

    private Context context;
    private ArrayList<Distrito_DTO> distrito_dtos;
    private ArrayList<Distrito_DTO> mdistrito_dtos;
    private LayoutInflater inflater;

    public Adapter_Distrito(Context context, ArrayList<Distrito_DTO> distrito_dtos) {
        this.context = context;
        this.distrito_dtos = distrito_dtos;
        this.mdistrito_dtos = new ArrayList<Distrito_DTO>();
        this.mdistrito_dtos.addAll(distrito_dtos);
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return distrito_dtos.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return distrito_dtos.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder = null;
        Distrito_DTO distrito_dto = distrito_dtos.get(position);

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_distrito, parent, false);
            holder = new Holder();

            holder.txtnombredistirto = (TextView) convertView.findViewById(R.id.txt_distrito_busquedas);

            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        holder.txtnombredistirto.setText(distrito_dto.getNombredistrito());
        holder.txtnombredistirto.setTypeface(Util_Fonts.setPNASemiBold(context));

        return convertView;
    }

    class Holder {
        TextView txtnombredistirto;
    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        distrito_dtos.clear();
        if (charText.length() == 0) {
            distrito_dtos.addAll(mdistrito_dtos);
        } else {
            for (Distrito_DTO wp : mdistrito_dtos) {
                if (wp.getNombredistrito().toLowerCase(Locale.getDefault()).contains(charText)) {
                    distrito_dtos.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }
}
