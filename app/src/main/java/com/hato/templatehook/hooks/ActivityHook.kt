package com.hato.templatehook.hooks

import android.content.Context
import android.content.Intent
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XC_MethodReplacement
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.XposedHelpers.findClass
import de.robv.android.xposed.callbacks.XC_LoadPackage

class ActivityHook : IXposedHookLoadPackage {
    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam?) {
        // check if the package it trying to hook is compatible
        if (!lpparam?.packageName.equals("com.hato.test")) {
            return
        }

        var hookedmethods = 0

        for (method in findClass(
            "com.hato.test.SplashActivity",
            lpparam?.classLoader
        ).declaredMethods) {
            if (method.name.equals("showRootDialog")) {

                XposedBridge.log("Hooking: $method")

                XposedHelpers.findAndHookMethod(
                    "com.hato.test.SplashActivity",
                    lpparam?.classLoader,
                    method.name,
                    object : XC_MethodReplacement() {
                        @Throws(Throwable::class)
                        override fun replaceHookedMethod(param: MethodHookParam): Any? {
                            // Get context
                            val context = param.thisObject as Context

                            try {
                                // Get activity
                                val mainActivityClassName = "com.hato.test.MainActivity"
                                val mainActivityClass =
                                    context.classLoader.loadClass(mainActivityClassName)

                                // Launch activity
                                val intent = Intent(context, mainActivityClass)
                                context.startActivity(intent)
                            } catch (e: ClassNotFoundException) {
                                e.printStackTrace()
                            }

                            return null
                        }
                    }
                )
                hookedmethods++
            }
        }

    }
}