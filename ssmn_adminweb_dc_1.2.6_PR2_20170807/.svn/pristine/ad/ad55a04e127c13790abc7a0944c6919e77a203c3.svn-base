var Request = new function(){
	this.pool = new Array();
	this.getXMLHttp = function (){
 	  for (var i = 0; i < this.pool.length; i++){
	  	 if (this.pool[i]["obj"].readyState == 0 || this.pool[i]["obj"].readyState == 4){
	   		 return this.pool[i]["obj"];
	  	 }
 	  }
	  this.pool[this.pool.length] = new Array();
	  this.pool[this.pool.length - 1]["obj"] = this.createXMLHttp();
	  return this.pool[this.pool.length - 1]["obj"];
	}

	this.createXMLHttp = function (){
 		if(window.XMLHttpRequest){
  			var xmlObj = new XMLHttpRequest();
 		} 
 		else{
  			var MSXML = ['Microsoft.XMLHTTP', 'MSXML2.XMLHTTP.5.0', 'MSXML2.XMLHTTP.4.0', 'MSXML2.XMLHTTP.3.0', 'MSXML2.XMLHTTP'];
  			for(var n = 0; n < MSXML.length; n++){
   				try{
    				var xmlObj = new ActiveXObject(MSXML[n]);        
    				break;
   				}catch(e){
   				}
  			}
 		 } 
 
 		return xmlObj;
	 }

	this.reSend = function (url,data,callback){
	 	 var objXMLHttp = this.getXMLHttp() 
		 if(typeof(objXMLHttp) != "object"){
	    	return ;
	 	 }
	 	 url += (url.indexOf("?") >= 0) ? "&random=" + new Date().getTime() : "?random=" + new Date().getTime();
	 	// alert(url);
		 if(data == ""){
	  		objXMLHttp.open('GET' , url, true);
	  		objXMLHttp.send('');
	 	 }
	 	 else{
		  	objXMLHttp.open('POST' , url, true);
		  	objXMLHttp.setRequestHeader("Content-Length",data.length); 
		  	objXMLHttp.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
		  	objXMLHttp.send(data);
	   }
 
		 if(typeof(callback) == "function" ){
			  objXMLHttp.onreadystatechange = function (){
				   if (objXMLHttp.readyState == 4){
						    if(objXMLHttp.status == 200 || objXMLHttp.status == 304){
						     		callback(objXMLHttp);
						    }
						    else{
						     		//alert("Error loading page\n"+ objXMLHttp.status +":"+ objXMLHttp.statusText);
						    }
						    objXMLHttp.abort();
				   }
			  }
		 }

	}

}

////////////////////////////////////////parse xml tool function start /////////////
function DataSet(xmldoc, tagLabel){
	 this.rootObj = xmldoc.getElementsByTagName(tagLabel) 
	 this.getCount = getCount
	 this.getData = getData
	 this.getAttribute = getAttribute
} 
function getCount(){
	 return this.rootObj.length
}
function getData(index, tagName){
	 if (index >= this.count) return "index overflow"
	 var node = this.rootObj[index]
	 var str = node.getElementsByTagName(tagName)[0].firstChild.data
	 return str
}
function getAttribute(index, tagName) {
	 if (index >= this.count) return "index overflow"
	 var node = this.rootObj[index]
	 var str = node.getAttribute(tagName)
	 return str
}
////////////////////////////////////////parse xml tool function end //////////

////////////////////////////// html convert /////////////////////////////////
function escapeHTML(str){
    var div = document.createElement('div');
    var text = document.createTextNode(str);
    div.appendChild(text);
    return div.innerHTML;
}
////////////////////////////////end html convert/////////////////////////////

/////////get document element /////////////
function $(){
  var elements = new Array();
  for (var i = 0; i < arguments.length; i++) {
    var element = arguments[i];
    if (typeof element == 'string')
      element = document.getElementById(element);
    if (arguments.length == 1)
      return element;
    elements.push(element);
  }
  return elements;
}
//////////reload Array.push function for IE5.0 work good//////////////////
Array.prototype.push = function(value)
{
  this[this.length] = value;
}
//////////reload Array.push function for IE5.0 work good//////////////////

//////////use className find DOM node method??getElementsByClassName////////////
function getElementsByClassName(ele,className) 
{
  //get all element
	  if(document.all)
	  {
	    var children = ele.all;
	  }
	  else
	  {
	    var children = ele.getElementsByTagName('*'); //for firefox
	  }
	  //get all node and check className
	  var elements = new Array();
	  for (var i = 0; i < children.length; i++) 
	  {
	    var child = children[i];
	    var classNames = child.className.split(' ');
	    for (var j = 0; j < classNames.length; j++) 
	    {
	      if (classNames[j] == className) 
	      {
	        elements[elements.length] = child;
	        break;
	      }
	    }
	  }
	  return elements;
}
//////////use className find DOM node method??getElementsByClassName////////////
//trim the string's space
function  Trim(str)
{
	return  str.replace(/^\s*(.*?)[\s\n]*$/g,  '$1');
}

String.prototype.endWith=function(oString){
  var reg=new RegExp(oString+"$");   
  return reg.test(this);
}
 
function getRequestBody(oForm) {
	var aParams = new Array();
	for (var i=0 ; i < oForm.elements.length; i++) {
	var sParam = encodeURIComponent(oForm.elements[i].name);
	sParam += "=";
	sParam += encodeURIComponent(oForm.elements[i].value);
	aParams.push(sParam);
	}
	return aParams.join("&");
}
//other......................
function MM_swapImgRestore() { //v3.0
  var i,x,a=document.MM_sr; for(i=0;a&&i<a.length&&(x=a[i])&&x.oSrc;i++) x.src=x.oSrc;
}

function MM_preloadImages() { //v3.0
  var d=document; if(d.images){ if(!d.MM_p) d.MM_p=new Array();
    var i,j=d.MM_p.length,a=MM_preloadImages.arguments; for(i=0; i<a.length; i++)
    if (a[i].indexOf("#")!=0){ d.MM_p[j]=new Image; d.MM_p[j++].src=a[i];}}
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