package runner;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import org.xmlpull.v1.XmlPullParserFactory;
import org.xmlpull.v1.XmlSerializer;

import junit.framework.TestSuite;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.test.InstrumentationTestRunner;
import android.test.suitebuilder.TestSuiteBuilder;
import android.util.Log;

public class MyInstrumentTestRunner extends InstrumentationTestRunner {
	
	
	private static final String TAG = "com.wenyizai.wangfuwen";
	
	
	private Writer mWriter;

	private XmlSerializer mTestSuiteSerializer;

	private long mTestStarted;

	private static final String JUNIT_XML_FILE = "wangfuwentest.xml";
	
	
	private XmlSerializer newSerializer(Writer writer) {

		try {
			XmlPullParserFactory pf = XmlPullParserFactory.newInstance();
			XmlSerializer serializer = pf.newSerializer();
			serializer.setOutput(writer);
			return serializer;
		} catch (Exception e) {
			throw new RuntimeException();
		}
	}
	
	
	void startJUnitOutput(Writer writer) {
		Log.i(TAG, "MyinstrumentTestRunner:startJUnitOutput=--start");
		try {
			mWriter = writer;
			mTestSuiteSerializer = newSerializer(mWriter);
			mTestSuiteSerializer.startDocument(null, null);
			mTestSuiteSerializer.startTag(null, "testsuites");
			mTestSuiteSerializer.startTag(null, "testsuite");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		Log.i(TAG, "MyinstrumentTestRunner:startJUnitOutput--end");
	}
	
	
	private boolean isSDCardAvaliable(){

		return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
	}
	
	
	private String getTestResultDir(Context context){

	String packageName = "/" + "robotium";

	String filepath = context.getCacheDir().getPath() + packageName;

	if(android.os.Build.VERSION.SDK_INT < 8){

		if(isSDCardAvaliable()){

			filepath = Environment.getExternalStorageDirectory().getAbsolutePath()+ packageName;

			}
		}else{

		if(isSDCardAvaliable()){

			filepath = Environment.getExternalStorageDirectory().getAbsolutePath()+ packageName;

		}

		}

		return filepath;

	}
	
	
	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		
		Log.i(TAG, "MyinstrumentTestRunner:onstart");
		
		File fileRobo = new File(getTestResultDir(getTargetContext()));

			if(!fileRobo.exists()){

				fileRobo.mkdir();
			}
		
			
		if(isSDCardAvaliable()){

			File resultFile = new File(getTestResultDir(getTargetContext()),JUNIT_XML_FILE);
			try {
				startJUnitOutput(new FileWriter(resultFile));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}else{

			try {
				startJUnitOutput(new FileWriter(new File(getTargetContext().getFilesDir(), JUNIT_XML_FILE)));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
			
		super.onStart();
		
	}
	
	
	@Override
	public void sendStatus(int resultCode, Bundle results) {
		// TODO Auto-generated method stub
		
		super.sendStatus(resultCode, results);
		
		switch (resultCode) {
			
			case REPORT_VALUE_RESULT_ERROR:
			case REPORT_VALUE_RESULT_FAILURE:
			case REPORT_VALUE_RESULT_OK:
			try {
				recordTestResult(resultCode, results);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
					Log.i(TAG, "MyinstrumentTestRunner:REPORT_VALUE_RESULT_OK");

			case REPORT_VALUE_RESULT_START:
					recordTestStart(results);
					Log.i(TAG, "MyinstrumentTestRunner:REPORT_VALUE_RESULT_OK");
					
			default:
				break;
		}

	}
	
	
	void recordTestStart(Bundle results) {
		mTestStarted = System.currentTimeMillis();
	}

	void recordTestResult(int resultCode, Bundle results) throws IOException {
		float time = (System.currentTimeMillis() - mTestStarted) / 1000.0f;
		String className = results.getString(REPORT_KEY_NAME_CLASS);
		String testMethod = results.getString(REPORT_KEY_NAME_TEST);
		String stack = results.getString(REPORT_KEY_STACK);
		int current = results.getInt(REPORT_KEY_NUM_CURRENT);
		int total = results.getInt(REPORT_KEY_NUM_TOTAL);
		mTestSuiteSerializer.startTag(null, "testcase");
		mTestSuiteSerializer.attribute(null, "classname", className);
		mTestSuiteSerializer.attribute(null, "name", testMethod);
		if (resultCode != REPORT_VALUE_RESULT_OK) {
			mTestSuiteSerializer.startTag(null, "failure");
		if (stack != null) {
			String reason = stack.substring(0, stack.indexOf('\n'));
			String message = "";
			int index = reason.indexOf(':');
		if (index > -1) {
			message = reason.substring(index+1);
			reason = reason.substring(0, index);
			}
			mTestSuiteSerializer.attribute(null, "message", message);
			mTestSuiteSerializer.attribute(null, "type", reason);
			mTestSuiteSerializer.text(stack);
		}
			mTestSuiteSerializer.endTag(null, "failure");
		} else {
			mTestSuiteSerializer.attribute(null, "time", String.format("%.3f", time));
			}
			mTestSuiteSerializer.endTag(null, "testcase");
		if (current == total) {
			mTestSuiteSerializer.startTag(null, "system-out");
			mTestSuiteSerializer.endTag(null, "system-out");
			mTestSuiteSerializer.startTag(null, "system-err");
			mTestSuiteSerializer.endTag(null, "system-err");
			mTestSuiteSerializer.endTag(null, "testsuite");
			mTestSuiteSerializer.flush();
			}
	}
	

	@Override
	public void finish(int resultCode, Bundle results) {
		// TODO Auto-generated method stub
		endTestSuites();
		super.finish(resultCode, results);
	}
	
	private void endTestSuites() {
		// TODO Auto-generated method stub
		
		try {
			mTestSuiteSerializer.endTag(null, "testsuites");
			mTestSuiteSerializer.endDocument();
			mTestSuiteSerializer.flush();
			mWriter.flush();
			mWriter.close();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}
	
	
	

	
}
