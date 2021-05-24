package com.htm.flutter_ironsource;

import com.ironsource.mediationsdk.IronSource;
import com.ironsource.mediationsdk.logger.IronSourceError;
import com.ironsource.mediationsdk.sdk.InterstitialListener;

import io.flutter.Log;

public class InterstitialAd implements InterstitialListener {
    String id;
    boolean loaded = false;
    public void Init(String id) {
        this.id = id;
        IronSource.setInterstitialListener(this);
        IronSource.loadInterstitial();
    }

    public void Show() {
        try {
            if(loaded){
                IronSource.showInterstitial();
            }
            loaded = false;
        } catch (Exception e) {
            Log.e("AppLovin", e.toString());
        }
    }

    public boolean IsLoaded() {
        return loaded;
    }

    public void Loaded() {
        IronSource.loadInterstitial();
    }


    @Override
    public void onInterstitialAdReady() {
        loaded = true;
        FlutterIronsourcePlugin.getInstance().Callback("AdLoaded");
    }

    @Override
    public void onInterstitialAdLoadFailed(IronSourceError ironSourceError) {
        loaded = false;
        FlutterIronsourcePlugin.getInstance().Callback("AdLoadFailed");
    }

    @Override
    public void onInterstitialAdOpened() {

        FlutterIronsourcePlugin.getInstance().Callback("AdDisplayed");
    }

    @Override
    public void onInterstitialAdClosed() {
        FlutterIronsourcePlugin.getInstance().Callback("AdHidden");
        IronSource.loadInterstitial();
    }

    @Override
    public void onInterstitialAdShowSucceeded() {

    }

    @Override
    public void onInterstitialAdShowFailed(IronSourceError ironSourceError) {
        FlutterIronsourcePlugin.getInstance().Callback("AdFailedToDisplay");
    }

    @Override
    public void onInterstitialAdClicked() {
        FlutterIronsourcePlugin.getInstance().Callback("AdClicked");
    }
}
