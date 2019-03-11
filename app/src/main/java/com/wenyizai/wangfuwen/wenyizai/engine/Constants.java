package com.wenyizai.wangfuwen.wenyizai.engine;


import com.wenyizai.wangfuwen.wenyizai.R;

public final class Constants {

	public static final String[] IMAGES = new String[] {
			// Heavy images
			
			// Light images
		
			// Special cases
			"http://cdn.urbanislandz.com/wp-content/uploads/2011/10/MMSposter-large.jpg", // Very large image
			"http://www.ioncannon.net/wp-content/uploads/2011/06/test9.webp", // WebP image
			"http://4.bp.blogspot.com/-LEvwF87bbyU/Uicaskm-g6I/AAAAAAAAZ2c/V-WZZAvFg5I/s800/Pesto+Guacamole+500w+0268.jpg", // Image with "Mark has been invalidated" problem
			"file:///sdcard/Universal Image Loader @#&=+-_.,!()~'%20.png", // Image from SD card with encoded symbols
			"assets://Living Things @#&=+-_.,!()~'%20.jpg", // Image from assets
			"drawable://" + R.drawable.bird, // Image from drawables
			"http://upload.wikimedia.org/wikipedia/ru/b/b6/Как_кот_с_мышами_воевал.png", // Link with UTF-8
			"https://www.eff.org/sites/default/files/chrome150_0.jpg", // Image from HTTPS
			"http://bit.ly/soBiXr", // Redirect link
			"http://img001.us.expono.com/100001/100001-1bc30-2d736f_m.jpg", // EXIF
			"", // Empty link
			"http://wrong.site.com/corruptedLink", // Wrong link
	};

	private Constants() {
	}

	public static class Config {
		public static final boolean DEVELOPER_MODE = false;
	}
	
	public static class Extra {
		public static final String FRAGMENT_INDEX = "com.nostra13.example.universalimageloader.FRAGMENT_INDEX";
		public static final String IMAGE_POSITION = "com.nostra13.example.universalimageloader.IMAGE_POSITION";
	}

	public static String cishan = "http://123.57.11.138:8080/cishan";
}

