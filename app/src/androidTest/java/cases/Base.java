package cases;


import android.app.Activity;
import android.os.Environment;
import com.robotium.solo.Solo;
import com.robotium.solo.Solo.Config;
import com.robotium.solo.Solo.Config.ScreenshotFileType;


import com.wenyizai.wangfuwen.wenyizai.activity.home.MainActivity;

import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import utils.LogCatSettings2;
import utils.LogUtil;
import utils.SoloUtil;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;


@RunWith(AndroidJUnit4.class)
public class Base {

	public String path = LogCatSettings2.getcasename(this.getClass().getName());
	LogCatSettings2 t = new LogCatSettings2(path);

	public Activity activity;
	public boolean b;
	public static String screenPath = "";
	public Activity currentacitivity;
	public boolean isRunning = true;


	@Rule
	public ActivityTestRule<MainActivity> activityTestRule =
			new ActivityTestRule<>(MainActivity.class);

	public Solo solo;

	@Before
	public void setUp() throws Exception {
		t.start();

		Config config = new Config();
		config.screenshotFileType = ScreenshotFileType.PNG; // 截图类型
		config.screenshotSavePath = Environment.getExternalStorageDirectory()
				+ "/Robotium/"; // 截图路径
		System.out.println(config.screenshotSavePath);
		// //config.shouldScroll = false; //是否滚屏
		solo = new Solo(InstrumentationRegistry.getInstrumentation(),
				activityTestRule.getActivity());

		// solo = new Solo(getInstrumentation(), getActivity());
		activity = activityTestRule.getActivity();


		b = false;


//		Thread t = new Thread() {
//			public void run() {
//				isRunning = true;
//				LogCatSettings2.writeLog(path);
//			}
//		};



	
	}

	@After
	public void tearDown() throws Exception {
		if (!b) {
			try {
				SoloUtil.saveFileShotName("TESTCASE_0_1");
				// SoloUtil.takeScreenShot(path,solo.getViews().get(0),
				//System.currentTimeMillis()+ "error");

				// getInstrumentation().waitForIdleSync();
				// Spoon.screenshot(solo.getCurrentActivity(), "error"); instead
				// as below:
				// SoloUtil.takeScreenShot2AppRoot(solo,activity, "error");

				LogUtil.LogMessage("TearDown", "screenPath is " + screenPath);
				SoloUtil.takeScreenShot2(screenPath, solo.getViews().get(0),
						System.currentTimeMillis() + "_" + "error" + ".png");

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}


		solo.finishOpenedActivities();
		isRunning = false;
		solo.sleep(5000);
		LogCatSettings2.setRunning(isRunning);
		solo.sleep(3000);
		LogCatSettings2.clear_log_buffer();


	}

}
