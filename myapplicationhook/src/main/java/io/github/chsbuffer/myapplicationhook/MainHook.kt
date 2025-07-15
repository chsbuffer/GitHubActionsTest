package io.github.chsbuffer.myapplicationhook

import android.widget.LinearLayout;
import android.app.Activity
import android.content.res.XModuleResources
import android.widget.TextView
import de.robv.android.xposed.IXposedHookInitPackageResources
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.IXposedHookZygoteInit
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XC_MethodReplacement
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_InitPackageResources
import de.robv.android.xposed.callbacks.XC_LoadPackage
import org.luckypray.dexkit.wrap.DexMethod
import java.lang.reflect.Method

class MainHook : IXposedHookLoadPackage, IXposedHookZygoteInit {
    private lateinit var startupParam: IXposedHookZygoteInit.StartupParam

    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        val classLoader = lpparam.classLoader
        XposedBridge.log("Start!")

        val outer = DexMethod("Lio/github/chsbuffer/myapplication/MainActivity\$MyTask;->run()V")
            .getMethodInstance(classLoader)

        val inner =
            DexMethod("Lio/github/chsbuffer/myapplication/MainActivity\$Companion;->getText()Ljava/lang/String;")
                .getMethodInstance(classLoader)


//        XposedBridge.hookMethod(inner, XC_MethodReplacement.returnConstant("Hooked!"))
//        return;

        fun MygetText(): String {
            val runtime = Runtime.getRuntime();
            return "Hooked!\nused: ${runtime.totalMemory() - runtime.freeMemory()}  free: ${runtime.freeMemory()} / ${runtime.totalMemory()}"
        }

//        var flag = false
//        XposedBridge.hookMethod(outer, object : XC_MethodHook() {
//            override fun beforeHookedMethod(param: MethodHookParam) {
//                flag = true
//            }
//
//            override fun afterHookedMethod(param: MethodHookParam?) {
//                flag = false
//            }
//        })
//        XposedBridge.hookMethod(inner, object : XC_MethodHook() {
//            override fun beforeHookedMethod(param: MethodHookParam) {
//                if (flag)
//                    param.result = MygetText()
//            }
//        })
//
//        return
        val innerHook = object : XC_MethodHook() {
            override fun beforeHookedMethod(param: MethodHookParam) {
                param.result = MygetText()
            }
        }

        XposedBridge.hookMethod(outer, object : XC_MethodHook() {
            lateinit var unhook: Unhook

            override fun beforeHookedMethod(param: MethodHookParam) {
                unhook = XposedBridge.hookMethod(inner, innerHook)
            }

            override fun afterHookedMethod(param: MethodHookParam) {
                unhook.unhook()
            }
        })

    }


    override fun initZygote(startupParam: IXposedHookZygoteInit.StartupParam) {
        this.startupParam = startupParam
    }
}