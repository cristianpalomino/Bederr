package com.bederr.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bederr.beans.Lista_DTO;

import pe.bederr.com.R;
import com.bederr.utils.Util_Fonts;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Gantz on 5/07/14.
 */
public class Adapter_Listas extends BaseAdapter {

    private Context context;
    private ArrayList<Lista_DTO> lista_dtos;
    private LayoutInflater inflater;

    public Adapter_Listas(Context context, ArrayList<Lista_DTO> lista_dtos) {
        this.context = context;
        this.lista_dtos = lista_dtos;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return lista_dtos.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return lista_dtos.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder = null;
        Lista_DTO lista_dto = lista_dtos.get(position);

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_lista, parent, false);
            holder = new Holder();

            holder.top_lista = (TextView) convertView.findViewById(R.id.txt_top_ten);
            holder.titulo_lista = (TextView) convertView.findViewById(R.id.txt_titulo_lista);
            holder.autor_lista = (TextView) convertView.findViewById(R.id.txt_autor_lista);
            holder.img_lista = (ImageView) convertView.findViewById(R.id.img_lista);

            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        String url = lista_dto.getImage_lista();
        Picasso.with(context).load(url).fit().placeholder(R.drawable.placeholder_listas).into(holder.img_lista);

        holder.top_lista.setText(lista_dto.getId_lista());
        holder.titulo_lista.setText(lista_dto.getTitle_lista());
        holder.autor_lista.setText(lista_dto.getDecription_lista());

        holder.top_lista.setTypeface(Util_Fonts.setPNASemiBold(context));
        holder.titulo_lista.setTypeface(Util_Fonts.setPNASemiBold(context));
        holder.autor_lista.setTypeface(Util_Fonts.setPNASemiBold(context));

        return convertView;
    }

    public void add(Lista_DTO lista_dto){
        lista_dtos.add(lista_dto);
        notifyDataSetChanged();
    }

    class Holder {
        TextView top_lista;
        TextView titulo_lista;
        TextView autor_lista;
        ImageView img_lista;
    }
}
