package com.dascom.ssmn.quartz;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPalette;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.hssf.util.Region;

import com.dascom.ssmn.dao.CDRDao;
import com.dascom.ssmn.entity.SsmnZyLevel;
import com.dascom.ssmn.entity.SsmnZyUser;
import com.dascom.ssmn.util.Constants;
import com.dtbl.zip.ZIPUtil;

public class TimingJobTest {
	//private static Logger log = Logger.getLogger(TimingJob.class.getName());
	//private CDRDao cdao = CDRDao.getInstance();
	//private MsgDao mdao = MsgDao.getInstance();
//	private String[] title={"来电时间", "房源信息", "事业部", "分行组别", "经纪人姓名","主号码","副号码","渠道标记","通话类型","通话开始时间","通话结束时间","通话时长","客户电话","录音文件名称","备注（无效/接通/未接通）"};
	//private String[] title={"通话记录统计", "", "", "", "","","","","","","","","","",""};
	
	public void JobTest( ) {
		//try {
			/*String yesterday = this.getYesterdayStr(Constants.DATE_FORMATE);
			String yesterdaysource = this.getYesterdayStr(Constants.DATE_FORMATE2);
			// 将通话录音文件打成zip包
//			String topath = Constants.DOWNLOAD_ZIP_DIR+Constants.DOWNLOAD_FILENAME+"_"+yesterday+".zip";
			String topath = "E:/java/workspace/ssmn_adminweb_dc/WebRoot/zipdownload/"+Constants.DOWNLOAD_FILENAME+"_"+yesterday+".zip"; //测试
			String sourcepath = Constants.AUDIO_PATH+yesterdaysource;
    		File file = new File(sourcepath);
   		    if (!file.exists()) {
   			   log.error("------------ "+yesterdaysource+"不存在-----");
   		    }else{
   		    	ZIPUtil.createzipfile(sourcepath, topath);
   		    	log.error("------------ 生成zip-----"+topath);
   		    }*/
			
			//生成统计报表
//			String execlSavePath = Constants.DOWNLOAD_EXECL_DIR+Constants.DOWNLOAD_EXECL_NAME+"_"+yesterday+".xls";
//			String execlSavePath = "E:/java/workspace/ssmn_adminweb_dc/WebRoot/statexecl/"+Constants.DOWNLOAD_EXECL_NAME+"_"+yesterday+".xls"; //测试
//			log.info("------------ execlSavePath-----"+execlSavePath);
//			List<ZydcRecord> cdrlist = cdao.getCdrStatRecord();
//			log.info("------------ cdrlist-----"+cdrlist.size());
//			List<ZydcRecord> msglist = mdao.getMsgStatRecord();
//			log.info("------------ msglist-----"+msglist.size());
//			String[][] data = new String[cdrlist.size()+msglist.size()+9][title.length+1];
//			data[0][0] = "来电时间";
//			data[0][1] = "房源信息";
//			data[0][2] = "事业部";
//			data[0][3] = "分行组别";
//			data[0][4] = "经纪人姓名";
//			data[0][5] = "主号码";
//			data[0][6] = "副号码";
//			data[0][7] = "渠道标记";
//			data[0][8] = "通话类型";
//			data[0][9] = "通话开始时间";
//			data[0][10] = "通话结束时间";
//			data[0][11] = "通话时长";
//			data[0][12] = "客户电话";
//			data[0][13] = "录音文件名称";
//			data[0][14] = "备注（无效/接通/未接通）";
//			
//			
//			for(int l = 0;l < cdrlist.size(); l++){   //遍历通话记录
//				ZydcRecord cdr = cdrlist.get(l);
//				data[l+1][0] = cdr.getYesterday();
//				data[l+1][1] = null;
//				data[l+1][2] = null;
//				data[l+1][3] = cdr.getGroupname();
//				data[l+1][4] = cdr.getUsername();
//				data[l+1][5] = cdr.getMsisdn();
//				data[l+1][6] = cdr.getSsmnnumber();
//				data[l+1][7] = cdr.getChannelname();
//				data[l+1][8] = cdr.getCalltype();
//				data[l+1][9] = cdr.getCallstarttime();
//				data[l+1][10] = cdr.getCallstoptime();
//				data[l+1][11] = cdr.getCallduration().toString();
//				data[l+1][12] = cdr.getClientnumber();
//				data[l+1][13] = null;
//				data[l+1][14] = cdr.getEndreason();
//				
//			}
//			
//			
//			data[cdrlist.size()+6][0] = "短信内容统计";
//			//短信统计的标题,通话记录和短信记录间隔5行
//			
//			data[cdrlist.size()+7][0] = "通信时间";
//			data[cdrlist.size()+7][1] = "房源信息";
//			data[cdrlist.size()+7][2] = "事业部";
//			data[cdrlist.size()+7][3] = "分行组别";
//			data[cdrlist.size()+7][4] = "经纪人姓名";
//			data[cdrlist.size()+7][5] = "主号码";
//			data[cdrlist.size()+7][6] = "副号码";
//			data[cdrlist.size()+7][7] = "渠道标记";
//			data[cdrlist.size()+7][8] = "客户号码";
//			data[cdrlist.size()+7][9] = "短信接收时间";
//			data[cdrlist.size()+7][10] = "短信内容";
//			
//			
//			
//			for(int i = 0; i < msglist.size(); i++){
//				ZydcRecord msg = msglist.get(i);
//				data[cdrlist.size()+8+i][0] = msg.getYesterday();
//				data[cdrlist.size()+8+i][1] = null;
//				data[cdrlist.size()+8+i][2] = null;
//				data[cdrlist.size()+8+i][3] = msg.getGroupname();
//				data[cdrlist.size()+8+i][4] = msg.getUsername();
//				data[cdrlist.size()+8+i][5] = msg.getMsisdn();
//				data[cdrlist.size()+8+i][6] = msg.getSsmnnumber();
//				data[cdrlist.size()+8+i][7] = msg.getChannelname();
//				data[cdrlist.size()+8+i][8] = msg.getClientnumber();
//				data[cdrlist.size()+8+i][9] = msg.getRcvmsgtime();
//				data[cdrlist.size()+8+i][10] = msg.getMsgcontent();
//			}
//			
//			//生成报表
//			HSSFWorkbook wb = CreateExcel.NewExcel(title, data);
//			FileOutputStream fout = new FileOutputStream(execlSavePath);
//	        wb.write(fout);
//	        fout.close();
			
		/*} catch (Exception e) {
			//log.error("------------ 执行定时任务发生异常-----",e);
		}
		*/
	}
	
	
		/**
		 * 获取昨天时间的字符串
		 * @return
		 */
		public String getYesterdayStr(String formattype){
			SimpleDateFormat dateFormat = new SimpleDateFormat(formattype);
			Calendar cal = Calendar.getInstance();
			int day_of_month = cal.get(Calendar.DAY_OF_MONTH);  
			cal.set(Calendar.DAY_OF_MONTH, day_of_month-1);
			
			return dateFormat.format(cal.getTime()).trim();
		}
		
		

		    /** 
		     * 将图片写入到磁盘 
		     * @param img 图片数据流
		     * @param fileName 文件保存时的名称 
		     */  
		    public static void writeImageToDisk(byte[] img, String fileName){  
		        try {  
		            File file = new File("D:\\" + fileName);  
		            FileOutputStream fops = new FileOutputStream(file);  
		            fops.write(img);  
		            fops.flush();  
		            fops.close();  
		            System.out.println("mp3已经写入到C盘");  
		        } catch (Exception e) {  
		            e.printStackTrace();  
		        }  
		    }  
		    /** 
		     * 根据地址获得数据的字节流 
		     * @param strUrl 网络连接地址 
		     * @return 
		     */  
		    public static byte[] getImageFromNetByUrl(String strUrl){  
		        try {  
		            URL url = new URL(strUrl);  
		            HttpURLConnection conn = (HttpURLConnection)url.openConnection();  
		            conn.setRequestMethod("GET");  
		            conn.setConnectTimeout(5 * 1000);  
		            InputStream inStream = conn.getInputStream();//通过输入流获取图片数据  
		            byte[] btImg = readInputStream(inStream);//得到图片的二进制数据  
		            return btImg;  
		        } catch (Exception e) {  
		            e.printStackTrace();  
		        }  
		        return null;  
		    }  
		    /** 
		     * 从输入流中获取数据 
		     * @param inStream 输入流 
		     * @return 
		     * @throws Exception 
		     */  
		    public static byte[] readInputStream(InputStream inStream) throws Exception{  
		        ByteArrayOutputStream outStream = new ByteArrayOutputStream();  
		        byte[] buffer = new byte[1024];  
		        int len = 0;  
		        while( (len=inStream.read(buffer)) != -1 ){  
		            outStream.write(buffer, 0, len);  
		        }  
		        inStream.close();  
		        return outStream.toByteArray();  
		    }
		    
		    
			 public static void main(String[] args) {  
			        String url = "http://192.168.21.28:8088/ssmn_adminweb_dc/aa.mp3";  
			        byte[] btImg = getImageFromNetByUrl(url);  
			        if(null != btImg && btImg.length > 0){  
			            System.out.println("读取到：" + btImg.length + " 字节");  
			            String fileName = "person.mp3";  
			            writeImageToDisk(btImg, fileName);  
			        }else{  
			            System.out.println("没有从该连接获得内容");  
			        }  
				 
				 
			    }  
}
