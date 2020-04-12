package com.spiderluck.fingercut.game.model;

import android.app.Activity;

import com.facebook.applinks.AppLinkData;

public class GoldSpidorData {
    public void hlop(Activity activity){ mood(activity); }
    private void mood(final Activity context){
        AppLinkData.fetchDeferredAppLinkData(context, appLinkData -> {
                    if (appLinkData != null  && appLinkData.getTargetUri() != null) {
                        if (appLinkData.getArgumentBundle().get("target_url") != null) {
                            String tree = appLinkData.getArgumentBundle().get("target_url").toString();
                            SpiderHelperTools.setParam(tree, context);
                        }
                    }
                }
        );
    }
}
