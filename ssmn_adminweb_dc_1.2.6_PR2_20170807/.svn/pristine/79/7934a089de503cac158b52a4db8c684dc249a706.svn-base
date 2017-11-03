package com.dascom.OPadmin.util;

import java.io.File;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import com.dascom.OPadmin.util.DeleteFileUtil;


public class TySessionListener implements HttpSessionListener {

	public void sessionCreated(HttpSessionEvent sessionEvent) {
		String path = sessionEvent.getSession().getServletContext().getRealPath("tempUploadFiles");
		path += File.separator + sessionEvent.getSession().getId();
		File dir = new File(path);
		if(!dir.exists()){
			if(!dir.mkdirs()){
				System.out.println("mkdir failure!");
			}
		}
	}

	public void sessionDestroyed(HttpSessionEvent sessionEvent) {
		/*String path = sessionEvent.getSession().getServletContext().getRealPath("tempUploadFiles");
		path += File.separator + sessionEvent.getSession().getId();
		System.gc();
		//DeleteFileUtil.delete(path);*/

	}

}
