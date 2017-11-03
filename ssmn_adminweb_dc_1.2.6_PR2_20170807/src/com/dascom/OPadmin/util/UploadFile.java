package com.dascom.OPadmin.util;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import com.oreilly.servlet.MultipartRequest;

/**
 *  文件上传处理�? �? cos.jar
 *  生成EXCEL文件 用org.apache.poi
 * <p>Title: Legend CRM for DaChen Fund</p>
 * <p>Description: Legend CRM 3.1</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Legend SoftWare</p>
 * @author chenbingc@legend.com
 * @version 3.1
 */

public class UploadFile {

/**
 *提供单个或多个文件上传及存储，并返回�?上传文件的各种参�?
 * @param request
 * @param path
 * @param size
 * @param ZJfiletype
 * @return java.util.Vector
 * @throws Exception
 */
	public static java.util.Vector<Hashtable<String, String>> Upload(javax.servlet.http.HttpServletRequest request, java.lang.String path, int size) throws Exception {
        Hashtable<String, String> htparam = new Hashtable<String, String>();
        Hashtable<String, String> htfilename = new Hashtable<String, String>();
        Hashtable<String, String> htfiletype = new Hashtable<String, String>();
        Hashtable<String, String> htfilelength=new Hashtable<String, String>();
        Vector<Hashtable<String, String>> upload = new Vector<Hashtable<String, String>>();
        String fileparam, filename, filetype, paramname, paramvalue = null;

        try{
            MultipartRequest multi = new MultipartRequest(request,path,size);
            Enumeration params = multi.getParameterNames() ;
            while (params.hasMoreElements()) {
                paramname  = (String)params.nextElement();
				paramvalue = multi.getParameter(paramname);
				paramvalue = new String(paramvalue.getBytes("ISO-8859-1"), "UTF-8");
                if(paramvalue == null){
                	paramvalue = "null";
                }
                  //�?般参�?
                  htparam.put(paramname,paramvalue);
            }
            
            //上传文件处理
            int num=1;
            Enumeration files = multi.getFileNames();
            while (files.hasMoreElements()) {
            	
                fileparam = (String)files.nextElement();
				filename = multi.getFilesystemName(fileparam);
                filetype = multi.getContentType(fileparam);

                if(filetype!=null){
                	  
                	File finalname = new File(path+File.separator+filename);

                	htfilename.put(Integer.toString(num),filename);
                	htfilelength.put(Integer.toString(num),Long.toString(finalname.length()));
                	htfiletype.put(Integer.toString(num),filetype);
                	num=num+1;
                }else if(num <= 1){
                	htfilename.put(Integer.toString(-1),"null");
                }
            }

            upload.add(htparam);
            upload.add(htfilename);
            upload.add(htfiletype);
            upload.add(htfilelength);
        }catch (IOException e){
              e.printStackTrace();
              throw e;
        }catch(SecurityException e){
              //Log.log(e.toString());
              throw e;
        }
        return upload;
	}

	
	
	/**
	 *提供单个或多个文件上传及存储，按照上传个数返回相对长度集合，空时返回Null占位
	 * @param request
	 * @param path
	 * @param size
	 * @param ZJfiletype
	 * @return java.util.Vector
	 * @throws Exception
	 */
		public static java.util.Vector<Hashtable<String, String>> Upload2(javax.servlet.http.HttpServletRequest request, java.lang.String path, int size) throws Exception {
	        Hashtable<String, String> htparam = new Hashtable<String, String>();
	        Hashtable<String, String> htfilename = new Hashtable<String, String>();
	        Hashtable<String, String> htfiletype = new Hashtable<String, String>();
	        Hashtable<String, String> htfilelength=new Hashtable<String, String>();
	        Vector<Hashtable<String, String>> upload = new Vector<Hashtable<String, String>>();
	        String fileparam, filename, filetype, paramname, paramvalue = null;

	        try{
	            MultipartRequest multi = new MultipartRequest(request,path,size);
	            Enumeration params = multi.getParameterNames() ;
	            while (params.hasMoreElements()) {
	                paramname  = (String)params.nextElement();
					paramvalue = multi.getParameter(paramname);
					paramvalue = new String(paramvalue.getBytes("ISO-8859-1"), "UTF-8");
	                if(paramvalue == null){
	                	paramvalue = "null";
	                }
	                  //�?般参�?
	                  htparam.put(paramname,paramvalue);
	            }
	            
	            //上传文件处理
	            int num=1;
	            Enumeration files = multi.getFileNames();
	            while (files.hasMoreElements()) {
	            	
	                fileparam = (String)files.nextElement();
					filename = multi.getFilesystemName(fileparam);
	                filetype = multi.getContentType(fileparam);

	                if(filetype!=null){
	                	  
	                	File finalname = new File(path+File.separator+filename);
	                	
 	                	htfilename.put(Integer.toString(num),filename);
	                	htfilelength.put(Integer.toString(num),Long.toString(finalname.length()));
	                	htfiletype.put(Integer.toString(num),filetype);
	                }else{
	                	htfilename.put(Integer.toString(num),"null");
	                }
	                num=num+1;
	            }

	            upload.add(htparam);
	            upload.add(htfilename);
	            upload.add(htfiletype);
	            upload.add(htfilelength);
	        }catch (IOException e){
	              e.printStackTrace();
	              throw e;
	        }catch(SecurityException e){
	              //Log.log(e.toString());
	              throw e;
	        }
	        return upload;
		}
	
	
	
	
	

    /**
     * 判断文件是否存在
     * @param path
     * @param filename
     * @return
     * @throws SecurityException
     */
	public static boolean File_isExist(java.lang.String path, java.lang.String filename) throws SecurityException
    {
		boolean flag=false;
		try{
			File finalname=new File(path,filename);
			if (finalname.exists()){
				flag=true;
			}
		}
		catch(SecurityException e){
			// Log.log(e.toString());
			throw e;
		}
		return flag;
    }


    /**
     * 文件改名
     * @param path 源路�?
     * @param filename 源文件名
     * @param newfilename 新文件名
     * @param newpath 新路�?
     * @return
     * @throws SecurityException
     */
    public static boolean File_rename(java.lang.String path, java.lang.String filename, java.lang.String newfilename, java.lang.String newpath) throws SecurityException
    {
         boolean flag=false;
         try{
        	 if(File_MakeDir(newpath))
        	 {
        		 File renamefile=new File(path,filename);
        		 File finalname =new File(newpath,newfilename);
        		 if (finalname.exists()){
        			 finalname.delete();
        		 }
        		 renamefile.renameTo(finalname);
        	 }
         }
         catch(SecurityException e){
        	 // Log.log(e.toString());
        	 throw e;
         }
         return flag;
    }

    /**
     * 新建文件目录
     * @param path
     * @return
     * @throws SecurityException
     */
    public static boolean File_MakeDir(java.lang.String path) throws SecurityException
    {
    	File Dir =new File(path);
        boolean flag=false;
        try{
        	if(!Dir.exists()){
        		Dir.mkdir();
        		flag=true;
        	}else{flag=true;}
        }
        catch(SecurityException e){
        	// Log.log(e.toString());
        	throw e;
        }
        return flag;
    }
}
