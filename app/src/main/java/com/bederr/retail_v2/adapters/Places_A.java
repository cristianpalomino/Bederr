package com.bederr.retail_v2.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bederr.beans_v2.Place_DTO;
import pe.bederr.com.R;
import com.bederr.utils.RoundedTransformation;
import com.bederr.utils.Util_Categorias;
import com.bederr.utils.Util_Fonts;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Gantz on 5/07/14.
 */
public class Places_A extends BaseAdapter {

    private Context context;
    private ArrayList<Place_DTO> place_dtos;
    private int tipo;
    private LayoutInflater inflater;

    public Places_A(Context context, ArrayList<Place_DTO> place_dtos, int tipo) {
        this.context = context;
        this.place_dtos = place_dtos;
        this.tipo = tipo;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return place_dtos.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return place_dtos.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Holder holder = null;
        Place_DTO place_dto = place_dtos.get(position);

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.view_local, parent, false);
            holder = new Holder();

            holder.nombre_local = (TextView) convertView.findViewById(R.id.txt_nombre_local);
            holder.direccion_local = (TextView) convertView.findViewById(R.id.txt_direccion_local);
            holder.categoria_local = (TextView) convertView.findViewById(R.id.txt_categoria_local);
            holder.distancia_local = (TextView) convertView.findViewById(R.id.txt_distancia_local);
            holder.img_categoria_local = (ImageView) convertView.findViewById(R.id.img_categoria_local);
            holder.img_verde = (ImageView) convertView.findViewById(R.id.img_cupon_verde);
            holder.img_azul = (ImageView) convertView.findViewById(R.id.img_cupon_celeste);
            holder.img_plomo = (ImageView) convertView.findViewById(R.id.img_cupon_plomo);

            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        holder.nombre_local.setText(place_dto.getName());
        holder.direccion_local.setText(place_dto.getAddress());
        holder.categoria_local.setText(place_dto.getCategory_name());

        if(tipo == 0) {
            String distancia = String.valueOf(round(Double.parseDouble(place_dto.getDistance()), 2));
            holder.distancia_local.setText(distancia + " mts.");
        }else{
            holder.distancia_local.setVisibility(View.GONE);
        }

        holder.nombre_local.setTypeface(Util_Fonts.setPNASemiBold(context));
        holder.direccion_local.setTypeface(Util_Fonts.setPNALight(context));
        holder.categoria_local.setTypeface(Util_Fonts.setPNACursivaLight(context));
        holder.distancia_local.setTypeface(Util_Fonts.setPNALight(context));

        Picasso.with(context).
                load(Util_Categorias.getImageCategory(place_dto.getCategory_name())).
                centerCrop().
                fit().
                transform(new RoundedTransformation(65, 0)).
                into(holder.img_categoria_local);

        holder.img_azul.setVisibility(place_dto.isInplace());
        holder.img_verde.setVisibility(place_dto.isEspecial());
        holder.img_plomo.setVisibility(place_dto.isCorporate());

        return convertView;
    }

    public void add(Place_DTO place_dto) {
        place_dtos.add(place_dto);
        notifyDataSetChanged();
    }

    class Holder {
        TextView nombre_local;
        TextView direccion_local;
        TextView categoria_local;
        TextView distancia_local;
        ImageView img_categoria_local;
        ImageView img_verde;
        ImageView img_azul;
        ImageView img_plomo;
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();
        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }
}
