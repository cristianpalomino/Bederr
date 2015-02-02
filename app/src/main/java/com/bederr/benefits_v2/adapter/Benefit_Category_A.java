package com.bederr.benefits_v2.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bederr.beans.Categoria_DTO;
import com.bederr.beans_v2.Category_DTO;
import com.bederr.utils.Util_Categorias;
import com.bederr.utils.Util_Fonts;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Locale;

import pe.bederr.com.R;

/**
 * Created by Gantz on 5/07/14.
 */
public class Benefit_Category_A extends BaseAdapter {

    private Context context;
    private ArrayList<Categoria_DTO> categoria_dtos;
    private ArrayList<Categoria_DTO> mcategory_dtos;
    private LayoutInflater inflater;

    public Benefit_Category_A(Context context, ArrayList<Categoria_DTO> categoria_dtos) {
        this.context = context;
        this.categoria_dtos = categoria_dtos;
        this.mcategory_dtos = new ArrayList<Categoria_DTO>();
        this.mcategory_dtos.addAll(categoria_dtos);
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
        Categoria_DTO category_dto = categoria_dtos.get(position);

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

        holder.txtcantidadcategoria.setText(category_dto.getCodigocategoria()+"");
        holder.txtnombrecategoria.setText(category_dto.getNombrecategoria());

        holder.txtnombrecategoria.setTypeface(Util_Fonts.setPNASemiBold(context));
        holder.txtcantidadcategoria.setTypeface(Util_Fonts.setPNALight(context));
        holder.txtcantidadcategoria.setVisibility(View.GONE);

        Picasso.with(context).
                load(Util_Categorias.getImageCategory(category_dto.getNombrecategoria())).
                into(holder.imgcategoria);

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
            categoria_dtos.addAll(mcategory_dtos);
        } else {
            for (Categoria_DTO wp : mcategory_dtos) {
                if (wp.getNombrecategoria().toLowerCase(Locale.getDefault()).contains(charText)) {
                    categoria_dtos.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }
}
