<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html; charset=utf-8" %>
<%@ include file="/jsp/include/taglib.jsp" %>
<%@ include file="/jsp/include/script.jsp" %>
<%@ include file="/css/libcss.css" %>
<html>
<head>

<script type="text/javascript">
$(document).ready(function(e) {
    $(".select1").uedSelect({
		width : 345			  
	});
	$(".select2").uedSelect({
		width : 167  
	});
	$(".select3").uedSelect({
		width : 248
	});
});
</script>
<script language="JavaScript" type="text/JavaScript">
var XMLHttpReq = false;

function reloadGroup(){
    var fm=document.forms[0];
	
	var grvalue = document.getElementById('group');
	var grtext = grvalue.options[grvalue.selectedIndex].value;//获取选中的text值
	if(grtext == '事业部')
	{
		document.getElementById("bdboxshow").style.display='none';
		document.getElementById("bdboxTxtShow").style.display='inline';
		document.getElementById("zoneboxshow").style.display='inline';
		var vzone =document.getElementById("zonebox1");
		vzone.selectedIndex = 0;
		document.getElementById("zonebox1").disabled="disabled" ;
		document.getElementById("zoneboxTxtShow").style.display='none';
		document.getElementById("areaboxshow").style.display='inline';
		var varene =document.getElementById("areabox1");
		varene.selectedIndex = 0;
		document.getElementById("areabox1").disabled="disabled" ;
		document.getElementById("areaboxTxtShow").style.display='none';
		document.getElementById("bagboxshow").style.display='inline';
		var vbagne =document.getElementById("bagbox1");
		vbagne.selectedIndex = 0;
		document.getElementById("bagbox1").disabled="disabled" ;
		document.getElementById("bagboxTxtShow").style.display='none';
		
	}
	if(grtext == '战区')
	{
		document.getElementById("bdboxshow").style.display='inline';
		document.getElementById("bdboxTxtShow").style.display='none';
		document.getElementById("zoneboxshow").style.display='none';
		document.getElementById("zoneboxTxtShow").style.display='inline';
		document.getElementById("zonebox1").disabled="" ;
		document.getElementById("areaboxshow").style.display='inline';
		var varene =document.getElementById("areabox1");
		varene.selectedIndex = 0;
		document.getElementById("areabox1").disabled="disabled" ;
		document.getElementById("areaboxTxtShow").style.display='none';
		document.getElementById("bagboxshow").style.display='inline';
		var vbagne =document.getElementById("bagbox1");
		vbagne.selectedIndex = 0;
		document.getElementById("bagbox1").disabled="disabled" ;
		document.getElementById("bagboxTxtShow").style.display='none';
	}
	if(grtext == '片区')
	{
		document.getElementById("bdboxshow").style.display='inline';
		document.getElementById("bdboxTxtShow").style.display='none';
		document.getElementById("zoneboxshow").style.display='inline';
		document.getElementById("zoneboxTxtShow").style.display='none';
		document.getElementById("areaboxshow").style.display='none';
		document.getElementById("areaboxTxtShow").style.display='inline';
		document.getElementById("zonebox1").disabled="" ;
		document.getElementById("areabox1").disabled="" ;
		document.getElementById("bagboxshow").style.display='inline';
		var vbagne =document.getElementById("bagbox1");
		vbagne.selectedIndex = 0;
		document.getElementById("bagbox1").disabled="disabled" ;
		document.getElementById("bagboxTxtShow").style.display='none';
	}
	if(grtext == '分行组别')
	{
		document.getElementById("bdboxshow").style.display='inline';
		document.getElementById("bdboxTxtShow").style.display='none';
		document.getElementById("zoneboxshow").style.display='inline';
		document.getElementById("zoneboxTxtShow").style.display='none';
		document.getElementById("areaboxshow").style.display='inline';
		document.getElementById("areaboxTxtShow").style.display='none';
		document.getElementById("bagboxshow").style.display='none';
		document.getElementById("bagboxTxtShow").style.display='inline';
		document.getElementById("zonebox1").disabled="" ;
		document.getElementById("areabox1").disabled="" ;
		document.getElementById("bagbox1").disabled="" ;
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

function sendRequest(){
 	createXMLHttpRequest();
	XMLHttpReq.open("POST","${ctx}/loadGroup",true);
	XMLHttpReq.onreadystatechange = processResponse;
	XMLHttpReq.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
	
}

function processResponse(){
 	if(XMLHttpReq.readyState == 4){
		if(XMLHttpReq.status == 200){
			clearPreviousResults();
			parseResults();
		}
		else{
			alert(XMLHttpReq.status);
		}
	}
 }
 

function clearPreviousResults(){
    var tableBody = document.getElementById("groupName");
    while(tableBody.childNodes.length > 0){
        tableBody.removeChild(tableBody.childNodes[0]);
    }
}
 
function parseResults(){
	var tableBody = document.getElementById("groupName");
	var results = XMLHttpReq.responseXML;
    var group = null;
    var groupid = "";
    var groupname = "";

    var groups = results.getElementsByTagName("Group");

	var id = document.createElement("select");
	id.setAttribute("name","groupId");
    for(var i = 0; i < groups.length; i++) {
        group = groups[i];
        groupid = group.getElementsByTagName("groupid")[0].firstChild.nodeValue;
        groupname = group.getElementsByTagName("groupname")[0].firstChild.nodeValue;
        	var option = document.createElement("option");
			option.setAttribute("value",groupid);
			var desc = document.createTextNode(groupname);
			option.appendChild(desc);
			id.appendChild(option);
	}
	tableBody.appendChild(id);
}

function MM_preloadImages(imginfo,msginfo) { //v3.0
  var d=document; if(d.images){ if(!d.MM_p) d.MM_p=new Array();
    var i,j=d.MM_p.length,a=MM_preloadImages.arguments; for(i=0; i<a.length; i++)
    if (a[i].indexOf("#")!=0){ d.MM_p[j]=new Image; d.MM_p[j++].src=a[i];}}
   
    if(msginfo != '')
    {
    	alert(msginfo);
    }
}

function MM_swapImgRestore() { //v3.0
  var i,x,a=document.MM_sr; for(i=0;a&&i<a.length&&(x=a[i])&&x.oSrc;i++) x.src=x.oSrc;
}

function MM_findObj(n, d) { //v4.01
  var p,i,x;  if(!d) d=document; if((p=n.indexOf("?"))>0&&parent.frames.length) {
    d=parent.frames[n.substring(p+1)].document; n=n.substring(0,p);}
  if(!(x=d[n])&&d.all) x=d.all[n]; for (i=0;!x&&i<d.forms.length;i++) x=d.forms[i][n];
  for(i=0;!x&&d.layers&&i<d.layers.length;i++) x=MM_findObj(n,d.layers[i].document);
  if(!x && d.getElementById) x=d.getElementById(n); return x;
}

function MM_swapImage() { //v3.0
  var i,j=0,x,a=MM_swapImage.arguments; document.MM_sr=new Array; for(i=0;i<(a.length-2);i+=3)
   if ((x=MM_findObj(a[i]))!=null){document.MM_sr[j++]=x; if(!x.oSrc) x.oSrc=x.src; x.src=a[i+2];}
}


function check(){
 	var fm=document.forms[0];
	var grvalue = document.getElementById('group');
	var grtext = grvalue.options[grvalue.selectedIndex].text;//获取选中的text值
	if(grtext == '事业部')
	{
		var bdtxt = document.getElementById('bdbox1Txt').value;
		if(bdtxt == '')
		{
			alert('请填写事业部名称!');
			return false;
		}
	}
	   if(grtext == '战区')
	   {
	   	//事业部必须选择
		   if(fm.bdbox1.options.selectedIndex==0){
		   	alert("请选择所属事业部!");
		   	return false;
		   }
		   var zonetxt = document.getElementById('zonebox1Txt').value;
		   if(zonetxt == '')
		   {
		   		alert('请填写战区名称!');
		   		return false;
		   }
	   }
	   
	   if(grtext == '片区')
	   {
	   //事业部必须选
	    if(fm.bdbox1.options.selectedIndex==0){
		   	alert("请选择所属事业部!");
		   	return false;
		   }
	   	//战区必须选择
		   if(fm.zonebox1.options.selectedIndex==0){
		   	alert("请选择所属战区!");
		   	return false;
		   }
		  var areatxt = document.getElementById('areabox1Txt').value;
		  if(areatxt == '')
		   {
		   		alert('请填写片区名称!');
		   		return false;
		   }
	   }
	   
	   if(grtext == '分行组别')
	   {
	   	//事业部必须选
	    if(fm.bdbox1.options.selectedIndex==0){
		   	alert("请选择所属事业部!");
		   	return false;
		   }
	   		//战区必须选择
		   if(fm.zonebox1.options.selectedIndex==0){
		   	alert("请选择所属战区!");
		   	return false;
		   }
		   //片区必选
		   if(fm.areabox1.options.selectedIndex==0){
		   	alert("请选择所属片区!");
		   	return false;
		   }
		   var bagtxt = document.getElementById('bagbox1Txt').value;
		   if(bagtxt == '')
		   {
		   		alert('请填写行动组名称!');
		   		return false;
		   }
	   }
   
	return true;
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

//-->
</script>
<style>
label
{
	width:80px;
	text-align:right;
	display:inline-block; 
}
.dfinput
{
	width:248px;
	height:28px;
}
tr
{
	height:40px;
}
</style>
</head>

<body  topmargin="0" >
<div class="place">
	<span>位置：</span>
	<ul class="placeul">
		<li><a href="#">首页</a></li>
		<li><a href="#">新增架构</a></li>
	</ul>
</div>

<div class="formbody" style="width:1100px;">
<form name="submitform" method="post" action="levelmanage.do?type=add"  onsubmit="return check();">
	<table width="100%" align="center" style="border-style:none;margin-left:30px;margin-top:30px;">
       <tr>
          <td width="90px;"><label>架构组别：</label></td>
          <td>
	          <select class="select3"  height:22px;" id="group" name="groupid" onChange="reloadGroup()">             		
	    		 <option value="事业部">${businName }</option>
				 <option value="战区">${warName }</option>
				 <option value="片区">${areaName }</option>
				 <option value="分行组别" selected="true">${branName }</option>
	     	  </select>
     	  </td>
        </tr>
         <tr>
          <td><label>${businName }：</label></td>
          <td>
          <div id="bdboxshow" style="display:inline;">
          	<select class="select3" height:22px;" id='bdbox1' name='bdbox1' onChange="changeHeadSelect(1)">             		
    			<option value=''>全部</option>
    			<c:forEach items="${bdlist}" var="bd" varStatus="s">
					<option value='${bd }' <c:if test="${bdparam==bd}">selected='true'</c:if>>${bd }</option>
    			</c:forEach>
     	 	 </select>
     	  </div>
     	  <div id="bdboxTxtShow" style="display:none;" >
     	   	<input  class="dfinput"  type="text" id="bdbox1Txt" name="bdbox1Txt" value=""  />
     	   </div>
     	  </td>
        </tr>
        
        <tr>
          <td><label>${warName }：</label></td>
          <td>
	          <div id="zoneboxshow" style="display:inline;">
	          	<select class="select3"  height:22px;" id='zonebox1' name='zonebox1' onChange="changeHeadSelect(2)">             		
	    		<option value=''>全部</option>
				<c:forEach items="${wzlist}" var="wz" varStatus="s">
					<option value='${wz }' <c:if test="${zoneparam==wz}">selected='true'</c:if>>${wz }</option>
				</c:forEach>
	     	  </select>
	     	  </div>
	     	  <div id="zoneboxTxtShow" style="display:none;">
	     	  	<input class="dfinput"  type="text" id="zonebox1Txt" name="zonebox1Txt" value=""  />
	     	  </div>
     	  </td>
        </tr>
        <tr>
          <td><label>${areaName }：</label></td>
          <td>
	          <div id="areaboxshow" style="display:inline;">
	          	<select class="select3" style="width:150px ; height:22px;" id='areabox1' name='areabox1' onChange="changeHeadSelect(3)">             		
		    		<option value=''>全部</option>
					<c:forEach items="${arealist}" var="ar" varStatus="s">
						<option value='${ar }' <c:if test="${areaparam==ar}">selected='true'</c:if>>${ar }</option>
					</c:forEach>
	     	  	</select>
	     	  </div>
	     	  <div id="areaboxTxtShow" style="display:none;">
	     	  	<input class="dfinput"  type="text" id="areabox1Txt" name="areabox1Txt" value=""  />
	     	  </div>
     	 </td>
       </tr>
       <tr>
          <td><label>${branName }：</label></td>
          <td>
	          <div id="bagboxshow" style="display:none;">
	          	<select class="select3" style="width:150px ; height:22px;" id='bagbox1' name='bagbox1' >             		
	    		<option value=''>全部</option>
				<c:forEach items="${baglist}" var="bag" varStatus="s">
					<option value='${bag }' <c:if test="${bagparam==bag}">selected='true'</c:if>>${bag }</option>
				</c:forEach>
	     	  </select>
	     	  </div>
	     	  <div id="bagboxTxtShow" style="display:inline;">
	     	  	<input class="dfinput"  type="text" id="bagbox1Txt"  name="bagbox1Txt" value=""  />
	     	  </div>
     	  </td>
        </tr>
      	<tr>
      		<td colspan="2">
	      		<input style="margin-left:8%;" class="scbtn" style="width:80px;" type="submit" value="保存"/>&nbsp;&nbsp;&nbsp;
	       		<input class="scbtn" style="width:80px;" type="button" value="返回" onClick="javascript:document.location='levelmanage.do'"/>
      		</td>
      	</tr>
      </table>
</form>
</div>
</body>
</html>
