package com.bederr.dialog;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Build;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.bederr.main.Bederr;
import com.bederr.adapter.Adapter_Distrito;
import com.bederr.beans.Distrito_DTO;

import pe.bederr.com.R;
import com.bederr.fragments.Fragment_Preguntar;

import java.util.ArrayList;

/**
 * Created by Gantz on 7/08/14.
 */

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class Dialog_Distritos extends AlertDialog implements AdapterView.OnItemClickListener {

    private ActionBarActivity actionBarActivity;
    private LinearLayout frame_container;

    public Dialog_Distritos(Context context, ActionBarActivity actionBarActivity) {
        super(context);
        this.actionBarActivity = actionBarActivity;
        initDialog();
    }

    public Dialog_Distritos(Context context, boolean cancelable, OnCancelListener cancelListener, ActionBarActivity actionBarActivity) {
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
    public Dialog_Distritos(Context context, int theme, ActionBarActivity actionBarActivity) {
        super(context, theme);
        this.actionBarActivity = actionBarActivity;
        initDialog();
    }

    private void initDialog() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        final View view = inflater.inflate(R.layout.dialog_distritos, null);

        final ListView lista_distritos = (ListView) view.findViewById(R.id.lista_distritos);

        if(((Bederr)actionBarActivity).getDistrito_dtos() != null){
            ArrayList<Distrito_DTO> distrito_dtos = ((Bederr)actionBarActivity).getDistrito_dtos();
            Adapter_Distrito adapter_distrito = new Adapter_Distrito(getContext(),distrito_dtos);
            lista_distritos.setAdapter(adapter_distrito);
            lista_distritos.setOnItemClickListener(this);
        }

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
        ((Bederr)actionBarActivity).setDistrito_dto((Distrito_DTO) parent.getItemAtPosition(position));
        ((Fragment_Preguntar) actionBarActivity.getSupportFragmentManager().findFragmentByTag(Fragment_Preguntar.class.getName())).getBtn_mi_ubicacion().setText(((Distrito_DTO) parent.getItemAtPosition(position)).getNombredistrito());
    }
}
