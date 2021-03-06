package com.tal.d_stack;

import androidx.annotation.NonNull;

import java.util.HashMap;
import java.util.Map;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;

/**
 * DStackPlugin
 */
public class DStackPlugin implements FlutterPlugin, MethodCallHandler {
    public static final String METHOD_ACTION_TO_FLUTTER = "methodActionToFlutter";
    public static final String METHOD_ACTION_TO_NATIVE = "methodActionToNative";

    public static final String ACTION_PUSH = "push";
    public static final String ACTION_POP = "pop";
    public static final String ACTION_ACTIVATE = "activate";

    public static DStackPlugin getInstance() {
        return (DStackPlugin) DStack.getInstance().flutterEngine.getPlugins().get(DStackPlugin.class);
    }

    /// The MethodChannel that will the communication between Flutter and native Android
    ///
    /// This local reference serves to register the plugin with the Flutter Engine and unregister it
    /// when the Flutter Engine is detached from the Activity
    private MethodChannel channel;

    @Override
    public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
        channel = new MethodChannel(flutterPluginBinding.getBinaryMessenger(), "d_stack");
        channel.setMethodCallHandler(this);
    }

    @Override
    public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
        channel.setMethodCallHandler(null);
    }

    @Override
    public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {
        if (call.method.equals(METHOD_ACTION_TO_NATIVE)) {
            Map<String, Object> args = (Map<String, Object>) call.arguments;
            String action = (String) args.get("action");
            if (action.equals(ACTION_PUSH)) {
                DNavigationManager.getInstance().pushRoute((String) args.get("routeName"));
            } else if (action.equals(ACTION_POP)) {
                DNavigationManager.getInstance().pop();
            }
            result.success(null);
        } else {
            result.notImplemented();
        }
    }

    public void activateFlutterNode(DNode node) {
        channel.invokeMethod(METHOD_ACTION_TO_FLUTTER, new HashMap() {{
            put("action", ACTION_ACTIVATE);
            put("node", node.toMap());
        }});
    }
}
