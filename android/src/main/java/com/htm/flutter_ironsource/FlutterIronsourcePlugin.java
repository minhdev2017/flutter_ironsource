package com.htm.flutter_ironsource;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.ironsource.adapters.supersonicads.SupersonicConfig;
import com.ironsource.mediationsdk.IronSource;
import com.ironsource.mediationsdk.integration.IntegrationHelper;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.embedding.engine.plugins.activity.ActivityAware;
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding;
import io.flutter.plugin.common.BinaryMessenger;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry.Registrar;
import io.flutter.plugin.common.StandardMethodCodec;

/** FlutterIronsourcePlugin */
public class FlutterIronsourcePlugin implements FlutterPlugin, MethodCallHandler, ActivityAware {
  private static FlutterIronsourcePlugin instance;
  private static RewardedVideo instanceReward;
  private static InterstitialAd instanceInter;
  private static Context context;
  private static MethodChannel channel;
  public static Activity activity;


  public static FlutterIronsourcePlugin getInstance() {
    return instance;
  }

  @Override
  public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
    this.onAttachedToEngine(flutterPluginBinding.getApplicationContext(), flutterPluginBinding.getBinaryMessenger());
  }

  public static void registerWith(Registrar registrar) {
    if (instance == null) {
      instance = new FlutterIronsourcePlugin();
    }
    instance.onAttachedToEngine(registrar.context(), registrar.messenger());
  }


  public void onAttachedToEngine(Context applicationContext, BinaryMessenger messenger) {
    if (channel != null) {
      return;
    }
    instance = new FlutterIronsourcePlugin();
    Log.i("AppLovin Plugin", "onAttachedToEngine");
    this.context = applicationContext;
    channel = new MethodChannel(messenger, "flutter_ironsource", StandardMethodCodec.INSTANCE);
    channel.setMethodCallHandler(this);
  }

  public FlutterIronsourcePlugin() {
  }

  @Override
  public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {
    try {
      switch (call.method) {
        case "Init":
          String app_id = call.argument("app_id").toString();
          IronSource.init(activity, app_id, IronSource.AD_UNIT.OFFERWALL, IronSource.AD_UNIT.INTERSTITIAL, IronSource.AD_UNIT.REWARDED_VIDEO, IronSource.AD_UNIT.BANNER);
          SupersonicConfig.getConfigObj().setClientSideCallbacks(true);
          IntegrationHelper.validateIntegration(activity);
          IronSource.initISDemandOnly(activity, app_id);
          instanceReward.Init(call.argument("reward").toString());
          instanceInter.Init(call.argument("full").toString());
          result.success(Boolean.TRUE);
          break;
        case "ShowRewardVideo":
          if(instanceReward.IsLoaded()) {
            instanceReward.Show();
            result.success(Boolean.TRUE);
          }else{
            instanceReward.Loaded();
            result.success(Boolean.FALSE);
          }
          break;
        case "ShowFullAds":
          if(instanceInter.IsLoaded()) {
            instanceInter.Show();
            result.success(Boolean.TRUE);
          }else{
            instanceInter.Loaded();
            result.success(Boolean.FALSE);

          }
          break;
        default:
          result.notImplemented();
      }
    } catch (Exception err) {
      Log.e("Method error", err.toString());
      result.notImplemented();
    }
  }

  static public void Callback(final String method) {
    if (instance.context != null && instance.channel != null && instance.activity != null) {
      instance.activity.runOnUiThread(new Runnable() {
        @Override
        public void run() {
          instance.channel.invokeMethod(method, null);
        }
      });
    } else {
      Log.e("AppLovin", "instance method channel not created");
    }
  }

  @Override
  public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
    this.context = null;
    this.channel.setMethodCallHandler(null);
    this.channel = null;
  }

  @Override
  public void onAttachedToActivity(ActivityPluginBinding binding) {
    this.activity = binding.getActivity();
    instance.instanceReward = new RewardedVideo();
    instance.instanceInter= new InterstitialAd();
    Log.i("AppLovin Plugin", "Instances created");
  }

  @Override
  public void onDetachedFromActivityForConfigChanges() {
  }

  @Override
  public void onReattachedToActivityForConfigChanges(ActivityPluginBinding binding) {
    this.activity = binding.getActivity();
  }

  @Override
  public void onDetachedFromActivity() {
  }
}
