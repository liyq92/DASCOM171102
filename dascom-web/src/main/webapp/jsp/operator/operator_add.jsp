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
var vbusinName;
var vwarName;
var vareaName;
var vbranName;

function reloadGroup(){
    var fm=document.forms[0];
	
	var grvalue = document.getElementById('group');
	var grtext = grvalue.options[grvalue.selectedIndex].text;//获取选中的text值
	if(grtext == '事业部组')
	{
		var vzone =document.getElementById("zonebox1");
		vzone.selectedIndex = 0;
		document.getElementById("zonebox1").disabled="disabled" ;
		var varene =document.getElementById("areabox1");
		varene.selectedIndex = 0;
		document.getElementById("areabox1").disabled="disabled" ;
		var vbagne =document.getElementById("bagbox1");
		vbagne.selectedIndex = 0;
		document.getElementById("bagbox1").disabled="disabled" ;
	}
	if(grtext == '战区组')
	{
		
		document.getElementById("zonebox1").disabled="" ;
		var varene =document.getElementById("areabox1");
		varene.selectedIndex = 0;
		document.getElementById("areabox1").disabled="disabled" ;
		var vbagne =document.getElementById("bagbox1");
		vbagne.selectedIndex = 0;
		document.getElementById("bagbox1").disabled="disabled" ;
	}
	if(grtext == '片区组')
	{
		
		document.getElementById("zonebox1").disabled="" ;
		document.getElementById("areabox1").disabled="" ;
		var vbagne =document.getElementById("bagbox1");
		vbagne.selectedIndex = 0;
		document.getElementById("bagbox1").disabled="disabled" ;
	}
	if(grtext == '分行行动组')
	{
		
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


function check(businName,warName ,areaName,branName){
	vbusinName = businName;
	vwarName = warName;
	vareaName = areaName;
	vbranName = branName;
	 var fm=document.forms[0];
	 var exp = new RegExp("^[A-Za-z0-9_]*$");
	 if(fm.openo=='') {
	     alert("操作员帐号不能为空！");  
	     return false;
     }
     
	 if (!exp.test(fm.openo.value)) {
	        alert("操作员帐号必须是普通英文字符、数字或下划线！");
			return false;
		}
	   
	  
	  if(fm.opepwd=='') {
	     alert("操作员密码不能为空！");  
	     return false;
     }
	   if(!exp.test(fm.opepwd.value) || (fm.opepwd.value.length < 4)||(fm.opepwd.value.length > 8)) {
	 	       alert("密码必须是4~8位普通英文字符、数字或下划线！");
				return false;
		}	
	   	if(fm.pswdConfirm=='') {
	     alert("操作员确认密码不能为空！");  
	     return false;
        }
	   
	   	if(fm.opepwd.value != fm.pswdConfirm.value){
	   	alert("密码不一致，请重新填写");
	   	return false;
	   }
	 
	 var grvalue = document.getElementById('group');
	var grtext = grvalue.options[grvalue.selectedIndex].text;//获取选中的text值
	if(grtext == '事业部组')
	{
		//什么也不判断，其他的都不可选
	}
	   if(grtext == '战区组')
	   {
	   	//战区必须选择
		   if(fm.zonebox1.options.selectedIndex==0){
		   	alert("请选择"+vwarName);
		   	return false;
		   }
	   }
	   
	   if(grtext == '片区组')
	   {
	   	//战区必须选择
		   if(fm.zonebox1.options.selectedIndex==0){
		   	alert("请选择"+vwarName);
		   	return false;
		   }
		   //片区必选
		   if(fm.areabox1.options.selectedIndex==0){
		   	alert("请选择"+vareaName);
		   	return false;
		   }
	   }
	   
	   if(grtext == '分行行动组')
	   {
	   		//战区必须选择
		   if(fm.zonebox1.options.selectedIndex==0){
		   	alert("请选择"+vwarName);
		   	return false;
		   }
		   //片区必选
		   if(fm.areabox1.options.selectedIndex==0){
		   	alert("请选择"+vareaName);
		   	return false;
		   }
		   //行动组必选
		    if(fm.bagbox1.options.selectedIndex==0){
		   	alert("请选择"+vbranName);
		   	return false;
		   }
	   }
	   
	 if(fm.agentinfo.value=='') {
	     alert("操作员真实姓名不能为空！");  
	     return false;
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
		<li><a href="#">增加操作员</a></li>
	</ul>
</div>

<div class="formbody" style="width:1100px;">
<form name="submitform" method="post" action="operatorAdd.do"  onsubmit="return check('${businName }','${warName }','${areaName }','${branName }');">
	<table width="100%" align="center" style="border-style:none;margin-left:50px;margin-top:30px;">
        <tr> 
            <td width="90px;"><label>帐号：</label></td>
          	<td> 
          		<input class="dfinput" type="text" name="openo" maxlength="20" value="" /><font color="red">*</font>
          	</td>
        </tr>
        <tr> 
            <td><label>密码：</label></td>
          	<td>
          		<input class="dfinput" type="password" name="opepwd" maxlength="20" /><font color="red">*</font>
          	</td>
        </tr>
        <tr> 
          <td><label>确认密码：</label></td>
          <td>
          	<input class="dfinput" type="password" name="pswdConfirm" maxlength="20" /><font color="red">*</font>
          </td>
        </tr> 
        <tr>
          <td><label>权限组别：</label></td>
          <td>
	          	<select class="select3" style="width:150px ; height:22px;" id="group" name="groupid" onChange="reloadGroup()">             		
		    		<c:forEach items="${grouplist}" var="group">
						<option value="${group.id}" <c:if test="${group.groupName == '分行行动组'  }" >selected='true'</c:if> >${group.groupName}</option>
		    		</c:forEach>
	     	  	</select>
     	  	</td>
        </tr> 
         <tr>
          <td><label>${businName }：</label></td>
          <td>
	          <select class="select3" style="width:150px ; height:22px;" id='bdbox1' name='bdbox1' onChange="changeHeadSelect(1)">             		
	    		<option value=''>全部</option>
	    		<c:forEach items="${bdlist}" var="bd" varStatus="s">
					<option value='${bd }' <c:if test="${bdparam==bd}">selected='true'</c:if>>${bd }</option>
	    		</c:forEach>
	     	  </select>
     	  </td>
        </tr>
        <tr>
          <td><label>${warName }：</label></td>
          <td>
	          <select class="select3" style="width:150px ; height:22px;" id='zonebox1' name='zonebox1' onChange="changeHeadSelect(2)">             		
	    		<option value=''>全部</option>
				<c:forEach items="${wzlist}" var="wz" varStatus="s">
					<option value='${wz }' <c:if test="${zoneparam==wz}">selected='true'</c:if>>${wz }</option>
				</c:forEach>
	     	  </select>
	     </td>
        </tr>
         <tr>
          <td><label>${areaName }：</label></td>
          <td>
	          <select class="select3" style="width:150px ; height:22px;" id='areabox1' name='areabox1' onChange="changeHeadSelect(3)">             		
	    		<option value=''>全部</option>
				<c:forEach items="${arealist}" var="ar" varStatus="s">
					<option value='${ar }' <c:if test="${areaparam==ar}">selected='true'</c:if>>${ar }</option>
				</c:forEach>
	     	  </select>
     	  </td>
        </tr>
         <tr>
          <td><label>${branName }：</label></td>
          <td>
	          <select class="select3" style="width:150px ; height:22px;" id='bagbox1' name='bagbox1' >             		
	    		<option value=''>全部</option>
				<c:forEach items="${baglist}" var="bag" varStatus="s">
					<option value='${bag }' <c:if test="${bagparam==bag}">selected='true'</c:if>>${bag }</option>
				</c:forEach>
	     	  </select>
		 </td>
        </tr>
       <tr>
          <td><label>真实姓名：</label></td>
          <td>
          	<input class="dfinput" type="text" name="agentinfo1" maxlength="32" value=""><font color="red">*</font>
          </td>
        </tr>   
	     <tr>
	      	<td height="40" colspan="2" >
	      		<input style="margin-left:8%;" class="scbtn" type="submit" value="保存"/>&nbsp;&nbsp;&nbsp;
	       		<input class="scbtn" type="button" value="返回" onClick="javascript:document.location='operatorSearch.do'"/>
	      	</td>
	     </tr>
      </table>
</form>
</div>
</body>
</html>
