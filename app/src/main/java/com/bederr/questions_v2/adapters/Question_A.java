package com.bederr.questions_v2.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bederr.beans_v2.Question_DTO;
import com.bederr.utils.PrettyTime;

import pe.bederr.com.R;

import com.bederr.utils.RoundedTransformation;
import com.bederr.utils.Util_Categorias;
import com.bederr.utils.Util_Fonts;
import com.bederr.utils.Util_Time;
import com.parse.ParseObject;
import com.squareup.picasso.Picasso;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

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

        String owner_photo = question_dto.getOwner_photo();
            Picasso.with(context).
                    load(owner_photo).
                    placeholder(R.drawable.placeholder_usuario).
                    fit().
                    transform(new RoundedTransformation(75, 0)).
                    into(holder.imagenusuario);


        holder.nombreusuario.setText(question_dto.getOwner_fullname() + " preguntó:");

        DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSSZ");
        DateTime dt = formatter.parseDateTime(question_dto.getCreated_at());
        holder.tiempopregunta.setText(Util_Time.getTimeAgo(dt.getMillis()));

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
