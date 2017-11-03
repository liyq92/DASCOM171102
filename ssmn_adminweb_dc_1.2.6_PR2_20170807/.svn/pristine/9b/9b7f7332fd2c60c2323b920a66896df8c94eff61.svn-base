package com.dascom.ssmn.action;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;

import com.dascom.ssmn.dao.CDRDao;
import com.dascom.ssmn.dao.UtilFunctionDao;
import com.dascom.ssmn.entity.ZydcRecord;

public class test {

	public void testExport()
	{
		CDRDao cDao = CDRDao.getInstance();
		List<ZydcRecord> exportlist = null;
		int lcount =0;
		
		/*exportlist = cDao.getCdrStatRecordExportList("","","",0,0
				,"","","","","","",
				"","","",1000000146); */
		
			FileOutputStream fileoutput = null;
			try {
				fileoutput = new FileOutputStream("E:\\java_work\\tt.xlsx");
			
				SXSSFWorkbook wboutput = new SXSSFWorkbook(10000);//内存中保留 10000 条数据，以免内存溢出，其余写入 硬盘
				
				//总的数据
			     Sheet sheetoutputFirst = wboutput.createSheet("tt.xlsx");
			     Row rowtitle0 = sheetoutputFirst.createRow(lcount);
			     
			   //内容样式
			     CellStyle cellStylecontent = wboutput.createCellStyle();
			     Font fontcontent = wboutput.createFont();
			     fontcontent.setFontHeightInPoints((short) 11);
			     cellStylecontent.setFont(fontcontent);
			     
			     CellStyle cellStyleFont = wboutput.createCellStyle();
				 Font font2 = wboutput.createFont();
			     font2.setFontName("宋体");
			     font2.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);//粗体显示
			     font2.setFontHeightInPoints((short) 11);
			     
		     	 Cell cell0row0 = rowtitle0.createCell(0);
				 cell0row0.setCellType(XSSFCell.CELL_TYPE_STRING);//文本格式   
				 cell0row0.setCellValue("序号");
				 lcount++;
				 
				 for(int p =0;p<10000;p++)
					{
					 int lcoutemp =lcount;
						for(int i=0;i<100;i++){
							Row rowcdrnum = sheetoutputFirst.createRow(lcoutemp+i+1);
							Cell cell0 = rowcdrnum.createCell(0);
							cell0.setCellType(XSSFCell.CELL_TYPE_STRING);
							cell0.setCellValue("序号为："+i);
							cell0.setCellStyle(cellStylecontent);
							lcount++;
						}
					}
				wboutput.write(fileoutput);
				fileoutput.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		
	}
	
	public static void main(String[] args)
	{
		test t = new test();
		t.testExport();
		
		
	}
}
