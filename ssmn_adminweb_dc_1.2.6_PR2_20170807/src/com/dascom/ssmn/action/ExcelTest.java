package com.dascom.ssmn.action;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelTest {
	
   
    		  
public static void write_Excel( String xls_write_Address,ArrayList<String> ls,String[] sheetnames,String tips_cmd ) throws IOException 
{  
       
       
    FileOutputStream output = new FileOutputStream(new File(xls_write_Address));  //读取的文件路径   
    SXSSFWorkbook wb = new SXSSFWorkbook(10000);//内存中保留 10000 条数据，以免内存溢出，其余写入 硬盘          
          
    CellStyle cellStyleTotalTitle = wb.createCellStyle();
    cellStyleTotalTitle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
    cellStyleTotalTitle.setFillForegroundColor((short)50);
    
    System.out.print("bbbbbbbbbbbb\n");
        for(int sn=0;sn<ls.size();sn++){  
            Sheet sheet = wb.createSheet(String.valueOf(sn));  
            System.out.print(ls.size());
            wb.setSheetName(sn, sheetnames[sn]);     
            String ls2 = ls.get(sn);   
            for(int i=0;i<60000;i++)
            {
                Row row = sheet.createRow(i);  
                
                    Cell cell = row.createCell(0);                     
                    cell.setCellType(XSSFCell.CELL_TYPE_STRING);//文本格式   
                    cell.setCellStyle(cellStyleTotalTitle);
                sheet.setColumnWidth(0, 384); //设置单元格宽度  
                cell.setCellValue(ls2);//写入内容  
               // System.out.print(ls2);
            }
                     
     }    
    wb.write(output);  
    output.close();           
} 

public static void write_Excel2003( String xls_write_Address,ArrayList<String> ls,String[] sheetnames,String tips_cmd ) throws IOException 
{  
       
    FileOutputStream output = new FileOutputStream(new File(xls_write_Address));  //读取的文件路径   
    HSSFWorkbook wb = new HSSFWorkbook();//内存中保留 10000 条数据，以免内存溢出，其余写入 硬盘          
          
    CellStyle cellStyleTotalTitle = wb.createCellStyle();
    cellStyleTotalTitle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
    cellStyleTotalTitle.setFillForegroundColor((short)50);
    
    System.out.print("bbbbbbbbbbbb\n");
        for(int sn=0;sn<ls.size();sn++){  
            Sheet sheet = wb.createSheet(String.valueOf(sn));  
            System.out.print(ls.size());
            wb.setSheetName(sn, sheetnames[sn]);     
            String ls2 = ls.get(sn);   
            for(int i=0;i<65500;i++)
            {
                Row row = sheet.createRow(i);  
                
                    Cell cell = row.createCell(0);                     
                    cell.setCellType(XSSFCell.CELL_TYPE_STRING);//文本格式   
                    cell.setCellStyle(cellStyleTotalTitle);
                sheet.setColumnWidth(0, 384); //设置单元格宽度  
                cell.setCellValue(ls2);//写入内容  
               // System.out.print(ls2);
            }
          
                     
     }    
    wb.write(output);  
    output.close();           
} 

	
	public static void main(String[] args) {
		
		String[] sheetnames ={"aa","bb","cc"};
		ArrayList<String> ls = new ArrayList<String>();
		for(int i=0;i<3;i++)
		{
			 
			String temp= "11";
			ls.add(temp);
		}
		System.out.print("ls.size():     "+ls.size());
		try {
			write_Excel("D:\\TEST\\aa.xls",ls, sheetnames, "test" );
			//write_Excel2003("D:\\TEST\\aa.xls",ls, sheetnames, "test" );
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

}
