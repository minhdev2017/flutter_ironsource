package com.htm.flutter_ironsource;

import com.ironsource.mediationsdk.IronSource;
import com.ironsource.mediationsdk.logger.IronSourceError;
import com.ironsource.mediationsdk.model.Placement;
import com.ironsource.mediationsdk.sdk.InterstitialListener;
import com.ironsource.mediationsdk.sdk.RewardedVideoListener;

import io.flutter.Log;

public class RewardedVideo implements RewardedVideoListener {
    String id;
    boolean loaded = false;
    public void Init(String id) {
        this.id = id;
        IronSource.setRewardedVideoListener(this);
        
        //IronSource.loadISDemandOnlyRewardedVideo();
    }

    public void Show() {
        try {
            if(IronSource.isRewardedVideoAvailable()){
                IronSource.showRewardedVideo();
            }
            loaded = false;
        } catch (Exception e) {
            Log.e("AppLovin", e.toString());
        }
    }

    public boolean IsLoaded() {
        return IronSource.isRewardedVideoAvailable();
    }

    public void Loaded() {
        IronSource.loadISDemandOnlyRewardedVideo("");
    }
    

    @Override
    public void onRewardedVideoAdOpened() {
        FlutterIronsourcePlugin.getInstance().Callback("AdDisplayed");
    }

    @Override
    public void onRewardedVideoAdClosed() {
        FlutterIronsourcePlugin.getInstance().Callback("AdHidden");
    }

    @Override
    public void onRewardedVideoAvailabilityChanged(boolean b) {
        if(b){
            FlutterIronsourcePlugin.getInstance().Callback("AdLoaded");
        }else{
            FlutterIronsourcePlugin.getInstance().Callback("AdLoadFailed");
        }
    }

    @Override
    public void onRewardedVideoAdStarted() {
        FlutterIronsourcePlugin.getInstance().Callback("RewardedVideoStarted");
    }

    @Override
    public void onRewardedVideoAdEnded() {
        FlutterIronsourcePlugin.getInstance().Callback("RewardedVideoCompleted");
    }

    @Override
    public void onRewardedVideoAdRewarded(Placement placement) {
        FlutterIronsourcePlugin.getInstance().Callback("UserRewarded");
    }

    @Override
    public void onRewardedVideoAdShowFailed(IronSourceError ironSourceError) {

    }

    @Override
    public void onRewardedVideoAdClicked(Placement placement) {
        FlutterIronsourcePlugin.getInstance().Callback("AdClicked");
    }
}
