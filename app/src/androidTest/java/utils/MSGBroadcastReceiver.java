package utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Environment;
import android.telephony.SmsMessage;
import android.util.Log;

//@SuppressLint("SimpleDateFormat")
public class MSGBroadcastReceiver extends BroadcastReceiver {

	public static final String TAG = "SMSReceiver";
	public static final String SMS_RECEIVED_ACTION = "android.provider.Telephony.SMS_RECEIVED";
	private SharedPreferences sharedPreferences;// 配置文件
	private Editor editor;// 更改配置文件的类实例
	private FileOutputStream out;
	
	
	@Override
	public void onReceive(Context context, Intent intent) {
		
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 完整的时间
		String time = sdf.format(date);
		Log.i(TAG, "Start");
		
		// 取出监听到的短信的电话号码
		String phone = this.getPhoneNumber(intent);
		// 如果收到的短信的电话号码为13693490547,则中断广播，让手机收不到该电话号码的短信
		Log.i(TAG, "1111111111111111111111111111");
		Log.i(TAG, phone);
		Log.i(TAG, "1111111111111111111111111111");
		
		
		// 取出监听到的短信的内容
		String content = this.getContent(intent);
		Log.i(TAG, "222222222222222222222222222");
		Log.i(TAG, content);
		Log.i(TAG, "222222222222222222222222222");
		
		writeFile(content);//将短信内容写入SD卡  
		
		if (intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {

			sharedPreferences = context.getSharedPreferences("x",
					Context.MODE_PRIVATE);
			editor = sharedPreferences.edit();
			editor.putLong("key", new Date().getTime());
			editor.commit();
			try {
				out = context
						.openFileOutput("veryfycode.txt", Context.MODE_APPEND);
				out.write((time+" "+phone+" "+content+"\n" ).getBytes());
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		
	}
	
	public static long getUpTime(Activity context) {
		SharedPreferences sharedPreferences = context.getSharedPreferences("x",
				Context.MODE_PRIVATE);
		long seconds = sharedPreferences.getLong("key", -1);
		return seconds;
	}

	
	 public final SmsMessage[] getMessagesFromIntent(Intent intent){  
	        Object[] messages = (Object[]) intent.getSerializableExtra("pdus");  
	        byte[][] pduObjs = new byte[messages.length][];  
	        for (int i = 0; i < messages.length; i++)  
	        {  
	            pduObjs[i] = (byte[]) messages[i];  
	        }  
	        byte[][] pdus = new byte[pduObjs.length][];  
	        int pduCount = pdus.length;  
	        SmsMessage[] msgs = new SmsMessage[pduCount];  
	        for (int i = 0; i < pduCount; i++)        {  
	            pdus[i] = pduObjs[i];  
	            msgs[i] = SmsMessage.createFromPdu(pdus[i]);  
	        }  
	        return msgs;  
	    }
	
	private String getContent(Intent intent){
		if (intent.getAction()
				.equals("android.provider.Telephony.SMS_RECEIVED")) {
			String smsContent = null;
			 SmsMessage[] messages = getMessagesFromIntent(intent);  
	           for (SmsMessage message : messages){  
	              Log.i(TAG, message.getOriginatingAddress() + " : " +  
	                  message.getDisplayOriginatingAddress() + " : " +  
	                  message.getDisplayMessageBody() + " : " +  
	                  message.getTimestampMillis());  
	              smsContent=message.getDisplayMessageBody();  
	              Log.i(TAG, smsContent);                
	           }  
	        return smsContent;
		}
		
		return null;
		
	}
	

	// 取出监听到的短信的电话号码
	private String getPhoneNumber(Intent intent) {
		if (intent.getAction()
				.equals("android.provider.Telephony.SMS_RECEIVED")) { /* 创建字符串变量sb */
			/** 接收由Intent传来的数据 */
			String phone = null;
			Bundle bundle = intent.getExtras();
			/** 判断Intent有无数据 */
			if (bundle != null) {
				/** pdus为 android内置短信参数 identifier 通过bundle.get("")返回一包含pdus对象 */
				Object[] myOBJpdus = (Object[]) bundle.get("pdus");
				/* 构建短信对象array,并根据收到的对象长度来定义array的大小 */
				SmsMessage[] messages = new SmsMessage[myOBJpdus.length];
				for (int i = 0; i < messages.length; i++) {
					messages[i] = SmsMessage
							.createFromPdu((byte[]) myOBJpdus[i]);
				}
				/* 把传来的短信合并定义在stringbuffer中 */
				for (SmsMessage currentMessage : messages) {
					/* 发送人电话号码 */
					phone = currentMessage.getDisplayOriginatingAddress();
				}
			}
			/* 以(Toase)形式展示 */

			return phone;
		}
		return null;
	}
	
	
	//将短信内容写到SD卡上的文件里，便于将文件pull到PC，这样可方便其它如WWW/WAP平台的自动化    
	@SuppressLint("SdCardPath")
	public void writeFile(String str){
		
		 boolean sdCardExist = Environment.getExternalStorageState().equals(
		         Environment.MEDIA_MOUNTED);
		 if (sdCardExist)
		 {	
			 String filePath= Environment.getExternalStorageDirectory() +File.separator+"verifyCode.txt"; 
			 File file = new File(filePath);
			 try {
				 
				 if(file.exists())    
				 {    
					 file.delete();
					 file.createNewFile();    
			
				 }    
				 
				 if(!file.exists())
				 	{
					 file.createNewFile();
				 	}
			 } catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			 }
			
			 byte [] bytes = str.getBytes();
			 try{
				FileOutputStream fos=new FileOutputStream(file);
				fos.write(bytes);
				fos.close();
		  	}catch(IOException e){
		  		e.printStackTrace();
		  	} 
		 }
  }
	
	
}