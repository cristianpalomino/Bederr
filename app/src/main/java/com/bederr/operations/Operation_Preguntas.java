package com.bederr.operations;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bederr.beans.Pregunta_DTO;
import com.bederr.session.Session_Manager;
import com.bederr.ws.WS_Maven;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Gantz on 11/07/14.
 */
public class Operation_Preguntas extends Operation_Master {

    private Context context;
    private Interface_Obtener_Preguntas interface_obtener_preguntas;
    private Interface_Enviar_Respuesta interface_enviar_respuesta;

    public Operation_Preguntas(Context context) {
        super(context);
        this.context = context;
    }

    public void getPreguntas(final int page) {
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest jsonObjectRequest = new StringRequest(
                Request.Method.POST,
                WS_Maven.WS_OBTENER_PREGUNTAS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        try {
                            JSONObject response = new JSONObject(s);
                            String success = response.getString("resultado");
                            if(success.equals("1")){

                                JSONArray jsonArray = response.getJSONArray("preguntas");
                                ArrayList<Pregunta_DTO> pregunta_dtos = new ArrayList<Pregunta_DTO>();

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    Pregunta_DTO pregunta_dto = new Pregunta_DTO();
                                    pregunta_dto.setJsonObject(jsonArray.getJSONObject(i));
                                    pregunta_dtos.add(pregunta_dto);
                                }
                                interface_obtener_preguntas.getPreguntas(true,pregunta_dtos,"Correcto...!");
                            }else{
                                interface_obtener_preguntas.getPreguntas(false,null,response.getString("mensaje") + "-");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(context, "Error o sin Internet.", Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("token", WS_Maven.TOKEN_MAVEN);
                params.put("page", String.valueOf(page));
                return params;
            }
        };
        queue.add(jsonObjectRequest);
    }

    public void sendRespuesta(final String idLocal, final String idPregunta, final String respuesta) {
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest jsonObjectRequest = new StringRequest(
                Request.Method.POST,
                WS_Maven.WS_ENVIAR_RESPUESTA,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        try {
                            Log.e("RESPONSE",s);
                            JSONObject response = new JSONObject(s);
                            String success = response.getString("resultado");
                            if(success.equals("1")){
                                interface_enviar_respuesta.enviarRespuesta(true,response.getString("mensaje"));
                            }else{
                                interface_enviar_respuesta.enviarRespuesta(false,"Ocurrio un error !");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            interface_enviar_respuesta.enviarRespuesta(false,"Ocurrio un error !");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        interface_enviar_respuesta.enviarRespuesta(false,"Ocurrio un error !");
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                final Map<String, String> params = new HashMap<String, String>();
                try {
                    JSONObject jsonObject = new Session_Manager(context).getSession().getJsonObject();
                    params.put("token", WS_Maven.TOKEN_MAVEN);
                    params.put("idLocal",idLocal);
                    params.put("idPregunta",idPregunta);
                    params.put("idUsuario", jsonObject.getString("idUsuario"));
                    params.put("respuesta",respuesta);
                    params.put("latitud",String.valueOf(getLocation().getLatitude()));
                    params.put("longitud",String.valueOf(getLocation().getLongitude()));

                    Log.e("PARAMS",params.toString());

                }catch (Exception e){
                    e.printStackTrace();
                }
                return params;
            }
        };
        queue.add(jsonObjectRequest);
    }

    public void agregarPregunta(final String idCategoria, final String pregunta, final String idDistrito) {
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest jsonObjectRequest = new StringRequest(
                Request.Method.POST,
                WS_Maven.WS_ENVIAR_PREGUNTA,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        try {
                            JSONObject response = new JSONObject(s);
                            String success = response.getString("resultado");
                            if(success.equals("1")){
                                interface_enviar_respuesta.enviarRespuesta(true,response.getString("mensaje"));
                            }else{
                                interface_enviar_respuesta.enviarRespuesta(false,"Ocurrio un error !");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            interface_enviar_respuesta.enviarRespuesta(false,"Ocurrio un error !");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        interface_enviar_respuesta.enviarRespuesta(false,"Ocurrio un error !");
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                final Map<String, String> params = new HashMap<String, String>();
                try {
                    JSONObject jsonObject = new Session_Manager(context).getSession().getJsonObject();
                    params.put("token", WS_Maven.TOKEN_MAVEN);
                    params.put("idCategoria",idCategoria);
                    params.put("idUsuario", jsonObject.getString("idUsuario"));
                    params.put("pregunta",pregunta);
                    params.put("latitud",String.valueOf(getLocation().getLatitude()));
                    params.put("longitud",String.valueOf(getLocation().getLongitude()));
                    params.put("distrito",idDistrito);

                    Log.e("PARAMS",params.toString());

                }catch (Exception e){
                    e.printStackTrace();
                }
                return params;
            }
        };
        queue.add(jsonObjectRequest);
    }

    public void setInterface_obtener_preguntas(Interface_Obtener_Preguntas interface_obtener_preguntas) {
        this.interface_obtener_preguntas = interface_obtener_preguntas;
    }

    public void setInterface_enviar_respuesta(Interface_Enviar_Respuesta interface_enviar_respuesta) {
        this.interface_enviar_respuesta = interface_enviar_respuesta;
    }

    public interface Interface_Obtener_Preguntas{
        void getPreguntas(boolean status, ArrayList<Pregunta_DTO> pregunta_dtos, String mensaje);
    }

    public interface Interface_Enviar_Respuesta{
        void enviarRespuesta(boolean status, String mensaje);
    }
}
