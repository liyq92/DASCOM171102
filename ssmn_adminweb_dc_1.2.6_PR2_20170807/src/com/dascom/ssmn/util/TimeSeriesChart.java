package com.dascom.ssmn.util;
/*
 * 折线图
 */
import java.awt.Color;
import java.awt.Font;  
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;  
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
  
import org.jfree.chart.ChartFactory;  
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.DateAxis;  
import org.jfree.chart.axis.DateTickUnit;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;  
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.labels.StandardXYItemLabelGenerator;
import org.jfree.chart.labels.StandardXYToolTipGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.XYPlot;  
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.Day;
import org.jfree.data.time.Month;  
import org.jfree.data.time.TimeSeries;  
import org.jfree.data.time.TimeSeriesCollection;  
import org.jfree.data.xy.XYDataset;  
import org.jfree.ui.RectangleInsets;
import org.jfree.ui.TextAnchor;


public class TimeSeriesChart {
	
	JFreeChart jfreechart; 
	//type: 1 按天 2：按月
	 public TimeSeriesChart(String title,String directxisTitle,String dateaxisTitle,Map<String,Map<String,Integer>> mapData,int type)
	 {  
	      XYDataset xydataset = createDataset(mapData,type);  
	      jfreechart = ChartFactory.createTimeSeriesChart(title,// 图表标题 
	    		  											directxisTitle,// 目录轴的显示标签 
	    		  											dateaxisTitle, // 数值轴的显示标签  
	    		  											xydataset,// 数据集   
	    		  											true,// 是否显示图例(对于简单的柱状图必须是false) 
	    		  											true,// 是否生成工具  
											false);// 是否生成URL链接 
		  XYPlot xyplot = (XYPlot) jfreechart.getPlot(); 
		  xyplot.setBackgroundPaint(Color.lightGray);//设置网格背景色
		  xyplot.setDomainGridlinePaint(Color.white);//设置网格竖线(Domain轴)颜色
		  xyplot.setRangeGridlinePaint(Color.white);//设置网格横线颜色
		  xyplot.setAxisOffset(new RectangleInsets(0D, 0D, 0D, 10D));//设置曲线图与xy轴的距离
		  xyplot.setDomainCrosshairVisible(true);
		  xyplot.setRangeCrosshairVisible(true);
		  XYItemRenderer r = xyplot.getRenderer();  
		  if (r instanceof XYLineAndShapeRenderer) 
		  {  
			  XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) r;  
			  renderer.setBaseShapesVisible(true);  
			  renderer.setBaseShapesFilled(true);  
			  renderer.setShapesVisible(true);// 设置曲线是否显示数据点  
		  }
		 /* XYLineAndShapeRenderer xylineandshaperenderer = (XYLineAndShapeRenderer)xyplot.getRenderer();
		  //设置曲线是否显示数据点
		  xylineandshaperenderer.setBaseShapesVisible(true);
		  //设置曲线显示各数据点的值
		  XYItemRenderer xyitem = xyplot.getRenderer();
		  xyitem.setBaseItemLabelsVisible(true);
		  xyitem.setBasePositiveItemLabelPosition(new ItemLabelPosition(ItemLabelAnchor.OUTSIDE12, TextAnchor.BASELINE_LEFT));
		  //下面三句是对设置折线图数据标示的关键代码
		  xyitem.setBaseItemLabelGenerator(new StandardXYItemLabelGenerator());
		  xyitem.setBaseItemLabelFont(new Font("Dialog", 1, 14));
		  xyplot.setRenderer(xyitem);*/
		  
		  // 设置X轴数据格式  
		  NumberAxis numAxis = (NumberAxis) xyplot.getRangeAxis();  
		  NumberFormat numFormater = NumberFormat.getNumberInstance();  
		  numFormater.setMinimumFractionDigits(0);  
		  numAxis.setNumberFormatOverride(numFormater);  

		  //// 设置Y轴数据格式   设置提示信息  
		  SimpleDateFormat df;
		  if(type == 1)
			  df=new SimpleDateFormat("yyyy-MM-dd");
		  else
			  df=new SimpleDateFormat("yyyy-MM");
	      XYItemRenderer renderer= xyplot.getRenderer();  
	      NumberFormat nf= NumberFormat.getNumberInstance();  
	      StandardXYToolTipGenerator toolg=new StandardXYToolTipGenerator("{0}:({1},{2})",df,nf);   
	      renderer.setToolTipGenerator(toolg);  
		  
	      //设置X轴
	      DateAxis dateaxis = (DateAxis) xyplot.getDomainAxis();  
	      dateaxis.setVerticalTickLabels(true);
	      dateaxis.setLabelFont(new Font("黑体",Font.BOLD,12));         //水平底部标题  
	      dateaxis.setTickLabelFont(new Font("宋体",Font.BOLD,12));  //垂直标题 

	      dateaxis.setDateFormatOverride(df);
	      if(type ==1)
	    	  dateaxis.setTickUnit(new DateTickUnit(DateTickUnit.DAY,1));//设置时间间隔为一天  
	      else
	    	  dateaxis.setTickUnit(new DateTickUnit(DateTickUnit.MONTH,1));//设置时间间隔为一个月  

		 // ValueAxis rangeAxis=xyplot.getRangeAxis(); 
		  //rangeAxis.setLabelFont(new Font("黑体",Font.BOLD,15));  
		  jfreechart.getLegend().setItemFont(new Font("黑体", Font.BOLD, 15));  
		  jfreechart.getTitle().setFont(new Font("宋体",Font.BOLD,20));//设置标题字体  
	 
	 }   
	 private static XYDataset createDataset(Map<String,Map<String,Integer>> mapData,int type) {  //这个数据集有点多，但都不难理解  
	  
		 Set<String> keys = mapData.keySet();
		 TimeSeriesCollection timeseriescollection = new TimeSeriesCollection();  
		 
		 for(Iterator it=keys.iterator();it.hasNext();)
		 {
			 String sKey =(String)it.next();
			 TimeSeries timeseries;
			 if(type ==1)//按天
				 timeseries= new TimeSeries(sKey,  
					  org.jfree.data.time.Day.class); 
			 else//按月
				 timeseries= new TimeSeries(sKey,  
						  org.jfree.data.time.Month.class); 
			 
			 Map<String,Integer> mapTemp = mapData.get(sKey);
			 Set<String> keysTemp = mapTemp.keySet();
			 for(Iterator ittemm =keysTemp.iterator();ittemm.hasNext();)
			 {
				 String sKeytemp = (String) ittemm.next();
				 if(type ==1)//按天
					 timeseries.add(getDayFromString(sKeytemp), mapTemp.get(sKeytemp));  
				 else//按月
					 timeseries.add(getMonthFromString(sKeytemp), mapTemp.get(sKeytemp));  
			 }
			 timeseriescollection.addSeries(timeseries);  
		 }  
		 timeseriescollection.setDomainIsPointsInTime(true);
	 return timeseriescollection;  
	 }  
	 public JFreeChart getChartPanel(){  
	          return jfreechart;  
	            
       }
	 //返回 天
	 public static org.jfree.data.time.Day getDayFromString(String sdate)
	{
		
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Calendar ca =Calendar.getInstance();
		try {
			ca.setTime(df.parse(sdate));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int yy= ca.get(Calendar.YEAR);
		int mm = ca.get(Calendar.MONTH)+1;
        int dd = ca.get(Calendar.DATE);
        org.jfree.data.time.Day d = new Day(dd,mm,yy);
        return d;
	}
	 //返回 月
	 public static org.jfree.data.time.Month getMonthFromString(String sdate)
		{
			
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM");
			Calendar ca =Calendar.getInstance();
			try {
				ca.setTime(df.parse(sdate));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			int yy= ca.get(Calendar.YEAR);
			int mm = ca.get(Calendar.MONTH)+1;
	        org.jfree.data.time.Month d = new Month(mm,yy);
	        return d;
		}

}
