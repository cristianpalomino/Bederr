package com.bederr.account_v2.services;

import android.content.Context;

/**
 * Created by Gantz on 23/12/14.
 */
public class Account_Service {

    private Context context;

    public Account_Service(Context context) {
        this.context = context;
    }

    public Service_Login getService_login() {
        return new Service_Login(context);
    }

    public Service_Me getService_me() {
        return new Service_Me(context);
    }

    public Service_Password getService_password() {
        return new Service_Password(context);
    }

    public Service_Register getService_register() {
        return new Service_Register(context);
    }
}
