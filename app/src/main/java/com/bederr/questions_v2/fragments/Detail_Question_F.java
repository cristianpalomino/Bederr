package com.bederr.questions_v2.fragments;

import android.app.ProgressDialog;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bederr.account_v2.fragments.Fragment_Entrar_v2;
import com.bederr.account_v2.fragments.Fragment_Perfil_v2;
import com.bederr.adapter.Adapter_Respuestas;
import com.bederr.application.Maven_Application;
import com.bederr.beans.Local_DTO;
import com.bederr.beans.Respuesta_DTO;
import com.bederr.beans_v2.Answer_DTO;
import com.bederr.beans_v2.Place_DTO;
import com.bederr.beans_v2.Question_DTO;
import com.bederr.benefits_v2.adapter.Benefit_A;
import com.bederr.fragments.Fragment_Master;
import com.bederr.main.Bederr;
import com.bederr.questions_v2.adapters.Answer_A;
import com.bederr.questions_v2.adapters.Places_Question_A;
import com.bederr.questions_v2.dialogs.Places_D;
import com.bederr.questions_v2.interfaces.OnSuccessAnswer;
import com.bederr.questions_v2.interfaces.OnSuccessQuestion;
import com.bederr.questions_v2.services.Service_Answers;
import com.bederr.questions_v2.services.Service_Question;
import com.bederr.retail_v2.fragments.Detail_Place_F;
import com.bederr.retail_v2.interfaces.OnSuccessPlaces;
import com.bederr.retail_v2.services.Service_Places;
import com.bederr.session.Session_Manager;
import com.bederr.utils.PrettyTime;
import com.bederr.utils.RoundedTransformation;
import com.bederr.utils.Util_Categorias;
import com.bederr.utils.Util_Fonts;
import com.bederr.utils.Util_Time;
import com.squareup.picasso.Picasso;

import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;
import pe.bederr.com.R;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Gantz on 30/09/14.
 */
public class Detail_Question_F extends Fragment_Master implements AdapterView.OnItemClickListener, AbsListView.OnScrollListener {

    protected Question_DTO question_dto;

    protected Adapter_Respuestas adapter_respuestas;
    protected ArrayList<Answer_DTO> answer_dtos = new ArrayList<Answer_DTO>();
    protected ListView lista_respuestas;
    private boolean isLoading = false;
    private Answer_A answer_a;

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
        lista_respuestas.setOnItemClickListener(this);
        lista_respuestas.setOnScrollListener(this);

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
                        if (!answer_dtos.isEmpty()) {
                            answer_a = new Answer_A(getBederr(), answer_dtos);
                            lista_respuestas.setAdapter(answer_a);
                            getEmptyView();

                            /**
                             * Stacks
                             */
                            PREVIOUS = previous;
                            NEXT = next;
                        } else {
                            setEmptyView(lista_respuestas);
                        }
                    } else {
                        setEmptyView(lista_respuestas);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    setEmptyView(lista_respuestas);
                }
            }
        });

        closeKeyboard();
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
                /*
                FragmentManager manager = getActivity().getSupportFragmentManager();
                FragmentTransaction trans = manager.beginTransaction();
                trans.setCustomAnimations(R.animator.izquierda_derecha, R.animator.derecha_izquierda);
                trans.remove(Detail_Question_F.this);
                trans.commit();
                manager.popBackStack();
                */
                getActivity().getSupportFragmentManager().
                        beginTransaction().
                        replace(R.id.container, Question_F.newInstance(), Question_F.class.getName()).
                        commit();
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
        Picasso.with(getBederr()).
                load(resourceid).
                centerInside().
                fit().
                transform(new RoundedTransformation(65, 0)).
                into(imagencategoriapregunta);

        String owner_photo = question_dto.getOwner_photo();
        Picasso.with(getBederr()).
                load(owner_photo).
                placeholder(R.drawable.placeholder_usuario).
                fit().
                transform(new RoundedTransformation(75, 0)).
                into(imagenusuario);

        nombreusuario.setText(question_dto.getOwner_fullname() + " preguntó:");
        pregunta.setText(question_dto.getContent());

        DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSSZ");
        DateTime dt = formatter.parseDateTime(question_dto.getCreated_at());
        tiempopregunta.setText(Util_Time.getTimeAgo(dt.getMillis()));

        btn_responder.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Session_Manager session_manager = new Session_Manager(getActivity());
                        btn_responder.setClickable(false);

                        if (session_manager.isLogin()) {
                            final ProgressDialog dialog = ProgressDialog.show(getBederr(),null,"Espere",false);

                            String lat = getUbication().getLatitude();
                            String lng = getUbication().getLongitude();
                            String name = "";
                            String cat = question_dto.getCategory_code();
                            String city = "";
                            String area = getUbication().getArea();

                            Service_Places service_places = new Service_Places(getBederr());
                            service_places.sendRequest(lat, lng, name, cat, city, area);
                            service_places.setOnSuccessPlaces(new OnSuccessPlaces() {
                                @Override
                                public void onSuccessPlaces(boolean success,
                                                            ArrayList<Place_DTO> place_dtos,
                                                            String count,
                                                            String next,
                                                            String previous) {
                                    dialog.hide();
                                    if (success) {
                                        final Places_D places_d = new Places_D(getActivity(), (Bederr) getActivity());
                                        places_d.getWindow().setWindowAnimations(R.style.Dialog_Animation_DOWN_UP);
                                        getBederr().setPlace_dtos(place_dtos);
                                        places_d.initDialog();
                                        places_d.show();
                                        btn_responder.setClickable(true);
                                    }
                                }
                            });
                        } else {
                            getActivity().
                                    getSupportFragmentManager().
                                    beginTransaction().
                                    replace(R.id.container, Fragment_Entrar_v2.newInstance("0"), Fragment_Entrar_v2.class.getName()).
                                    commit();
                        }
                    }
                }
        );
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
                        /*
                        FragmentManager manager = getActivity().getSupportFragmentManager();
                        FragmentTransaction trans = manager.beginTransaction();
                        trans.setCustomAnimations(R.animator.izquierda_derecha, R.animator.derecha_izquierda);
                        trans.remove(Detail_Question_F.this);
                        trans.commit();
                        manager.popBackStack();
                        */

                        getActivity().getSupportFragmentManager().
                                beginTransaction().
                                replace(R.id.container, Question_F.newInstance(), Question_F.class.getName()).
                                commit();

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
        Place_DTO place_dto = ((Answer_DTO) parent.getItemAtPosition(position)).getPlace_dto();
        ((Bederr) getActivity()).setPlace_dto(place_dto);
        getActivity().getSupportFragmentManager().beginTransaction().
                setCustomAnimations(R.animator.izquierda_derecha_b, R.animator.izquierda_derecha_b).
                add(R.id.container, Detail_Place_F.newInstance("Explore"), Detail_Place_F.class.getName()).
                addToBackStack(null).commit();
    }

    @Override
    public void onScrollStateChanged(AbsListView absListView, int i) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
                         int totalItemCount) {
        if (firstVisibleItem + visibleItemCount == totalItemCount && totalItemCount != 0) {
            if (!isLoading) {
                isLoading = true;

                Service_Answers service_answers = new Service_Answers(getBederr());
                service_answers.loadMore(NEXT);
                service_answers.setOnSuccessAnswer(new OnSuccessAnswer() {
                    @Override
                    public void onSuccessAnswer(boolean success,
                                                ArrayList<Answer_DTO> answer_dtos,
                                                String count,
                                                String next,
                                                String previous) {
                        try {
                            if (success) {
                                for (int i = 0; i < answer_dtos.size(); i++) {
                                    answer_a.add(answer_dtos.get(i));
                                }

                                /**
                                 * Stacks
                                 */
                                NEXT = next;
                                PREVIOUS = previous;
                                isLoading = false;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }
    }
}