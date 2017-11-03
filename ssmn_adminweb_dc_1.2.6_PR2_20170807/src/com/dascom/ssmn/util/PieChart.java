package com.dascom.ssmn.util;

import java.awt.Font;  
import java.text.DecimalFormat;  
import java.text.NumberFormat;  
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
 
import javax.swing.JPanel;  
 
import org.jfree.chart.ChartFactory;  
import org.jfree.chart.ChartPanel;  
import org.jfree.chart.JFreeChart;  
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;  
import org.jfree.chart.plot.PiePlot;  
import org.jfree.data.general.DefaultPieDataset;

/*
 * 饼图
 */
public class PieChart {
	JFreeChart jfreechart;
	 public PieChart(String title,Map<String,Integer> mapData)
	 {  
		 DefaultPieDataset data = getDataSet(mapData);  
		 jfreechart = ChartFactory.createPieChart3D(title,data,true,true,false);  
		 //设置百分比  
		 PiePlot pieplot = (PiePlot) jfreechart.getPlot();  
		 DecimalFormat df = new DecimalFormat("0%");//获得一个DecimalFormat对象，主要是设置小数问题  
		 NumberFormat nf = NumberFormat.getNumberInstance();//获得一个NumberFormat对象  
		 nf.setGroupingUsed(false);
		 //设置上面的样式,0表示KEY,1表示VALUE,2表示百分之几,DecimalFormat用来显示百分比的格式  
		 pieplot.setLabelGenerator(new StandardPieSectionLabelGenerator("{0}:({1},{2})",nf,df));      
		 //设置下面方框的样式,0表示KEY,1表示VALUE,2表示百分之几,DecimalFormat用来显示百分比的格式  
		// pieplot.setLegendLabelGenerator(new StandardPieSectionLabelGenerator("{0}=>{1}({2})",nf, df));  

		 //没有数据的时候显示的内容  
		 pieplot.setNoDataMessage("无数据显示");  
		 pieplot.setCircular(false);  
		 pieplot.setLabelGap(0.02D);  
	     
		 pieplot.setIgnoreNullValues(true);//设置不显示空值  
		 pieplot.setIgnoreZeroValues(true);//设置不显示负值   
		 jfreechart.getTitle().setFont(new Font("宋体",Font.BOLD,20));//设置标题字体  
		 PiePlot piePlot= (PiePlot) jfreechart.getPlot();//获取图表区域对象  
		 piePlot.setLabelFont(new Font("宋体",Font.BOLD,14));//解决乱码  
		 jfreechart.getLegend().setItemFont(new Font("黑体",Font.BOLD,15));  
	}  
	 private static DefaultPieDataset getDataSet(Map<String,Integer> mapData)
	 {  
		 DefaultPieDataset dataset = new DefaultPieDataset();  
		 Set<String> keys = mapData.keySet();
		 for(Iterator it =keys.iterator();it.hasNext();)
		 {
			 String skey = (String) it.next();
			 int icount = mapData.get(skey);
			 dataset.setValue(skey,icount); 
		 }
		 
		 return dataset;  
	}  
	 public JFreeChart getChartPanel(){  
		  return jfreechart;  
		  } 

}
