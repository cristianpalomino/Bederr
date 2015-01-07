package com.bederr.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bederr.beans.Categoria_DTO;

import pe.bederr.com.R;
import com.bederr.utils.Util_Fonts;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by Gantz on 5/07/14.
 */
public class Adapter_Categoria extends BaseAdapter {

    private Context context;
    private ArrayList<Categoria_DTO> categoria_dtos;
    private ArrayList<Categoria_DTO> mategoria_dtos;
    private LayoutInflater inflater;

    public Adapter_Categoria(Context context, ArrayList<Categoria_DTO> categoria_dtos) {
        this.context = context;
        this.categoria_dtos = categoria_dtos;
        this.mategoria_dtos = new ArrayList<Categoria_DTO>();
        this.mategoria_dtos.addAll(categoria_dtos);
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return categoria_dtos.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return categoria_dtos.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder = null;
        Categoria_DTO categoria_dto = categoria_dtos.get(position);

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_categoria, parent, false);
            holder = new Holder();

            holder.txtnombrecategoria = (TextView) convertView.findViewById(R.id.txt_nombre_categoria_busqueda);
            holder.txtcantidadcategoria = (TextView) convertView.findViewById(R.id.txt_cantidad_categoria_busqueda);
            holder.imgcategoria = (ImageView) convertView.findViewById(R.id.img_categoria_busqueda);

            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        holder.txtcantidadcategoria.setText(categoria_dto.getCantidadcategoria());
        holder.txtnombrecategoria.setText(categoria_dto.getNombrecategoria());

        holder.txtnombrecategoria.setTypeface(Util_Fonts.setPNASemiBold(context));
        holder.txtcantidadcategoria.setTypeface(Util_Fonts.setPNALight(context));

        holder.imgcategoria.setImageResource(categoria_dto.getImagencategoria());

        return convertView;
    }

    class Holder {
        ImageView imgcategoria;
        TextView txtcantidadcategoria;
        TextView txtnombrecategoria;
    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        categoria_dtos.clear();
        if (charText.length() == 0) {
            categoria_dtos.addAll(mategoria_dtos);
        } else {
            for (Categoria_DTO wp : mategoria_dtos) {
                if (wp.getNombrecategoria().toLowerCase(Locale.getDefault()).contains(charText)) {
                    categoria_dtos.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }
}
