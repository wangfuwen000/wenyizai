package utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


import android.annotation.SuppressLint;
import android.os.Environment;
import android.util.Log;


/**
 * 
 * @author BC wang
 * 
 */

@SuppressLint("SimpleDateFormat")
public class LogCatSettings2 extends Thread {
	private static final String TAG = "LogService";
	public static boolean isRunning = true;
	public static String path;
	
	public static boolean isRunning() {
		return isRunning;
	}

	public static void setRunning(boolean isRunning) {
		LogCatSettings2.isRunning = isRunning;
	}
	
	
	public LogCatSettings2(String path) {
		// TODO Auto-generated constructor stub
		this.path = path;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		isRunning = true;
		LogCatSettings2.writeLog(this.path);
		super.run();
	}

	public static void writeLog(String path) {

		List<String> progs = new ArrayList<String>();

		progs.add("logcat");
//		progs.add("-d");
		
		// progs.add("-f");
		// progs.add(saveLogcat(path));
		 progs.add("-v");
		 progs.add("time");
		 progs.add("-s");
		 progs.add("*:I");

		// commandList.add("*:E");// 过滤所有的错误信息
		// 过滤指定TAG的信息
		// commandList.add("MyAPP:V");
		// commandList.add("*:S");

		Process logcatProc;
		try {
			logcatProc = Runtime.getRuntime().exec(
					progs.toArray(new String[0]));
		} catch (IOException e2) {
			return;
		}

		BufferedReader mReader = new BufferedReader(new InputStreamReader(
				logcatProc.getInputStream()), 1024);

		StringBuffer sb = new StringBuffer();
		while (isRunning) {
			String line = null;
			try {
				line = mReader.readLine();
			} catch (IOException e1) {
				continue;
			}
			if (line != null) {
				sb.append(line);
				sb.append('\n');
			}
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				continue;
			}
			System.out.println("-------------");
		}

		if (mReader != null) {
			try {
				mReader.close();
				mReader = null;
			} catch (IOException e) {
			}
		}
		System.out.println("------------over-------"+sb.toString());
		writeLogToSdcard(path,sb.toString());

	}

	
	
	public static void writeLogToSdcard(String path,String content) {
		
		File fw = null;
		boolean sdCardExist = Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED);

		if (sdCardExist) {
			Boolean b = FileUtils.isFileExist(path);
			Log.i(TAG, "b=" + b);

			if (!b) {
				FileUtils.createSDDir(path);
			}

			try {
				fw = FileUtils.createSDFile(path, "logcat.log");

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		Log.i(TAG, "logcat" + fw.getPath());
		writeLog(fw.getPath(), content);
	}

	public static void writeLog(String fileName, String content) {
		File f = new File(fileName);
		try {
			FileWriter writer = new FileWriter(fileName, true);
			writer.write(content);
			writer.close();
		} catch (IOException e) {
		}
	}


	static class StreamConsumer extends Thread {
		InputStream is;
		List<String> list;

		StreamConsumer(InputStream is) {
			this.is = is;
		}

		StreamConsumer(InputStream is, List<String> list) {
			this.is = is;
			this.list = list;
		}

		public void run() {
			try {
				InputStreamReader isr = new InputStreamReader(is);
				BufferedReader br = new BufferedReader(isr);

				String line = null;
				while ((line = br.readLine()) != null) {
					if (list != null) {
						list.add(line);
					}
				}
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}
	}

	public static void clear_log_buffer() {
		Process proc = null;
		List<String> commandList = new ArrayList<String>();
		// commandList.add("adb");
		// commandList.add("shell");
		commandList.add("logcat");
		commandList.add("-c");
		try {
			proc = Runtime.getRuntime().exec(
					commandList.toArray(new String[commandList.size()]));
			StreamConsumer errorGobbler = new StreamConsumer(
					proc.getErrorStream());
			StreamConsumer outputGobbler = new StreamConsumer(
					proc.getInputStream());

			errorGobbler.start();
			outputGobbler.start();
			if (proc.waitFor() != 0) {
				Log.e(TAG, " clearLogCache proc.waitFor() != 0");

			}
		} catch (Exception e) {
			Log.e(TAG, "clearLogCache failed", e);

		} finally {
			try {
				Log.i(TAG, "clearLogCache destroy");
				proc.destroy();
			} catch (Exception e) {
				Log.e(TAG, "clearLogCache failed", e);

			}
		}
	}

	public static String getcasename(String clazz) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
		Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
		String date = formatter.format(curDate);
		String casename = clazz.substring(clazz.lastIndexOf(".") + 1);
		return date + "_" + casename;

	}

}
