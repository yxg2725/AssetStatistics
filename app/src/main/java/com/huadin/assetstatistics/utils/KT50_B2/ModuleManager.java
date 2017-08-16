
package com.huadin.assetstatistics.utils.KT50_B2;


import com.huadin.assetstatistics.app.MyApplication;

public class ModuleManager {
	//全局的串口句柄，底层通过句柄操作模块

	public static int initLibSO() {//RM_KT45Q">dev/ttyMT2
		DeviceControl DevCtr = new DeviceControl("RM_KT45Q");//Configs.companyPower
		//初始化模块，为模块上电，为保证上电成功，建议先下电，在上电
		DevCtr.PowerOffDevice();
		DevCtr.PowerOnDevice();
		int result = MyApplication.getLinkage().open_serial("dev/ttyMT2");
		 MyApplication.getLinkage().Radio_Initialization();
		return result;
	}

	public static void destroyLibSO() {
		//关闭串口
		//if (MainActivity.success_state)
		MyApplication.getLinkage().close_serial();
		DeviceControl DevCtr = new DeviceControl("RM_KT45Q");
		//下电
		DevCtr.PowerOffDevice();
	}



}

