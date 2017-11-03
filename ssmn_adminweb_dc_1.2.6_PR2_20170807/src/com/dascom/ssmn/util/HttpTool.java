package com.dascom.ssmn.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.InputStreamRequestEntity;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import com.dascom.ssmn.dao.BlackCallNumberDao;

public class HttpTool {
	 /**
     * 发送post请求
     * 
     * @param params
     *            参数
     * @param requestUrl
     *            请求地址
     * @param authorization
     *            授权书
     * @return 返回结果
     * @throws IOException
     */
	private static final Logger logger =  Logger.getLogger(BlackCallNumberDao.class);
	public static String sendPost(String params, String requestUrl,
            String authorization) throws IOException {

        byte[] requestBytes = params.getBytes("utf-8"); // 将参数转为二进制流
        HttpClient httpClient = new HttpClient();// 客户端实例化
        PostMethod  postMethod = new PostMethod (requestUrl);
        //设置请求头Authorization
        postMethod.setRequestHeader("Authorization", "Basic " + authorization);
        // 设置请求头  Content-Type
        postMethod.setRequestHeader("Content-Type", "application/json");
//        postMethod.setRequestHeader("Content-Length",requestBytes.length);
        InputStream inputStream = new ByteArrayInputStream(requestBytes, 0,
                requestBytes.length);
        RequestEntity requestEntity = new InputStreamRequestEntity(inputStream,
                requestBytes.length, "application/json; charset=utf-8"); // 请求体
        postMethod.setRequestEntity(requestEntity);
        httpClient.executeMethod(postMethod);// 执行请求
        InputStream soapResponseStream = postMethod.getResponseBodyAsStream();// 获取返回的流
        byte[] datas = null;
        try {
            datas = readInputStream(soapResponseStream);// 从输入流中读取数据
        } catch (Exception e) {
//            e.printStackTrace();
            logger.error("--------sendPost error-----", e);
        }
        String result = new String(datas, "UTF-8");// 将二进制流转为String
        // 打印返回结果
         logger.info("----res------"+new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date())+"----------"+result+"\n");
        //解析取到的结果
         try {
			JSONObject jsonObject = new JSONObject(result);
			System.out.println("-------key: "+jsonObject.getString("resultMsg")+"     \n");
	        System.out.println("-------value: "+jsonObject.getString("resultCode")+"   \n");
	        result =jsonObject.getString("resultMsg");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			result ="呼叫失败!";
			e.printStackTrace();
		}
        return result;

    }

    /**
     * 从输入流中读取数据
     * 
     * @param inStream
     * @return
     * @throws Exception
     */
    public static byte[] readInputStream(InputStream inStream) throws Exception {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }
        byte[] data = outStream.toByteArray();
        outStream.close();
        inStream.close();
        return data;
    }
    
    public static void main(String[] args)
    {
    	String url = "http://163.177.146.82:9981/zhserver/doubleCall";
    	try {
	    	JSONObject jsonObject = new JSONObject();
			jsonObject.put("caller", "15010347829");
	        jsonObject.put("callee", "17701264106");
	        jsonObject.put("flag", "0");
	        System.out.print("----req------"+new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date())+"----------"+ jsonObject.toString()+"\n");
			HttpTool.sendPost(jsonObject.toString(),url,"test");
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
