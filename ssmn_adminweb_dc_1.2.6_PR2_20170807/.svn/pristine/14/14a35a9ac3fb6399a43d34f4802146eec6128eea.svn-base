	var containstr = "%\(\)\/\/><-';";
	var nicknamestr = "><-\/\/;'";
	var flag = true;

	String.prototype.trim=function()
    {
	    return this.replace(/^\s*/g,"").replace(/\s*$/g,"");
    }	
    function  isChinese(name)  //中文值检测
    {  
		var flag = true;
        if(name.length  ==  0)
        {
              flag = false;
              return flag;
        }
        for(i  =  0;  i  <  name.length;  i++)  
        {  
            if(name.charCodeAt(i)  <=  128)
            {
                flag = false;
                break;
            }
        }
        return  flag;
    }
   
    function  isNumber(name)  //数值检测
    {  
        if(name.length  ==  0)
            return  false;
        for(i  =  0;  i  <  name.length;  i++)  
        {  
            if(name.charAt(i)  <  "0"  ||  name.charAt(i)  >  "9")
                return  false;
        }
        return  true;
    }
    
    function   isValidDate(iY,   iM,   iD)   
    { 
		//alert(iY+":"+iM+":"+iD);
      var   a=new   Date(iY,   iM-1,   iD);  //除了只要记住月份是从0－11就不会乱了，先把取得月份减一，生成时间后获取月份时在把月份加一就对了。
      var   y=a.getFullYear();   
      var   m=a.getMonth();   
      var   d=a.getDate(); 
      m=m+1;
      //alert(y+":"+m+":"+d);
      if   (y!=iY   ||   m!=iM   ||   d!=iD)   
      {   
              return   false;   
      }   
     return   true  ; 
  	   
    }   
    function  isValidAgeAndIdno(iY, iM, iD,iAge)   //检查年龄和身份证号码是否一致
    {     
          var now = new Date();
          var nowy = now.getFullYear();
          var   a=new   Date(iY,iM-1,iD);   
          var   d=a.getDate();   
          var ay = a.getFullYear();
         
          if   ((parseInt(nowy)-parseInt(ay)!=parseInt(iAge)))   
          {   
                  return   false;   
          }   
  	    return  true;   
    }   
    function isValidateSexAndIdno(stridno,objsex) //检查性别和身份证号码是否一致
    {
        //var objsex;
		//objsex= objP.value;
        var idsex;
        if(stridno.length==18)
        {
			if(parseInt(stridno.substr(16,1))%2==0)
				idsex = 0;
			else
				idsex = 1;  
        }
        else
        {
			if(parseInt(stridno.substr(14,1))%2==0)
				idsex = 0;
			else
				idsex = 1;  
        }
        if(idsex != objsex)
            return false;
        return true;
    }
    function   isChinaIDCard(StrNo) //检查身份证号码
    {   
      
  	    StrNo   =   StrNo.toString();
  	    
  	    if   (StrNo.length==18)   
  	    {   
                var   a,b,c   
                if   (!isNumber(StrNo.substr(0,17)))   
                {
            	    return   false;
                }   
                a=parseInt(StrNo.substr(0,1))*7+parseInt(StrNo.substr(1,1))*9+parseInt(StrNo.substr(2,1))*10;   
                a=a+parseInt(StrNo.substr(3,1))*5+parseInt(StrNo.substr(4,1))*8+parseInt(StrNo.substr(5,1))*4;   
                a=a+parseInt(StrNo.substr(6,1))*2+parseInt(StrNo.substr(7,1))*1+parseInt(StrNo.substr(8,1))*6;     
                a=a+parseInt(StrNo.substr(9,1))*3+parseInt(StrNo.substr(10,1))*7+parseInt(StrNo.substr(11,1))*9;     
                a=a+parseInt(StrNo.substr(12,1))*10+parseInt(StrNo.substr(13,1))*5+parseInt(StrNo.substr(14,1))*8;     
                a=a+parseInt(StrNo.substr(15,1))*4+parseInt(StrNo.substr(16,1))*2;   
                b=a%11;   
        
                if   (b==2)   //最后一位为校验位   
                {   
            	    c=StrNo.substr(17,1).toUpperCase();   //转为大写X   
                }   
                else   
                {   
            	    c=parseInt(StrNo.substr(17,1));   
                }   
        
                switch(b)   
                {   
	                case   0:   
		                if   (   c!=1   )   
		                {
		            	    //alert("身份证好号码格式不正确1");
		            	    return   false;
		                }
	                break;   
	                case   1:   if   (   c!=0   )   {//alert("身份证好号码格式不正确");
	                return   false;}break;   
	                case   2:   if   (   c!="X")    {//alert("身份证好号码格式不正确");
	                return   false;}break;   
	                case   3:   if   (   c!=9   )   {//alert("身份证好号码格式不正确");
	                return   false;}break;   
	                case   4:   if   (   c!=8   )   {//alert("身份证好号码格式不正确");
	                return   false;}break;   
	                case   5:   if   (   c!=7   )   {//alert("身份证好号码格式不正确");
	                return   false;}break;   
	                case   6:   if   (   c!=6   )   {//alert("身份证好号码格式不正确");
	                return   false;}break;   
	                case   7:   if   (   c!=5   )   {//alert("身份证好号码格式不正确");
	                return   false;}break;   
	                case   8:   if   (   c!=4   )   {//alert("身份证好号码格式不正确");
	                return   false;}break;   
	                case   9:   if   (   c!=3   )   {//alert("身份证好号码格式不正确");
	                return   false;}break;   
	                case   10:   if   (   c!=2   )  {//alert("身份证好号码格式不正确");
	                return   false}   
                }
             
          	    if   (!isValidDate(StrNo.substr(6,4),StrNo.substr(10,2),StrNo.substr(12,2)))   
                      {return   false;}     
            } 
            else if(StrNo.length==15)
            { 
               
  		        var r = /^[\d]{15}$/; 
                if (!r.test(StrNo)) 
                    {return false; }
                if (!isValidDate("19"+StrNo.substr(6,2),StrNo.substr(8,2),StrNo.substr(10,2)))   
                      {return   false;}     
       	    } 
       	    else
       	    {
       	        alert("输入的身份证号码长度不正确！");   
  		        return   false   ;	 
       	    } 
        return true;
    }



  function checkformidinfo()
  {
  
		  var objP = document.getElementById("txtTrueName");
		   var reg=/^[^']{1,20}$/;
		if(""==objP.value.trim())
		{
			alert("请输入[真实姓名]");
			objP.select();
			objP.focus();
			return false;
		}
		if(reg.test(objP.value.trim())==false)
		{
			alert("[真实姓名]格式不正确");
			objP.select();
			objP.focus();
			return false;
		}
		if(!isChinese(objP.value.trim()))
		{
			alert("[真实姓名]格式不正确");
			objP.select();
			objP.focus();
			return false;
		}
      var objP = document.getElementById("txtIdnum");
	  var stridno = objP.value.trim();
	 
	  if(18!=objP.value.trim().length&&15!=objP.value.trim().length)
		{
			alert("[身份证号]长度为18或15位");
			objP.select();
			objP.focus();
			return false;
		}
		
		if(stridno == "111111111111111")
		{
			alert("[身份证号]格式不正确");
			objP.select();
			objP.focus();
			return false;
		}

      if(!isChinaIDCard(objP.value.trim()))
		{
			alert("[身份证号]格式不正确");
			objP.select();
			objP.focus();
			return false;
		} 
		var i1 = stridno.substr(0,1);
		var i2 = stridno.substr(1,1);
		var i3 = stridno.substr(2,1);
		
		if(!(parseInt(i1)>=1&&parseInt(i1)<7))
		{
			alert("[身份证号]格式不正确");
			objP.select();
			objP.focus();
			return false;
		}
		if(!(parseInt(i2)>=0&&parseInt(i2)<=7))
		{
			alert("[身份证号]格式不正确");
			objP.select();
			objP.focus();
			return false;
		}
		if(i1==i2&&i2==i3&&i1==i3&&i1!='2')
		{
			alert("[身份证号]格式不正确");
			objP.select();
			objP.focus();
			return false;
		}
		
		var i4 = stridno.substr(3,1);
		if(i1==i2&&i2==i3&&i3==i4)
		{
			alert("[身份证号]格式不正确");
			objP.select();
			objP.focus();
			return false;
		}
		
		/*
		var ie1;
		var ie2;
		var ie3;
		if(stridno.length==15)
		{
			ie1 = stridno.substr(12,1);
			ie2 = stridno.substr(13,1);
			ie3 = stridno.substr(14,1);
		}
		else if(stridno.length==18)
		{
			ie1 = stridno.substr(15,1);
			ie2 = stridno.substr(16,1);
			ie3 = stridno.substr(17,1);
		}
		if(ie1==ie2&&ie2==ie3)
		{
			alert("[身份证号]格式不正确");
			objP.select();
			objP.focus();
			return false;
		}
		
		*/
		
		var objP = document.getElementById("ddlSex");
		var sexV = objP.options[objP.selectedIndex].value
		if("请选择性别"==sexV)
		{
			alert("请选择[性别]");
			objP.focus();
			return false;
		}
		if(!isValidateSexAndIdno(stridno,parseInt(sexV)))
		{
			alert("[性别]必须和[身份证]一致");
			objP.focus();
			return false; 
		}
	
		return true;
  }
  