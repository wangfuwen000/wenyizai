package utils;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.annotation.SuppressLint;
import android.os.Environment;
import android.util.Log;

@SuppressLint("SimpleDateFormat")
public class LogCatSettings {


	private static final String TAG = "LocatService";

	@SuppressLint("SimpleDateFormat")
	private static SimpleDateFormat myLogSdf = new SimpleDateFormat(
			"yyyy_MM_dd_HH_mm_ss");

	private static FileOutputStream fos;
	private static OutputStreamWriter writer;


	/**
	 * call LogCatSettings.savefile(date,"testruilt.txt");
	 *
	 * @param path
	 *            保存的路径
	 * @param name
	 *            文件名称
	 */

	public static void savefile(String path, String name) {
		Boolean b = FileUtils.isFileExist(path);

		if (!b) {
			File dir = FileUtils.createSDDir(path);
		}
		try {
			FileUtils.createSDFile(path, name);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 记录日志服务的基本信息 防止日志服务有错，在LogCat日志中无法查找 此日志名称为Log.log
	 * 
	 * @param msg
	 */
	private static void recordLogServiceLog(String msg) {
		if (writer != null) {
			try {
				Date time = new Date();
				writer.write(myLogSdf.format(time) + " : " + msg);
				writer.write("\n");
				writer.flush();
			} catch (IOException e) {
				e.printStackTrace();
				Log.e(TAG, e.getMessage(), e);
			}
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

	/**
	 * 每次记录日志之前先清除日志的缓存, 不然会在两个日志文件中记录重复的日志
	 */

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
				recordLogServiceLog("clearLogCache clearLogCache proc.waitFor() != 0");
			}
		} catch (Exception e) {
			Log.e(TAG, "clearLogCache failed", e);
			recordLogServiceLog("clearLogCache failed");
		} finally {
			try {
				proc.destroy();
			} catch (Exception e) {
				Log.e(TAG, "clearLogCache failed", e);
				recordLogServiceLog("clearLogCache failed");
			}
		}
	}

	public static void saveLogcat(String path, InputStream inputStream) {
		Log.i(TAG, "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
		File fw = null;
		boolean sdCardExist = Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED);

		if (sdCardExist) {
			Boolean b = FileUtils.isFileExist(path);
			Log.i(TAG, "b" + b);

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
		Log.i(TAG, "fw" + fw.getPath());

		byte[] buffer = new byte[1024];
		int bytesLeft = 5 * 1024 * 1024;

		try {
			fos = new FileOutputStream(fw);
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		while (true) {
			int read = 0;
			try {
				read = inputStream.read(buffer, 0,
						Math.min(bytesLeft, buffer.length));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (read == -1) {
				break;
			}
			try {
				fos.write(buffer, 0, read);
				fos.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			bytesLeft -= read;
		}

		if (fos != null) {
			try {
				fos.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		try {
			inputStream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// return fw.getPath();

	public static void getLogcat(String path) {

		System.out.println("--------func begin--------");
		try {
			ArrayList<String> cmdLine = new ArrayList<String>(); // 设置命令 logcat
			cmdLine.add("logcat");
			cmdLine.add("-d");// -d 读取日志
			// cmdLine.add("-f");
			// cmdLine.add(saveLogcat(path));
			// cmdLine.add("-v");
			// cmdLine.add("time");
			// cmdLine.add("-s");
			// cmdLine.add("*:D");

			// commandList.add("*:E");// 过滤所有的错误信息
			// 过滤指定TAG的信息
			// commandList.add("MyAPP:V");
			// commandList.add("*:S");

			ArrayList<String> clearLog = new ArrayList<String>(); // 设置命令 logcat
			clearLog.add("logcat");
			clearLog.add("-c");
			//
			// Process process = Runtime.getRuntime().exec(
			// cmdLine.toArray(new String[cmdLine.size()])); // 捕获日志
			//
			Process process = Runtime.getRuntime().exec("logcat -d"); // 捕获日志

			process.waitFor();

			saveLogcat(path, process.getInputStream());

			BufferedReader bufferedReader = new BufferedReader(
					new InputStreamReader(process.getInputStream())); // 将捕获内容转换为BufferedReader

			System.out.println("bufferedReader" + bufferedReader.read());

			String str = null;
			while ((str = bufferedReader.readLine()) != null) // 开始读取日志，每次读取一行
			{
				// Runtime.getRuntime().exec(clearLog.toArray(new
				// String[clearLog.size()])); //
				// 清理日志....这里至关重要，不清理的话，任何操作都将产生新的日志，代码进入死循环，直到bufferreader满
				System.out.println("xxxxgetLogxxx" + str); // 输出，在logcat中查看效果，也可以是其他操作，比如发送给服务器..
				// save logcat
				saveLogcat(path, process.getInputStream());
			}

			// if (str == null) {
			// System.out.println("--   is null   --");
			// }

		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("--------func end--------");
	}

	public static String getcasename(String clazz) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
		Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
		String date = formatter.format(curDate);
		String casename = clazz.substring(clazz.lastIndexOf(".") + 1);
		return date + "_" + casename;

	}

}
