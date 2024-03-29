package com.bederr.dialog;

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

import com.bederr.beans.Local_DTO;
import com.bederr.main.Bederr;
import com.bederr.operations.Operation_Busquedas;
import com.bederr.adapter.Adapter_Locales_Pregunta;

import pe.bederr.com.R;
import com.bederr.fragments.Fragment_Responder;
import com.bederr.utils.Util_Fonts;

import java.util.ArrayList;

/**
 * Created by Gantz on 7/08/14.
 */

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class Dialog_Local_Pregunta_v2 extends AlertDialog implements AdapterView.OnItemClickListener {

    private ActionBarActivity actionBarActivity;
    private ListView lista_locales;

    public Dialog_Local_Pregunta_v2(Context context, ActionBarActivity actionBarActivity) {
        super(context);
        this.actionBarActivity = actionBarActivity;
        initDialog();
    }

    public Dialog_Local_Pregunta_v2(Context context, boolean cancelable, OnCancelListener cancelListener, ActionBarActivity actionBarActivity) {
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
    public Dialog_Local_Pregunta_v2(Context context, int theme, ActionBarActivity actionBarActivity) {
        super(context, theme);
        this.actionBarActivity = actionBarActivity;
        initDialog();
    }

    private void initDialog() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        final View view = inflater.inflate(R.layout.dialog_locales, null);
        initActionBar(view);
        lista_locales = (ListView) view.findViewById(R.id.lista_locales);
        Adapter_Locales_Pregunta adapter_locales_pregunta = new Adapter_Locales_Pregunta(getContext(), ((Bederr) actionBarActivity).getLocal_dtos(),0);
        lista_locales.setAdapter(adapter_locales_pregunta);
        lista_locales.setOnItemClickListener(this);

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
        ((Bederr) actionBarActivity).setLocal_dto((Local_DTO) parent.getItemAtPosition(position));
        actionBarActivity.getSupportFragmentManager().beginTransaction().setCustomAnimations(R.animator.izquierda_derecha_b, R.animator.izquierda_derecha_b).add(R.id.container, Fragment_Responder.newInstance(), Fragment_Responder.class.getName()).addToBackStack(null).commit();
    }

    protected void initActionBar(final View view) {
        ImageView action_left = (ImageView) view.findViewById(R.id.action_right);
        final EditText action_middle = (EditText) view.findViewById(R.id.edt_action_middle);
        action_middle.setTypeface(Util_Fonts.setPNALight(getContext()));

        action_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        action_middle.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    Operation_Busquedas operation_busquedas = new Operation_Busquedas(getContext());
                    operation_busquedas.buscarLocalesPorNombre(action_middle.getText().toString(),1);
                    operation_busquedas.setInterface_operation_busquedas(new Operation_Busquedas.Interface_Operation_Busquedas() {
                        @Override
                        public void getLocalesCercanos(boolean status, final ArrayList<Local_DTO> local_dtos, int paramInt, String message) {
                            if (status) {
                                Adapter_Locales_Pregunta adapter_locales_pregunta = new Adapter_Locales_Pregunta(getContext(),local_dtos,0);
                                lista_locales.setAdapter(adapter_locales_pregunta);
                            }
                        }
                    });
                    return true;
                }
                return false;
            }
        });
    }

    public void hideSoftKeyboard() {
        if(((Bederr)getContext()).getCurrentFocus()!=null) {
            InputMethodManager inputMethodManager = (InputMethodManager)getContext().getSystemService(getContext().INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(((Bederr)getContext()).getCurrentFocus().getWindowToken(), 0);
        }
    }
}
