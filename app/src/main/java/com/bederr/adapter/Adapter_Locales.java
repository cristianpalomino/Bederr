package com.bederr.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bederr.beans.Local_DTO;
import pe.bederr.com.R;
import com.bederr.utils.RoundedTransformation;
import com.bederr.utils.Util_Categorias;
import com.bederr.utils.Util_Fonts;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Gantz on 5/07/14.
 */
public class Adapter_Locales extends BaseAdapter {

    private Context context;
    private ArrayList<Local_DTO> local_dtos;
    private int tipo;
    private LayoutInflater inflater;

    public Adapter_Locales(Context context, ArrayList<Local_DTO> local_dtos, int tipo) {
        this.context = context;
        this.local_dtos = local_dtos;
        this.tipo = tipo;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return local_dtos.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return local_dtos.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Holder holder = null;
        Local_DTO local_dto = local_dtos.get(position);

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

        try {
            JSONObject jsonObject = local_dto.getJsonObject();
            holder.nombre_local.setText(jsonObject.getString("NombreLocal"));
            holder.direccion_local.setText(jsonObject.getString("Direccion"));
            holder.categoria_local.setText(jsonObject.getString("NombreCategoria"));

            holder.nombre_local.setTypeface(Util_Fonts.setPNASemiBold(context));
            holder.direccion_local.setTypeface(Util_Fonts.setPNALight(context));
            holder.categoria_local.setTypeface(Util_Fonts.setPNACursivaLight(context));
            holder.distancia_local.setTypeface(Util_Fonts.setPNALight(context));

            holder.img_azul.setVisibility(local_dto.isAzul());
            holder.img_verde.setVisibility(local_dto.isVerde());

            if (!jsonObject.isNull("Distancia")) {
                if (jsonObject.getString("Distancia").equals("")) {
                    holder.distancia_local.setVisibility(View.GONE);
                } else {
                    double distance = round(Double.parseDouble(jsonObject.getString("Distancia")), 2);
                    if(distance < 1.00){
                        holder.distancia_local.setText(String.valueOf(distance) + " Mts.");
                    }else{
                        holder.distancia_local.setText(String.valueOf(distance) + " kms.");
                    }
                }
            } else {
                holder.distancia_local.setVisibility(View.GONE);
            }

            int resourceid = Util_Categorias.getImageCateogry(Integer.parseInt(jsonObject.getString("idCategoria")));
            Picasso.with(context).load(resourceid).centerCrop().fit().transform(new RoundedTransformation(65, 0)).into(holder.img_categoria_local);

            if(tipo == 1){
                holder.img_plomo.setVisibility(local_dto.isPlomo(context));
                holder.img_azul.setVisibility(View.GONE);
                holder.img_verde.setVisibility(View.GONE);
            }else{
                holder.img_plomo.setVisibility(local_dto.isPlomo(context));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return convertView;
    }

    public void add(Local_DTO local_dto) {
        local_dtos.add(local_dto);
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
