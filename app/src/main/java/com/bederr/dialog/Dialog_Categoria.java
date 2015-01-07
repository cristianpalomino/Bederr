package com.bederr.dialog;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Build;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.bederr.main.Bederr;
import com.bederr.adapter.Adapter_Categoria_Pregunta;
import com.bederr.beans.Categoria_DTO;

import pe.bederr.com.R;

import com.bederr.fragments.Fragment_Preguntar;

import java.util.ArrayList;

/**
 * Created by Gantz on 7/08/14.
 */

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class Dialog_Categoria extends AlertDialog implements AdapterView.OnItemClickListener {

    private ActionBarActivity actionBarActivity;

    public Dialog_Categoria(Context context, ActionBarActivity actionBarActivity) {
        super(context);
        this.actionBarActivity = actionBarActivity;
        initDialog();
    }

    public Dialog_Categoria(Context context, boolean cancelable, OnCancelListener cancelListener, ActionBarActivity actionBarActivity) {
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
    public Dialog_Categoria(Context context, int theme, ActionBarActivity actionBarActivity) {
        super(context, theme);
        this.actionBarActivity = actionBarActivity;
        initDialog();
    }

    private void initDialog() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        final View view = inflater.inflate(R.layout.dialog_categoria, null);
        setView(view);

        final GridView grillacategorias = (GridView) view.findViewById(R.id.grid_category);
        grillacategorias.setOnItemClickListener(this);

        ArrayList<Categoria_DTO> categoria_dtos = new ArrayList<Categoria_DTO>();
        categoria_dtos.add(new Categoria_DTO("Comida", "", R.drawable.categoria_comida, false, 2));
        categoria_dtos.add(new Categoria_DTO("Ropa", "", R.drawable.categoria_ropa, false, 4));
        categoria_dtos.add(new Categoria_DTO("Salud", "", R.drawable.categoria_salud, false, 9));
        categoria_dtos.add(new Categoria_DTO("Bar", "", R.drawable.categoria_bar, false, 3));
        categoria_dtos.add(new Categoria_DTO("Diversión", "", R.drawable.categoria_entretenimiento, false, 1));
        categoria_dtos.add(new Categoria_DTO("Markets", "", R.drawable.categoria_markets, false, 7));
        categoria_dtos.add(new Categoria_DTO("Viajes", "", R.drawable.categoria_viajes, false, 5));
        categoria_dtos.add(new Categoria_DTO("Grifos", "", R.drawable.grifos, false, 8));
        //categoria_dtos.add(new Categoria_DTO("", "", R.drawable.white, false, 9));
        //categoria_dtos.add(new Categoria_DTO("", "", R.drawable.white, false, 10));

        Adapter_Categoria_Pregunta adapter_categoria = new Adapter_Categoria_Pregunta(getContext(), categoria_dtos);
        grillacategorias.setAdapter(adapter_categoria);
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
        Categoria_DTO categoria_dto = (Categoria_DTO) parent.getItemAtPosition(position);
        ((Bederr) actionBarActivity).setCategoria_dto(categoria_dto);
        actionBarActivity.getSupportFragmentManager().beginTransaction().setCustomAnimations(R.animator.izquierda_derecha_b, R.animator.izquierda_derecha_b).add(R.id.container, Fragment_Preguntar.newInstance(), Fragment_Preguntar.class.getName()).addToBackStack(null).commit();
    }
}
