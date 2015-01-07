package com.bederr.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bederr.beans.Respuesta_DTO;
import com.bederr.utils.PrettyTime;
import com.bederr.beans.Local_DTO;

import pe.bederr.com.R;

import com.bederr.utils.RoundedTransformation;
import com.bederr.utils.Util_Categorias;
import com.bederr.utils.Util_Fonts;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Gantz on 5/07/14.
 */
public class Adapter_Respuestas extends BaseAdapter {

    private Context context;
    private ArrayList<Respuesta_DTO> respuesta_dtos;
    private LayoutInflater inflater;

    public Adapter_Respuestas(Context context, ArrayList<Respuesta_DTO> respuesta_dtos) {
        this.context = context;
        this.respuesta_dtos = respuesta_dtos;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return respuesta_dtos.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return respuesta_dtos.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder = null;
        Respuesta_DTO respuesta_dto = respuesta_dtos.get(position);

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.view_respuesta, parent, false);
            holder = new Holder();

            holder.nombreusuario = (TextView) convertView.findViewById(R.id.nombreusuario);
            holder.tiempopregunta = (TextView) convertView.findViewById(R.id.tiempopregunta);
            holder.texto_respuesta = (TextView) convertView.findViewById(R.id.texto_respuesta);
            holder.nombre_local = (TextView) convertView.findViewById(R.id.nombre_local);
            holder.direccion_local = (TextView) convertView.findViewById(R.id.direccion_local);
            holder.categoria_local = (TextView) convertView.findViewById(R.id.categoria_local);
            holder.imagenusuario = (ImageView) convertView.findViewById(R.id.imgusuariopregunta);
            holder.img_categoria_local = (ImageView) convertView.findViewById(R.id.img_categoria_local);

            holder.img_verde = (ImageView) convertView.findViewById(R.id.img_cupon_verde);
            holder.img_azul = (ImageView) convertView.findViewById(R.id.img_cupon_celeste);
            holder.img_plomo = (ImageView) convertView.findViewById(R.id.img_cupon_plomo);

            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        holder.nombreusuario.setTypeface(Util_Fonts.setPNALight(context));
        holder.tiempopregunta.setTypeface(Util_Fonts.setPNACursivaLight(context));
        holder.texto_respuesta.setTypeface(Util_Fonts.setPNALight(context));
        holder.nombre_local.setTypeface(Util_Fonts.setPNASemiBold(context));
        holder.direccion_local.setTypeface(Util_Fonts.setPNALight(context));
        holder.categoria_local.setTypeface(Util_Fonts.setPNALight(context));

        try {
            JSONObject jsonObject = respuesta_dto.getJsonObject();

            JSONObject localObject = jsonObject.getJSONObject("local");
            Local_DTO local_dto = new Local_DTO(localObject);

            int resourceid = Util_Categorias.getImageCateogry(Integer.parseInt(localObject.getString("idCategoria")));
            Picasso.with(context).load(resourceid).centerCrop().fit().transform(new RoundedTransformation(65, 0)).into(holder.img_categoria_local);

            if (jsonObject.getString("FacebookId") != null) {
                String str = "https://graph.facebook.com/" + jsonObject.getString("FacebookId") + "/picture";
                Picasso.with(context).load(str).placeholder(R.drawable.placeholder_usuario).centerCrop().fit().transform(new RoundedTransformation(75, 0)).into(holder.imagenusuario);
            }

            holder.nombre_local.setText(localObject.getString("NombreLocal"));
            holder.direccion_local.setText(localObject.getString("Direccion"));
            holder.categoria_local.setText(localObject.getString("NombreCategoria"));
            holder.nombreusuario.setText(jsonObject.getString("NombreUsuario") + " respondió:");
            Date localDate = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(jsonObject.getString("date_register"));
            holder.tiempopregunta.setText(new PrettyTime(context).getTimeAgo(localDate));
            holder.texto_respuesta.setText(jsonObject.getString("respuesta"));

            holder.img_azul.setVisibility(local_dto.isAzul());
            holder.img_verde.setVisibility(local_dto.isVerde());
            holder.img_plomo.setVisibility(local_dto.isPlomo(context));

        } catch (Exception e) {
            e.printStackTrace();
        }

        return convertView;
    }

    public void add(Respuesta_DTO local_dto) {
        respuesta_dtos.add(local_dto);
        notifyDataSetChanged();
    }

    class Holder {
        TextView nombreusuario;
        TextView tiempopregunta;
        TextView texto_respuesta;
        TextView nombre_local;
        TextView direccion_local;
        TextView categoria_local;
        ImageView imagenusuario;
        ImageView img_categoria_local;
        ImageView img_verde;
        ImageView img_azul;
        ImageView img_plomo;
    }
}
