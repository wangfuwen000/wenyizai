package com.wenyizai.wangfuwen.wenyizai.utils;

/**
 * Created by user on 15/8/27.
 */
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Objects;

/**
 * List<String> 转换成'',格式的字符串输出
 * @author Administrator
 *
 */
public class ToStringUtils {

    /**
     * Object To json String
     *
     * @param obj
     *
     * @return json String
     */
    public static String objToJsonString(Object obj) {

        // 初始化返回值
        String json = "str_empty";

        if (obj == null) {
            return json;
        }

        StringBuilder buff = new StringBuilder();
        Field[] fields = obj.getClass().getFields();
        try {
            buff.append("[");
            buff.append("{");
            int i = 0;
            for (Field field : fields) {
                if (i != 0) {
                    buff.append(",");
                }
                buff.append(field.getName());
                buff.append(":");
                buff.append("\"");
                buff.append(field.get(obj) == null ? "" : field.get(obj));
                buff.append("\"");
                i++;
            }
            buff.append("}");
            buff.append("]");
            json = buff.toString();
        } catch (Exception e) {
            throw new RuntimeException("cause:" + e.toString());
        }
        return json;
    }

    public static  String listToString(List ss) {
        StringBuffer s = new StringBuffer("");
        if (null != ss) {
            String[] str = new String[ss.size()];
            for (int i=0; i<ss.size(); i++){
                str[i] = ss.get(i).toString();
            }
            arrayToString(str);
            s.append(arrayToString(str));
        }
        return s.toString();
    }

    /**
     * 把数组转换成'',格式的字符串输出
     * @param ss
     * @return
     */
    public static String arrayToString(String[] ss){
        StringBuffer s = new StringBuffer("");
        if(null != ss){
            for(int i=0;i<ss.length-1;i++){
                s.append("'")
                        .append(ss[i])
                        .append("'")
                        .append(",");
            }
            if(ss.length>0){
                s.append("'").append(ss[ss.length-1]).append("'");
            }
        }
        return s.toString();
    }
    /**
     *  Convert an array of strings to one string.
     *  Put the 'separator' string between each element.
     * @param a
     * @param separator
     * @return
     */
    public static String arrayToString(String[] a, String separator) {
        StringBuffer result = new StringBuffer();
        if(a==null){
            return "";
        }
        if (a.length > 0) {
            result.append(a[0]);
            for (int i=1; i<a.length; i++) {
                result.append(separator);
                result.append(a[i]);
            }
        }
        return result.toString();
    }

    public static int covertinToInt(Object value,int defailtValue){
        if(value == null || "".equals(value.toString().trim())){
            return  defailtValue;
        }
        try{
            return Integer.valueOf(value.toString());
        }catch (Exception e){
            try{
                return Double.valueOf(value.toString()).intValue();
            }catch (Exception E){
                return  defailtValue;
            }
        }
    }

    public static String inputStreamToString(InputStream inputStream) {

        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = 0;
            while (-1 != (len = inputStream.read(buffer))) {
                baos.write(buffer, 0, len);
                baos.flush();
            }
            return baos.toString("utf-8");
        } catch (IOException e) {
            e.printStackTrace();
            return  null;
        }

    }

}