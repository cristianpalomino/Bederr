package com.bederr.main;

import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.bederr.account_v2.interfaces.OnSuccessCounty;
import com.bederr.account_v2.services.Service_Country;
import com.bederr.application.Maven_Application;
import com.bederr.beans.Lista_DTO;
import com.bederr.account_v2.fragments.Fragment_Login_v2;
import com.bederr.beans.Categoria_DTO;
import com.bederr.beans.Cupon_DTO;
import com.bederr.beans.Distrito_DTO;
import com.bederr.beans.Empresa_DTO;
import com.bederr.beans.Local_DTO;
import com.bederr.beans.Pregunta_DTO;
import com.bederr.beans_v2.Area_DTO;
import com.bederr.beans_v2.Bederr_DTO;
import com.bederr.beans_v2.Benefit_Program_DTO;
import com.bederr.beans_v2.Category_DTO;
import com.bederr.beans_v2.CorporateOffer_DTO;
import com.bederr.beans_v2.Country_DTO;
import com.bederr.beans_v2.Listing_DTO;
import com.bederr.beans_v2.Locality_DTO;
import com.bederr.beans_v2.Offer_DTO;
import com.bederr.beans_v2.Place_DTO;
import com.bederr.beans_v2.Question_DTO;
import com.bederr.beans_v2.Ubication_DTO;
import com.bederr.fragments.Fragment_Menu;
import com.bederr.questions_v2.fragments.Question_F;
import com.bederr.retail_v2.dialog.Ubication_D;
import com.bederr.retail_v2.fragments.Explore_F;
import com.bederr.retail_v2.interfaces.OnSuccessPlaces;
import com.bederr.retail_v2.services.Service_Places;
import com.bederr.util_v2.Bederr_WS;
import com.bederr.util_v2.LocationAddress;
import com.bederr.utils.GPSTracker;
import com.bederr.utils.GpsLocationTracker;
import com.bederr.utils.Locator;
import com.google.android.gms.maps.model.LatLng;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.parse.ParseFacebookUtils;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;
import pe.bederr.com.R;

public class Bederr extends ActionBarActivity {

    public SlidingMenu sm_menu;
    public SlidingMenu sm_empresas;

    /**
     * Deprecated
     */
    private Pregunta_DTO pregunta_dto;
    private Local_DTO local_dto;
    private Categoria_DTO categoria_dto;
    private Distrito_DTO distrito_dto;
    private Cupon_DTO cupon_dto;
    private Lista_DTO lista_dto;
    private Empresa_DTO empresa_dto;
    private ArrayList<Local_DTO> local_dtos;
    private ArrayList<Distrito_DTO> distrito_dtos;
    private Fragment active_fragment;

    /**
     * Active
     */
    private Ubication_D ubication_d;
    private View viewExtra;
    private Listing_DTO listing_dto;
    private Place_DTO place_dto;
    private Offer_DTO offer_dto;
    private Question_DTO question_dto;
    private CorporateOffer_DTO corporateOffer_dto;
    private Benefit_Program_DTO benefit_program_dto;
    private ArrayList<Locality_DTO> locality_dtos;
    private ArrayList<Category_DTO> category_dtos;
    private ArrayList<Categoria_DTO> categoria_dtos;
    private ArrayList<Place_DTO> place_dtos;

    public ArrayList<Categoria_DTO> getCategoria_dtos() {
        return categoria_dtos;
    }

    public void setCategoria_dtos(ArrayList<Categoria_DTO> categoria_dtos) {
        this.categoria_dtos = categoria_dtos;
    }

    public ArrayList<Place_DTO> getPlace_dtos() {
        return place_dtos;
    }

    public void setPlace_dtos(ArrayList<Place_DTO> place_dtos) {
        this.place_dtos = place_dtos;
    }

    public Question_DTO getQuestion_dto() {
        return question_dto;
    }

    public void setQuestion_dto(Question_DTO question_dto) {
        this.question_dto = question_dto;
    }

    public ArrayList<Locality_DTO> getLocality_dtos() {
        return locality_dtos;
    }

    public void setLocality_dtos(ArrayList<Locality_DTO> locality_dtos) {
        this.locality_dtos = locality_dtos;
    }

    public ArrayList<Category_DTO> getCategory_dtos() {
        return category_dtos;
    }

    public void setCategory_dtos(ArrayList<Category_DTO> category_dtos) {
        this.category_dtos = category_dtos;
    }

    public Benefit_Program_DTO getBenefit_program_dto() {
        return benefit_program_dto;
    }

    public void setBenefit_program_dto(Benefit_Program_DTO benefit_program_dto) {
        this.benefit_program_dto = benefit_program_dto;
    }

    public CorporateOffer_DTO getCorporateOffer_dto() {
        return corporateOffer_dto;
    }

    public void setCorporateOffer_dto(CorporateOffer_DTO corporateOffer_dto) {
        this.corporateOffer_dto = corporateOffer_dto;
    }

    public Offer_DTO getOffer_dto() {
        return offer_dto;
    }

    public void setOffer_dto(Offer_DTO offer_dto) {
        this.offer_dto = offer_dto;
    }

    public Place_DTO getPlace_dto() {
        return place_dto;
    }

    public void setPlace_dto(Place_DTO place_dto) {
        this.place_dto = place_dto;
    }

    public Listing_DTO getListing_dto() {
        return listing_dto;
    }

    public void setListing_dto(Listing_DTO listing_dto) {
        this.listing_dto = listing_dto;
    }

    public View getViewExtra() {
        return viewExtra;
    }

    public void setViewExtra(View viewExtra) {
        this.viewExtra = viewExtra;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.main_bederr);
    }

    @Override
    public void onBackPressed() {
        FragmentManager fm = getSupportFragmentManager();
        if (fm.getBackStackEntryCount() > 0) {
            fm.popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.bederr, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void clearHistory() {
        FragmentManager fm = getSupportFragmentManager();
        for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
            fm.popBackStack();
        }
    }

    public Fragment getActive_fragment() {
        return active_fragment;
    }

    public void setLista_dto(Lista_DTO lista_dto) {
        this.lista_dto = lista_dto;
    }

    public Lista_DTO getLista_dto() {
        return lista_dto;
    }

    public void setCupon_dto(Cupon_DTO cupon_dto) {
        this.cupon_dto = cupon_dto;
    }

    public Cupon_DTO getCupon_dto() {
        return cupon_dto;
    }

    public void setDistrito_dto(Distrito_DTO distrito_dto) {
        this.distrito_dto = distrito_dto;
    }

    public Distrito_DTO getDistrito_dto() {
        return distrito_dto;
    }

    public void setDistrito_dtos(ArrayList<Distrito_DTO> distrito_dtos) {
        this.distrito_dtos = distrito_dtos;
    }

    public ArrayList<Distrito_DTO> getDistrito_dtos() {
        return distrito_dtos;
    }

    public void setActive_fragment(Fragment active_fragment) {
        this.active_fragment = active_fragment;
    }

    public void setCategoria_dto(Categoria_DTO categoria_dto) {
        this.categoria_dto = categoria_dto;
    }

    public Categoria_DTO getCategoria_dto() {
        return categoria_dto;
    }

    public ArrayList<Local_DTO> getLocal_dtos() {
        return local_dtos;
    }

    public void setLocal_dtos(ArrayList<Local_DTO> local_dtos) {
        this.local_dtos = local_dtos;
    }

    public void setLocal_dto(Local_DTO local_dto) {
        this.local_dto = local_dto;
    }

    public Local_DTO getLocal_dto() {
        return local_dto;
    }

    public void setPregunta_dto(Pregunta_DTO pregunta_dto) {
        this.pregunta_dto = pregunta_dto;
    }

    public Pregunta_DTO getPregunta_dto() {
        return pregunta_dto;
    }

    public void setEmpresa_dto(Empresa_DTO empresa_dto) {
        this.empresa_dto = empresa_dto;
    }

    public Empresa_DTO getEmpresa_dto() {
        return empresa_dto;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ParseFacebookUtils.finishAuthentication(requestCode, resultCode, data);
    }


    public void loadPlaces() {
        Maven_Application application = ((Maven_Application) getApplication());
        String lat = application.getUbication().getLatitude();
        String lng = application.getUbication().getLongitude();
        String name = "";
        String cat = "";
        String city = "";
        String area = application.getUbication().getArea();

        Service_Places service_places = new Service_Places(Bederr.this);
        service_places.sendRequest(lat, lng, name, cat, city,area);
        service_places.setOnSuccessPlaces(new OnSuccessPlaces() {
            @Override
            public void onSuccessPlaces(boolean success,
                                        ArrayList<Place_DTO> place_dtos,
                                        String count,
                                        String next,
                                        String previous) {
                if (success) {
                    setPlace_dtos(place_dtos);
                }
            }
        });
    }

    /**
     * VALIDATE GPS
     */

    @Override
    protected void onResume() {
        super.onResume();
        //loadPlaces();

        final GpsLocationTracker gpsTracker = new GpsLocationTracker(Bederr.this);
        if (gpsTracker.canGetLocation()) {
            Service_Country service_country = new Service_Country(this);
            service_country.sendRequest();
            service_country.setOnSuccessCounty(new OnSuccessCounty() {
                @Override
                public void onSuccessCountry(boolean success,
                                             final ArrayList<Country_DTO> country_dtos,
                                             String message) {
                    if (success) {
                        final Maven_Application application = ((Maven_Application) getApplication());
                        if (gpsTracker.canGetLocation()) {
                            final LatLng latlng = new LatLng(gpsTracker.getLatitude(), gpsTracker.getLongitude());
            /*
            Usa : 35.227672 - -97.734375
            Australia : -27.313214 - 131.132813
             */

                            String URL = Bederr_WS.BEDERR_GEOCODE.
                                    replace("LAT", String.valueOf(latlng.latitude)).
                                    replace("LON", String.valueOf(latlng.longitude));
                            Log.e("URL", URL);
                            AsyncHttpClient httpClient = new AsyncHttpClient();
                            httpClient.get(Bederr.this, URL, null, new JsonHttpResponseHandler() {
                                @Override
                                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                    super.onSuccess(statusCode, headers, response);

                                    String area = "NO AREA";
                                    try {
                                        Bederr_DTO bederr_dto = new Bederr_DTO();
                                        String success = bederr_dto.parseString("status", response);

                                        if (success.equals("OK")) {
                                            JSONObject object = bederr_dto.parseJSONArray("results", response).getJSONObject(0);
                                            JSONArray adresses = object.getJSONArray("address_components");
                                            for (int i = 0; i < adresses.length(); i++) {
                                                JSONObject address = adresses.getJSONObject(i);
                                                JSONArray types = address.getJSONArray("types");
                                                for (int j = 0; j < types.length(); j++) {
                                                    String admin_area = "administrative_area_level_1";
                                                    String type = types.getString(j);
                                                    if (admin_area.equals(type)) {
                                                        area = bederr_dto.parseString("long_name", address);
                                                    }
                                                }
                                            }
                                        }


                                        for (int i = 0; i < country_dtos.size(); i++) {
                                            Country_DTO country_dto = country_dtos.get(i);
                                            for (int j = 0; j < country_dto.getAreas().size(); j++) {
                                                Area_DTO area_dto = country_dto.getAreas().get(j);
                                                if (area.equals(area_dto.getName())) {
                                                    Ubication_DTO ubication_dto = new Ubication_DTO(latlng, area_dto.getId());
                                                    application.setUbication(ubication_dto);

                                                    showMessage("GPS : ON" + "\n" +
                                                            "LAT : " + latlng.latitude + "\n" +
                                                            "LNG : " + latlng.longitude + "\n" +
                                                            "CIUDAD : " + area + "\n" +
                                                            "CODE : " + area_dto.getId());
                                                }
                                            }
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                    initApp();
                                }

                                @Override
                                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                                    super.onFailure(statusCode, headers, throwable, errorResponse);
                                }
                            });
                        } else {
                            Ubication_DTO ubication_dto = new Ubication_DTO(null, null);
                            application.setUbication(ubication_dto);
                            initApp();
                        }
                    }
                }
            });
        } else {
            Ubication_D ubication_d = new Ubication_D(Bederr.this);
            ubication_d.show();
            initApp();
        }
    }

    private void initApp() {
        int flag = getIntent().getIntExtra("flag", -1);
        if (flag == 0) {
            getSupportFragmentManager().beginTransaction().replace(R.id.container, Fragment_Login_v2.newInstance()).commit();
        } else {
            getSupportFragmentManager().beginTransaction().replace(R.id.container, Explore_F.newInstance()).commit();
        }

        getSupportFragmentManager().beginTransaction().replace(R.id.menu_frame, Fragment_Menu.newInstance()).commit();

        sm_menu = new SlidingMenu(this);
        sm_menu.setShadowWidthRes(R.dimen.navigation_drawer_width);
        sm_menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        sm_menu.setFadeDegree(0.35f);
        sm_menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        sm_menu.setMode(SlidingMenu.LEFT);
        sm_menu.setMenu(R.layout.menu_frame);
        sm_menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);

        sm_empresas = new SlidingMenu(this);
        sm_empresas.setShadowWidthRes(R.dimen.navigation_drawer_width);
        sm_empresas.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        sm_empresas.setMode(SlidingMenu.RIGHT);
        sm_empresas.setFadeDegree(0.35f);
        sm_empresas.setMenu(R.layout.fragment_empresas);
        sm_empresas.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
        sm_empresas.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
    }

private class GeocoderHandler extends Handler {
    @Override
    public void handleMessage(Message message) {
        String locationAddress;
        switch (message.what) {
            case 1:
                Bundle bundle = message.getData();
                locationAddress = bundle.getString("address");
                break;
            default:
                locationAddress = null;
        }
        Log.e("LUGAR", locationAddress);
    }

}

    public void showMessage(String message) {
        Toast.makeText(Bederr.this, message, Toast.LENGTH_SHORT).show();
    }
}
