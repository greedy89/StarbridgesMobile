package id.co.indocyber.android.starbridges;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.support.multidex.MultiDex;

import id.co.indocyber.android.starbridges.service.StarbridgeService;

public class StarbridgeApplication extends Application {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        startService(new Intent(this, StarbridgeService.class));
    }
}