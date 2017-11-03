package com.dascom.init;

import java.io.File;

import javax.servlet.http.HttpServlet;

import com.dascom.ssmn.util.Constants;

public class Initialization extends HttpServlet{
	
	private static final long serialVersionUID = 1L;

	public void init(){
		
		try {
			Thread.sleep(30000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String execlDir = getServletContext().getRealPath(Constants.EXECL_FOLDER_NAME)+File.separator ;
        //String zipDir = getServletContext().getRealPath(Constants.ZIP_FOLDER_NAME)+File.separator ;
		String zipDir = "D:\\TEST\\";
        String callStisDir = getServletContext().getRealPath(Constants.DOWNLOAD_CALL_STATISTIC_FILENAME)+File.separator;
        
        Constants.setExeclDir(execlDir);
        Constants.setZipDir(zipDir);
        Constants.setCallStatistic(callStisDir);
		//配置文件初始化
		ConfigMgr.config_params();
	}
}
