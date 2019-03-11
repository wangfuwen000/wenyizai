package utils;

import static android.graphics.Bitmap.CompressFormat.PNG;



import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

import com.robotium.solo.Solo;



import junit.framework.Assert;
import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class SoloUtil {

	private final static int WAIT_TIMEOUT = 20000;
	private static final String LAUNCHER_ACTIVITY_FULL_CLASSNAME = "com.elong.activity.others.AppGuidActivity";

	public static View waitForView(Solo solo, String arg1) {
		return waitForView(solo, arg1, WAIT_TIMEOUT);
	}

	public static View waitForView(Solo solo, String arg1, int timeout) {
		boolean useResId = arg1 != null && arg1.contains("id/") ? true : false;
		long endTime = System.currentTimeMillis() + timeout;

		while (System.currentTimeMillis() < endTime) {
			View targetView = null;
			// it must be the same as ViewRecorder.getTargetViews()
			if (useResId) {
				targetView = getViewByResName(solo, arg1);
			} else {
				targetView = pickViewByFamilyString(solo, arg1);
			}

			if (targetView != null) {
				return targetView;
			}

			solo.sleep(500);
		}

		Assert.assertTrue(
				String.format("view not found! failed! id is:[%s]", arg1),
				false);

		return null;
	}

	private static View pickViewByFamilyString(Solo solo, String familyString) {
		ArrayList<View> views = solo.getCurrentViews();
		for (View view : views) {
			if (getFamilyString(view).equals(familyString)) {
				return view;
			}
		}
		return null;
	}

	public static String getFamilyString(View v) {
		View view = v;
		String familyString = "";
		while (view.getParent() instanceof ViewGroup) {
			ViewGroup parent = (ViewGroup) view.getParent();
			if (null == parent) {
				return rmTheLastChar(familyString);
			}
			if (Build.VERSION.SDK_INT >= 14
					&& parent
							.getClass()
							.getName()
							.equals("com.android.internal.policy.impl.PhoneWindow$DecorView")) {
			} else {
				familyString += getChildIndex(parent, view) + "-";
			}
			view = parent;
		}

		return rmTheLastChar(familyString);
	}

	private static String rmTheLastChar(String str) {
		return str.length() == 0 ? str : str.substring(0, str.length() - 1);
	}

	private static int getChildIndex(ViewGroup parent, View child) {
		int countInvisible = 0;
		for (int i = 0; i < parent.getChildCount(); i++) {
			if (parent.getChildAt(i).equals(child)) {
				return i - countInvisible;
			}
			if (parent.getChildAt(i).getVisibility() != View.VISIBLE) {
				countInvisible++;
			}
		}
		return -1;
	}

	public static View getViewByResName(Solo solo, String resId) {
		ArrayList<View> views = solo.getCurrentViews();
		for (View view : views) {
			if (getResName(solo, view).equals(resId)) {
				return view;
			}
		}
		return null;
	}

	private static String getResName(Solo solo, View view) {
		int resid = view.getId();
		if (View.NO_ID == resid) {
			return "";
		}

		try {
			String resIdString = solo.getCurrentActivity().getResources()
					.getResourceName(resid);

			return resIdString.split(":")[1].trim();
		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * clickCtrlById(String s, int t ) s: Id(hierarchyviewer) 即按键的id，字符串格式 t:
	 * time delay, 按键后停留多久(ms),如果没有，俺就后立即返回，人眼看起来不直观
	 * TARGET_PACKAGE_ID，是个宏变量，字符串格式，定义目标apk的包名 同waitForView方法
	 */

	private static final String TARGET_PACKAGE_ID = "com.dp.android.elong";

	public static int clickCtrlById(Solo solo, String s, int t) {
		int ctrl;
		View v;

		if (s.equals("")) {
			return -1;
		}
		ctrl = solo.getCurrentActivity().getResources()
				.getIdentifier(s, "id", TARGET_PACKAGE_ID);
		v = solo.getView(ctrl);
		solo.clickOnView(v);
		solo.sleep(t);

		return 0;

	}

	public static void saveFileShotName(String name) {
		String mReportDir = Environment.getExternalStorageDirectory().getPath()
				+ "/cases";
		try {
			File outputFile = new File(mReportDir, name + ".txt");
			outputFile.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 调用函数： takeScreenShot(solo.getViews().get(0), "String name");
	 * 
	 */

	public static String SCREEN_SHOTS_LOCATION = "/sdcard/robotium/";

	/*
	 * public static void takeScreenShot(View view) throws Exception {
	 * takeScreenShot(view, "default"); }
	 */
	public static void takeScreenShot(String path, View view, String name) {
		view.setDrawingCacheEnabled(true);
		view.buildDrawingCache();
		Bitmap b = view.getDrawingCache();
		FileOutputStream fos = null;
		try {
			File sddir = new File(SCREEN_SHOTS_LOCATION + "/" + path);
			if (!sddir.exists()) {
				sddir.mkdirs();
			}
			fos = new FileOutputStream(SCREEN_SHOTS_LOCATION + "/" + path + "/"
					+ name + "_" + System.currentTimeMillis() + ".jpg");
			if (fos != null) {
				b.compress(Bitmap.CompressFormat.JPEG, 90, fos);
				fos.close();
			}
		} catch (Exception e) {
		}
	}

	public static void takeScreenShot2(String path, View view, String name) {
		view.setDrawingCacheEnabled(true);
		view.buildDrawingCache();
		Bitmap b = view.getDrawingCache();
		try {

			// Log.i(LAUNCHER_ACTIVITY_FULL_CLASSNAME, "pathname is: " +
			// path+"/"+name);
			// fos1 = new FileOutputStream(path+"/"+name);
			// if (fos1 != null) {
			// b.compress(Bitmap.CompressFormat.JPEG, 90, fos1);
			// fos1.close();
			// }
			
			
			File screenshotFile = new File(path, name);
			
			OutputStream fos = null;
		    try {
		      fos = new BufferedOutputStream(new FileOutputStream(screenshotFile));
		      b.compress(PNG, 100 /* quality */, fos);
		      Chmod.chmodPlusR(screenshotFile);
		    } finally {
		    	b.recycle();
		      if (fos != null) {
		        fos.close();
		      }
		    }
			
//			return screenshotFile;

		} catch (Exception e) {
		}

		// Process process;
		// try {
		// process = Runtime.getRuntime().exec(
		// "screencap -p " + SCREEN_SHOTS_LOCATION+"/"+path+"/"+name+"_" +
		// System.currentTimeMillis() + ".png");
		// process.waitFor();
		// } catch (IOException e) {
		// e.printStackTrace();
		// } catch (InterruptedException e) {
		// e.printStackTrace();
		// }
	}

	public static void takeScreenShot2AppRoot(Solo solo, Activity activity,
			String tag) {
		try {

			File screenshotDirectory = SpoonUtil
					.obtainScreenshotDirectory(activity);
			String screenshotName = System.currentTimeMillis()
					+ SpoonUtil.NAME_SEPARATOR + tag + SpoonUtil.EXTENSION;

			File screenshotFile = new File(screenshotDirectory, screenshotName);
			Log.i(LAUNCHER_ACTIVITY_FULL_CLASSNAME, screenshotFile.getParent());

			// Spoon.screenshot(solo.getCurrentActivity(), "error");
			takeScreenShot2(screenshotFile.getParent(), solo.getViews().get(0),
					screenshotName);

		} catch (Exception e) {
			throw new RuntimeException("Unable to capture screenshot.", e);
		}
	}

	/**
	 * 我没搞懂 滚动超过一屏的listview item子项，并点击它
	 * 
	 * param index：在listview中的总的索引 param linesNum： 一屏内listview
	 * Item的行数，比如可以指定多少行，根据不同的分辨率指定不同的行数 param vIndex： 一屏内listview Item有效的行数索引
	 * param screenIndex： listview 指定行数的屏个数的索引 param screenCount： listview
	 * 指定行数的总屏数
	 * 
	 * */
	public static void scrollOutsideScreenListItem(Solo solo, int index,
			int linesNum, int vIndex, int screenIndex, int screenCount) {
		if (vIndex < (linesNum + 1)) {
			solo.clickInList(vIndex);

			solo.sleep(5000);
			vIndex++;

			solo.goBack();
			solo.sleep(3000);

			if (vIndex == linesNum) {
				DisplayMetrics outMetrics = new DisplayMetrics();
				Activity act = solo.getCurrentActivity();
				act.getWindowManager().getDefaultDisplay()
						.getMetrics(outMetrics);

				float formX = outMetrics.widthPixels / 4;
				float formY = outMetrics.heightPixels / 4;

				if (formX < 30) {
					formX = 30;
				}
				if (formY < (linesNum - 1)) {
					int temp = (index % linesNum);
					solo.scrollDownList(temp);
					solo.sleep(3000);
					if (screenIndex < screenCount) {
						screenIndex++;
					}
					vIndex = 0;
				}
			}

		}
	}

	/**
	 * * 获得dialog上面的指定类型和位置的控件，在dialog弹出时调用 *
	 * 
	 * @param solo
	 * @param viewType
	 *            View类型
	 * @param index
	 *            view位置
	 * @return 返回dialog上面的指定类型和位置的控，不存在则返回null
	 */

	public static View getViewFromDialogByIndex(Solo solo, Class viewType,
			int index) {
		ArrayList<View> dialogViews = solo.getCurrentViews();
		ArrayList<View> typeViews = new ArrayList<View>();
		for (View currentTypeView : dialogViews) {
			if (viewType.isInstance(currentTypeView)) {
				typeViews.add(currentTypeView);
			}
		}
		if (dialogViews.size() > 0) {
			return typeViews.get(index);
		} else {
			return null;
		}
	}

	/*
	 * 登陆退出
	 */

	public static void login(Solo solo, String username, String password) {
		solo.sleep(5000);
		// LinearLayout login = (LinearLayout)SoloUtil.waitForView(solo,
		// "id/login_submit");
		boolean actual = solo.searchText("个人信息");

		if (!actual) {
		
			solo.clearEditText(0);
			solo.enterText(solo.getEditText("手机号"), username);
			solo.clearEditText(1);
			solo.enterText(solo.getEditText("输入"), password);
			solo.sleep(5000);
			TextView submit = (TextView) SoloUtil.waitForView(solo,
					"id/login_submit");
			solo.clickOnView(submit);

		}
		solo.sleep(5000);
		// solo.waitForActivity("MyElongPersonalCenterActivity");
		solo.assertCurrentActivity(
				"Expected activity is MyElongPersonalCenterActivity",
				"MyElongPersonalCenterActivity");

	}

	public static void logout(Solo solo) {
		solo.waitForActivity("MyElongPersonalCenterActivity");
		// solo.assertCurrentActivity("Expected activity is MyElongPersonalCenterActivity","MyElongPersonalCenterActivity");
		solo.searchText("退出");
		solo.sleep(5000);
		TextView submit = (TextView) SoloUtil.waitForView(solo,
				"id/myelong_personal_center_head_logout");
		solo.clickOnView(submit);
		TextView okview = (TextView) SoloUtil.waitForView(solo,
				"id/dialog_positive_button");
		// solo.searchText("确    定");
		// solo.clickOnButton("确    定");
		solo.clickOnView(okview);
		solo.sleep(5000);
	}

	/**
	 * 上拉加载 加载后滑动到顶端
	 * 
	 * @param listView
	 * @return 返回是否已经到底了
	 */
	public static boolean upToRefresh(Solo solo, ListView listView,
			boolean upToTop) {
		boolean b = false;
		solo.scrollToBottom();
		solo.sleep(5000);

		int oldCount = listView.getCount();
		Log.i(LAUNCHER_ACTIVITY_FULL_CLASSNAME,
				"-------------------加载前的listView的count：" + oldCount
						+ "------------------------------");
		int[] location = new int[2];
		listView.getLocationOnScreen(location);
		solo.drag(location[0] + 10, location[0] + 10,
				listView.getBottom() - 10,
				listView.getBottom() - 10 - listView.getHeight(), 3);
		solo.sleep(5000);

		int newCount = listView.getCount();
		Log.i(LAUNCHER_ACTIVITY_FULL_CLASSNAME,
				"-------------------加载后的listView的count：" + newCount
						+ "------------------------------");
		// if (newCount != oldCount) {
		// //还没有全部加载 此处打开的话 会将所有的内容都加载出来后才停止加载
		// upToRefresh(listView);
		// }
		if (newCount == oldCount) {
			b = true;
		}

		if (upToTop) {
			solo.scrollToTop();
		}

		return b;

	}
	
	
	/* 通过ID输入文本内容
	 * enterTextById(String id,String s,int t)
	 * id:Id(hierarchviewer)获取的文本框id,字符串格式id
	 * s:需要输入的文本信息
	 * t:timedelay 触发按键后，停留多久（ms）
	*/
	public int enterTextById(Solo solo,String id,String s,int t)
	 {
	     int ctrl;
	     EditText v;

	     if( s.equals(""))
	     {
	         return -1;
	     }

	     ctrl=solo.getCurrentActivity().getResources().getIdentifier(id,"id",solo.getCurrentActivity().getPackageName());
	     v=(EditText)solo.getView(ctrl);

	     solo.enterText(v,s);
	     solo.sleep(t);
	     return 0;

	 }
	
	
	/**
	 * 重写drag方法，可以从view的任意点滑倒任意点. 从view的最底部滑倒view的最上方，用来判断区域的滑动比例，可以选择滑动的比例是多少
	 * dragPage(View view,float dragPercentFrom_X,float dragPercentFrom_Y,float
	 * dragPercentTo_X,float dragPercentTo_Y)
	 * 
	 * @param View
	 *            第一个参数是指定的view名称
	 * @param dragPercentFrom_X
	 *            第二个参数是设置起点位置x的百分比
	 * @param dragPercentFrom_Y
	 *            第三个参数是设置起点位置y的百分比
	 * @param dragPercentTo_X
	 *            第四个参数是设置终点位置x的百分比
	 * @param dragPercentTo_Y
	 *            第五个参数是设置终点位置x的百分比
	 * 
	 *            参考solo.drag(fromX, toX, fromY, toY, stepCount);
	 *            第一个参数是从起始点的横轴x点，等于view的左上角x坐标点＋view的宽度百分比；默认百分比是view的0.5倍
	 *            第二个参数是从起始点的横轴y点，等于view的左上角y坐标点＋view的高度百分百
	 *            第三个参数是从终点的横轴y点（滑动到的坐标点x轴），fromX＋view的滑动的百分百
	 *            第四个参数是从终点的横轴y点（滑动到的坐标点y轴），fromY＋view的滑动的百分百
	 *            第5个参数是拖动的步骤数，等于（屏幕滑动高度的平方+屏幕滑动高度的平方）的开根号/100
	 */
	public void dragPage(Solo solo, View view, float dragPercentFrom_X,
			float dragPercentFrom_Y, float dragPercentTo_X,
			float dragPercentTo_Y) {
		// 存储view的高度和宽度
		int[] xyLocation = new int[2];
		// 存储view的xy坐标，左下角坐标值
		view.getLocationOnScreen(xyLocation);
		// 获取view的宽度
		final int viewWidth = view.getWidth();
		// 获取view的高度
		final int viewHeight = view.getHeight();
		// 计算view的左上角的x坐标
		final float viewLeftTop_x = xyLocation[0];
		// 计算view的左上角的y坐标
		final float viewLeftTop_y = xyLocation[1];
		float FromX = viewLeftTop_x + viewWidth * dragPercentFrom_X;
		float FromY = viewLeftTop_y + viewHeight * dragPercentFrom_Y;
		float ToX = viewLeftTop_x + viewWidth * dragPercentTo_X;
		float ToY = viewLeftTop_y + viewHeight * dragPercentTo_Y;
		float MoveX = ToX - FromX;
		float MoveY = ToY - FromY;
		int StepCount = (int) ((Math.sqrt(Math.pow(MoveX, 2.0)
				+ Math.pow(MoveY, 2.0))) / 100);
		solo.drag(FromX, ToX, FromY, ToY, StepCount);
		// Log.d(TAG,
		// "viewwidth"+" :"+String.valueOf(viewWidth)+" "+"viewheight"+":"+String.valueOf(viewHeight));
		// Log.d(TAG,
		// "xylocation［0］"+" :"+String.valueOf(xyLocation[0])+" "+"xylocation[1]"+":"+String.valueOf(xyLocation[1]));
	}
	
	
}
