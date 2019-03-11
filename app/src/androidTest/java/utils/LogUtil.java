/**
 * 
 */

package utils;


import android.os.Build;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.Thread.UncaughtExceptionHandler;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * @author bc
 */
public class LogUtil {

    private static String LOGPATH;

    private static final int LOGFILE_MAX_SIZE = 1 * 1024 * 1024;

    
    public static void catchError() {
        Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler() {
            // 给主线程设置一个处理运行时异常的handler
            public void uncaughtException(Thread thread, final Throwable ex) {
                StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter(sw);
                ex.printStackTrace(pw);

                StringBuilder sb = new StringBuilder("");

                SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
                Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
                String date = formatter.format(curDate);

                sb.append("\n");
                sb.append(date + "     ");
                sb.append("Version code is ");
                sb.append(Build.VERSION.SDK_INT + "     ");// 设备的Android版本号
                sb.append("Model is ");
                sb.append(Build.MODEL + "\n");// 设备型号
                sb.append("error:" + sw.toString());

                copyToFile(sb.toString());
                MyApplication.getMyApplication().exit();
                android.os.Process.killProcess(android.os.Process.myPid());
            }
        });
    }

    private static void copyToFile(String msg) {
        if (MyApplication.LogFlag.equals("true")) {
            if (Environment.getExternalStorageState().equals(
                    Environment.MEDIA_MOUNTED)) {
                checkLogFile();
                File dir = new File(LOGPATH);
                File[] files = dir.listFiles();
                for (int i = 0; i < files.length; i++) {
                    if (files[i].length() < LOGFILE_MAX_SIZE) {
                        FileOutputStream out = null;
                        try {
                            out = new FileOutputStream(files[i], true);
                            out.write(msg.getBytes());
                            out.flush();
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            if (out != null) {
                                try {
                                    out.close();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        break;
                    }
                }
            }
        }
    }

    public static void LogMessage(String tag, String message) {
        if (MyApplication.LogFlag.equals("true")) {
            if (Environment.getExternalStorageState().equals(
                    Environment.MEDIA_MOUNTED)) {
                checkLogFile();
                File dir = new File(LOGPATH);
                File[] files = dir.listFiles();
                for (int i = 0; i < files.length; i++) {
                    if (files[i].length() < LOGFILE_MAX_SIZE) {
                        FileOutputStream out = null;
                        try {
                            out = new FileOutputStream(files[i], true);
                            StringBuilder sb1 = new StringBuilder();
                            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
                            Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
                            String date = formatter.format(curDate);
                            sb1.append("\n");
                            sb1.append(date);
                            sb1.append("     ");
                            sb1.append("D     ");
                            sb1.append(tag);
                            sb1.append("     ");
                            sb1.append(message);
                            out.write(sb1.toString().getBytes());
                            out.flush();
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            if (out != null) {
                                try {
                                    out.close();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        break;
                    }
                }
            }
            Log.d(tag, message);
        }
    }

    private static void checkLogFile() {
        LOGPATH = Environment.getExternalStorageDirectory() + "/" + "robotium/Log/";
        File dir = new File(LOGPATH);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File[] files = dir.listFiles();
        int size = files.length;

        if (size == 0) {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
            Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
            String date = formatter.format(curDate);
            File file = new File(LOGPATH + date + ".log");
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else if (size == 1) {
            if (files[0].length() >= LOGFILE_MAX_SIZE) {
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
                Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
                String date = formatter.format(curDate);
                try {
                    File file = new File(LOGPATH + date + ".log");
                    file.createNewFile();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else if (size == 2) {
            if (files[0].lastModified() > files[1].lastModified()) {
                if (files[0].length() >= LOGFILE_MAX_SIZE) {
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
                    Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
                    String date = formatter.format(curDate);
                    try {
                        File file = new File(LOGPATH + date + ".log");
                        file.createNewFile();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    files[1].delete();
                }
            } else if (files[0].lastModified() < files[1].lastModified()) {
                if (files[1].length() >= LOGFILE_MAX_SIZE) {
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
                    Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
                    String date = formatter.format(curDate);
                    try {
                        File file = new File(LOGPATH + date + ".log");
                        file.createNewFile();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    files[0].delete();
                }
            }
        }
    }
}
