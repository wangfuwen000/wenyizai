package utils;

import java.util.ArrayList;
import java.util.Date;



import com.robotium.solo.Solo;

import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import junit.framework.Assert;

public class Operation extends Assert{
	private Solo solo;
	 static final String TAG="Test->Operation";
	 Operation(Solo so)
	 {
		 solo=so;
	 }
	 
	 /**
	  * 日志输出页面中所有View的信息
	  */
	public  void printAllViews()
	{
		Log.d(TAG, "以下是此页面的所有View信息");
		ArrayList<View> views=solo.getViews();
		if(views!=null){
			for (int i = 0; i < views.size(); i++) {
				if(((View) views.get(i))!=null){
				
				String id = ((View) views.get(i)).getId() + "";
				String name = ((View) views.get(i)).toString();
				String nn = ((View) views.get(i)).getClass().toString();
				Log.d(TAG, "Id:" + id);
				Log.d(TAG, "Name:" + name);
				Log.d(TAG, "Class" + nn);
				if(views.get(i) instanceof  TextView)
				{
					
					Log.d(TAG, ((TextView)views.get(i)).getText().toString());
				}
				}			
			}
		}
	}
	/**
	  * 获取当前页面的所有文字
	  */
	public  ArrayList<String>  getAllViewsText()
	{
		Log.d(TAG, "得到页面的所有View的文字");
		ArrayList<View> views=solo.getViews();
		ArrayList<String> viewstext=new ArrayList<String>();
		for(int i=0;i<views.size();i++)
		{
			if(views.get(i) instanceof TextView)
			{
				viewstext.add( (  (TextView)views.get(i) ).getText().toString());
				Log.d(TAG, "Class" +  ( (TextView)views.get(i) ).getText().toString());
			}
		}
		return viewstext;
	}
	/**
	 * 查找某字符串
	 */

	public boolean findText(String text)
	{
		boolean result=false;
		ArrayList<String> viewsText= getAllViewsText();
		for(int i=0;i<viewsText.size();i++)
		{
			if(   viewsText.get(i).toString().indexOf(text)>=0)
			{
			result= true;
			Log.d(TAG, "找到了");
			break;
			}
		}
		if(result==false)
			Log.d(TAG,"没有找到----"+text);
		return result;
	}
	
	/**
	 * 日志输出现在时间
	 */
	public void printDateAndTime()
	{
		Date date=new Date();
		String formatDay=String.format("%tF", date);
		String formatTime=String.format("%tT", date);
		Log.d(TAG,formatDay+":"+formatTime);
	}
	/**
	 * 获取正在执行的Activity类名
	 */
	public String getCurrentActivityName()
	{
		String  activityName=solo.getCurrentActivity().getClass().getName();
		Log.d(TAG,"当前ActivityName是"+activityName);
		return activityName;
	}
	
}
