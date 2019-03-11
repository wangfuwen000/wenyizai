
package utils;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FileUtils {
    /**
     * 需要知道当前SD卡的目录，Environment.getExternalStorageDierctory()
     */

    //private static String SDPATH = "/storage/sdcard1/robotium";// Environment.getExternalStorageDirectory()+ File.separator + "robotium";
    private static String SDPATH = Environment.getExternalStorageDirectory() + "/" + "robotium/Log/";

    public static String getSDPATH() {
        return SDPATH;
    }
    
    // 判断sd卡上的文件夹是否存在
    public static boolean isFileExist(String fileName) {
        File file = new File(SDPATH + File.separator+ fileName);
        Chmod.chmodPlusR(file);
        return file.exists();
    }

    // 在sd卡上创建目录
    public  static File createSDDir(String dirName) {
        File dir = new File(SDPATH + File.separator +dirName);
        
        if (!dir.exists() && !dir.mkdirs()) {
        	try {
				throw new IllegalAccessException("Unable to create output dir: " + dir.getAbsolutePath());
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
          }
        
        // mkdir只能创建一级目录 ,mkdirs可以创建多级目录
        dir.mkdirs();
        Chmod.chmodPlusRWX(dir);
        Log.i("LocatService", "dir" + dir);
        return dir;
    }
    
    // 在sdcard卡上创建文件
    public  static File createSDFile(String path,String fileName) throws IOException {
        File file = new File(SDPATH+ File.separator + path + File.separator +fileName);
        file.createNewFile();
        Chmod.chmodPlusR(file);
        return file;
    }



    /**
     * 将一个inputstream里面的数据写入SD卡中 第一个参数为目录名 第二个参数为文件名
     */
    public  static File write2SDFromInput(String path, String fileName, InputStream inputstream) {
        File file = null;
        OutputStream output = null;
        try {
            createSDDir(path);
            //System.out.println(createSDDir(path).getParentFile());
            file = createSDFile(path,fileName);
            output = new FileOutputStream(file);
            // 4k为单位，每4K写一次
            byte buffer[] = new byte[4 * 1024];
            int temp = 0;
            while ((temp = inputstream.read(buffer)) != -1) {
                // 获取指定信,防止写入没用的信息
                output.write(buffer, 0, temp);
            }
            output.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
            	if (output !=null){
                output.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return file;
    }

    public  static void delete(String path, String name) {
        File file = new File(Environment.getExternalStorageDirectory() + path, name + ".txt");
        if (file.exists()) {
            file.delete();
        }
    }
    
    
 
}
