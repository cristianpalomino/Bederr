package com.bederr.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bederr.utils.PrettyTime;
import com.bederr.beans.Pregunta_DTO;
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
public class Adapter_Preguntas extends BaseAdapter {

    private Context context;
    private ArrayList<Pregunta_DTO> pregunta_dtos;
    private LayoutInflater inflater;

    public Adapter_Preguntas(Context context, ArrayList<Pregunta_DTO> pregunta_dtos) {
        this.context = context;
        this.pregunta_dtos = pregunta_dtos;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return pregunta_dtos.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return pregunta_dtos.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder = null;
        Pregunta_DTO pregunta_dto = pregunta_dtos.get(position);

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.view_pregunta, parent, false);
            holder = new Holder();

            holder.nombreusuario = (TextView)convertView.findViewById(R.id.nombreusuario);
            holder.tiempopregunta = (TextView)convertView.findViewById(R.id.tiempopregunta);
            holder.cantidadrespuesta = (TextView)convertView.findViewById(R.id.cantidadrespuesta);
            holder.pregunta = (TextView)convertView.findViewById(R.id.textopregunta);
            holder.imagenusuario = (ImageView)convertView.findViewById(R.id.imgusuariopregunta);
            holder.imagencategoriapregunta = (ImageView)convertView.findViewById(R.id.imgcategoriapregunta);

            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        try {
            JSONObject jsonObject = pregunta_dto.getJsonObject();

            int resourceid = Util_Categorias.getImageCateogry(Integer.parseInt(jsonObject.getString("idCategoria")));
            Picasso.with(context).load(resourceid).centerInside().fit().transform(new RoundedTransformation(65, 0)).into(holder.imagencategoriapregunta);

            if (jsonObject.getString("FacebookId") != null){
                String str = "https://graph.facebook.com/" + jsonObject.getString("FacebookId") + "/picture";
                Picasso.with(context).load(str).placeholder(R.drawable.placeholder_usuario).transform(new RoundedTransformation(75, 0)).into(holder.imagenusuario);
            }

            holder.nombreusuario.setTypeface(Util_Fonts.setPNALight(context));
            holder.tiempopregunta.setTypeface(Util_Fonts.setPNACursivaLight(context));
            holder.pregunta.setTypeface(Util_Fonts.setPNALight(context));
            holder.cantidadrespuesta.setTypeface(Util_Fonts.setPNACursivaLight(context));

            holder.nombreusuario.setText(jsonObject.getString("NombreUsuario") + " preguntó:");

            Date localDate = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(jsonObject.getString("fecha_creacion"));
            holder.tiempopregunta.setText(new PrettyTime(context).getTimeAgo(localDate));

            holder.cantidadrespuesta.setText(jsonObject.getString("totalRespuestas") + " respuesta(s).");
            holder.pregunta.setText(jsonObject.getString("pregunta"));

        }catch (Exception e){
            e.printStackTrace();
        }

        return convertView;
    }

    public void add(Pregunta_DTO local_dto){
        pregunta_dtos.add(local_dto);
        notifyDataSetChanged();
    }

    class Holder {
        TextView nombreusuario;
        TextView tiempopregunta;
        TextView cantidadrespuesta;
        TextView pregunta;
        ImageView imagenusuario;
        ImageView imagencategoriapregunta;
    }
}
