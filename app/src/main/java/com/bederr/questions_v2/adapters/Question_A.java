package com.bederr.questions_v2.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bederr.beans_v2.Question_DTO;
import com.bederr.utils.PrettyTime;

import pe.bederr.com.R;

import com.bederr.utils.RoundedTransformation;
import com.bederr.utils.Util_Categorias;
import com.bederr.utils.Util_Fonts;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Gantz on 5/07/14.
 */
public class Question_A extends BaseAdapter {

    private Context context;
    private ArrayList<Question_DTO> question_dtos;
    private LayoutInflater inflater;

    public Question_A(Context context, ArrayList<Question_DTO> question_dtos) {
        this.context = context;
        this.question_dtos = question_dtos;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return question_dtos.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return question_dtos.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder = null;
        Question_DTO question_dto = question_dtos.get(position);

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.view_pregunta, parent, false);
            holder = new Holder();

            holder.nombreusuario = (TextView) convertView.findViewById(R.id.nombreusuario);
            holder.tiempopregunta = (TextView) convertView.findViewById(R.id.tiempopregunta);
            holder.cantidadrespuesta = (TextView) convertView.findViewById(R.id.cantidadrespuesta);
            holder.pregunta = (TextView) convertView.findViewById(R.id.textopregunta);
            holder.imagenusuario = (ImageView) convertView.findViewById(R.id.imgusuariopregunta);
            holder.imagencategoriapregunta = (ImageView) convertView.findViewById(R.id.imgcategoriapregunta);

            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        holder.nombreusuario.setTypeface(Util_Fonts.setPNALight(context));
        holder.tiempopregunta.setTypeface(Util_Fonts.setPNACursivaLight(context));
        holder.pregunta.setTypeface(Util_Fonts.setPNALight(context));
        holder.cantidadrespuesta.setTypeface(Util_Fonts.setPNACursivaLight(context));

        
        Picasso.with(context).
                load(Util_Categorias.getImageCategory(question_dto.getCategory_name())).
                centerInside().
                fit().
                transform(new RoundedTransformation(65, 0)).
                into(holder.imagencategoriapregunta);
        
        Picasso.with(context).
                load(question_dto.getOwner_photo()).
                placeholder(R.drawable.placeholder_usuario).
                transform(new RoundedTransformation(75, 0)).
                into(holder.imagenusuario);

        holder.nombreusuario.setText(question_dto.getOwner_fullname() + " preguntó:");

        /*
        try {
            SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSZ");
            Date localDate = formater.parse(question_dto.getCreated_at());
            holder.tiempopregunta.setText(new PrettyTime(context).getTimeAgo(localDate));
            holder.tiempopregunta.setText(question_dto.getCreated_at());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        */

        holder.cantidadrespuesta.setText(question_dto.getNum_answers() + " respuesta(s).");
        holder.pregunta.setText(question_dto.getContent());

        return convertView;
    }

    public void add(Question_DTO local_dto) {
        question_dtos.add(local_dto);
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
