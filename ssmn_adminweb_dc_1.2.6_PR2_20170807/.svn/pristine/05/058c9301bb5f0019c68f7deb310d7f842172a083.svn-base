<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="/jsp/include/taglib.jsp" %>
<%@ include file="/jsp/include/script.jsp" %>
<%@ include file="/css/libcss.css" %>
<%@ page import="org.jfree.chart.JFreeChart,com.dascom.ssmn.util.TimeSeriesChart,
				java.util.Map,java.util.LinkedHashMap,java.io.OutputStream,java.io.FileOutputStream
				,java.io.File,java.text.SimpleDateFormat,java.util.Date,
				org.jfree.chart.ChartUtilities,java.io.PrintWriter,
				org.jfree.chart.ChartRenderingInfo,org.jfree.chart.servlet.ServletUtilities,
				org.jfree.chart.entity.StandardEntityCollection,java.io.IOException" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

<script type="text/javascript">
$(document).ready(function(e) {
    $(".select1").uedSelect({
		width : 345			  
	});
	$(".select2").uedSelect({
		width : 167  
	});
	$(".select3").uedSelect({
		width : 150
	});
});
</script>
<script language="JavaScript" type="text/JavaScript">
<!--
var wBox;
var vuid;

function cancel(){
	wBox.close();
}

function init(msg){
	if(msg != ''){
		wBox=jQuery("#wbox10").wBox({
		title: "返回结果",
		html: "<div style='width:280px;height:70px;'><table align='center' style='margin-top:40px;width:100%;'><tr><td align='center' style='font-size:15px;'>"+msg+"</td></tr></table></div>"
		});
		wBox.showBox();
	}
}

function createXMLHttpRequest(){
 	if(window.XMLHttpRequest){
		XMLHttpReq = new XMLHttpRequest();
	}
	else if(window.ActiveXObject){
		try{
			XMLHttpReq = new ActiveXObject("Msxml2.XMLHTTP");
		}catch(e){
			try{
				XMLHttpReq = new ActiveXObject("Microsoft.XMLHTTP");
			}catch(e){}
		}
	}
 }
 
//点击选择查询条件各等级下拉菜单  
function changeHeadSelect(type){
    if(type == '1'){
        var opObj = document.getElementById('bdbox1'); 
	       var select_root=document.getElementById('zonebox1');    
   	       select_root.options.length=0;
   	       var defa = new Option("全部","");
     	   select_root.add(defa);
     	   
		   var select_root1=document.getElementById('areabox1');    
   	       select_root1.options.length=0;
   	       var defa1 = new Option("全部","");
     	   select_root1.add(defa1);
     	
     	   var select_root2=document.getElementById('bagbox1');    
   	       select_root2.options.length=0;
   	       var defa2 = new Option("全部","");
     	   select_root2.add(defa2);
     	   
     	   if(opObj.selectedIndex!=0){
		      sendHeadRequest(type);
	        }
    }else if(type == '2'){
          var opObj = document.getElementById('zonebox1'); 
		   var select_root1=document.getElementById('areabox1');    
   	       select_root1.options.length=0;
   	       var defa1 = new Option("全部","");
     	   select_root1.add(defa1);
     	
     	   var select_root2=document.getElementById('bagbox1');    
   	       select_root2.options.length=0;
   	       var defa2 = new Option("全部","");
     	   select_root2.add(defa2);
     	   
     	   if(opObj.selectedIndex!=0){
     	      sendHeadRequest(type);
     	   }
	    
    }else{
        var opObj = document.getElementById('areabox1'); 
     	var select_root2=document.getElementById('bagbox1');    
   	    select_root2.options.length=0;
   	    var defa2 = new Option("全部","");
     	select_root2.add(defa2);
	    if(opObj.selectedIndex!=0){
		   sendHeadRequest(type);
	    }
    }
}

function sendHeadRequest(type){
 	createXMLHttpRequest();
 	var opObj = document.getElementById('bdbox1'); 
 	var opObj1 = document.getElementById('zonebox1'); 
    var opObj2 = document.getElementById('areabox1'); 
 	XMLHttpReq.open("POST","${ctx}/selectlevel",true);
	XMLHttpReq.onreadystatechange = processHeadResponse;
	XMLHttpReq.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
	XMLHttpReq.send("type="+type+"&bd="+opObj.value+"&warzone="+opObj1.value+"&area="+opObj2.value);
}

function processHeadResponse(type){
 	if(XMLHttpReq.readyState == 4){
		if(XMLHttpReq.status == 200){
		    //alert('success');
			parseHeadMessage();
		}else{
			alert(XMLHttpReq.status);
		}
	}
 }

 function parseHeadMessage()
{
  	var all=XMLHttpReq.responseText.split("#");  
  	var type = all[0];   
  	var select_root=document.getElementById('bdbox1');   
  	var defa;
  	if(type == '1'){
  	     select_root=document.getElementById('zonebox1');   
  	     select_root.options.length=0;
       	 defa = new Option("全部",""); 
  	}else if(type == '2'){
  	     select_root=document.getElementById('areabox1');  
  	     select_root.options.length=0;
       	 defa = new Option("全部","");   
  	}else{
  		select_root=document.getElementById('bagbox1');  
  	     select_root.options.length=0;
       	 defa = new Option("全部",""); 
  	}
  	
   	select_root.options.length=0;
    select_root.add(defa);
    for(var i=1; i<all.length-1; i++)
    {
        var xValue=all[i];
        var option=new Option(xValue,xValue);
        try{
            select_root.add(option);
        }catch(e){
        }
    }   
}

function download(path,type){
 var sUrl = "jsp/zydc/download.jsp?path="+path+"&type="+type;
 
 location.href = sUrl;
}

function changeDateSelect()
{
	var dateSe = document.getElementById('dateSelect');
	if(dateSe.value == '1'  )
	{
		document.getElementById('selectDay').style.display='inline';
		document.getElementById('selectMonth').style.display='none';
		document.getElementById('startdateMonth').value='';
		document.getElementById('enddateMonth').value='';
		
	}
	if(dateSe.value == '2'  )
	{
		document.getElementById('selectDay').style.display='none';
		document.getElementById('selectMonth').style.display='inline';
		document.getElementById('startdate').value='';
		document.getElementById('enddate').value='';
	}
}

function check()
{
	var dateSe = document.getElementById('dateSelect');
	if(dateSe.value == '')
	{
		alert('请输入日期!');
		return;
	}
	
	if(dateSe.value == '1'  )
	{
		var starDay = document.getElementById('startdate').value;
		var endDay = document.getElementById('enddate').value;
		if(starDay == '' && endDay =='' )
		{
			alert('请输入日期!');
			return;
		}
		if(starDay == '')
		{
			alert('请输入起始日期!');
			return;
		}
		if(endDay == '')
		{
			alert('请输入结束日期!');
			return;
		}
	}
	
	if(dateSe.value == '2'  )
	{
		var starMon = document.getElementById('startdateMonth').value;
		var endMon = document.getElementById('enddateMonth').value;
		if(starMon == '' && endMon =='')
		{
			alert('请输入日期!');
			return;
		}
		if(starMon == '')
		{
			alert('请输入起始月!');
			return;
		}
		if(endMon == '')
		{
			alert('请输入结束月!');
			return;
		}
	}
}

//-->
</script>
<style>
.tu{
	display:table-cell
	BORDER-RIGHT: #dcdbec 1px solid;
    BORDER-TOP: #dcdbec 1px solid;
    PADDING-LEFT:#dcdbec 10px;
    BORDER-LEFT: #dcdbec 1px solid;
    border-bottom: 1px;
    border-style: solid;
    border-color:#dcdbec;
    
    line-height: 35px;
    text-indent: 11px;
    border-right: dotted 1px #c7c7c7;
    text-align: center;
}
</style>
</head>

<body topmargin="0" onLoad="init('${msg}');changeDateSelect();">
<div class="place">
	<span>位置：</span>
	<ul class="placeul">
		<li><a href="#">首页</a></li>
		<li><a href="#">渠道呼入统计</a></li>
	</ul>
</div>

<div class="formbody" style="width:1100px;"> 
<form method="post" action="statchannelcallin.do"  onsubmit="return check();" name="mainfm">
	<table width="100%" align="center">
	 	 <tr style="height:40px;">
			<td class="t6" style="text-indent:10px; ">${businName }：</td>
			<td class="t7">
				<select class="select3" id='bdbox1' name='bdbox1' style='width:120px ; height:20px;' onchange='changeHeadSelect(1)' <c:if test="${slevelid==6 || slevelid==5 || slevelid==4 || slevelid==3 }"> disabled="disabled" </c:if> >
					<option value=''>全部</option>
					<c:forEach items="${bdlist}" var="bd" varStatus="s">
						<option value='${bd }' <c:if test="${bdparam==bd}">selected='true'</c:if>>${bd }</option>
					</c:forEach>
				</select>
			</td>
	    	<td class="t6" style="text-indent:10px;">${warName }：</td>
	    	<td class="t7">
		    	<select class="select3" id='zonebox1' name='zonebox1' style='width:120px ; height:20px;' onchange='changeHeadSelect(2)' <c:if test="${ slevelid==6 || slevelid==5 || slevelid==4 }"> disabled="disabled" </c:if> >
					<option value=''>全部</option>
					<c:forEach items="${wzlist}" var="wz" varStatus="s">
						<option value='${wz }' <c:if test="${zoneparam==wz}">selected='true'</c:if>>${wz }</option>
					</c:forEach>
				</select>
			</td>
	    	<td class="t6" style="text-indent:10px;">${areaName }：</td>
	    	<td class="t7">
		    	<select class="select3" id='areabox1' name='areabox1' style='width:120px ; height:20px;' onchange='changeHeadSelect(3)' <c:if test="${slevelid==6 || slevelid==5 }"> disabled="disabled" </c:if>>
					<option value=''>全部</option>
					<c:forEach items="${arealist}" var="ar" varStatus="s">
						<option value='${ar }' <c:if test="${areaparam==ar}">selected='true'</c:if>>${ar }</option>
					</c:forEach>
				</select>
			</td>
	    	<td class="t6" style="text-indent:10px;">${branName }：</td>
	    	<td class="t7">
		    	<select class="select3" id='bagbox1' name='bagbox1' style='width:120px ; height:20px;' <c:if test="${slevelid==6 }"> disabled="disabled" </c:if>>
					<option value=''>全部</option>
					<c:forEach items="${baglist}" var="bag" varStatus="s">
						<option value='${bag }' <c:if test="${bagparam==bag}">selected='true'</c:if>>${bag }</option>
					</c:forEach>
				</select>
			</td>
		</tr>
		<tr style="height:40px;">
			<td class="t6" style="text-indent:10px;">渠道：</td>
	    	<td class="t7">
		    	<select class="select3" id='channelbox' name='channelbox' style='width:120px ; height:20px;'>
					<option value=''>全部</option>
					<c:forEach items="${channels}" var="bag" varStatus="s">
						<option value='${bag.id }' <c:if test="${channelid==bag.id}">selected='true'</c:if>>${bag.name }</option>
					</c:forEach>
				</select>
			</td>
			<td class="t6" style="text-indent:10px;">时间：</td>
			<td class="t7">
				<select class="select3" style='width:120px ; height:20px;' id="dateSelect" name="dateSelect" onchange="changeDateSelect();" >
					<option value="1" <c:if test="${sDateSelect=='1'}">selected='true'</c:if>>按天查询</option>
					<option value="2" <c:if test="${sDateSelect=='2'}">selected='true'</c:if>>按月查询</option>
				</select>
			</td>
	    	<td class="t6" style="text-indent:10px;" >起止时间：</td>
			<td class="t7" colspan="3"  >
				<div id="selectDay" style="display:inline">
				&nbsp;从&nbsp;<input class="scinput" type="text" id="startdate"  name="startdate" value="${startdate }" onfocus="WdatePicker({firstDayOfWeek:1,readOnly:false,dateFmt:'yyyy-MM-dd'})" class="Wdate" style="width:120px"/> 日
				&nbsp;到&nbsp;<input class="scinput" type="text" id="enddate" name="enddate" value="${enddate }" onfocus="WdatePicker({firstDayOfWeek:1,readOnly:false,dateFmt:'yyyy-MM-dd',minDate:'#F{$dp.$D(\'startdate\')}',maxDate:'#F{$dp.$D(\'startdate\',{d:30})}'})" class="Wdate" style="width:120px"/> 日
		        </div>
		        <div id="selectMonth" style="display:none">
				&nbsp;从&nbsp;<input class="scinput" type="text" id="startdateMonth"  name="startdateMonth" value="${startdateMonth }" onfocus="WdatePicker({firstDayOfWeek:1,readOnly:false,dateFmt:'yyyy-MM',maxDate:'%y-%M'})" class="Wdate" style="width:120px"/> 月
				&nbsp;到&nbsp;<input class="scinput" type="text" id="enddateMonth" name="enddateMonth" value="${enddateMonth }" onfocus="WdatePicker({firstDayOfWeek:1,readOnly:false,dateFmt:'yyyy-MM',minDate:'#F{$dp.$D(\'startdateMonth\')}',maxDate:'#F{$dp.$D(\'startdateMonth\',{M:12})}'})" class="Wdate" style="width:120px"/> 月
		        </div>
	        </td>    	
	    </tr>    
	    <tr style="height:40px;">
	     	<td class="t7" style="text-align:center" colspan="8">
	  			<input class="scbtn" type="submit" value="查询" />&nbsp; &nbsp; &nbsp;  	
	  		</td>
	  	</tr>
	    <tr>
        	<td height="6" colspan="8" bgcolor="ECECF5"></td>
    	</tr> 
     	<tr><td>&nbsp;</td></tr>
	</table>
</form>

<c:if test="${numchannelcounts >0}" >
<table class="tablelist" align="left" style="border-collapse:collapse;margin-bottom:18px;" border="1">
	<thead>
		<tr>
			<th style="text-align:center;" colspan=${channelcountNumin} >渠道呼入统计</th>
		</tr>
	</thead>
	<c:forEach items="${channelcount}" var="c" varStatus="s">
	<tbody>
		<tr align="center" >
			<c:forEach items="${c}" var="m" varStatus="s">
				<td>${m} </td>
			</c:forEach>
		</tr>
	</tbody>
	</c:forEach>
</table>
<br/><br/><br/><br/><br/><br/><br/><br/>

<!-- 图表 -->
<%
	ChartRenderingInfo info = new ChartRenderingInfo(
			            	    new StandardEntityCollection());
	PrintWriter pw = new PrintWriter(out);
	String graphURLLine ="";
	String graphURLLineMonth = "";
	String fileNamePie = "";
	String fileName="";
	String fileNameLineMonth ="";
	String graphURLPie = "";
	
	//按天统计折线图
	JFreeChart chartLine = (JFreeChart)request.getAttribute("chartLine");
	if(chartLine !=null)
	{
		//生成图片热点的map，能鼠标到结点有提示数据    
	    fileName = ServletUtilities.saveChartAsPNG(chartLine, 900 , 300,info, request.getSession());//保存图表为文件
	    ChartUtilities.writeImageMap(pw, fileName,info,  false);
	    pw.flush();
		graphURLLine = request.getContextPath()+"/servlet/DisplayChart?filename="+fileName;
	}
	//按月统计折线图
	JFreeChart chartLineMonth = (JFreeChart)request.getAttribute("chartLineMonth");
	if(chartLineMonth !=null)
	{
		//生成图片热点的map，能鼠标到结点有提示数据
	    //PrintWriter pw = new PrintWriter(out);
	    fileNameLineMonth = ServletUtilities.saveChartAsPNG(chartLineMonth, 900 , 300,info, request.getSession());//保存图表为文件
	    ChartUtilities.writeImageMap(pw, fileNameLineMonth,info,  false);
	    pw.flush();
		graphURLLineMonth = request.getContextPath()+"/servlet/DisplayChart?filename="+fileNameLineMonth;
	}
	//按饼图统计
	JFreeChart chartPie = (JFreeChart)request.getAttribute("chartPie");
	if(chartPie !=null)
	{
		//生成图片热点的map，能鼠标到结点有提示数据
	    //PrintWriter pw = new PrintWriter(out);
	    fileNamePie = ServletUtilities.saveChartAsPNG(chartPie, 654 , 353,info, request.getSession());//保存图表为文件
	    ChartUtilities.writeImageMap(pw, fileNamePie,info,  false);
	    pw.flush();
		graphURLPie = request.getContextPath()+"/servlet/DisplayChart?filename="+fileNamePie;
	}
 %>
<!-- /图表  -->

<c:if test="${dayLineShow == '1'}">
<table style="width:100%;">
	<tr>
		<td class="tu" style="text-align:left;">按天统计：</td>
	</tr>
	<tr>
		<td class="tu">
			 <img src="<%=graphURLLine %>" width="900" height="300" usemap="#<%=fileName %>" alt="" />
		</td>
	</tr>
	<tr>
		<td>
			<table style="width:100%;">
				<tr>
					<td class="tu" style="text-align:left;"><a style="color:#056dae;" href="javascript:download('${graphURLLinePath}','PNG')" >下载PNG图片</a></td>
				</tr>
				<tr>
					<td class="tu" style="text-align:left;"><a style="color:#056dae;" href="javascript:download('${graphURLLinePath}','JPEG')" >下载JPEG图片</a></td>
				</tr>
			</table>
		</td>
	</tr>
</table>
</c:if>

<c:if test="${monthLineShow == '1'}">
<table style="width:100%;">
	<tr>
		<td class="tu" style="text-align:left;">按月统计：</td>
	</tr>
	<tr>
		<td class="tu">
			<img src="<%=graphURLLineMonth %>" width="900" height="300" usemap="#<%=fileNameLineMonth %>" alt="" />
		</td>
	</tr>
	<tr>
		<td>			 
			<table style="width:100%;">
				<tr>
					<td class="tu" style="text-align:left;"><a style="color:#056dae;" href="javascript:download('${graphURLLinePathMonth }','PNG')" >下载PNG图片</a><br/></td>
				</tr>
				<tr>
					<td class="tu" style="text-align:left;"><a style="color:#056dae;" href="javascript:download('${graphURLLinePathMonth }','JPEG')" >下载JPEG图片</a></td>
				</tr>
			</table>
		</td>		
	</tr>
</table>
</c:if>
 <br/>
 
<table style="width:100%;">
	<tr>
		<td class="tu" >
			<img src="${graphURLPieShowPath }" width="654" height="353" usemap="#<%=fileNamePie %>" alt="" />
		</td>
	</tr>
	<tr>
		<td>
			<table style="width:100%;">
				<tr>
					<td class="tu" style="text-align:left;"><a style="color:#056dae;" href="javascript:download('${graphURLPiePath }','PNG')" >下载PNG图片</a></td>
				</tr>
				<tr>
					<td class="tu" style="text-align:left;"><a style="color:#056dae;" href="javascript:download('${graphURLPiePath }','JPEG')" >下载JPEG图片</a></td>
				</tr>
			</table>
		</td>
	</tr>
</table>
<br/>
<br/>
<br/>
</c:if>


<br/>
<br/>
</div>

<script type="text/javascript"> 
 	$("#usual1 ul").idTabs(); 
</script>
    
<script type="text/javascript">
	$('.tablelist tbody tr:odd').addClass('odd');
</script>
</body>
</html>
