package com.bederr.questions_v2.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bederr.beans_v2.Answer_DTO;
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
public class Answer_A extends BaseAdapter {

    private Context context;
    private ArrayList<Answer_DTO> answer_dtos;
    private LayoutInflater inflater;

    public Answer_A(Context context, ArrayList<Answer_DTO> answer_dtos) {
        this.context = context;
        this.answer_dtos = answer_dtos;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return answer_dtos.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return answer_dtos.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder = null;
        Answer_DTO answer_dto = answer_dtos.get(position);

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

        int resourceid = Util_Categorias.getImageCategory(answer_dto.getPlace_dto().getCategory_code());
        Picasso.with(context).
                load(resourceid).
                centerCrop().
                fit().
                transform(new RoundedTransformation(65, 0)).
                into(holder.img_categoria_local);

        String str = answer_dto.getOwner_photo();
        Picasso.with(context).
                load(str).
                placeholder(R.drawable.placeholder_usuario).
                centerCrop().
                fit().
                transform(new RoundedTransformation(75, 0)).
                into(holder.imagenusuario);

        Place_DTO place_dto = answer_dto.getPlace_dto();

        holder.nombre_local.setText(place_dto.getName());
        holder.direccion_local.setText(place_dto.getAddress());
        holder.categoria_local.setText(place_dto.getCategory_name());
        holder.nombreusuario.setText(answer_dto.getOwner_fullname() + " respondió:");

        //Date localDate = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(jsonObject.getString("date_register"));
        //holder.tiempopregunta.setText(new PrettyTime(context).getTimeAgo(localDate));
        //holder.texto_respuesta.setText(jsonObject.getString("respuesta"));

        holder.img_azul.setVisibility(place_dto.isInplace());
        holder.img_verde.setVisibility(place_dto.isEspecial());
        holder.img_plomo.setVisibility(place_dto.isCorporate());

        return convertView;
    }

    public void add(Answer_DTO local_dto) {
        answer_dtos.add(local_dto);
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
