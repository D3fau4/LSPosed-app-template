package com.hato.templatehook

import de.robv.android.xposed.IXposedHookZygoteInit
import de.robv.android.xposed.XposedBridge

class Main : IXposedHookZygoteInit {
    override fun initZygote(startupParam: IXposedHookZygoteInit.StartupParam?) {
        XposedBridge.log("Hello from Main!")
    }
}