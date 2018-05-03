package com.bankscene.bes.welllinkbank.exception;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;


import com.bankscene.bes.welllinkbank.Util.Trace;
import com.bankscene.bes.welllinkbank.db1.DBHelper;
import com.bankscene.bes.welllinkbank.db1.Data;
import com.bankscene.bes.welllinkbank.db1.DataKey;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AppUncaughtExceptionHandler implements UncaughtExceptionHandler {

	// 需求是 整个应用程序 只有一个 MyCrash-Handler   
    private static AppUncaughtExceptionHandler myCrashHandler ;  
    private Context context;
    
  //1.私有化构造方法  
    private AppUncaughtExceptionHandler(){  
          
    }  
      
    public static synchronized AppUncaughtExceptionHandler getInstance(){  
        if(myCrashHandler!=null){  
            return myCrashHandler;  
        }else {  
            myCrashHandler  = new AppUncaughtExceptionHandler();  
            return myCrashHandler;  
        }  
    } 
    
    public void init(Context context){
        this.context = context;  
    }

    public static void saveToSDCard(String name, String content)
    {

        FileOutputStream fos = null;

        try
        {

            // Environment.getExternalStorageDirectory()。获取sd卡的路径
            File file = new File(Environment.getExternalStorageDirectory(), name);
            file.createNewFile();
            if (file.exists()){
                fos = new FileOutputStream(file);
                fos.write(content.getBytes());
            }
        } catch (Exception e)
        {

            e.printStackTrace();

        } finally
        {

            try
            {
                fos.close();
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }


    @Override
	public void uncaughtException(Thread thread, Throwable ex) {
        Trace.e("抛出异常","");
		 // 1.获取当前程序的版本号. 版本的id  
        String versioninfo = getVersionInfo();
          
        // 2.获取手机的硬件信息.  
        String mobileInfo  = getMobileInfo();
          
        // 3.把错误的堆栈信息 获取出来   
        String errorinfo = getErrorInfo(ex);
        Trace.e("crash",errorinfo);
        if (!TextUtils.isEmpty(DBHelper.getDataByKey(DataKey.cookie))){
            DBHelper.insert(new Data(DataKey.cookie,""));
        }
        // 4.把所有的信息 还有信息对应的时间 提交到服务器   
        try {  
//            service.createNote(new PlainTextConstruct(dataFormat.format(new Date())),   
//                    new PlainTextConstruct(versioninfo+mobileInfo+errorinfo), "public", "yes");  
        	
        	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        	saveToSDCard("WelllinkBankException.txt",sdf.format(new Date()) + ":" + versioninfo + "-" +mobileInfo + "-" +errorinfo);
//        	System.out.println();
        	
        } catch (Exception e) {
            e.printStackTrace();  
        }  
        
//        System.out.println(Calendar.getInstance().getTime() + ":" + versioninfo+mobileInfo+errorinfo);
      
        //重启
//        Intent intent = new Intent();
//        intent.setClass(context,MainActivity.class);
//        intent.addFlag(Intent.FLAG_ACTIVITY_NEW_TASK);
//        mContext.startActivity(intent);
        exitApp();
//        ActivityManager activityManager  = ActivityManager.getInstance();
//		Activity topActivity = activityManager.getTopActivity();
//        SZRCBNotification.logout(topActivity, CommStateManager.NAV_SJYH, false);
//        //干掉当前的程序   
//        android.os.Process.killProcess(android.os.Process.myPid()); 
//        System.exit(1);
	}
	
	/** 
     * 获取错误的信息  
     * @param arg1 
     * @return 
     */  
    private String getErrorInfo(Throwable arg1) {
        Writer writer = new StringWriter();
        PrintWriter pw = new PrintWriter(writer);
        arg1.printStackTrace(pw);  
        pw.close();  
        String error= writer.toString();
        return error;  
    }  
  
    /** 
     * 获取手机的硬件信息  
     * @return 
     */  
    private String getMobileInfo() {
        StringBuffer sb = new StringBuffer();
        //通过反射获取系统的硬件信息   
        try {  
  
            Field[] fields = Build.class.getDeclaredFields();
            for(Field field: fields){
                //暴力反射 ,获取私有的信息   
                field.setAccessible(true);  
                String name = field.getName();
                String value = field.get(null).toString();
                sb.append(name+"="+value);  
                sb.append("\n");  
            }  
        } catch (Exception e) {
            e.printStackTrace();  
        }  
        return sb.toString();  
    }  
  
    /** 
     * 获取手机的版本信息 
     * @return 
     */  
    private String getVersionInfo(){
        try {  
            PackageManager pm = context.getPackageManager();
             PackageInfo info =pm.getPackageInfo(context.getPackageName(), 0);
             return  info.versionName;  
        } catch (Exception e) {
            e.printStackTrace();  
            return "版本号未知";  
        }  
    }
    public static void exitApp()
    {
//        ActivityManager activityManager = ActivityManager.getInstance();
//        Activity topActivity = activityManager.getTopActivity();
//
//        if (topActivity != null)
//        {
//            android.app.ActivityManager activityManager2 = (android.app.ActivityManager) topActivity
//                    .getSystemService(Context.ACTIVITY_SERVICE);
//            activityManager2.killBackgroundProcesses("com.mobilex.ynrcbmobilebank");
//        }

//        if (CommDictAction.IsLogin)
//        {
//            SZRCBNotification.logout(topActivity, CommStateManager.NAV_SJYH, false);
//        }
//
//        activityManager.clearCurrentActivityStack();
//        activityManager.closeExitAppNeedClosedActivitys();

        int pid = android.os.Process.myPid();
        android.os.Process.killProcess(pid);
        // System.exit(0);
    }
}
