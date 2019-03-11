package utils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import com.robotium.solo.Solo;

import android.app.NotificationManager;
import android.content.Context;
import android.os.Environment;
import android.util.Log;

public class MessageUtils {

	private static String TAG = "MessageUtils->SMSReceiver";
	private static String verify = "";

	public static String readSMS() throws IOException{
		
		
		boolean sdCardExist = Environment.getExternalStorageState().equals(
		         Environment.MEDIA_MOUNTED);
		 if (sdCardExist)
		 {	
			 String filePath= Environment.getExternalStorageDirectory() +File.separator+"verifyCode.txt"; 
			 File file = new File(filePath);
			 try {
				 
				 if(file.exists()) {   
		
					FileInputStream fis=new FileInputStream(file);
					
					BufferedInputStream bis=new BufferedInputStream(fis);
					BufferedReader  read = new BufferedReader (new InputStreamReader(bis));
					int c=0;
					StringBuffer sb=new StringBuffer();
					while ((c=read.read())!=-1) {
			            sb.append((char) c);
			        }
					read.close();
					bis.close();
					fis.close();
					Log.i(TAG, sb.toString());		
					verify=sb.toString();		
			
				 	}
				 }catch (Exception e) {  
				 	    e.printStackTrace();  
				 	}  

		 }
		 
		return verify;
		
	}

	public static boolean isNotficationShow(Solo solo, String s, int num) {
		int t = 0;
		boolean result = false;
		while (t <= num) {
			result = solo.waitForText(s);
			if (result) {
				break;
			}
			solo.sleep(1);
			t += 1;
		}
		t = 0;
		return result;
	}

}
