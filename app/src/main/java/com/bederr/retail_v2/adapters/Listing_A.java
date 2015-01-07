package com.bederr.retail_v2.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bederr.beans_v2.Listing_DTO;

import pe.bederr.com.R;
import com.bederr.utils.Util_Fonts;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Gantz on 5/07/14.
 */
public class Listing_A extends BaseAdapter {

    private Context context;
    private ArrayList<Listing_DTO> listing_dtos;
    private LayoutInflater inflater;

    public Listing_A(Context context, ArrayList<Listing_DTO> listing_dtos) {
        this.context = context;
        this.listing_dtos = listing_dtos;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return listing_dtos.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return listing_dtos.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder = null;
        Listing_DTO listing_dto = listing_dtos.get(position);

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

        Picasso.with(context).load(listing_dto.getImage()).fit().placeholder(R.drawable.placeholder_listas).into(holder.img_lista);

        holder.top_lista.setText("10");
        holder.titulo_lista.setText(listing_dto.getName());
        //holder.autor_lista.setText();

        holder.top_lista.setTypeface(Util_Fonts.setPNASemiBold(context));
        holder.titulo_lista.setTypeface(Util_Fonts.setPNASemiBold(context));
        holder.autor_lista.setTypeface(Util_Fonts.setPNASemiBold(context));

        return convertView;
    }

    public void add(Listing_DTO lista_dto){
        listing_dtos.add(lista_dto);
        notifyDataSetChanged();
    }

    class Holder {
        TextView top_lista;
        TextView titulo_lista;
        TextView autor_lista;
        ImageView img_lista;
    }
}
