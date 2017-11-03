package com.dascom.OPadmin.util;

import java.io.File;

public class DeleteFileUtil {
	/**    
     * 删除文件，可以是单个文件或文件夹    
    * @param   fileName    待删除的文件�?    
    * @return 文件删除成功返回true,否则返回false    
     */     
    public static boolean delete(String fileName){      
        File file = new File(fileName);      
      if(!file.exists()){         
           return false;      
       }else{      
            if(file.isFile()){      
                      
                return deleteFile(fileName);      
           }else{      
                return deleteDirectory(fileName);      
           }      
        }      
    }      
          
    /**    
     * 删除单个文件    
     * @param   fileName    被删除文件的文件�?    
    * @return 单个文件删除成功返回true,否则返回false    
    */     
    public static boolean deleteFile(String fileName){      
       File file = new File(fileName);      
       if(file.isFile() && file.exists()){   
    	   if(file.delete() == true){
    		   return true;
    	   }
    	   int n = 0;
    	   //如果删除失败，重�?50�?
    	   while(file.delete() == false && n < 50){
    		   n++;
    		   if(file.delete()){
    			   break;
    		   }
    	   }
           return true;      
        }else{        
            return false;      
        }      
   }      
          
    /**    
     * 删除目录（文件夹）以及目录下的文�?    
     * @param   dir 被删除目录的文件路径    
    * @return  目录删除成功返回true,否则返回false    
     */     
   public static boolean deleteDirectory(String dir){      
        //如果dir不以文件分隔符结尾，自动添加文件分隔�?      
        if(!dir.endsWith(File.separator)){      
            dir = dir+File.separator;      
        }      
       File dirFile = new File(dir);      
        //如果dir对应的文件不存在，或者不是一个目录，则�??�?      
        if(!dirFile.exists() || !dirFile.isDirectory()){          
            return false;      
       }      
       boolean flag = true;      
        //删除文件夹下的所有文�?(包括子目�?)      
        File[] files = dirFile.listFiles();      
        for(int i=0;i<files.length;i++){      
            //删除子文�?      
            if(files[i].isFile()){      
                flag = deleteFile(files[i].getAbsolutePath());      
                if(!flag){      
                    break;      
               }      
            }      
            //删除子目�?      
            else{      
                flag = deleteDirectory(files[i].getAbsolutePath());      
                if(!flag){      
                    break;      
                }      
            }      
        }      
              
        if(!flag){      
            return false;      
        }      
             
        //删除当前目录      
        if(dirFile.delete()){            
            return true;      
        }else{            
            return false;      
        }      
    }      
          
   public static void main(String[] args) {      
       //String fileName = "g:/temp/xwz.txt";      
       //DeleteFileUtil.deleteFile(fileName);      
       String fileDir = "G:/temp/temp0/temp1";      
       //DeleteFileUtil.deleteDirectory(fileDir);      
       DeleteFileUtil.delete(fileDir);      
             
   }      
}
