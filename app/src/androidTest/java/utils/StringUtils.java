package utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.util.Log;

public class StringUtils {

	public static final String STR_EMPTY = "";

	public static boolean isEmpty(String content) {
		if (content == null || STR_EMPTY.equals(content)) {
			return true;
		}
		return false;
	}

	public static String markStr(String str) {
		if (isEmpty(str)) {
			str = STR_EMPTY;
		}
		return "\"" + str + "\"";
	}

	public static String getMD5(String content) {
		try {
			MessageDigest digest = MessageDigest.getInstance("MD5");
			digest.update(content.getBytes());
			return getHashString(digest);

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}

	private static String getHashString(MessageDigest digest) {
		StringBuilder builder = new StringBuilder();
		for (byte b : digest.digest()) {
			builder.append(Integer.toHexString((b >> 4) & 0xf));
			builder.append(Integer.toHexString(b & 0xf));
		}
		return builder.toString();
	}

	/**
	 * 
	 * @param str
	 *            需要过滤的字符串
	 * @return
	 * @Description:过滤数字以外的字符
	 */
	public static String filterUnNumber(String str) {
		// 只允非数字
		String regEx = "[^0-9]";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(str);
		// 替换与模式匹配的所有字符（即非数字的字符将被""替换）
		return m.replaceAll("").trim();
	}

	/*
	 * 随机成成0到f-1范围内的整数
	 */

	public static int RandomGetNum(int f) {
		Random rand = new Random();
		int i;
		i = rand.nextInt(f);
		i = Math.abs(rand.nextInt() % f);
		System.out.println(i);
		return i;
	}


	public static String Shuffle() {
		// TODO Auto-generated method stub
		StringBuilder sb = new StringBuilder();
		// List<String> list = Arrays.asList(str);
		List<String> list = Arrays.asList("a", "b", "c", "d", "e", "f", "g");
		System.out.println("打乱前:");
		Log.i("LocatService", "list" + list.toString());
		Collections.shuffle(list);
		System.out.println("打乱后:");
		Log.i("LocatService", "list" + list.toString());

		for (int i = 0; i < list.size(); i++) {
			sb.append(list.get(i));
		}
		Log.i("LocatService", "sb" + sb.toString());
		return sb.toString();
	}

}