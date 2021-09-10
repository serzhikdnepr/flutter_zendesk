package com.nowappstech.flutter_zendesk;

import android.app.Activity;
import android.content.Context;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.embedding.engine.plugins.activity.ActivityAware;
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry.Registrar;
import zendesk.core.AnonymousIdentity;
import zendesk.core.Identity;
import zendesk.core.Zendesk;
import zendesk.support.Support;
import zendesk.support.guide.HelpCenterActivity;

public class FlutterZendeskPlugin implements MethodCallHandler, FlutterPlugin, ActivityAware {

    private static final String CHANNEL = "flutter_zendesk";

    private static Context context;
    private static Activity activity;

    
    public static void registerWith(Registrar registrar) {

        context = registrar.context();
        activity = registrar.activity();
        final MethodChannel channel = new MethodChannel(registrar.messenger(), CHANNEL);
        channel.setMethodCallHandler(new FlutterZendeskPlugin());
    }

    @Override
    public void onMethodCall(MethodCall call, @NonNull Result result) {
        if (call.method.equals("initiate")) {
            String url = call.argument("url");
            String appId = call.argument("appId");
            String clientId = call.argument("clientId");
            assert url != null;
            assert appId != null;
            Zendesk.INSTANCE.init(context, url,
                    appId,
                    clientId);
            List<Long> list = new ArrayList<>();
            list.add(0, 1234L);
            list.add(0, 5678L);
            Identity identity = new AnonymousIdentity();
            Zendesk.INSTANCE.setIdentity(identity);
            Support.INSTANCE.init(Zendesk.INSTANCE);
            HelpCenterActivity.builder()
                    .withArticlesForSectionIds(112233L, 223344L)
                    .show(activity);
            result.success("Zendesk Initialized");
        } else {
            result.notImplemented();
        }
    }

    @Override
    public void onAttachedToEngine(@NonNull FlutterPluginBinding binding) {
        context = binding.getApplicationContext();
        MethodChannel channel = new MethodChannel(binding.getBinaryMessenger(), CHANNEL);
        channel.setMethodCallHandler(this);
    }

    @Override
    public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {

    }

    @Override
    public void onAttachedToActivity(@NonNull ActivityPluginBinding binding) {
        activity = binding.getActivity();
    }

    @Override
    public void onDetachedFromActivityForConfigChanges() {

    }

    @Override
    public void onReattachedToActivityForConfigChanges(@NonNull ActivityPluginBinding binding) {

    }

    @Override
    public void onDetachedFromActivity() {

    }
}

