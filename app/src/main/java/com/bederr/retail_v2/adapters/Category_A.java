package com.bederr.retail_v2.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bederr.beans_v2.Category_DTO;
import pe.bederr.com.R;
import com.bederr.utils.RoundedTransformation;
import com.bederr.utils.Util_Categorias;
import com.bederr.utils.Util_Fonts;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by Gantz on 5/07/14.
 */
public class Category_A extends BaseAdapter {

    private Context context;
    private ArrayList<Category_DTO> category_dtos;
    private ArrayList<Category_DTO> mcategory_dtos;
    private LayoutInflater inflater;

    public Category_A(Context context, ArrayList<Category_DTO> category_dtos) {
        this.context = context;
        this.category_dtos = category_dtos;
        this.mcategory_dtos = new ArrayList<Category_DTO>();
        this.mcategory_dtos.addAll(category_dtos);
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return category_dtos.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return category_dtos.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder = null;
        Category_DTO category_dto = category_dtos.get(position);

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

        holder.txtcantidadcategoria.setText(category_dto.getId());
        holder.txtnombrecategoria.setText(category_dto.getName());

        holder.txtnombrecategoria.setTypeface(Util_Fonts.setPNASemiBold(context));
        holder.txtcantidadcategoria.setTypeface(Util_Fonts.setPNALight(context));
        holder.txtcantidadcategoria.setVisibility(View.GONE);

        Picasso.with(context).
                load(Util_Categorias.getImageCategory(category_dto.getName())).
                centerCrop().
                fit().
                transform(new RoundedTransformation(65, 0)).
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
        category_dtos.clear();
        if (charText.length() == 0) {
            category_dtos.addAll(mcategory_dtos);
        } else {
            for (Category_DTO wp : mcategory_dtos) {
                if (wp.getName().toLowerCase(Locale.getDefault()).contains(charText)) {
                    category_dtos.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }
}
