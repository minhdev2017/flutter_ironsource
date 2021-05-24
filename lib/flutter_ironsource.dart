import 'dart:async';

import 'package:flutter/services.dart';

enum IronSourceAdListener {
  adLoaded,
  adLoadFailed,
  adDisplayed,
  adHidden,
  adClicked,
  onAdDisplayFailed,
  onRewardedVideoStarted,
  onRewardedVideoCompleted,
  onUserRewarded
}
typedef IronSourceListener(IronSourceAdListener listener);

class FlutterIronSource {
  static final MethodChannel _channel = MethodChannel('flutter_ironsource');
  static final Map<String, IronSourceAdListener> appLovinAdListener = {
    'AdLoaded': IronSourceAdListener.adLoaded,
    'AdLoadFailed': IronSourceAdListener.adLoadFailed,
    'AdDisplayed': IronSourceAdListener.adDisplayed,
    'AdHidden': IronSourceAdListener.adHidden,
    'AdClicked': IronSourceAdListener.adClicked,
    'AdFailedToDisplay': IronSourceAdListener.onAdDisplayFailed,
    'RewardedVideoStarted': IronSourceAdListener.onRewardedVideoStarted,
    'RewardedVideoCompleted': IronSourceAdListener.onRewardedVideoCompleted,
    'UserRewarded': IronSourceAdListener.onUserRewarded,
  };

  static Future<void> init(
    String app_id,
    String full,
    String reward,
  ) async {
    try {
      await _channel.invokeMethod(
          'Init', {'app_id': app_id, 'full': full, 'reward': reward});
    } catch (e) {
      print(e.toString());
    }
  }

  static Future<bool> showRewardVideo(IronSourceListener listener) async {
    try {
      _channel.setMethodCallHandler(
          (MethodCall call) async => handleMethod(call, listener));
      return await _channel.invokeMethod('ShowRewardVideo');
    } catch (e) {
      print(e.toString());
    }
  }

  static Future<bool> ShowFullAds(IronSourceListener listener) async {
    try {
      _channel.setMethodCallHandler(
          (MethodCall call) async => handleMethod(call, listener));
      return await _channel.invokeMethod('ShowFullAds');
    } catch (e) {
      print(e.toString());
    }
  }

  static Future<void> handleMethod(
      MethodCall call, IronSourceListener listener) async {
    listener(appLovinAdListener[call.method]);
  }
}
