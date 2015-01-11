package com.bederr.session;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.bederr.main.Bederr;
import com.bederr.benefits_v2.fragments.Benefits_F;
import com.bederr.beans.Usuario_DTO;

import pe.bederr.com.R;
import com.bederr.main.Tutorial;
import com.bederr.questions_v2.fragments.Question_F;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Gantz on 23/06/14.
 */
public class Session_Manager {

    private static final String PREFERENCE_NAME = "roypi_preferences";
    private int PRIVATE_MODE = 0;

    /*
    USUARIO DATA SESSION - JSON
     */
    public static final String USER_DATA = "userData";
    public static final String USER_JSON = "userJSON";
    public static final String USER_LOGIN = "userLogin";
    public static final String TIPO_SESSION = "tipo_session";

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private Context context;

    public Session_Manager(Context context) {
        this.context = context;
        preferences = context.getSharedPreferences(PREFERENCE_NAME, PRIVATE_MODE);
        editor = preferences.edit();
    }

    public boolean isLogin() {
        return preferences.getBoolean(USER_LOGIN, false);
    }

    public void crearSession(Usuario_DTO usuario_dto, int tipo_session) throws JSONException {
        editor.putBoolean(USER_LOGIN, true);
        editor.putString(USER_DATA, usuario_dto.getJsonObject().toString());
        editor.putInt(TIPO_SESSION, tipo_session);
        editor.commit();

        ((Bederr) context).clearHistory();
        ((Bederr) context).getSupportFragmentManager().
                beginTransaction().
                setCustomAnimations(R.animator.izquierda_derecha, R.animator.derecha_izquierda).
                replace(R.id.container, Benefits_F.newInstance()).
                commit();
        hideSoftKeyboard();
    }

    public void crearSession_v2(String token, int tipo_session) {
        editor.putBoolean(USER_LOGIN, true);
        editor.putString(USER_DATA, token);
        editor.putInt(TIPO_SESSION, tipo_session);
        editor.commit();

        ((Bederr) context).clearHistory();
        ((Bederr) context).getSupportFragmentManager().
                beginTransaction().
                setCustomAnimations(R.animator.izquierda_derecha, R.animator.derecha_izquierda).
                replace(R.id.container, Benefits_F.newInstance()).
                commit();
        hideSoftKeyboard();
    }

    public void crearSessionTutorial_v2(String token, int tipo_session) {
        editor.putBoolean(USER_LOGIN, true);
        editor.putString(USER_DATA, token);
        editor.putInt(TIPO_SESSION, tipo_session);
        editor.commit();

        context.startActivity(new Intent(context, Bederr.class));
        ((Tutorial) context).overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        ((Tutorial) context).finish();
    }

    public void crearSessionTutorial(Usuario_DTO usuario_dto, int tipo_session) throws JSONException {
        editor.putBoolean(USER_LOGIN, true);
        editor.putString(USER_DATA, usuario_dto.getJsonObject().toString());
        editor.putInt(TIPO_SESSION, tipo_session);
        editor.commit();

        context.startActivity(new Intent(context, Bederr.class));
        ((Tutorial) context).overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        ((Tutorial) context).finish();
    }

    public void cerrarSession() {
        editor.putBoolean(USER_LOGIN, false);
        editor.putString(USER_DATA, null);
        editor.putInt(TIPO_SESSION, -1);
        editor.commit();

        ((Bederr) context).clearHistory();
        ((Bederr) context).getSupportFragmentManager().beginTransaction().replace(R.id.container, Question_F.newInstance(), Question_F.class.getName()).commit();
        Toast.makeText(context, "Cerrando sessión", Toast.LENGTH_SHORT).show();
        hideSoftKeyboard();
    }

    public Usuario_DTO getSession() throws JSONException {
        if (isLogin()) {
            String userData = preferences.getString(USER_DATA, null);
            Usuario_DTO usuario_dto = new Usuario_DTO();
            usuario_dto.setJsonObject(new JSONObject(userData));
            return usuario_dto;
        } else {
            return null;
        }
    }

    public JSONObject getSession_v2() {
        try {
            if (isLogin()) {
                String userData = preferences.getString(USER_JSON, null);
                return  new JSONObject(userData);
            }else{
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void setUserJson(String json) {
        editor.putString(USER_JSON, json);
        editor.commit();
    }

    public String getUserToken() {
        if (isLogin()) {
            String token = preferences.getString(USER_DATA, null);
            return token;
        } else {
            return null;
        }
    }

    public int getSessionType() {
        if (isLogin()) {
            int tipo_session = preferences.getInt(TIPO_SESSION, -1);
            return tipo_session;
        } else {
            return -1;
        }
    }

    public void hideSoftKeyboard() {
        if (((ActionBarActivity) context).getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) ((ActionBarActivity) context).getSystemService(ActionBarActivity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(((ActionBarActivity) context).getCurrentFocus().getWindowToken(), 0);
        }
    }
}