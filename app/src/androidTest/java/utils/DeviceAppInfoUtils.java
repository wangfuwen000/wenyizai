package utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.net.NetworkInfo;

public class DeviceAppInfoUtils {
	
    private static String rn;
    private static String cr;
    private static String as;
    private static String uid;
    private Context context;
    
    public static final String NETWORK_TYPE_NONE = StringUtils.markStr("NONE");
    public static final String NETWORK_TYPE_WIFI = StringUtils.markStr("WIFI");
    public static final String NETWORK_TYPE_CDMA = StringUtils.markStr("3G");
    public static final String NETWORK_TYPE_GPRS = StringUtils.markStr("GPRS");
    private static final String UNICOM_CARRIER = StringUtils.markStr("46001");
    private static String os = StringUtils.markStr("Android");
    private static String ov = StringUtils.markStr(android.os.Build.VERSION.RELEASE);
    private static String dn = StringUtils.markStr(android.os.Build.MODEL);

    

    public DeviceAppInfoUtils(Context c) {
		this.context =  c;
		
	}

	public DeviceAppInfoUtils() {

	}


	/**
	 * 取出IMEI
	 * @return
	 */
	
    public  String getUid() {
    	
        TelephonyManager tm = (TelephonyManager) this.context.getSystemService(
                Context.TELEPHONY_SERVICE);
        String imei = tm.getDeviceId();
        if (StringUtils.isEmpty(imei)) {
            WifiManager wifi = (WifiManager) context.getSystemService(
                    Context.WIFI_SERVICE);
            WifiInfo info = wifi.getConnectionInfo();
            uid = StringUtils.getMD5(info.getMacAddress());
        } else {
            if (imei.equals("000000000000000")) {
                imei = "123456789123456";
            }
            uid = StringUtils.getMD5(imei);
        }
        return uid;
    }
	   
	  /**
	   * /取出IMSI，
	   * @return
	   */
	   
	
	
	   public  String getCr() {
	        if (StringUtils.isEmpty(cr)) {
	            cr = StringUtils.markStr("00000");
	            try {
	                TelephonyManager tm = (TelephonyManager) this.context
	                        .getSystemService(Context.TELEPHONY_SERVICE);
	                if (!StringUtils.isEmpty(tm.getSubscriberId())) {
	                    cr = StringUtils.markStr(tm.getSubscriberId().substring(0, 5));
	                }
	            } catch (Exception e) {
	            }
	        }
	        return cr;
	    }
	   
	   
	  /**
	   * 获取网络类型
	   * @return
	   */
	   
	   public String getAs() {
	        as = NETWORK_TYPE_NONE;
	        TelephonyManager tm = (TelephonyManager) this.context
	                .getSystemService(Context.TELEPHONY_SERVICE);
	        ConnectivityManager cm = (ConnectivityManager) context
	                .getSystemService(Context.CONNECTIVITY_SERVICE);
	        if (cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
	            as = NETWORK_TYPE_WIFI;
	        } else if (cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED) {
	            int asCode = tm.getNetworkType();
	            if (asCode != TelephonyManager.NETWORK_TYPE_UNKNOWN
	                    && asCode > TelephonyManager.NETWORK_TYPE_EDGE) {
	                as = NETWORK_TYPE_CDMA;
	            } else {
	                as = NETWORK_TYPE_GPRS;
	            }
	            if (UNICOM_CARRIER.equals(getCr())) {
	            	if (cm.getActiveNetworkInfo().getExtraInfo().toLowerCase().equals("cmnet")) {  
	            		as = "CMNET";  
	                } else {  
	                	as = "CMWAP";  
	                }  
	            }
	        }
	        return as;
	    }
	   
	   
	   
	   public  String getAllDeviceInfo() {
			DisplayMetrics metrics = this.context.getResources().getDisplayMetrics();
			rn = StringUtils.markStr(metrics.widthPixels + "*" + metrics.heightPixels);
			uid = getUid();
			cr = getCr();
			as = getAs();
	        StringBuilder str = new StringBuilder("");
	        str = str.append(",\"os\":").append(os).append(",\"ov\":")
	                .append(ov).append(",\"rn\":").append(rn).append(",\"dn\":").append(dn)
	                .append(",\"cr\":").append(cr).append(",\"as\":").append(as).append(",\"uid\":")
	                .append(StringUtils.markStr(uid)).append("}");
	        return str.toString();
	    }
	   
	   
	   
}
