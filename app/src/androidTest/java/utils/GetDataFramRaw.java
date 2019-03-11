package utils;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.http.util.EncodingUtils;

import android.content.Context;
import android.util.Log;

import com.wenyizai.wangfuwen.wenyizai.R;


public class GetDataFramRaw {
	
	private static final String LAUNCHER_ACTIVITY_FULL_CLASSNAME = "com.elong.activity.others.AppGuidActivity";
	private Map<String,String> map;
	private Context context;
	
	
	
	public GetDataFramRaw(Context context) {
		super();
		this.context = context;
	}



	public Map<String,String> getGcityHotel(){
		
		
		map = new HashMap<String,String>();
		String res = "";  
    	try{  
    		
    	    //得到资源中的Raw数据流 
    	    InputStream in =  context.getResources().openRawResource(R.raw.ghotelciy);
    	    //得到数据的大小 
    	    int length = in.available();        
    	    byte [] buffer = new byte[length];         
    	    //读取数据 
    	    in.read(buffer);
    	    //依test.txt的编码类型选择合适的编码，如果不调整会乱码  
    	    
    	    res = EncodingUtils.getString(buffer, "utf-8");
//    	    Log.i(LAUNCHER_ACTIVITY_FULL_CLASSNAME, res);

    	    String stringarray1[]=res.split("\n");  
    	    
    	    for(String stemp:stringarray1){  
    	    	map.put(stemp.split(",")[0],stemp.split(",")[1]);
    	    	
    	    }  
    	   
    	    
//    	    Log.i(LAUNCHER_ACTIVITY_FULL_CLASSNAME, String.valueOf(map));
    	   
    	  
    	    //关闭     
    	    in.close();
    	   
    	   }catch(Exception e){  
    	      e.printStackTrace();          
    	   }
		
    	return map;
	}

}
