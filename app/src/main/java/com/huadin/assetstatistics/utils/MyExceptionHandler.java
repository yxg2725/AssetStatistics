package com.huadin.assetstatistics.utils;

import android.content.Context;
import android.os.Build;
import android.os.Looper;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;

public class MyExceptionHandler implements UncaughtExceptionHandler {

	private static MyExceptionHandler mHandler;
	private static Context mContext;
	private MyExceptionHandler(){}
	public synchronized static MyExceptionHandler getInstance(Context context){
		if(mHandler==null){
			mHandler = new MyExceptionHandler();
			mContext = context;
		}
		return mHandler;
	}
	
	
	
	@Override
	public void uncaughtException(Thread arg0, Throwable ex) {
		try {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			System.out.println("错误信息" + sw.toString());
			File file = new File("/sdcard/err.txt");
			FileOutputStream fos = new FileOutputStream(file);
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yy-MM-dd HH:mm:ss");
			String time = simpleDateFormat.format(System.currentTimeMillis());
			fos.write(("time:"+time+"\n").getBytes());
			fos.flush();
			
			fos.write(sw.toString().getBytes());
			fos.flush();
			

			// U880

			// 获取手机的版本信息
			Field[] fields = Build.class.getFields();
			for (Field field : fields) {
				field.setAccessible(true); // 暴力反射
				String key = field.getName();
				String value = field.get(null).toString();
				
				fos.write((key+"="+value+"\n").getBytes());
				fos.flush();
				
			}

			new Thread(){public void run() {
				Looper.prepare();
				Toast.makeText(mContext, "程序挂掉了", Toast.LENGTH_SHORT).show();
				Looper.loop();
				
			};}.start();
			new Thread(){
				public void run() {
					try {
						Thread.sleep(3000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					android.os.Process.killProcess(android.os.Process.myPid());
				};
			}.start();
			
			fos.close();
			System.out.println("程序发生了异常,但是被哥捕获了");
	
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
