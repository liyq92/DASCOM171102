<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="/jsp/include/taglib.jsp" %>
<%@ include file="/jsp/include/script.jsp" %>
<%@ include file="/css/libcss.css" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %> 
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

<script language="JavaScript" type="text/JavaScript">
<!--
var wBox;
//var jq=jQuery.noConflict();

<!--下面三个值为查询条件，由于通过js传递有问题，所以定义成全局的了 -->
var selectMsisdn;
var selectname;
var selectssmnnumber;
var varchannelname;
var oldmsisdn;
var switchuid;
var switchssmnnumber;
var selectsempno;
var selectChannels;//根据副号码类型取相同类型的渠道
var ty;//1:换主号码　2:更换渠道
var visHaveSsmnNum;//判断 ，是否只有A+渠道不给分配副号码
var vareSsmnnumber;//把副号码也暂存一下，因为它可能为空

function cancel(){
	wBox.close();
}

function init(msg,stype,oldmsisdn,switchtomsisdn,switchtossmnnumber,switchtochannelname,switchusername,stmsisdn,suid,switchtochannelId,selectName,selectSsmnNumber,selectsempno){

	//如果type== 3,显示确定绑定消息
	if(stype == '3' && msg == '')
	{
		selectMsisdn=stmsisdn;
		selectname = selectName;
		selectssmnnumber = selectSsmnNumber;
		selectsempno = selectsempno

		wBox=jQuery("#wbox10").wBox({
		title: "",
		html: 	"<div style='width:300px;height:170px;'>"+
				"<div style='text-align=left;margin-left:30px;font-family:Arial;font-size:14px;font-weight:bold;font-style:normal;text-decoration:none;color:#333333;'>经纪人:"+switchusername+"</div>"+
				"<table style='margin-top:5px;margin-left:30px;width:90%;'>"+
					"<tr><td style='height:25px;font-size:14px;'>主号码：</td><td style='font-size:14px;'>副号码：</td><td style='font-size:14px;'>渠道：</td></tr>"+
					"<tr><td style='height:25px;font-size:14px;'>"+switchtomsisdn+"</td><td style='font-size:14px;'>"+switchtossmnnumber+"</td><td style='font-size:14px;'>"+switchtochannelname+"</td>"+
					"</tr>"+
				"</table>"+
				"<div style='margin-bottom:10px;margin-top:10px;'>"+
				"<table width='300' border='0' cellpadding='0' cellspacing='0' bgcolor='#000000'>"+
					"<tr><td height='1'></td></tr>"+
				"</table></div> "+
				"<div style='text-align:center;font-family:Arial;font-size:14px;color:#333333;'>是否确认进行换绑操作?<div> "+
				"<input class='cancel' style='margin-top:10px;margin-left:20px;width:30%;' type='button' value='取消' onclick='cancel();' />&nbsp;&nbsp;&nbsp;&nbsp;"+
				"<input class='sure' style='margin-top:5px;margin-left:10px;width:30%;' type='button' value='确定' onclick='switchBind("+oldmsisdn+","+suid+","+switchtossmnnumber+","+switchtomsisdn+","+switchtochannelId+");' /> </div>"
		});
		wBox.showBox();
		
	}else	
	{
		if(msg != ''){
			wBox=jQuery("#wbox10").wBox({
			title: "返回结果",
			html: "<div style='width:280px;height:70px;'><table align='center' style='margin-top:40px;width:100%;'><tr><td align='center' style='font-size:15px;'>"+msg+"</td></tr></table></div>"
			});
			wBox.showBox();
		}
	}
}

 function IsNum(e) {
            var k = window.event ? e.keyCode : e.which;
            if (((k >= 48) && (k <= 57)) || k == 8 || k == 0) {
            } else {
                if (window.event) {
                    window.event.returnValue = false;
                }
                else {
                    e.preventDefault(); //for firefox 
                }
            }
        } 

function showBindBox(sname,smsisdn,strEnableNumberList,strChannel,uid,stmsisdn,stname,stssmnnumber)
{
	var strs= new Array(); //定义一数组 
	var channels = new Array();
	if(strEnableNumberList.length >0)
	{
		strs = strEnableNumberList.split(",");
	}
	var opstr='';
	for(var i =0; i<strs.length; i++)
	{
		opstr +="<option value='";
		opstr +=strs[i];
		opstr +="'>";
		opstr +=strs[i];
		opstr +="</option>";
	}
	if(strChannel.length >0)
	{
		channels = strChannel.split(",");
	}
	
	var opchannelstr = '';
	for(var j=0; j<channels.length; j++)
	{
		var str = channels[j];
		var strsplit = str.split("|");
		opchannelstr += "<option value='";
		opchannelstr +=strsplit[0];
		opchannelstr +="'>";
		opchannelstr +=strsplit[1];
		opchannelstr +="</option>";
	}
	
	selectMsisdn = stmsisdn;
	selectname = stname;
	selectssmnnumber = stssmnnumber;
		
	if(sname != '' && smsisdn != '')
	{	
		<!--&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;绑定 -->
		wBox=jQuery("	#wbox10").wBox({
		title: "",
		html: "<div style='width:300px;height:150px;'><table style='margin-top:0px;margin-left:30px;width:80%;'><tr style='margin-top:0px;margin-bottom:10px;font-family:Arial;font-size:13px;font-weight:bold;font-style:normal;text-decoration:none;color:#333333;'><td>经纪人：</td><td>"+sname+
		"</td></tr><tr style='margin-top:10px;margin-bottom:10px;font-family:Arial;font-size:13px;font-weight:bold;font-style:normal;text-decoration:none;color:#333333;'><td>主号码：</td><td>"+smsisdn+"</td></tr><tr style='margin-top:20px;margin-bottom:50px;'><td>新增副号码：</td><td><select id='ssmnnumbervalue' style='width:150px ; height:20px;progid:DXImageTransform.Microsoft.Alpha(Opacity=1);filter:alpha(opacity=100);-moz-opacity:1;-khtml-opacity:1; opacity:1; border:1px;border-style:solid;'><option value=''>请选择副号码</option>"+opstr +"</select></td></tr><tr style='margin-top:50px;margin-bottom:20px;'><td>渠道：</td><td><select id='ssmnchannelvalue' style='width:150px ; height:20px;progid:DXImageTransform.Microsoft.Alpha(Opacity=1);filter:alpha(opacity=100);-moz-opacity:1;-khtml-opacity:1; opacity:1; border:1px;border-style:solid;'><option value=''>请选择渠道</option>"+
		opchannelstr+"</select></td></tr></table> <div style='margin-bottom:10px;margin-top:10px;'><table width='300' border='0' cellpadding='0' cellspacing='0' bgcolor='#000000'><tr><td height='1'></td></tr></table></div>"+
		" <input class='cancel'  style='margin-top:5px;margin-left:50px;width:30%;' type='button' value='取消' onclick='cancel();' /><input class='sure'  style='margin-top:5px;margin-left:20px;width:30%;' type='button' value='确定' onclick='bindButoon("+smsisdn+","+uid+");' /> </div>"
		});
		wBox.showBox();
	}
}
//绑定
function bindButoon(smsisdn,suid)
{
	wBox.close();
	var sm = document.getElementById('ssmnnumbervalue');
	var index = sm.selectedIndex; // 选中索引
	var smvalue = sm.options[index].value; // 选中值
	if(smvalue == '')
	{
		alert("请选择副号码!");
		return;
	}
	
	var sc = document.getElementById('ssmnchannelvalue');
	var scindex = sc.selectedIndex;
	var scvalue = sc.options[scindex].value;
	if(scvalue == '')
	{
		alert("请选择渠道!");
		return;
	}
	
	document.location.href="queryssmn.do?type=1&&strsmnnumber="+smvalue+"&&channelvalue="+scvalue+"&&bindmsisdn="+smsisdn+"&&uid="+suid+"&&msisdn="+selectMsisdn+"&&name="+selectname+"&&ssmnnumber="+selectssmnnumber+"&vempno="+selectsempno;
}
//解绑
function showCancelBindBox(sname,smsisdn,uid,schannelname,sssmnuber,stmsisdn,stname,stssmnnumber,sempno,isHaveSsmnNum)
{
	visHaveSsmnNum =isHaveSsmnNum;
	selectMsisdn = stmsisdn;
	selectname = stname;
	selectssmnnumber = stssmnnumber;
	selectsempno = sempno;
	vareSsmnnumber =sssmnuber;
	
	if(sname != '' && smsisdn != '')
	{	
		<!--&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;解绑 -->
		wBox=jQuery("	#wbox10").wBox({
		title: "",
		html: 	"<div style='width:300px;height:170px;'>"+
				"<div style='text-align=left;margin-left:20px;font-family:Arial;font-size:14px;font-weight:bold;font-style:normal;text-decoration:none;color:#333333;'>经纪人:"+sname+"</div>"+
				"<table style='margin-top:5px;margin-left:20px;width:90%;'>"+
					"<tr>"+
						"<td style='height:25px;font-size:14px;'>主号码：</td><td style='font-size:14px;'>副号码：</td><td style='font-size:14px;'>渠道：</td>"+
					"</tr>"+
					"<tr>"+
						"<td style='height:25px;font-size:13px;'>"+smsisdn+"</td><td style='font-size:13px;'>"+sssmnuber+"</td><td style='font-size:13px;'>"+schannelname+"</td>"+
					"</tr>"+
				"</table>"+
				"<div style='margin-bottom:10px;margin-top:10px;'>"+
				"<table width='300' border='0' cellpadding='0' cellspacing='0' bgcolor='#000000'>"+
				"<tr><td height='1'></td></tr></table>"+
				"</div>"+
				" <div style='text-align:center;font-family:Arial;font-size:14px;color:#333333;'>是否确认进行解绑操作?<div> "+
				"<input class='cancel' style='margin-top:9px;margin-left:2px;width:30%;' class='cancel' type='button' value='取消' onclick='cancel();' />&nbsp;&nbsp;&nbsp;&nbsp;"+
				"<input class='sure' style='margin-top:5px;margin-left:10px;width:30%;' class='sure' type='button' value='确定' onclick='cancelBind("+smsisdn+","+uid+");' />"+
				" </div>"
		});
		wBox.showBox();
	}
}


function cancelBind(smsisdn,uid)
{
	if(visHaveSsmnNum == '1' && vareSsmnnumber =='')
		vareSsmnnumber ='00000000000';	
	document.location.href="queryssmn.do?type=2&&smsisdn="+smsisdn+"&&uid="+uid+"&&sssmnuber="+vareSsmnnumber+"&&msisdn="+selectMsisdn+"&&name="+selectname+"&&ssmnnumber="+selectssmnnumber+"&vempno="+selectsempno;
}
//显示换绑窗口
function showSwitchBindBox(sname,smsisdn,uid,schannelname,sssmnuber,strChannel,stmsisdn,stname,stssmnnumber,sempno)
{
	ty =1;
	sendRequestChannel(sssmnuber);//根据副号码类型查相应渠道类型
		
	selectMsisdn = stmsisdn;
	selectname = stname;
	selectssmnnumber = stssmnnumber;
	selectsempno = sempno;

	if(sname != '' && smsisdn != '')
	{		
		<!--&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;换绑 -->
		wBox=jQuery("	#wbox10").wBox({
		title: "",
		html: 	"<div style='width:460px;height:160px;'>"+
				"<table style='margin-left:15px;width:96%;'>"+
					"<tr style='font-family:Arial;font-style:normal;text-decoration:none;color:#333333;'>"+
						"<td style='font-size:14px;height:25px;font-weight:bold;'>"+'原经纪人：'+"</td><td style='font-size:14px;'>"+sname+"</td>"+
						"<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>"+
						"<td style='font-size:14px;height:25px;font-weight:bold;'>换绑经纪人：</td>"+
						"<td></td>"+
					"</tr>"+
					"<tr style='font-family:Arial;font-style:normal;text-decoration:none;color:#333333;'>"+
						"<td style='font-size:14px;height:25px;font-weight:bold;'>主号码：</td>"+
						"<td style='font-size:14px;'>"+smsisdn+"</td>"+
						"<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>"+
						"<td style='font-size:14px;height:25px;font-weight:bold;'>主号码：</td>"+
						"<td>"+
							"<input style='font-size:14px;height:20px;width:152px;border:1px;border-style:solid;' id='bindmsisdn' name='bindmsisdn' type='text' size='20' maxlength='11' onkeypress='return IsNum(event)' />"+
						"</td>"+
					"</tr>"+
					"<tr style='font-family:Arial;font-style:normal;text-decoration:none;color:#333333;'>"+
						"<td style='font-size:14px;height:25px;font-weight:bold;'>副号码：</td>"+
						"<td style='font-size:14px;'>"+sssmnuber+"</td>"+
						"<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>"+
						"<td style='font-size:14px;height:25px;font-weight:bold;'>副号码：</td>"+
						"<td style='font-size:14px;'>"+sssmnuber+"</td>"+
					"</tr>"+
					"<tr style='font-family:Arial;font-style:normal;text-decoration:none;color:#333333;'>"+
						"<td style='font-size:14px;height:25px;font-weight:bold;'>渠道：</td>"+
						"<td style='font-size:14px;'>"+schannelname+"</td>"+
						"<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>"+
						"<td style='font-size:14px;height:25px;font-weight:bold;'>渠道：</td>"+
						"<td>"+
							"<select id='ssmnchannelvalue' style='font-size:14px;width:150px ; height:20px;progid:DXImageTransform.Microsoft.Alpha(Opacity=1);filter:alpha(opacity=100);-moz-opacity:1;-khtml-opacity:1; opacity:1; border:1px;border-style:solid;'>"+
							"</select>"+
						"</td>"+
					"</tr>"+
				"</table>"+
				"<div style='margin-bottom:5px;margin-top:10px;'><table width='450' border='0' cellpadding='0' cellspacing='0' bgcolor='#000000'><tr><td height='1'></td></tr></table></div>"+
				"<input  style='margin-top:5px;margin-left:78px;width:30%;' class='cancel' type='button' value='取消' onclick='cancel();' />&nbsp;&nbsp;&nbsp;&nbsp;"+
				"<input  style='margin-top:5px;margin-left:10px;width:30%;' class='sure' type='button' value='确定' onclick='checkswitchBind("+smsisdn+","+uid+","+sssmnuber+");' /> </div>"
		});
		wBox.showBox();
	}
}
//显示换绑确认窗口
function checkswitchBind(smsisdn,uid,sssmnuber)
{
	var sm = document.getElementById('bindmsisdn').value;
	if(sm == '')
	{
		alert('请输入主号码!');
		return;
	}
	var reg = new RegExp("^[0-9]*$");
	 if(!reg.test(sm))
	 {
	 	alert("主号码必须是数字!");
	 	return ;
	 }
	
	var sc = document.getElementById('ssmnchannelvalue');
	var scindex = sc.selectedIndex;
	var scvalue = sc.options[scindex].value;
	if(scvalue == '')
	{
		alert("请选择渠道!");
		return;
	}
	
	//判断两个号码是否在同一个区域
	oldmsisdn=smsisdn;
	switchuid = uid;
	switchssmnnumber=sssmnuber;
	sendRequest();
			
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
 
 //根据用户id获取该用户类型的渠道
function sendRequestChannel(sssmnuber)
{	createXMLHttpRequest();
	
	XMLHttpReq.open("POST","${ctx}/selectenablenumber",true);
	XMLHttpReq.onreadystatechange = processResponseChannel;
	XMLHttpReq.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
	XMLHttpReq.send("type=4&ssmnnumber="+sssmnuber );
}

function processResponseChannel()
{
	if(XMLHttpReq.readyState == 4){
		if(XMLHttpReq.status == 200){
		    //alert('success');
			parseMessageChannel();
		}
		else{
			alert(XMLHttpReq.status);
		}
	}
}

function parseMessageChannel()
{
	var sn=XMLHttpReq.responseText;
	selectChannels =sn;
	
	var channels = new Array();
	if(selectChannels.length >0)
	{
		channels = selectChannels.split(",");
	}
	var voption ;
	if(ty ==1)
	 	voption = document.getElementById('ssmnchannelvalue');
	else
		voption = document.getElementById('ssmnchannelvalueswitch');
  	voption.options.length=0;
	var opchannelstr = '';
	for(var j=0; j<channels.length; j++)
	{
		var str = channels[j];
		var strsplit = str.split("|");
		
		var option=new Option(strsplit[1],strsplit[0]);
        
        try{
            voption.add(option);
        }catch(e){
        }
	}

}

function sendRequest(type){
 	createXMLHttpRequest();
 	var sm = document.getElementById('bindmsisdn').value;

 	XMLHttpReq.open("POST","${ctx}/selectenablenumber",true);
	XMLHttpReq.onreadystatechange = processResponse;
	XMLHttpReq.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
	XMLHttpReq.send("type=3&oldmsisdn="+oldmsisdn+"&newmsisdn="+sm);
}

function processResponse(type){
 	if(XMLHttpReq.readyState == 4){
		if(XMLHttpReq.status == 200){
		    //alert('success');
			parseMessage();
		}
		else{
			alert(XMLHttpReq.status);
		}
	}
 }

 function parseMessage()
{
  	var all=XMLHttpReq.responseText;  
  	if(all == '')//不在同一个区域
  	{
  	
  		alert("换绑的经纪人不在同一区域,无法换绑!");
  	}else
  	{
  		var sm = document.getElementById('bindmsisdn').value;
		if(sm == '')
		{
			alert('请输入主号码!');
			return;
		}
		
		var sc = document.getElementById('ssmnchannelvalue');
		var scindex = sc.selectedIndex;
		var scvalue = sc.options[scindex].value;
		if(scvalue == '')
		{
			alert("请选择渠道!");
			return;
		}
		
		document.location.href="queryssmn.do?type=3&smsisdn="+oldmsisdn+"&uid="+switchuid+"&sssmnuber="+switchssmnnumber+"&bindmsisdn="+sm+"&channelvalue="+scvalue+"&msisdn="+selectMsisdn+"&name="+selectname+"&ssmnnumber="+selectssmnnumber+"&vempno="+selectsempno;

  	}
}

//换绑操作
function switchBind(oldmsisdn,suid,switchtossmnnumber,switchtomsisdn,switchtochannelId)
{
	var vuempno = document.getElementById('sempno').value;
	document.location.href="queryssmn.do?type=4&smsisdn="+oldmsisdn+"&uid="+suid+"&sssmnuber="+switchtossmnnumber+"&bindmsisdn="+switchtomsisdn+"&channelvalue="+switchtochannelId+"&msisdn="+selectMsisdn+"&name="+selectname+"&ssmnnumber="+selectssmnnumber+"&vempno="+vuempno;
}
//变更渠道
function showSwitchChannelBox(sname,smsisdn,uid,ssmnnumber,strChannel,channelname)
{
	varchannelname = channelname;
	
	ty =2;
	sendRequestChannel(ssmnnumber)
	
	if(sname != '' && smsisdn != '' )
	{	
		
		wBox=jQuery("#wbox10").wBox({
		title: "",
		html: 	"<div style='width:300px;height:190px;'>"+
				"<table style='margin-left:30px;width:80%;'>"+
					"<tr style='text-align=left;margin-left:30px;font-family:Arial;font-size:14px;font-style:normal;text-decoration:none;color:#333333;'>"+
						"<td style='height:25px;font-size:14px;font-weight:bold;'>经纪人:</td><td style='font-size:14px;'>"+sname+"</td>"+
					"</tr>"+
					"<tr><td style='height:25px;font-size:14px;font-weight:bold;'>主号码：</td><td style='font-size:14px;'>"+smsisdn+"</td>"+
					"</tr>"+
					"<tr>"+
						"<td style='height:25px;font-size:14px;font-weight:bold;'>副号码:</td><td style='font-size:14px;'>"+ssmnnumber+"</td>"+
					"</tr>"+
					"<tr>"+
						"<td style='height:27px;font-size:14px;font-weight:bold;'>变更渠道:</td>"+
						"<td>"+
							"<select id='ssmnchannelvalueswitch' style='font-size:14px;width:150px ; height:23px;progid:DXImageTransform.Microsoft.Alpha(Opacity=1);filter:alpha(opacity=100);-moz-opacity:1;-khtml-opacity:1; opacity:1; border:1px;border-style:solid;'>"+
							"</select>"+
						"</td>"+
					"</tr>"+
				"</table>"+
				"<div style='margin-bottom:5px;margin-top:10px;'>"+
					"<table width='300' border='0' cellpadding='0' cellspacing='0' bgcolor='#000000'><tr><td height='1'></td></tr></table></div> "+
				"<div style='text-align:center;font-family:Arial;font-size:14px;color:#333333;'>是否确认变更渠道操作?<div>"+
				"<input  style='margin-top:10px;margin-left:-1px;width:30%;' class='cancel' type='button' value='取消' onclick='cancel();' />&nbsp;&nbsp;&nbsp;&nbsp;"+
				"<input  style='margin-top:5px;margin-left:10px;width:30%;' class='sure' type='button' value='确定' onclick='switchchannel("+uid+","+smsisdn+");' /> </div>"
		});
		wBox.showBox();
	}

}
function switchchannel(uid,smsisdn)
{

	var sc = document.getElementById('ssmnchannelvalueswitch');
	var scindex = sc.selectedIndex;
	var sctext = sc.options[scindex].text;
	var scvalue = sc.options[scindex].value;
	var vuempno = document.getElementById('sempno').value;

	if(scvalue == '')
	{
		alert("请选择渠道!");
		return;
	}
	
	if(sctext == varchannelname)
	{
		alert("所变渠道与当前渠道一致!");	
		return;
	}
	
	document.location.href="queryssmn.do?type=5&msisdn="+smsisdn+"&uid="+uid+"&channelvalue="+scvalue+"&vempno="+vuempno;
}

function gobackbut()
{
	var vurl =document.referrer;
	alert(vurl);
	var indexofff = vurl.indexOf('method=edit');
	if(indexofff >=0)
	{
		
		//javascript:window.history.go(-2);
		
		//alert(document.referrer);
		self.location=document.referrer;
		
		//window.location.reload();
		
		//javascript:window.history.go(-2);
	}
	else
	{
		//javascript:window.history.go(-1);
		self.location=document.referrer;
	}
		
	// window.location.reload();
}

function returntoagent(selectmsisdn,selectname,selectssmnnum,selectchannelbox,selectbdbox1,selectzonebox1,selectareabox1,selectbagbox1,selectempno,pagetemp)
{
	var vurl = "manageagent.do?method=queryagent";
	if(selectmsisdn != '')
		vurl +="&msisdn="+selectmsisdn;
	if(selectempno !='')
		vurl +="&empno="+selectempno;
	if(selectname != '')
		vurl +="&name="+selectname;
	if(selectssmnnum != '')
		vurl+="&ssmnnum="+selectssmnnum;
	if(selectchannelbox != '')
		vurl+="&channelbox="+selectchannelbox;
	if(selectbdbox1 != '')
		vurl+="&bdbox1="+selectbdbox1;
	if(selectzonebox1 != '')
		vurl+="&zonebox1="+selectzonebox1;
	if(selectareabox1 != '')
		vurl+="&areabox1="+selectareabox1;
	if(selectbagbox1 != '')
		vurl+="&bagbox1="+selectbagbox1;
	if(pagetemp != '')
		vurl +="&page="+pagetemp;
		
	document.location.href=vurl;
	  
}

function returntossmnnumber(selectssmnnum,selectbdbox1,selectzonebox1,selectareabox1,selectbagbox1,pagetemp,selectstatusbox,selectenmtype)
{
	var vurl = "modifyssmnnumber.do?method=list";

	if(selectssmnnum != '')
		vurl+="&ssmnnum="+selectssmnnum;
	if(selectbdbox1 != '')
		vurl+="&bdbox1="+selectbdbox1;
	if(selectzonebox1 != '')
		vurl+="&zonebox1="+selectzonebox1;
	if(selectareabox1 != '')
		vurl+="&areabox1="+selectareabox1;
	if(selectbagbox1 != '')
		vurl+="&bagbox1="+selectbagbox1;
	if(pagetemp != '')
		vurl +="&page="+pagetemp;
	if(selectstatusbox != '')
		vurl +="&statusbox="+selectstatusbox;
	if(selectenmtype !='')
		vurl +="&enmtype="+selectenmtype;
	
	document.location.href=vurl;

}


function showRemarkBox(vsempno,ssmsisdn,vremark)
{
	selectsempno = vsempno;
	selectMsisdn = ssmsisdn;
	
	wBox=jQuery("#wbox10").wBox({
	title: "添加备注",

	html: "<div style='width:380px;height:280px;'><textarea style='margin-top:10px;margin-left:10px;width:350px;height:186px;' id='remark' name='remark'>"+vremark+"</textarea>" +
		  "<br/>&nbsp;&nbsp;&nbsp;<font color='red'>注：输入限制256个字符</font>"+
		  "<div style='margin-bottom:28px;margin-top:17px;'><table width='380' border='0' cellpadding='0' cellspacing='0' bgcolor='#000000'><tr><td height='1'></td></tr></table></div>" + 
		  "<div style='text-align:center;'><input  style='margin-top:-18px;width:30%;' class='scbtn' type='button' value='保存' onclick='onSave();' /></div> </div>"  });
	wBox.showBox();
}

function onSave()
{
	var vremark = document.getElementById('remark').value;
	
	if(vremark.length >256)
	{
		alert('备注信息长度不能超过256个字符!');
		return;
	}
	if(selectsempno == '')
	{
		alert('该经纪人没有员工编号!');
		return ;
	}

	var vres = vremark.replace(/\n/g,"\\n");
	document.location.href="queryssmn.do?type=6&vempno="+selectsempno+"&remark="+vres+"&msisdn="+selectMsisdn;
}

//-->
</script>
</head>

<body topmargin="0" onLoad="init('${msg}','${stype}','${oldmsisdn}','${switchtomsisdn}','${switchtossmnnumber}','${switchtochannelname}','${switchusername}','${msisdn}','${suid}','${switchtochannelId}','${name}','${ssmnnumber}','${sempno}');">
<%java.util.List authlist = (java.util.List)request.getAttribute("authMethodList");%>
 
 <div class="place">
 	<span>位置：</span>
	<ul class="placeul">
		<li><a href="#">经纪人管理</a></li>
		<li><a href="#">经纪人详情</a></li>
	</ul>
</div>

<div class="formbody" style="width:1100px;"> 
<input type="hidden" id="sempno" name="sempno" value="${sempno}" />

 <table class="tablelist">
 
 <c:if test="${isAll ==1}">
	<thead>
		<tr>
			<th colspan="6"><b>副号码信息查询</b></th>
		</tr>
		<tr>
			<th>经纪人姓名</th>
			<th>主号码</th>
			<th>副号码</th>
			<th>所属行动组</th>
			<th>所属渠道</th>
			<th>绑定时间</th>
		</tr>
	</thead>
	<tbody>
    	<c:forEach items="${ulist}" var="u" varStatus="s">
		    <tr>
			    <td>${u.name}</td>
			    <td>${u.msisdn}</td>
			    <td>${u.ssmnnumber}</td>
			    <td>${u.groupname}</td>
			    <td>${u.channelname}</td>
	 			<td><fmt:formatDate value="${u.intime}" pattern="yyyy-MM-dd HH:mm:ss"/></td> 
		    </tr> 
	    </c:forEach>
    </tbody>
 </c:if>
 
<c:if test="${isAll ==0}">
	<thead>
	   	<tr>
		 	<%if(authlist != null && (authlist.contains("manageUsers"))){ %>
		 		<c:if test="${isSecondMsisdn == '1'}">
		 			<th colspan="9" style="text-align:center;">当前状态</th>
		 		</c:if>
		 		<c:if test="${isSecondMsisdn != '1'}">
					<th colspan="8" style="text-align:center;">当前状态</th>
				</c:if>
			<%}%>
		    <%if(authlist == null ||  !(authlist.contains("manageUsers"))){ %>
		    	<c:if test="${isSecondMsisdn == '1'}">
					<th colspan="8" style="text-align:center;">当前状态</th>
				</c:if>
				<c:if test="${isSecondMsisdn != '1'}">
					<th colspan="7" style="text-align:center;">当前状态</th>
				</c:if>
			<%}%>
		</tr>	    
		<tr> 
	        <th>经纪人姓名</th>
	        <th>员工编号</th>
	        <th>主号码</th>
	        <th>副号码</th>
	        <c:if test="${isSecondMsisdn == '1'}">
	        	<th>第二联系人</th>
	        </c:if>
	        <th>所属行动组</th>
	        <th>所属渠道</th>
	        <th>绑定时间</th>
	        <%if(authlist != null && (authlist.contains("manageUsers"))){ %>
				 <c:if test="${stype !=7 }">
		         	<th>操作</th>
		         </c:if>
	         <%}%>
	   	</tr>
   	</thead>
   	<tbody>
   		<c:forEach items="${newList}" var="u" varStatus="s">
   			<tr>
	 			<td>${u.name}</td>
	 			<td>${u.empno}</td>
	 			<td>${u.msisdn}</td> 
	 			<td>${u.ssmnnumber}</td>
	 			<c:if test="${isSecondMsisdn == '1'}">
	 				<td>${u.secondMsisdn}</td>
	 			</c:if> 
	 			<td>${u.groupname}</td> 
	 			<td>${u.channelname}</td> 
	 			<td><fmt:formatDate value="${u.intime}" pattern="yyyy-MM-dd HH:mm:ss"/></td> 
	 			<%if(authlist != null && (authlist.contains("manageUsers"))){ %>
					<c:if test="${stype !=7 }">
				 		 <td>
				 				<a href="javascript:" style="color:#056dae;" onClick="showCancelBindBox('${u.name}','${u.msisdn}','${u.id}','${u.channelname}','${u.ssmnnumber}','${msisdn}','${name}','${ssmnnumber}','${sempno}','${isHaveSsmnNum}');" >解绑</a>&nbsp;&nbsp;|&nbsp;&nbsp;
				 				<!-- 当isHaveSsmnNum开关打开，并且是A+渠道时候，换绑和变更渠道 不可点，至灰 -->
				 				<c:if test="${u.channeltype ==1 && isHaveSsmnNum=='1' }">
				 					<a href="javascript:" style="color:#cad3d8;"  >换绑</a>&nbsp;&nbsp;|&nbsp;&nbsp;
				 					<a href="javascript:" style="color:#cad3d8;"  >变更渠道</a>&nbsp;&nbsp;|&nbsp;&nbsp;
				 				</c:if>
				 				<c:if test="${u.channeltype ==0 || isHaveSsmnNum !='1' }">
				 					<a href="javascript:" style="color:#056dae;" onClick="showSwitchBindBox('${u.name}','${u.msisdn}','${u.id}','${u.channelname}','${u.ssmnnumber}','${strChannels}','${msisdn}','${name}','${ssmnnumber}','${sempno}');" >换绑</a>&nbsp;&nbsp;|&nbsp;&nbsp;
				 					<a href="javascript:" style="color:#056dae;" onClick="showSwitchChannelBox('${u.name}','${u.msisdn}','${u.id}','${u.ssmnnumber}','${strChannels}','${u.channelname}','${sempno}');" >变更渠道</a>&nbsp;&nbsp;|&nbsp;&nbsp;
				 				</c:if>
				 				<a href="javascript:" style="color:#056dae;" onClick="showRemarkBox('${sempno}','${u.msisdn}','${u.remark }');" >备注</a>
				 		 </td>
		 			</c:if>
	 			<%} %>
 			</tr>
   		</c:forEach>
   	</tbody>
</c:if>
</table>

<br/>
<c:if test="${isAll ==0}">
 <table class="tablelist">
 	<thead>
	 	<tr>
	 		<c:if test="${isSecondMsisdn == '1'}">
				<th colspan="9" style="text-align:center;">历史信息</th>
			</c:if>
			<c:if test="${isSecondMsisdn != '1'}">
				<th colspan="8" style="text-align:center;">历史信息</th>
			</c:if>
		</tr>	    
		<tr> 
	        <th>经纪人姓名</th>
	        <th>员工编号</th>
	        <th>主号码</th>
	        <th>副号码</th>
	        <c:if test="${isSecondMsisdn == '1'}">
	        	<th>第二联系人</th>
	        </c:if>
	        <th>所属行动组</th>
	        <th>所属渠道</th>
	        <th>绑定时间</th>
	        <th>解绑/变更时间</th>
	   	</tr>
   	</thead>
   	<tbody>
	 	<c:forEach items="${oldList}" var="u" varStatus="s">
	 		<tr>
	 			<td>${u.name}</td>
	 			<td>${u.empno}</td>
	 			<td>${u.msisdn}</td> 
	 			<td>${u.ssmnnumber}</td> 
	 			<c:if test="${isSecondMsisdn == '1'}">
	 				<td>${u.secondMsisdn}</td>
	 			</c:if>
	 			<td>${u.groupname}</td> 
	 			<td>${u.channelname}</td> 
	 			<td><fmt:formatDate value="${u.subDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td> 
	 		 	<td><fmt:formatDate value="${u.cancelDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
	 		</tr>
	 	</c:forEach>
 	</tbody>
 </table>
</c:if>
<br/>
<div align="center"><input class="scbtn" type="button" value="返回" <c:if test="${stype !=7 }"> onClick="returntoagent('${selectmsisdn}','${selectname}','${selectssmnnum}','${selectchannelbox}','${selectbdbox1}','${selectzonebox1}','${selectareabox1}','${selectbagbox1}','${selectempno}','${pagetemp }');" </c:if>  <c:if test="${stype ==7 }"> onClick="returntossmnnumber('${selectssmnnum}','${selectbdbox1}','${selectzonebox1}','${selectareabox1}','${selectbagbox1}','${pagetemp }','${selectstatusbox }','${selectenmtype }');" </c:if>  /></div>
  
<form method="post" action="queryssmn.do" name="pageForm">
<input type="hidden" name="msisdn" value="${msisdn}" />
<input type="hidden" name="name" value="${name}" />
<input type="hidden" name="ssmnnumber" value="${ssmnnumber}" />
<input type="hidden" name="selectmsisdn" value="${selectmsisdn}" />
<input type="hidden" name="selectempno" value="${selectempno}" />
<input type="hidden" name="selectname" value="${selectname}" />
<input type="hidden" name="selectssmnnum" value="${selectssmnnum}" />
<input type="hidden" name="selectchannelbox" value="${selectchannelbox}" />
<input type="hidden" name="selectbdbox1" value="${selectbdbox1}" />
<input type="hidden" name="selectzonebox1" value="${selectzonebox1}" />
<input type="hidden" name="selectareabox1" value="${selectareabox1}" />
<input type="hidden" name="selectbagbox1" value="${selectbagbox1}" />
<input type="hidden" name="vempno" value="${sempno}" />
</form>

<br/>
<c:if test="${isAll ==1 && recordCount>0}">
 <div style="width:1000px;" align="right" > <input style="width:120px ; height:30px;"  type="button" value="副号码信息导出" onClick="javascript:document.location='queryssmn.do?type=5&msisdn=${msisdn}&name=${name}&ssmnnumber=${ssmnnumber}'" /></div> 
</c:if>


</div>

<script type="text/javascript"> 
 	$("#usual1 ul").idTabs(); 
</script>
    
<script type="text/javascript">
	$('.tablelist tbody tr:odd').addClass('odd');
</script>

</body>
</html>
