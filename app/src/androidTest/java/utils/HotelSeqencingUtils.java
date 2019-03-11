package utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import com.robotium.solo.Solo;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.net.NetworkInfo;

public class HotelSeqencingUtils {

	private static final String LAUNCHER_ACTIVITY_FULL_CLASSNAME = "com.elong.activity.others.HomeActivity";

	// LinearLayout parentView = (LinearLayout) solo.getText("起").getParent();
	// TextView TextPriceView = (TextView) parentView.getChildAt(1);
	// TextView TextFromView = (TextView) parentView.getChildAt(2);
	// Log.i(LAUNCHER_ACTIVITY_FULL_CLASSNAME,TextPriceView.getText().toString());
	// Log.i(LAUNCHER_ACTIVITY_FULL_CLASSNAME,TextFromView.getText().toString());

	/*
	 * 获取酒店的个数 id 是标题逇id："id/common_head_title"
	 */
	public static int getHotelCount(Solo solo, String s) {
		RelativeLayout parentView = (RelativeLayout) solo.getText("家")
				.getParent();
		TextView TextView = (TextView) parentView.getChildAt(1);
		
		// String resIdString =
		// solo.getCurrentActivity().getResources().getResourceName(manyHotel.getId());
		// Log.i(LAUNCHER_ACTIVITY_FULL_CLASSNAME,resIdString.split(":")[1].trim());
	
		String str = TextView.getText().toString();
		Log.i(LAUNCHER_ACTIVITY_FULL_CLASSNAME, "酒店个数是  "+ str);
		
		String strNum = StringUtils.filterUnNumber(str);
		
		Log.i(LAUNCHER_ACTIVITY_FULL_CLASSNAME, strNum);
		return Integer.parseInt(strNum);
	}

	public static ArrayList<String> priceAsSort(Solo solo, int count) {
		ArrayList priceList = new ArrayList();
		for (int i = 1; i <= count - 2; i++) {
			solo.scrollListToLine(0, i);
			solo.sleep(3000);
			LinearLayout parentView = (LinearLayout) solo.getText("起")
					.getParent();
			TextView TextPriceView = (TextView) parentView.getChildAt(1);
			TextView TextFromView = (TextView) parentView.getChildAt(2);
			Log.i(LAUNCHER_ACTIVITY_FULL_CLASSNAME, TextPriceView.getText()
					.toString());
			Log.i(LAUNCHER_ACTIVITY_FULL_CLASSNAME, TextFromView.getText()
					.toString());

			priceList.add(TextPriceView.getText().toString());

		}

		return priceList;
	}

	public static ArrayList<String> rpAsSort(Solo solo, int count) {
		ArrayList rpList = new ArrayList();
		for (int i = 1; i <= count - 2; i++) {
			solo.scrollListToLine(0, i);
			TextView rpRate = (TextView) SoloUtil.waitForView(solo,
					"id/hotel_listitem_totalcomment");
			Log.i(LAUNCHER_ACTIVITY_FULL_CLASSNAME, rpRate.getText().toString());
			String rp = StringUtils.filterUnNumber(rpRate.getText().toString());
			Log.i(LAUNCHER_ACTIVITY_FULL_CLASSNAME, rp);
			rpList.add(rp);
		}

		return rpList;
	}

	// "id/hotel_listitem_area"
	public static ArrayList<String> distanceAsSort(Solo solo, int count,
			String id) {
		ArrayList distanceList = new ArrayList();
		for (int i = 1; i <= count - 2; i++) {
			solo.scrollListToLine(0, i);
			solo.sleep(3000);
			TextView ds = (TextView) SoloUtil.waitForView(solo, id);
			LogUtil.LogMessage("every distance of hotel: ", ds.getText()
					.toString());
			String rp = StringUtils.filterUnNumber(ds.getText().toString());
			distanceList.add(rp);
		}

		return distanceList;
	}

	// hotel_listitem_starcode

	public static ArrayList<String> startBox(Solo solo, int count, String id) {

		ArrayList rpList = new ArrayList();
		for (int i = 0; i <= count - 2; i++) {
			solo.scrollListToLine(0, i);
			solo.sleep(3000);
			TextView totalcomment = (TextView) SoloUtil.waitForView(solo, id);
			Log.i(LAUNCHER_ACTIVITY_FULL_CLASSNAME, totalcomment.getText()
					.toString());
			Log.i(LAUNCHER_ACTIVITY_FULL_CLASSNAME, totalcomment.getText()
					.toString().substring(0, 2));
			rpList.add(totalcomment.getText().toString().substring(0, 2));
		}

		return rpList;
	}

	public static ArrayList<String> princeOverNum(Solo solo, int count) {
		ArrayList priceList = new ArrayList();
		for (int i = 0; i <= count - 2; i++) {
			solo.scrollListToLine(0, i);
			solo.sleep(3000);
			// LinearLayout parentView = (LinearLayout)
			// solo.getText("起",true).getParent();
			// TextView TextPriceView = (TextView) parentView.getChildAt(1);
			// TextView TextFromView = (TextView) parentView.getChildAt(2);

			// Log.i(LAUNCHER_ACTIVITY_FULL_CLASSNAME,TextPriceView.getText().toString());
			// Log.i(LAUNCHER_ACTIVITY_FULL_CLASSNAME,TextFromView.getText().toString());

			// priceList.add(TextPriceView.getText().toString());

			TextView TextPriceView = (TextView) SoloUtil.waitForView(solo,
					"id/hotel_listitem_price");
			Log.i(LAUNCHER_ACTIVITY_FULL_CLASSNAME, TextPriceView.getText()
					.toString());

			priceList.add(TextPriceView.getText().toString());
		}

		return priceList;
	}

	/*
	 * Function is to get the hotle name by "id/hotel_listitem_name" First
	 * @param Solo is robotium Second @param page is i want get how many hotel
	 * from page list third @param c is how many there is hotel from list
	 */
	// hotel_listitem_name

	public static ArrayList<String> getHotlelBrand(Solo solo, int count, int c,
			String id) {
		ArrayList<String> brandList = new ArrayList<String>();

		ListView listview = solo.getCurrentViews(ListView.class).get(0);
		Log.i(LAUNCHER_ACTIVITY_FULL_CLASSNAME, "count" + String.valueOf(count)
				+ " c= " + String.valueOf(c));
		solo.sleep(3000);

		if (c > 0) {
			int n = c >= count ? count : c;
			Log.i(LAUNCHER_ACTIVITY_FULL_CLASSNAME, "n is " + String.valueOf(n));
			solo.sleep(3000);

			for (int i = 1; i <= n; i++) {
				solo.scrollListToLine(0, i);
				if (everyBrand(solo, listview, 1, id) != null) {
					brandList.add(everyBrand(solo, listview, 1, id));
				}

				Log.i(LAUNCHER_ACTIVITY_FULL_CLASSNAME,
						"最后一个item位置 "
								+ String.valueOf(listview
										.getLastVisiblePosition()));
				if (listview.getLastVisiblePosition() >= c) {

					if (everyBrand(solo, listview, 1, id) != null) {
						brandList.add(everyBrand(solo, listview, 1, id));
					}
					if (everyBrand(solo, listview, 2, id) != null) {
						brandList.add(everyBrand(solo, listview, 2, id));
					}
					if (everyBrand(solo, listview, 3, id) != null) {
						brandList.add(everyBrand(solo, listview, 3, id));
					}

					break;
				}

			}
			solo.sleep(5000);

		}
		Log.i(LAUNCHER_ACTIVITY_FULL_CLASSNAME, String.valueOf(brandList));

		return brandList;
	}

	// hotel_listitem_name
	public static String everyBrand(Solo solo, ListView listview, int i,
			String id) {
		String s = null;

		LinearLayout lf = (LinearLayout) listview.getChildAt(i);

		TextView t = (TextView) SoloUtil.waitForView(solo, id);
		if (t.isShown()) {

			// Log.i(LAUNCHER_ACTIVITY_FULL_CLASSNAME,"酒店名称的view " +
			// t.toString());
			TextView im = (TextView) lf.findViewById(t.getId());
			Log.i(LAUNCHER_ACTIVITY_FULL_CLASSNAME, "酒店的名称是  " + id + " "
					+ im.getText().toString());
			s = im.getText().toString();
			solo.sleep(3000);
		}
		return s;

	}

	public static ArrayList<String> getTextListitemIdbyId(Solo solo, String id) {
		ArrayList<String> dataList = new ArrayList<String>();
		for (int i = 1; i <= 10; i++) {
			solo.sleep(3000);
			solo.scrollListToLine(0, i);
			TextView trainstart = (TextView) SoloUtil.waitForView(solo, id);
			Log.i(LAUNCHER_ACTIVITY_FULL_CLASSNAME, trainstart.getText()
					.toString());
			dataList.add(trainstart.getText().toString());

		}

		Log.i(LAUNCHER_ACTIVITY_FULL_CLASSNAME, String.valueOf(dataList));
		return dataList;
	}

	public static boolean isPaymentType(Solo solo, int count) {
		boolean b = true;
		for (int i = 0; i <= count - 2; i++) {
			solo.scrollListToLine(0, i);
			solo.clickInList(2);
			solo.goBack();
		}

		return b;
	}

	/*
	 * 判断list是否相等
	 */

	public static boolean equals(ArrayList<String> a, ArrayList<String> b) {

		// int i =a.toString().compareTo(b.toString());
		// if (i)
		Log.i(LAUNCHER_ACTIVITY_FULL_CLASSNAME, "myequals a=" + a.toString());
		Log.i(LAUNCHER_ACTIVITY_FULL_CLASSNAME, "myequals b=" + b.toString());
		int length = a.size();
		for (int i = 0; i < length; i++) {
			String o1 = a.get(i);
			String o2 = b.get(i);
			Log.i(LAUNCHER_ACTIVITY_FULL_CLASSNAME, "a" + o1);
			Log.i(LAUNCHER_ACTIVITY_FULL_CLASSNAME, "b" + o2);
			if (!o1.equals(o2))
				return false;
		}
		return true;

	}

	/*
	 * ArrayList<String> b = new ArrayList<String>(); for (String ai: a){
	 * b.add(ai); }
	 * 
	 * Log.i(LAUNCHER_ACTIVITY_FULL_CLASSNAME, "a = " + String.valueOf(a));
	 * 
	 * Collections.sort(a, new ArrySortUtil(1)); boolean t =
	 * HotelSeqencingUtils.equals(b, a); Log.i(LAUNCHER_ACTIVITY_FULL_CLASSNAME,
	 * String.valueOf(t));
	 * 
	 * 
	 * assertTrue(t);
	 */
	public static int isUpSort(ArrayList<String> a) {
		int length = 0;
		if (a != null) {
			length = a.size();
		}

		int b = 0;

		if (a != null && length > 0) {

			for (int i = 0; i < length - 1; i++) {
				for (int j = i + 1; j <= length - 1; j++) {
					if (a.get(i).compareTo(a.get(j)) <= 0) {
						b = 0;
					} else {
						b = j;
						break;
					}
				}
			}
		}

		return b;
	}

	public static int isDownSort(ArrayList<String> a) {
		int length = 0;
		if (a != null) {
			length = a.size();
		}
		int b = 0;

		if (a != null && length > 0) {

			for (int i = 0; i < length - 1; i++) {
				for (int j = i + 1; j <= length - 1; j++) {
					if (a.get(i).compareTo(a.get(j)) >= 0) {
						b = 0;
					} else {
						b = j;
						break;
					}
				}
			}
		}

		return b;
	}

	// 非递归的方法，两个两个判断，如果有后面的大于前面的就返回false
	public static boolean Arraydown(int a[], int n) {
		int i;
		for (i = 0; i < n - 2; i++) {
			Log.i(LAUNCHER_ACTIVITY_FULL_CLASSNAME, "a[i] = " + a[i]);
			Log.i(LAUNCHER_ACTIVITY_FULL_CLASSNAME, "a[i+1] = " + a[i + 1]);
			if (a[i] >= a[i + 1]) {
				return true;
			}
		}
		return false;
	}

	// 非递归的方法，两个两个判断，如果有后面的大于前面的就返回false
	public static boolean ArrayUp(int a[], int n) {
		int i;
		for (i = 0; i < n - 2; i++) {
			if (a[i] <= a[i + 1]) {
				return true;
			}
		}
		return false;
	}
}
