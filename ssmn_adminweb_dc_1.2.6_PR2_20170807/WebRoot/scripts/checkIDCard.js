	var containstr = "%\(\)\/\/><-';";
	var nicknamestr = "><-\/\/;'";
	var flag = true;

	String.prototype.trim=function()
    {
	    return this.replace(/^\s*/g,"").replace(/\s*$/g,"");
    }	
    function  isChinese(name)  //����ֵ���
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
   
    function  isNumber(name)  //��ֵ���
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
      var   a=new   Date(iY,   iM-1,   iD);  //����ֻҪ��ס�·��Ǵ�0��11�Ͳ������ˣ��Ȱ�ȡ���·ݼ�һ������ʱ����ȡ�·�ʱ�ڰ��·ݼ�һ�Ͷ��ˡ�
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
    function  isValidAgeAndIdno(iY, iM, iD,iAge)   //�����������֤�����Ƿ�һ��
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
    function isValidateSexAndIdno(stridno,objsex) //����Ա�����֤�����Ƿ�һ��
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
    function   isChinaIDCard(StrNo) //������֤����
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
        
                if   (b==2)   //���һλΪУ��λ   
                {   
            	    c=StrNo.substr(17,1).toUpperCase();   //תΪ��дX   
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
		            	    //alert("���֤�ú����ʽ����ȷ1");
		            	    return   false;
		                }
	                break;   
	                case   1:   if   (   c!=0   )   {//alert("���֤�ú����ʽ����ȷ");
	                return   false;}break;   
	                case   2:   if   (   c!="X")    {//alert("���֤�ú����ʽ����ȷ");
	                return   false;}break;   
	                case   3:   if   (   c!=9   )   {//alert("���֤�ú����ʽ����ȷ");
	                return   false;}break;   
	                case   4:   if   (   c!=8   )   {//alert("���֤�ú����ʽ����ȷ");
	                return   false;}break;   
	                case   5:   if   (   c!=7   )   {//alert("���֤�ú����ʽ����ȷ");
	                return   false;}break;   
	                case   6:   if   (   c!=6   )   {//alert("���֤�ú����ʽ����ȷ");
	                return   false;}break;   
	                case   7:   if   (   c!=5   )   {//alert("���֤�ú����ʽ����ȷ");
	                return   false;}break;   
	                case   8:   if   (   c!=4   )   {//alert("���֤�ú����ʽ����ȷ");
	                return   false;}break;   
	                case   9:   if   (   c!=3   )   {//alert("���֤�ú����ʽ����ȷ");
	                return   false;}break;   
	                case   10:   if   (   c!=2   )  {//alert("���֤�ú����ʽ����ȷ");
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
       	        alert("��������֤���볤�Ȳ���ȷ��");   
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
			alert("������[��ʵ����]");
			objP.select();
			objP.focus();
			return false;
		}
		if(reg.test(objP.value.trim())==false)
		{
			alert("[��ʵ����]��ʽ����ȷ");
			objP.select();
			objP.focus();
			return false;
		}
		if(!isChinese(objP.value.trim()))
		{
			alert("[��ʵ����]��ʽ����ȷ");
			objP.select();
			objP.focus();
			return false;
		}
      var objP = document.getElementById("txtIdnum");
	  var stridno = objP.value.trim();
	 
	  if(18!=objP.value.trim().length&&15!=objP.value.trim().length)
		{
			alert("[���֤��]����Ϊ18��15λ");
			objP.select();
			objP.focus();
			return false;
		}
		
		if(stridno == "111111111111111")
		{
			alert("[���֤��]��ʽ����ȷ");
			objP.select();
			objP.focus();
			return false;
		}

      if(!isChinaIDCard(objP.value.trim()))
		{
			alert("[���֤��]��ʽ����ȷ");
			objP.select();
			objP.focus();
			return false;
		} 
		var i1 = stridno.substr(0,1);
		var i2 = stridno.substr(1,1);
		var i3 = stridno.substr(2,1);
		
		if(!(parseInt(i1)>=1&&parseInt(i1)<7))
		{
			alert("[���֤��]��ʽ����ȷ");
			objP.select();
			objP.focus();
			return false;
		}
		if(!(parseInt(i2)>=0&&parseInt(i2)<=7))
		{
			alert("[���֤��]��ʽ����ȷ");
			objP.select();
			objP.focus();
			return false;
		}
		if(i1==i2&&i2==i3&&i1==i3&&i1!='2')
		{
			alert("[���֤��]��ʽ����ȷ");
			objP.select();
			objP.focus();
			return false;
		}
		
		var i4 = stridno.substr(3,1);
		if(i1==i2&&i2==i3&&i3==i4)
		{
			alert("[���֤��]��ʽ����ȷ");
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
			alert("[���֤��]��ʽ����ȷ");
			objP.select();
			objP.focus();
			return false;
		}
		
		*/
		
		var objP = document.getElementById("ddlSex");
		var sexV = objP.options[objP.selectedIndex].value
		if("��ѡ���Ա�"==sexV)
		{
			alert("��ѡ��[�Ա�]");
			objP.focus();
			return false;
		}
		if(!isValidateSexAndIdno(stridno,parseInt(sexV)))
		{
			alert("[�Ա�]�����[���֤]һ��");
			objP.focus();
			return false; 
		}
	
		return true;
  }
  