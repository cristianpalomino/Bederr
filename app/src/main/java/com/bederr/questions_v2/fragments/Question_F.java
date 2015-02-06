package com.bederr.questions_v2.fragments;

import android.app.Activity;
import android.location.Location;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import com.bederr.beans_v2.Place_DTO;
import com.bederr.beans_v2.Question_DTO;
import com.bederr.dialog.Dialog_Categoria;
import com.bederr.fragments.Fragment_Entrar;
import com.bederr.fragments.Fragment_Master;
import com.bederr.main.Bederr;
import com.bederr.questions_v2.adapters.Question_A;
import com.bederr.questions_v2.interfaces.OnSuccessQuestion;
import com.bederr.questions_v2.services.Service_Question;
import com.bederr.retail_v2.interfaces.OnSuccessPlaces;
import com.bederr.retail_v2.services.Service_Places;
import com.bederr.session.Session_Manager;
import com.bederr.utils.Util_Fonts;

import java.util.ArrayList;

import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;
import pe.bederr.com.R;

/**
 * Created by Gantz on 30/09/14.
 */
public class Question_F extends Fragment_Master implements AdapterView.OnItemClickListener, AbsListView.OnScrollListener {

    private ListView lista_preguntas;
    private Question_A question_a;
    private boolean isLoading = false;

    public Question_F() {
        setId_layout(R.layout.fragment_preguntas_v2);
        setId_container(R.id.frame_container);
    }

    public static final Question_F newInstance() {
        return new Question_F();
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle bundle) {
        return super.onCreateView(inflater, viewGroup, bundle);
    }

    @Override
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
    }

    @Override
    protected void initView() {
        super.initView();
        closeKeyboard();

        lista_preguntas = (ListView) getView().findViewById(R.id.lista_preguntas);
        lista_preguntas.setOnItemClickListener(this);
        lista_preguntas.setOnScrollListener(this);
        lista_preguntas.setVisibility(View.GONE);

        Session_Manager session_manager = new Session_Manager(getBederr());
        if (session_manager.isLogin()) {
            openLoginUser(session_manager.getUserToken());
        } else {
            openLogin();
        }
    }

    private void openLogin() {
        String lat = getUbication().getLatitude();
        String lng = getUbication().getLongitude();
        String area = getUbication().getArea();
        Service_Question service_question = new Service_Question(getBederr());
        service_question.sendRequest(lat, lng, area);
        service_question.setOnSuccessQuestion(new OnSuccessQuestion() {
            @Override
            public void onSuccessQuestion(boolean success,
                                          ArrayList<Question_DTO> question_dtos,
                                          String count,
                                          String next,
                                          String previous) {
                try {
                    if (success) {
                        question_a = new Question_A(getBederr(), question_dtos);
                        lista_preguntas.setAdapter(question_a);
                        Question_F.this.onFinishLoad(lista_preguntas);

                        /**
                         * Stacks
                         */
                        PREVIOUS = previous;
                        NEXT = next;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void openLoginUser(String token) {
        String lat = getUbication().getLatitude();
        String lng = getUbication().getLongitude();
        String area = getUbication().getArea();

        Service_Question service_question = new Service_Question(getBederr());
        service_question.sendRequestUser(token, lat, lng, area);
        service_question.setOnSuccessQuestion(new OnSuccessQuestion() {
            @Override
            public void onSuccessQuestion(boolean success,
                                          ArrayList<Question_DTO> question_dtos,
                                          String count,
                                          String next,
                                          String previous) {
                try {
                    if (success) {
                        question_a = new Question_A(getBederr(), question_dtos);
                        lista_preguntas.setAdapter(question_a);
                        Question_F.this.onFinishLoad(lista_preguntas);

                        /**
                         * Stacks
                         */
                        PREVIOUS = previous;
                        NEXT = next;
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
        Button action_middle = (Button) getView().findViewById(R.id.btn_action_middle);
        action_middle.setTypeface(Util_Fonts.setPNASemiBold(getActivity()));

        action_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Bederr) getActivity()).sm_menu.toggle();
            }
        });

        action_middle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Session_Manager session_manager = new Session_Manager(getActivity());
                if (session_manager.isLogin()) {
                    Dialog_Categoria dialog_categoria = new Dialog_Categoria(getActivity(), (Bederr) getActivity());
                    dialog_categoria.getWindow().setWindowAnimations(R.style.Dialog_Animation_UP_DOWN);
                    dialog_categoria.show();
                } else {
                    getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.animator.abajo_arriba_b, R.animator.arriba_abajo_b).add(R.id.container, Fragment_Entrar.newInstance("1"), Fragment_Entrar.class.getName()).addToBackStack(null).commit();
                }
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
        Question_DTO question_dto = (Question_DTO) parent.getItemAtPosition(position);
        ((Bederr) getActivity()).setQuestion_dto(question_dto);
        getActivity().getSupportFragmentManager().
                beginTransaction().
                setCustomAnimations(R.animator.izquierda_derecha_b, R.animator.izquierda_derecha_b).
                add(R.id.container, Detail_Question_F.newInstance(), Detail_Question_F.class.getName()).
                addToBackStack(null).commit();
    }

    /**
     * Callback method to be invoked while the list view or grid view is being scrolled. If the
     * view is being scrolled, this method will be called before the next frame of the scroll is
     * rendered. In particular, it will be called before any calls to
     *
     * @param view        The view whose scroll state is being reported
     * @param scrollState The current scroll state. One of
     *                    {@link #SCROLL_STATE_TOUCH_SCROLL} or {@link #SCROLL_STATE_IDLE}.
     */
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    /**
     * Callback method to be invoked when the list or grid has been scrolled. This will be
     * called after the scroll has completed
     *
     * @param view             The view whose scroll state is being reported
     * @param firstVisibleItem the index of the first visible cell (ignore if
     *                         visibleItemCount == 0)
     * @param visibleItemCount the number of visible cells
     * @param totalItemCount   the number of items in the list adaptor
     */
    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (firstVisibleItem + visibleItemCount == totalItemCount && totalItemCount != 0) {
            if (!isLoading) {
                isLoading = true;
                Service_Question service_question = new Service_Question(getBederr());
                service_question.loadMore(NEXT);
                service_question.setOnSuccessQuestion(new OnSuccessQuestion() {
                    @Override
                    public void onSuccessQuestion(boolean success,
                                                  ArrayList<Question_DTO> question_dtos,
                                                  String count,
                                                  String next,
                                                  String previous) {
                        try {
                            if (success) {
                                for (int i = 0; i < question_dtos.size(); i++) {
                                    question_a.add(question_dtos.get(i));
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Question_F.this.getView().setFocusableInTouchMode(true);
        Question_F.this.getView().requestFocus();
        Question_F.this.getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        getBederr().clearHistory();
                        getBederr().finish();
                        return true;
                    }
                }
                return false;
            }
        });
    }
}
