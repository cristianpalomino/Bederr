package com.bederr.dialog;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Build;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bederr.main.Bederr;
import com.bederr.operations.Operation_Busquedas;
import com.bederr.views.View_Local_Responder;
import com.bederr.beans.Local_DTO;

import pe.bederr.com.R;

import com.bederr.utils.Util_Fonts;

import java.util.ArrayList;

/**
 * Created by Gantz on 7/08/14.
 */

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class Dialog_Local_Pregunta extends AlertDialog {

    private ActionBarActivity actionBarActivity;
    private LinearLayout frame_container;

    public Dialog_Local_Pregunta(Context context, ActionBarActivity actionBarActivity) {
        super(context);
        this.actionBarActivity = actionBarActivity;
        initDialog();
    }

    public Dialog_Local_Pregunta(Context context, boolean cancelable, OnCancelListener cancelListener, ActionBarActivity actionBarActivity) {
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
    public Dialog_Local_Pregunta(Context context, int theme, ActionBarActivity actionBarActivity) {
        super(context, theme);
        this.actionBarActivity = actionBarActivity;
        initDialog();
    }

    private void initDialog() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        final View view = inflater.inflate(R.layout.fragment_local_pregunta, null);
        frame_container = (LinearLayout) view.findViewById(R.id.frame_container);
        initActionBar(view, frame_container);
        setView(view);
        loadList();
    }

    protected void initActionBar(final View view, final LinearLayout frame_container) {
        ImageView action_left = (ImageView) view.findViewById(R.id.action_right);
        EditText action_middle = (EditText) view.findViewById(R.id.edt_action_middle);
        action_middle.setTypeface(Util_Fonts.setPNALight(getContext()));

        action_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        action_middle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Operation_Busquedas operation_busquedas = new Operation_Busquedas(getContext());
                operation_busquedas.buscarLocalesPorNombre(s.toString(),1);
                operation_busquedas.setInterface_operation_busquedas(new Operation_Busquedas.Interface_Operation_Busquedas() {
                    @Override
                    public void getLocalesCercanos(boolean status, final ArrayList<Local_DTO> local_dtos, int paramInt, String message) {
                        try {
                            if (status) {
                                Runnable r = new Runnable() {
                                    public void run() {
                                        frame_container.removeAllViews();
                                        for (int i = 0; i < local_dtos.size(); i++) {
                                            Local_DTO local_dto = local_dtos.get(i);
                                            View_Local_Responder view_local_responder = new View_Local_Responder(getContext(), local_dto);
                                            view_local_responder.setDialog_local_pregunta(Dialog_Local_Pregunta.this);
                                            view_local_responder.setActionBarActivity(actionBarActivity);
                                            frame_container.addView(view_local_responder);
                                        }
                                    }
                                };
                                frame_container.post(r);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    public void loadList() {
        final ArrayList<Local_DTO> local_dtos = ((Bederr) actionBarActivity).getLocal_dtos();
        Runnable r = new Runnable() {
            public void run() {
                for (int i = 0; i < local_dtos.size(); i++) {
                    Local_DTO local_dto = local_dtos.get(i);
                    View_Local_Responder view_local_responder = new View_Local_Responder(getContext(), local_dto);
                    view_local_responder.setDialog_local_pregunta(Dialog_Local_Pregunta.this);
                    view_local_responder.setActionBarActivity(actionBarActivity);
                    frame_container.addView(view_local_responder);
                }
                findViewById(R.id.progressBar).setVisibility(View.GONE);
            }
        };
        frame_container.post(r);
    }
}
