package com.hato.templatehook.hooks

import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XC_MethodReplacement
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage

class ClassHook : IXposedHookLoadPackage {
    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam?) {
        if (!lpparam?.packageName.equals("com.hato.test")) {
            return
        }
        var hookedmethods = 0
        XposedBridge.log("Installing hooks...")

        for (method in XposedHelpers.findClass(
            "com.hato.test.testActivity",
            lpparam?.classLoader
        ).declaredMethods) {
            // search an action in PlayerActivity
            if (method.name.equals("settestListener")) {
                XposedHelpers.findAndHookMethod(
                    "com.hato.test.testActivity",
                    lpparam?.classLoader,
                    method.name,
                    object : XC_MethodReplacement() {
                        @Throws(Throwable::class)
                        override fun replaceHookedMethod(param: MethodHookParam): Any? {
                            // Do things

                            return XposedBridge.invokeOriginalMethod(
                                param.method,
                                param.thisObject,
                                param.args
                            )
                        }
                    }
                )
                hookedmethods++
            }
        }
    }
}