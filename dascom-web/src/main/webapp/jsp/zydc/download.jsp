<%@ page contentType="text/html; charset=utf-8" import="java.net.*,java.util.*,java.io.*"  %>
<%
 String path = request.getParameter("path");
  String fileType = request.getParameter("type");
 BufferedInputStream bis = null;
 BufferedOutputStream bos = null;
 InputStream inStream = null;
 String fileName="";
 if(path.indexOf("\\")>0)
 	fileName = path.substring(path.lastIndexOf("\\")+1,path.length());
 else
 	fileName =path.substring(path.lastIndexOf("/")+1,path.length());
 //给文件改扩展名
 if(fileType !=null && !"".equals(fileType) && fileType.equals("JPEG") && fileName.length()>0 )
 {
 	fileName = fileName.substring(0,fileName.lastIndexOf(".")) +".jpeg";
 } 
   
 response.setHeader("Content-disposition","attachment; filename=" + fileName); 
 //Content-disposition为属性名。
 //attachment表示以附件方式下载。如果要在页面中打开，则改为inline。
 //filename如果为中文，则会出现乱码。解决办法：
 //使用fileName = new String(fileName.getBytes(), "ISO8859-1")语句
 try {
	  //String appname=request.getContextPath();
	  //String filePath = request.getSession().getServletContext().getRealPath("/");
	  //filePath += path.substring(path.indexOf(appname)+appname.length()+1,path.length());
	 
	 bos = new BufferedOutputStream(response.getOutputStream());
	 
	 if(path.indexOf("http") >=0)
	 {
	 	//说明是url地址
	 	URL url = new URL(path);  
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();  
        conn.setRequestMethod("GET");  
        conn.setConnectTimeout(5 * 1000);  
        inStream = conn.getInputStream();//通过输入流获取录音
        
        byte[] buff = new byte[2048];
	  	int bytesRead;
	  	while(-1 != (bytesRead =inStream.read(buff))) {
	  	bos.write(buff,0,bytesRead);
	  }
        
	 }else
	 {
	 	//不是url地址
	  	bis = new BufferedInputStream(new FileInputStream(path));
	  	byte[] buff = new byte[2048];
	  	int bytesRead ;
	  	
	  	while(-1 != (bytesRead=bis.read(buff, 0, buff.length))) {
	  	bos.write(buff,0,bytesRead);
	  }
	 }	  
	  
	  out.clear();
	  out = pageContext.pushBody();
 }finally {
	  if (bis != null)
	 	 bis.close();
	  if (bos != null)
	 	 bos.close();
	 if(inStream !=null)
	 	inStream.close();
 }
%>