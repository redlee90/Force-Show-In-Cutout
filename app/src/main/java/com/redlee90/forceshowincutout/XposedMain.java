package com.redlee90.forceshowincutout;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class XposedMain implements IXposedHookLoadPackage {

	@Override
	public void handleLoadPackage(final XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {

		XposedHelpers.findAndHookMethod("android.app.Activity", lpparam.classLoader, "onAttachedToWindow", new XC_MethodHook() {
			@TargetApi(Build.VERSION_CODES.P)
			@Override
			protected void afterHookedMethod(MethodHookParam param) throws Throwable {
				super.afterHookedMethod(param);

				Activity activity = (Activity) param.thisObject;
				Window window = activity.getWindow();
				WindowManager.LayoutParams attributes = window.getAttributes();
				if (attributes.layoutInDisplayCutoutMode != WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES) {
					attributes.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
					window.setAttributes(attributes);
				}

				if ((window.getDecorView().getSystemUiVisibility() & View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN) == 0) {
					window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
				}
			}
		});

		XposedHelpers.findAndHookMethod("android.view.Window", lpparam.classLoader, "setFlags", int.class, int.class, new XC_MethodHook() {
			@TargetApi(Build.VERSION_CODES.P)
			@Override
			protected void afterHookedMethod(MethodHookParam param) throws Throwable {
				super.afterHookedMethod(param);

				Window window = (Window) param.thisObject;
				WindowManager.LayoutParams attributes = window.getAttributes();
				if (attributes.layoutInDisplayCutoutMode != WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES) {
					attributes.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
					window.setAttributes(attributes);
				}

				if ((window.getDecorView().getSystemUiVisibility() & View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN) == 0) {
					window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
				}
			}
		});

		XposedHelpers.findAndHookMethod("android.view.Window", lpparam.classLoader, "setAttributes", WindowManager.LayoutParams.class, new XC_MethodHook() {
			@TargetApi(Build.VERSION_CODES.P)
			@Override
			protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
				super.beforeHookedMethod(param);

				WindowManager.LayoutParams attributes = (WindowManager.LayoutParams) param.args[0];
				if (attributes.layoutInDisplayCutoutMode != WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES) {
					attributes.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
				}
			}
		});

	}
}
