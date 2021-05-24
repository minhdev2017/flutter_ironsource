import 'package:flutter/material.dart';

import 'package:flutter_ironsource/flutter_ironsource.dart';

void main() => runApp(MyApp());

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  @override
  void initState() {
    FlutterIronSource.init("9b563ab5", "", "");
    super.initState();
  }

  listener(IronSourceAdListener event) {
    print(event);
    if (event == IronSourceAdListener.onUserRewarded) {
      print('👍get reward');
    }
  }

  bool isRewardedVideoAvailable = false;
  bool isFullAvailable = false;

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: Center(
          child: Column(
            children: [
              RaisedButton(
                onPressed: () async {
                  var result = await FlutterIronSource.showRewardVideo(
                      (IronSourceAdListener event) => listener(event));
                  print('Show Reward Video $result');
                },
                child: Text('Show Reward Video'),
              ),
              RaisedButton(
                onPressed: () async {
                  var result = await FlutterIronSource.ShowFullAds(
                      (IronSourceAdListener event) => listener(event));
                  print('Show Full Ads $result');
                },
                child: Text('Show Full Ads'),
              ),
            ],
          ),
        ),
      ),
    );
  }
}
