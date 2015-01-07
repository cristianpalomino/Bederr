package com.bederr.operations;

import android.content.Context;
import android.location.Location;

import com.bederr.session.Session_Manager;
import com.bederr.utils.Locator;

/**
 * Created by Gantz on 12/10/14.
 */
public class Operation_Master {

    protected Location location;
    protected String user_id;

    public Operation_Master(Context context) {
        new Locator(context).getLocation(Locator.Method.NETWORK_THEN_GPS,new Locator.Listener() {
            @Override
            public void onLocationFound(Location location) {
                Operation_Master.this.location = location;
            }

            @Override
            public void onLocationNotFound() {

            }
        });

        try {
            user_id = new Session_Manager(context).getSession().getJsonObject().getString("idUsuario");
        } catch (Exception e) {
            user_id = "";
            e.printStackTrace();
        }
    }

    public Location getLocation() {
        return location;
    }

    public String getUser_id() {
        return user_id;
    }
}
