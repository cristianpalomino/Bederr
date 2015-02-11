package com.bederr.questions_v2.dialogs;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Build;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bederr.adapter.Adapter_Locales_Pregunta;
import com.bederr.application.Maven_Application;
import com.bederr.beans.Local_DTO;
import com.bederr.beans_v2.Place_DTO;
import com.bederr.fragments.Fragment_Responder;
import com.bederr.main.Bederr;
import com.bederr.operations.Operation_Busquedas;
import com.bederr.questions_v2.adapters.Places_Question_A;
import com.bederr.questions_v2.fragments.Answer_F;
import com.bederr.retail_v2.adapters.Places_A;
import com.bederr.retail_v2.interfaces.OnSuccessPlaces;
import com.bederr.retail_v2.services.Service_Places;
import com.bederr.session.Session_Manager;
import com.bederr.utils.Util_Fonts;

import java.util.ArrayList;

import pe.bederr.com.R;

/**
 * Created by Gantz on 7/08/14.
 */

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class Places_D extends AlertDialog implements AdapterView.OnItemClickListener {

    private ActionBarActivity actionBarActivity;
    private ListView lista_locales;
    private EditText action_middle;

    public Places_D(Context context, ActionBarActivity actionBarActivity) {
        super(context);
        this.actionBarActivity = actionBarActivity;
        initDialog();
    }

    public Places_D(Context context, boolean cancelable, OnCancelListener cancelListener, ActionBarActivity actionBarActivity) {
        super(context, cancelable, cancelListener);
        this.actionBarActivity = actionBarActivity;
        initDialog();
    }

    /**
     * Construct an AlertDialog that uses an explicit theme.  The actual style
     * that an AlertDialog uses is a private implementation, however you can
     * here supply either the name of an attribute in the theme from which
     * to get the dialog's style (such as {@link android.R.attr#alertDialogTheme}
     * or one of the constants {@link #THEME_TRADITIONAL},
     * {@link #THEME_HOLO_DARK}, or {@link #THEME_HOLO_LIGHT}.
     *
     * @param context
     * @param theme
     */
    public Places_D(Context context, int theme, ActionBarActivity actionBarActivity) {
        super(context, theme);
        this.actionBarActivity = actionBarActivity;
        initDialog();
    }

    private void initDialog() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        final View view = inflater.inflate(R.layout.dialog_locales, null);
        initActionBar(view);
        lista_locales = (ListView) view.findViewById(R.id.lista_locales);
        lista_locales.setOnItemClickListener(this);

        Places_Question_A places_question_a = new Places_Question_A(getContext(), ((Bederr) actionBarActivity).getPlace_dtos(), 0);
        lista_locales.setAdapter(places_question_a);

        setView(view);
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
        hide();
        ((Bederr) actionBarActivity).setPlace_dto((Place_DTO) parent.getItemAtPosition(position));
        actionBarActivity.getSupportFragmentManager().
                beginTransaction().
                setCustomAnimations(R.animator.izquierda_derecha_b, R.animator.izquierda_derecha_b).
                add(R.id.container, Answer_F.newInstance(), Answer_F.class.getName()).
                addToBackStack(null).commit();
        closeKeyboard();
    }

    protected void initActionBar(final View view) {
        ImageView action_left = (ImageView) view.findViewById(R.id.action_right);
        action_middle = (EditText) view.findViewById(R.id.edt_action_middle);
        action_middle.setTypeface(Util_Fonts.setPNALight(getContext()));

        action_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeKeyboard();
                dismiss();
            }
        });

        action_middle.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                    Maven_Application app = (Maven_Application) actionBarActivity.getApplication();

                    Session_Manager session_manager = new Session_Manager(actionBarActivity);
                    String lat = app.getUbication().getLatitude();
                    String lng = app.getUbication().getLongitude();
                    String name = action_middle.getText().toString();
                    String cat = "";
                    String city = "";
                    String locality = "";
                    String area = app.getUbication().getArea();

                    if (session_manager.isLogin()) {
                        Service_Places service_places = new Service_Places(actionBarActivity);
                        service_places.sendRequestUser(session_manager.getUserToken(), lat, lng, name, cat, locality, area);
                        service_places.setOnSuccessPlaces(new OnSuccessPlaces() {
                            @Override
                            public void onSuccessPlaces(boolean success,
                                                        ArrayList<Place_DTO> place_dtos,
                                                        String count,
                                                        String next,
                                                        String previous) {
                                try {
                                    if (success) {
                                        Places_Question_A places_a = new Places_Question_A(actionBarActivity , place_dtos, 0);
                                        lista_locales.setAdapter(places_a);
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }


                    return true;
                }
                return false;
            }
        });
    }

    public void closeKeyboard() {
        View view = actionBarActivity.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) actionBarActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }

        InputMethodManager imm = (InputMethodManager)actionBarActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(action_middle.getWindowToken(), 0);
    }
}
