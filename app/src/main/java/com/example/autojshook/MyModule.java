package com.example.autojshook;

import android.os.Environment;

import java.io.FileWriter;
import java.io.IOException;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class MyModule implements IXposedHookLoadPackage {
    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam loadPackageParam) throws Throwable {
        final Class<?> class1 = XposedHelpers.findClass("com.stardust.autojs.script.StringScriptSource", loadPackageParam.classLoader);
        XposedHelpers.findAndHookConstructor(class1, String.class, String.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                String data = (String)param.args[1];   //数据
                String name = (String)param.args[0];   //文件名
                XposedBridge.log("前几个数据为"+data.substring(0, 100));
                XposedBridge.log("开始保存"+name);
                strToFile(data, name);
                XposedBridge.log("保存完成"+name);
                super.afterHookedMethod(param);
            }
        });
    }

    private static void strToFile(String data, String name){
        String path = Environment.getExternalStorageDirectory()+"/"+name;
        XposedBridge.log("保存路径为："+path);

        FileWriter fwriter = null;
        try {
            fwriter = new FileWriter(path);
            fwriter.write(data);
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if(fwriter != null)
                try {
                    fwriter.flush();
                    fwriter.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
        }

    }


}


