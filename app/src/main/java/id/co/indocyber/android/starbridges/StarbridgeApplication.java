package id.co.indocyber.android.starbridges;

import android.app.Application;
import android.content.Intent;

import id.co.indocyber.android.starbridges.service.StarbridgeService;

public class StarbridgeApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        startService(new Intent(this, StarbridgeService.class));
    }
}