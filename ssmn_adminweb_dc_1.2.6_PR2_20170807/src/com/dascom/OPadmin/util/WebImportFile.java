package com.dascom.OPadmin.util;

import java.io.File;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import com.oreilly.servlet.MultipartRequest;

/**
 * @author liangshuai 20100309 create 
 *
 */
public class WebImportFile {
	
	/**
	 * @param args
	 * request:HttpServletRequest请求对象
	 * path:文件存放路径 如D:\\upload
	 * size:文件大小限制
	 * 
	 * @returns
	 * java.util.Vector:存放post请求内容
	 * 
	 * @Throws:
	 * 失败则throw Exception
	 * 
	 */
	public static Vector<Hashtable<String,String>> Upload(javax.servlet.http.HttpServletRequest request, String path, int size) throws Exception {
		
		Hashtable<String,String> htparam = new Hashtable<String,String>();
		Hashtable<String,String> htfilename = new Hashtable<String,String>();
	  	Hashtable<String,String> htfiletype = new Hashtable<String,String>();
	   	Hashtable<String,String> htfilelength = new Hashtable<String,String>();
	  	Vector<Hashtable<String,String>> upload = new Vector<Hashtable<String,String>>();
	  	
	  	String fileparam, filename, filetype, paramname, paramvalue = null;

	  	try {
	        MultipartRequest multi = new MultipartRequest(request,path,size,"utf-8");
	        Enumeration params = multi.getParameterNames() ;
	        
	        while (params.hasMoreElements()) {
	          	paramname = (String)params.nextElement();
				paramvalue = multi.getParameter(paramname);
				
	            if(paramvalue == null) 
	            	paramvalue = "null";
	            
	           	//一般参数
	            htparam.put(paramname,paramvalue);
	        }

            //上传文件处理
            int num = 1;
            Enumeration files = multi.getFileNames();
            
            while (files.hasMoreElements()) {
            	
            	fileparam = (String)files.nextElement();
				filename = multi.getFilesystemName(fileparam);
             	filetype = multi.getContentType(fileparam);
             	
             	if(filetype != null) {
             		File finalname=new File(path,filename);
                    htfilename.put(Integer.toString(num), filename);
                    htfilelength.put(Integer.toString(num),Long.toString(finalname.length()));
                    htfiletype.put(Integer.toString(num),filetype);
                    num=num+1;
                }
            }
            upload.add(htparam);
            upload.add(htfilename);
            upload.add(htfiletype);
            upload.add(htfilelength);
        } catch (Exception e){
        	throw e;
        }
		return upload;
	}
	/**
	 * @param args
	 * request:HttpServletRequest请求对象
	 * path:文件存放路径 如D:\\upload
	 * size:文件大小限制
	 * 
	 * @returns
	 * java.util.Vector:存放post请求内容
	 * 
	 * @Throws:
	 * 失败则throw Exception
	 * 
	 */
	public static Vector<Hashtable<String,String>> Upload2(javax.servlet.http.HttpServletRequest request, String path, int size) throws Exception {
		
		Hashtable<String,String> htparam = new Hashtable<String,String>();
		Hashtable<String,String> htfilename = new Hashtable<String,String>();
	  	Hashtable<String,String> htfiletype = new Hashtable<String,String>();
	   	Hashtable<String,String> htfilelength = new Hashtable<String,String>();
	  	Vector<Hashtable<String,String>> upload = new Vector<Hashtable<String,String>>();
	  	
	  	String fileparam, filename, filetype, paramname, paramvalue = null;

	  	try {
	        MultipartRequest multi = new MultipartRequest(request,path,size,"utf-8");
	        Enumeration params = multi.getParameterNames() ;
	        
	        while (params.hasMoreElements()) {
	          	paramname = (String)params.nextElement();
				paramvalue = multi.getParameter(paramname);
	            if(paramvalue == null) 
	            	paramvalue = "null";
	            
	           	//一般参数
	            htparam.put(paramname,paramvalue);
	        }

            //上传文件处理
            Enumeration files = multi.getFileNames();
            while (files.hasMoreElements()) {
            	
            	fileparam = (String)files.nextElement();
				filename = multi.getFilesystemName(fileparam);
             	filetype = multi.getContentType(fileparam);

             	if(filetype != null) {
             		File finalname=new File(path,filename);
                    htfilename.put(fileparam, filename);
                    htfilelength.put(fileparam,Long.toString(finalname.length()));
                    htfiletype.put(fileparam,filetype);
                }
            }
            upload.add(htparam);
            upload.add(htfilename);
            upload.add(htfiletype);
            upload.add(htfilelength);
        } catch (Exception e){
        	throw e;
        }
		return upload;
	}
}
