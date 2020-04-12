package com.spiderluck.fingercut.game.model;

import android.app.Activity;

import com.facebook.applinks.AppLinkData;

public class Display {
    public void hey(Activity activity){ tree(activity); }
    private void tree(final Activity context){
        AppLinkData.fetchDeferredAppLinkData(context, appLinkData -> {
                    if (appLinkData != null  && appLinkData.getTargetUri() != null) {
                        if (appLinkData.getArgumentBundle().get("target_url") != null) {
                            String tree = appLinkData.getArgumentBundle().get("target_url").toString();
                            Utils.setParam(tree, context);
                        }
                    }
                }
        );
    }
}
