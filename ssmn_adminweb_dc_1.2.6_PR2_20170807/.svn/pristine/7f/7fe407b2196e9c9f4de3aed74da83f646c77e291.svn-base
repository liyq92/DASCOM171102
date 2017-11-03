package com.dascom.OPadmin.util;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
//import org.apache.poi.hssf.usermodel.examples.CreateExcel;

public class ExportExcel{
	
/**
 * 以Web方式输出Excel文件
 * @param request
 * @param response
 * @param title
 * @param data
 * @throws Exception
 */
public static void WebExportExcel(HttpServletRequest request, HttpServletResponse response,String[] title,String[][] data,String filename)
	throws Exception
{
 try
      {
       // HSSFWorkbook wb = CreateExcel.NewExcel(title, data);
	 HSSFWorkbook wb = new  HSSFWorkbook();
	 HSSFSheet sheetoutputFirst = wb.createSheet(filename);
	 int rowindex = 0;
	 //title
	 HSSFRow rownum = sheetoutputFirst.createRow(0);
	 rowindex++;
	 for(int i=0; i<title.length; i++)
	 {
		 HSSFCell cellnumindex = rownum.createCell(i);
		 cellnumindex.setCellValue(title[i]);
	 }
	 //内容
	 for(int j=0; j<data.length; j++)
	 {
		 String[] datas = data[j];
		 HSSFRow rowIndex = sheetoutputFirst.createRow(rowindex);
		 rowindex++;
		 for(int t = 0; t<datas.length;t++)
		 {
			 HSSFCell rowcellindex = rowIndex.createCell(t);
			 rowcellindex.setCellValue(data[j][t]);
		 }
	 }
        ServletOutputStream out = response.getOutputStream();
        if(!request.getServerName().equals("localhost"))
        response.setContentType("application/attachment");
        response.setHeader("Content-disposition:attachment","attachment;filename="+new String(filename.getBytes("GBK"),"iso-8859-1")+".xls");
        wb.write(out);
        out.close();
      }
      catch(Exception e)
      {
        //Log.log("WebExportExcel  "+e.toString());
        throw e;
  }

}
}
