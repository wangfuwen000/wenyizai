package utils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.CountDownLatch;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.util.Log;
import static android.content.Context.MODE_WORLD_READABLE;
import static android.graphics.Bitmap.CompressFormat.PNG;
import static android.graphics.Bitmap.Config.ARGB_8888;

/** Utility class for capturing screenshots for Spoon. */
public final class SpoonUtil {
	static final String SPOON_SCREENSHOTS = "spoon-screenshots";
	static final String NAME_SEPARATOR = "_";
	static final String TEST_CASE_CLASS = "android.test.InstrumentationTestCase";
	static final String TEST_CASE_METHOD = "runMethod";
	static final String EXTENSION = ".jpg";
	private static final String LAUNCHER_ACTIVITY_FULL_CLASSNAME = "com.elong.activity.others.AppGuidActivity";

	/**
	 * Take a screenshot with the specified tag.
	 * 
	 * @param activity
	 *            Activity with which to capture a screenshot.
	 * @param tag
	 *            Unique tag to further identify the screenshot. Must match
	 *            [a-zA-Z0-9_-]+.
	 * @return the image file that was created
	 */

	public static File obtainScreenshotDirectory(Context context)
			throws IllegalAccessException {
		File screenshotsDir = context.getDir(SPOON_SCREENSHOTS,
				MODE_WORLD_READABLE);
		StackTraceElement testClass = findTestClassTraceElement(Thread
				.currentThread().getStackTrace());
		String className = testClass.getClassName().replaceAll(
				"[^A-Za-z0-9._-]", "_");
		File dirClass = new File(screenshotsDir, className);
		File dirMethod = new File(dirClass, testClass.getMethodName());

		createDir(dirMethod);
		return dirMethod;
	}

	public static void takeScreenshot(File file, final Activity activity)
			throws IOException {
		DisplayMetrics dm = activity.getResources().getDisplayMetrics();
		final Bitmap bitmap = Bitmap.createBitmap(dm.widthPixels,
				dm.heightPixels, ARGB_8888);

		if (Looper.myLooper() == Looper.getMainLooper()) {
			// On main thread already, Just Do It™.
			drawDecorViewToBitmap(activity, bitmap);
		} else {
			// On a background thread, post to main.
			final CountDownLatch latch = new CountDownLatch(1);
			activity.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					try {
						drawDecorViewToBitmap(activity, bitmap);
					} finally {
						latch.countDown();
					}
				}
			});
			try {
				latch.await();
			} catch (InterruptedException e) {
				String msg = "Unable to get screenshot "
						+ file.getAbsolutePath();

				throw new RuntimeException(msg, e);
			}
		}

		OutputStream fos = null;
		try {
			fos = new BufferedOutputStream(new FileOutputStream(file));
			bitmap.compress(PNG, 100 /* quality */, fos);
			Chmod.chmodPlusR(file);

		} finally {
			bitmap.recycle();
			if (fos != null) {
				fos.close();
			}
		}
	}

	private static void drawDecorViewToBitmap(Activity activity, Bitmap bitmap) {
		Canvas canvas = new Canvas(bitmap);
		activity.getWindow().getDecorView().draw(canvas);
	}

	/**
	 * Returns the test class element by looking at the method
	 * InstrumentationTestCase invokes.
	 */
	static StackTraceElement findTestClassTraceElement(StackTraceElement[] trace) {
		for (int i = trace.length - 1; i >= 0; i--) {
			StackTraceElement element = trace[i];
			if (TEST_CASE_CLASS.equals(element.getClassName()) //
					&& TEST_CASE_METHOD.equals(element.getMethodName())) {
				return trace[i - 3];
			}
		}

		throw new IllegalArgumentException("Could not find test class!");
	}

	private static void createDir(File dir) throws IllegalAccessException {
		File parent = dir.getParentFile();
		if (!parent.exists()) {
			createDir(parent);
		}
		if (!dir.exists() && !dir.mkdirs()) {
			throw new IllegalAccessException("Unable to create output dir: "
					+ dir.getAbsolutePath());
		}
		Chmod.chmodPlusRWX(dir);
	}

	private SpoonUtil() {
		// No instances.
	}
}
