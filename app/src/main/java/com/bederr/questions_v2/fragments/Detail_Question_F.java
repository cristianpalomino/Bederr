package com.bederr.questions_v2.fragments;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bederr.adapter.Adapter_Respuestas;
import com.bederr.beans.Local_DTO;
import com.bederr.beans.Respuesta_DTO;
import com.bederr.beans_v2.Answer_DTO;
import com.bederr.beans_v2.Question_DTO;
import com.bederr.fragments.Fragment_Master;
import com.bederr.main.Bederr;
import com.bederr.questions_v2.adapters.Answer_A;
import com.bederr.questions_v2.interfaces.OnSuccessAnswer;
import com.bederr.questions_v2.services.Service_Answers;
import com.bederr.session.Session_Manager;
import com.bederr.utils.PrettyTime;
import com.bederr.utils.RoundedTransformation;
import com.bederr.utils.Util_Categorias;
import com.bederr.utils.Util_Fonts;
import com.squareup.picasso.Picasso;

import pe.bederr.com.R;

import org.json.JSONException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Gantz on 30/09/14.
 */
public class Detail_Question_F extends Fragment_Master implements AdapterView.OnItemClickListener {

    protected Question_DTO question_dto;

    protected Adapter_Respuestas adapter_respuestas;
    protected ArrayList<Answer_DTO> answer_dtos = new ArrayList<Answer_DTO>();
    protected ListView lista_respuestas;

    private TextView nombreusuario;
    private TextView tiempopregunta;
    private TextView pregunta;
    private ImageView imagenusuario;
    private ImageView imagencategoriapregunta;
    private Button btn_responder;

    public Detail_Question_F() {
        setId_layout(R.layout.fragment_detalle_pregunta_v2);
        setId_container(R.id.frame_container);
    }

    public static final Detail_Question_F newInstance() {
        return new Detail_Question_F();
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        question_dto = ((Bederr) getActivity()).getQuestion_dto();
    }

    @Override
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
    }

    @Override
    protected void initView() {
        super.initView();
        initHeader();

        lista_respuestas = (ListView) getView().findViewById(R.id.lista_respuestas);

        Service_Answers service_answers = new Service_Answers(getBederr());
        service_answers.sendRequest(String.valueOf(question_dto.getId()));
        service_answers.setOnSuccessAnswer(new OnSuccessAnswer() {
            @Override
            public void onSuccessAnswer(boolean success,
                                        ArrayList<Answer_DTO> answer_dtos,
                                        String count,
                                        String next,
                                        String previous) {
                try {
                    if (success) {
                        Answer_A answer_a = new Answer_A(getBederr(), answer_dtos);
                        lista_respuestas.setAdapter(answer_a);
                        getLayout().removeView(getView_message());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void initActionBar() {
        super.initActionBar();

        ImageView action_left = (ImageView) getView().findViewById(R.id.action_left);
        TextView action_middle = (TextView) getView().findViewById(R.id.action_middle);
        action_middle.setTypeface(Util_Fonts.setPNASemiBold(getActivity()));

        action_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getActivity().getSupportFragmentManager();
                FragmentTransaction trans = manager.beginTransaction();
                trans.setCustomAnimations(R.animator.izquierda_derecha, R.animator.derecha_izquierda);
                trans.remove(Detail_Question_F.this);
                trans.commit();
                manager.popBackStack();
            }
        });
    }

    private void initHeader() {
        nombreusuario = (TextView) getView().findViewById(R.id.nombreusuario);
        tiempopregunta = (TextView) getView().findViewById(R.id.tiempopregunta);
        pregunta = (TextView) getView().findViewById(R.id.textopregunta);
        imagenusuario = (ImageView) getView().findViewById(R.id.imgusuariopregunta);
        imagencategoriapregunta = (ImageView) getView().findViewById(R.id.imgcategoriapregunta);
        btn_responder = (Button) getView().findViewById(R.id.btn_responder);

        nombreusuario.setTypeface(Util_Fonts.setPNALight(getActivity()));
        tiempopregunta.setTypeface(Util_Fonts.setPNACursivaLight(getActivity()));
        pregunta.setTypeface(Util_Fonts.setPNALight(getActivity()));
        btn_responder.setTypeface(Util_Fonts.setPNASemiBold(getActivity()));

        int resourceid = Util_Categorias.getImageCategory(question_dto.getCategory_name());
        Picasso.with(getActivity()).
                load(resourceid).
                centerCrop().
                fit().
                transform(new RoundedTransformation(65, 0)).
                into(imagencategoriapregunta);

        Picasso.with(getActivity()).
                load(question_dto.getOwner_photo()).
                placeholder(R.drawable.placeholder_usuario).
                transform(new RoundedTransformation(75, 0)).
                into(imagenusuario);

        nombreusuario.setText(question_dto.getOwner_fullname() + " preguntó:");
        pregunta.setText(question_dto.getContent());

        /*
        try {
            SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSZ");
            Date localDate = formater.parse(question_dto.getCreated_at());
            tiempopregunta.setText(new PrettyTime(getActivity()).getTimeAgo(localDate));
            tiempopregunta.setText(question_dto.getCreated_at());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        */

        btn_responder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Session_Manager session_manager = new Session_Manager(getActivity());
                    /*
                if (session_manager.isLogin()) {
                    final Dialog_Local_Pregunta_v2 dialog_local_pregunta_v2 = new Dialog_Local_Pregunta_v2(getActivity(), (Bederr) getActivity());
                    dialog_local_pregunta_v2.getWindow().setWindowAnimations(R.style.Dialog_Animation_DOWN_UP);
                    dialog_local_pregunta_v2.show();

                } else {
                    getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.animator.abajo_arriba_b, R.animator.arriba_abajo_b).add(R.id.container, Fragment_Entrar.newInstance("1"), Fragment_Entrar.class.getName()).addToBackStack(null).commit();
                }
                */
            }
        });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Detail_Question_F.this.getView().setFocusableInTouchMode(true);
        Detail_Question_F.this.getView().requestFocus();
        Detail_Question_F.this.getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        FragmentManager manager = getActivity().getSupportFragmentManager();
                        FragmentTransaction trans = manager.beginTransaction();
                        trans.setCustomAnimations(R.animator.izquierda_derecha, R.animator.derecha_izquierda);
                        trans.remove(Detail_Question_F.this);
                        trans.commit();
                        manager.popBackStack();
                        return true;
                    }
                }
                return false;
            }
        });
    }

    /**
     * Callback method to be invoked when an item in this AdapterView has
     * been clicked.
     * <p/>
     * Implementers can call getItemAtPosition(position) if they need
     * to access the data associated with the selected item.
     *
     * @param parent   The AdapterView where the click happened.
     * @param view     The view within the AdapterView that was clicked (this
     *                 will be a view provided by the adapter)
     * @param position The position of the view in the adapter.
     * @param id       The row id of the item that was clicked.
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        try {
            Local_DTO local_dto = new Local_DTO(((Respuesta_DTO) parent.getItemAtPosition(position)).getJsonObject().getJSONObject("local"));
            getBederr().setLocal_dto(local_dto);
            //getBederr().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.animator.izquierda_derecha_b,R.animator.izquierda_derecha_b).add(R.id.container, Fragment_Detalle_Local.newInstance(),Fragment_Detalle_Local.class.getName()).addToBackStack(null).commit();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}