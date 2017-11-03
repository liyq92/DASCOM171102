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
function check(){
	 var fm=document.forms[0];
	 var exp = new RegExp("^[A-Za-z0-9_]*$");
//     if(!checkValue(fm.opename,'操作员别名','EMPTY')) return false;

		if(fm.opepwd.value!=''){
		       if (!exp.test(fm.opepwd.value) || fm.opepwd.value.length < 4||fm.opepwd.value.length > 8) 
		       {
	 	        alert("密码必须是4~8位普通英文字符、数字或下划线！");
				return false;
		       }		
			   if(fm.pswdConfirm.value=='')
			   {
	 	        alert("必须确认密码！");
				return false;			   
			   }
			   if(fm.pswdConfirm.value!=fm.opepwd.value)
			   {
	 	        alert("两次输入的密码不一致！");
				return false;			   
			   }
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
		   if(fm.zonebox2.options.selectedIndex==0){
		   	alert("请选择所属区域!");
		   	return false;
		   }
	   }
	   
	   if(grtext == '片区组')
	   {
	   	//战区必须选择
		   if(fm.zonebox2.options.selectedIndex==0){
		   	alert("请选择所属区域!");
		   	return false;
		   }
		   //片区必选
		   if(fm.areabox2.options.selectedIndex==0){
		   	alert("请选择所属片区!");
		   	return false;
		   }
	   }
	   
	   if(grtext == '分行行动组')
	   {
	   		//战区必须选择
		   if(fm.zonebox2.options.selectedIndex==0){
		   	alert("请选择所属区域!");
		   	return false;
		   }
		   //片区必选
		   if(fm.areabox2.options.selectedIndex==0){
		   	alert("请选择所属片区!");
		   	return false;
		   }
		   //行动组必选
		    if(fm.bagbox2.options.selectedIndex==0){
		   	alert("请选择所属分行行动组!");
		   	return false;
		   }
	   }

		if(fm.agentinfomod.value=='') {
		     alert("操作员真实姓名不能为空！");  
		     return false;
    	 }
    	 
    	 //判断是否做了修改
    	 if(fm.openomod.value == fm.openomodOld.value && fm.opepwd.value==fm.opepwdOld.value 
    	    && fm.pswdConfirm.value == fm.pswdConfirmOld.value && fm.groupid.value == fm.groupidOld.value 
    	    && 	fm.bdbox2.value == fm.bdbox2Old.value && fm.zonebox2.value == fm.zonebox2Old.value 
    	    && fm.areabox2.value == fm.areabox2Old.value && fm.bagbox2.value == fm.bagbox2Old.value 
    	    && fm.agentinfomod.value == fm.agentinfomodOld.value )
    	    {
    	    	alert('没有修改!');
    	    	return false;
    	    }  
	    return true;
}

function changSelSta(itemValue){
   var i;
   var opObj = document.forms[0].selectId;
 //  var desObj = document.forms[0].selurlDes;
   for (i=0;i<opObj.length;i++) {
      if (opObj[i].value == itemValue) {
        opObj[i].status = true;
        break;
      }
   }
}

//点击选择查询条件各等级下拉菜单  
function changeHeadSelect(type){

    if(type == '1'){
        var opObj = document.getElementById('bdbox2'); 
	       var select_root=document.getElementById('zonebox2');    
   	       select_root.options.length=0;
   	       var defa = new Option("全部","");
     	   select_root.add(defa);
     	   
		   var select_root1=document.getElementById('areabox2');    
   	       select_root1.options.length=0;
   	       var defa1 = new Option("全部","");
     	   select_root1.add(defa1);
     	
     	   var select_root2=document.getElementById('bagbox2');    
   	       select_root2.options.length=0;
   	       var defa2 = new Option("全部","");
     	   select_root2.add(defa2);
     	   
     	   if(opObj.selectedIndex!=0){
		      sendHeadRequest(type);
	        }
    }else if(type == '2'){
          var opObj = document.getElementById('zonebox2'); 
		   var select_root1=document.getElementById('areabox2');    
   	       select_root1.options.length=0;
   	       var defa1 = new Option("全部","");
     	   select_root1.add(defa1);
     	
     	   var select_root2=document.getElementById('bagbox2');    
   	       select_root2.options.length=0;
   	       var defa2 = new Option("全部","");
     	   select_root2.add(defa2);
     	   if(opObj.selectedIndex!=0){
     	      sendHeadRequest(type);
     	   }
	    
    }else{
        var opObj = document.getElementById('areabox2'); 
     	var select_root2=document.getElementById('bagbox2');    
   	    select_root2.options.length=0;
   	    var defa2 = new Option("全部","");
     	select_root2.add(defa2);
	    if(opObj.selectedIndex!=0){
		   sendHeadRequest(type);
	    }
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

function sendHeadRequest(type){
 	createXMLHttpRequest();
 	var opObj = document.getElementById('bdbox2'); 
 	var opObj1 = document.getElementById('zonebox2'); 
    var opObj2 = document.getElementById('areabox2'); 
 	XMLHttpReq.open("POST","${ctx}/selectlevel",true);
	XMLHttpReq.onreadystatechange = processHeadResponse;
	XMLHttpReq.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
	XMLHttpReq.send("type="+type+"&bd="+opObj.value+"&warzone="+opObj1.value+"&area="+opObj2.value);
}

function processHeadResponse(){
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
  	var select_root=document.getElementById('bdbox2');   
  	var defa;
  	if(type == '1'){
  	     select_root=document.getElementById('zonebox2');   
  	     select_root.options.length=0;
       	 defa = new Option("全部",""); 
  	}else if(type == '2'){
  	     select_root=document.getElementById('areabox2');  
  	     select_root.options.length=0;
       	 defa = new Option("全部","");   
  	}else{
  		select_root=document.getElementById('bagbox2');  
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

function reloadGroup(){
    var fm=document.forms[0];
	
	var grvalue = document.getElementById('group');
	var grtext = grvalue.options[grvalue.selectedIndex].text;//获取选中的text值
	if(grtext == '事业部组')
	{
		var vzone =document.getElementById("zonebox2");
		vzone.selectedIndex = 0;
		document.getElementById("zonebox2").disabled="disabled" ;
		var varene =document.getElementById("areabox2");
		varene.selectedIndex = 0;
		document.getElementById("areabox2").disabled="disabled" ;
		var vbagne =document.getElementById("bagbox2");
		vbagne.selectedIndex = 0;
		document.getElementById("bagbox2").disabled="disabled" ;
	}
	if(grtext == '战区组')
	{
		
		document.getElementById("zonebox2").disabled="" ;
		var varene =document.getElementById("areabox2");
		varene.selectedIndex = 0;
		document.getElementById("areabox2").disabled="disabled" ;
		var vbagne =document.getElementById("bagbox2");
		vbagne.selectedIndex = 0;
		document.getElementById("bagbox2").disabled="disabled" ;
	}
	if(grtext == '片区组')
	{
		
		document.getElementById("zonebox2").disabled="" ;
		document.getElementById("areabox2").disabled="" ;
		var vbagne =document.getElementById("bagbox2");
		vbagne.selectedIndex = 0;
		document.getElementById("bagbox2").disabled="disabled" ;
	}
	if(grtext == '分行行动组')
	{
		document.getElementById("zonebox2").disabled="" ;
		document.getElementById("areabox2").disabled="" ;
		document.getElementById("bagbox2").disabled="" ;
	}
 }
 
 function gobackbut(seopeno1,selectbdbox1 ,selectzonebox1 ,selectareabox1 ,selectbagbox1,pagetemp)
{		
	var vurl = "operatorSearch.do?1=1";	
	
	if(seopeno1 != '')
		vurl +="&openo="+seopeno1;
	
	if(selectbdbox1 !='')
		vurl +="&bdbox1="+selectbdbox1;
	if(selectzonebox1 != '')
		vurl +="&zonebox1="+selectzonebox1;
	if(selectareabox1 != '')
		vurl +="&areabox1="+selectareabox1;
	if(selectbagbox1 != '')
		vurl +="&bagbox1="+selectbagbox1;
	if(pagetemp != '')
		vurl +="&page="+pagetemp;

	document.location.href=vurl;
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
<body  topmargin="0"  onLoad="reloadGroup();">
<div class="place">
	<span>位置：</span>
	<ul class="placeul">
		<li><a href="#">首页</a></li>
		<li><a href="#">操作员修改信息</a></li>
	</ul>
</div>

<div class="formbody" style="width:1100px;">
<c:if test = "${not empty(msg)}">
	 <font color="red">${msg}</font>
</c:if>

<div align="center"><font color="red" align="center">如果不输入密码，此用户的密码不变；如果想修改此用户的密码，请直接输入新的密码。</font></div>
<form name="submitform" method="post" action="operatorModify.do"  onsubmit="return check(); ">
  <input type="hidden" id="seopeno1" name ="seopeno1" value='${seopeno1 }' />
  <input type="hidden" id="selectbdbox1" name ="selectbdbox1" value='${selectbdbox1 }' />
  <input type="hidden" id="selectzonebox1" name ="selectzonebox1" value='${selectzonebox1 }' />
  <input type="hidden" id="selectareabox1" name ="selectareabox1" value='${selectareabox1 }' />
  <input type="hidden" id="selectbagbox1" name ="selectbagbox1" value='${selectbagbox1 }' />
  <input type="hidden" id="pagetemp" name ="pagetemp" value='${pagetemp }' />
  <input type="hidden" name="groupidOld" value='${groupId }'/>
  <input type="hidden" name="bdbox2Old" value='${bdparam }'/>
  <input type="hidden" name="zonebox2Old" value='${zoneparam }'/>
  <input type="hidden" name="areabox2Old" value='${areaparam }'/>
  <input type="hidden" name="bagbox2Old" value='${bagparam }'/>
  <input type="hidden" name="agentinfomodOld" value='${opermodify.agentInfo }' />
  <table width="100%" align="center" style="border-style:none;margin-left:50px;margin-top:30px;">
	<tr> 
		<td width="90px;"><label>帐号：</label></td>
        <td>
			<input class="dfinput"  type="text" name="openomod" value = "${opermodify.id.openo}" />
			<input style="width:146px" type="hidden" name="openomodOld" value = "${opermodify.id.openo}" />
		</td>
	</tr> 
    <!--合并两种操作-->
    <tr> 
       <td><label>新密码：</label></td>
      <td>
     	 <input class="dfinput"  type="password" name="opepwd" maxlength="20"/>
      	 <input type="hidden" name="opepwdOld" />
      </td>
    </tr>
    <tr> 
      <td><label>确认密码：</label></td>
      <td>
      	<input class="dfinput"  type="password" name="pswdConfirm" maxlength="20" />
      	<input type="hidden" name="pswdConfirmOld" />
      </td>
    </tr>
    <tr>
       <td><label>权限组别：</label></td>
       <td>
	       <select class="select3"  height:22px;" id="group" name="groupid" onChange="reloadGroup()">             		
	 		<c:forEach items="${grouplist}" var="group">
				<option value="${group.id}" <c:if test="${groupId==group.id}" >selected='true'</c:if>>${group.groupName}</option>
	 		</c:forEach>
	  	  </select>
	  	</td>
     </tr>
      <tr>
        <td><label>${businName }：</label></td>
        <td>
	        <select class="select3"  height:22px;" id='bdbox2' name='bdbox2' onChange="changeHeadSelect(1)">             		
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
	        <select class="select3"  height:22px;" id='zonebox2' name='zonebox2' onChange="changeHeadSelect(2)">             		
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
	       <select class="select3" height:22px;" id='areabox2' name='areabox2' onChange="changeHeadSelect(3)">             		
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
			<select class="select3"  height:22px;" id='bagbox2' name='bagbox2' >             		
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
			<input class="dfinput" type="text" id="agentinfomod" name="agentinfomod" maxlength="32" value="${opermodify.agentInfo}"><font color="red">*</font>
		</td>
	</tr>
    <tr>
     	<td height="40" colspan="2" >
     		<input style="margin-left:8%;" class="scbtn" type="submit" value="保存"/>&nbsp;&nbsp;&nbsp;
      		<input class="scbtn" type="button" value="返回" onClick="gobackbut('${seopeno1}','${selectbdbox1 }','${selectzonebox1 }','${selectareabox1 }','${selectbagbox1 }','${pagetemp}');"/>
	</td>
     </tr>
      </table>

</form>
</div>
</body>
</html>
