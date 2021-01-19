package com.redlee90.forceshowincutout;

import android.annotation.TargetApi;
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

				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
					window.setDecorFitsSystemWindows(false);
				} else {
					if ((window.getDecorView().getSystemUiVisibility() & View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN) == 0) {
						window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
					}
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

//		XposedHelpers.findAndHookMethod("com.android.internal.policy.PhoneWindow", lpparam.classLoader, "setDecorFitsSystemWindows", boolean.class, new XC_MethodHook() {
//			@Override
//			protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
//				super.beforeHookedMethod(param);
//
//				param.args[0] = false;
//			}
//		});

//		XposedHelpers.findAndHookMethod("androidx.core.view.WindowCompat", lpparam.classLoader, "setDecorFitsSystemWindows", Window.class, boolean.class, new XC_MethodHook() {
//			@Override
//			protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
//				super.beforeHookedMethod(param);
//
//				param.args[1] = false;
//			}
//		});
	}
}
