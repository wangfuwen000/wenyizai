package com.wenyizai.wangfuwen.wenyizai.activity.others;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.duer.dcs.androidapp.DcsSampleBaseActivity;
import com.baidu.duer.dcs.androidapp.DcsSampleMainActivity;
//import com.baidu.duer.dcs.androidsystemimpl.PlatformFactoryImpl;
import com.baidu.duer.dcs.androidsystemimpl.webview.BaseWebView;
import com.baidu.duer.dcs.devicemodule.screen.ScreenDeviceModule;
import com.baidu.duer.dcs.devicemodule.screen.message.RenderVoiceInputTextPayload;
import com.baidu.duer.dcs.devicemodule.voiceinput.VoiceInputDeviceModule;
import com.baidu.duer.dcs.framework.DcsFramework;
import com.baidu.duer.dcs.framework.DeviceModuleFactory;
import com.baidu.duer.dcs.framework.IResponseListener;
import com.baidu.duer.dcs.http.HttpConfig;
import com.baidu.duer.dcs.oauth.api.IOauth;
import com.baidu.duer.dcs.oauth.api.OauthImpl;
import com.baidu.duer.dcs.systeminterface.IMediaPlayer;
import com.baidu.duer.dcs.systeminterface.IPlatformFactory;
import com.baidu.duer.dcs.systeminterface.IWakeUp;
import com.baidu.duer.dcs.util.CommonUtil;
import com.baidu.duer.dcs.util.FileUtil;
import com.baidu.duer.dcs.util.LogUtil;
import com.baidu.duer.dcs.util.NetWorkUtil;
import com.baidu.duer.dcs.wakeup.WakeUp;
import com.wenyizai.wangfuwen.wenyizai.R;
import com.wenyizai.wangfuwen.wenyizai.dcs.MyOauthImpl;
import com.wenyizai.wangfuwen.wenyizai.dcs.MyPlatformFactoryImpl;
import com.wenyizai.wangfuwen.wenyizai.service.MyApplication;

import java.io.File;


public class Charity extends DcsSampleBaseActivity implements View.OnClickListener{

    public static final String TAG = "Charity";
    private Button voiceButton;
    private TextView textViewTimeStopListen;
    private TextView textViewRenderVoiceInputText;
    private Button pauseOrPlayButton;
    private BaseWebView webView;
    private LinearLayout mTopLinearLayout;
    private DcsFramework dcsFramework;
    private DeviceModuleFactory deviceModuleFactory;
    private IPlatformFactory platformFactory;
    private boolean isPause = true;
    private long startTimeStopListen;
    private boolean isStopListenReceiving;
    private String mHtmlUrl;
    // 唤醒
    private WakeUp wakeUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        initView();
        initOauth();
        initFramework();
    }

    @Override
    protected void onStart() {
        super.onStart();
        initOauth();
    }



    private void initView() {
        setContentView(R.layout.activity_charity);
        Button openLogBtn = (Button) findViewById(R.id.openLogBtn);
        openLogBtn.setOnClickListener(this);
        voiceButton = (Button) findViewById(R.id.voiceBtn);
        voiceButton.setOnClickListener(this);

        textViewTimeStopListen = (TextView) findViewById(R.id.id_tv_time_0);
        textViewRenderVoiceInputText = (TextView) findViewById(R.id.id_tv_RenderVoiceInputText);
        mTopLinearLayout = (LinearLayout) findViewById(R.id.topLinearLayout);

        webView = new BaseWebView(Charity.this.getApplicationContext());
        webView.setWebViewClientListen(new BaseWebView.WebViewClientListener() {
            @Override
            public BaseWebView.LoadingWebStatus shouldOverrideUrlLoading(WebView view, String url) {
                // 拦截处理不让其点击
                return BaseWebView.LoadingWebStatus.STATUS_TRUE;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {

            }

            @Override
            public void onPageFinished(WebView view, String url) {
                if (!url.equals(mHtmlUrl) && !"about:blank".equals(mHtmlUrl)) {
                    platformFactory.getWebView().linkClicked(url);
                }

                mHtmlUrl = url;
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {

            }
        });
        mTopLinearLayout.addView(webView);

        Button mPreviousSongBtn = (Button) findViewById(R.id.previousSongBtn);
        pauseOrPlayButton = (Button) findViewById(R.id.pauseOrPlayBtn);
        Button mNextSongBtn = (Button) findViewById(R.id.nextSongBtn);
        mPreviousSongBtn.setOnClickListener(this);
        pauseOrPlayButton.setOnClickListener(this);
        mNextSongBtn.setOnClickListener(this);
    }

    private void initFramework() {
        platformFactory = new MyPlatformFactoryImpl(this);
        platformFactory.setWebView(webView);
        dcsFramework = new DcsFramework(platformFactory);
        deviceModuleFactory = dcsFramework.getDeviceModuleFactory();

        deviceModuleFactory.createVoiceOutputDeviceModule();
        deviceModuleFactory.createVoiceInputDeviceModule();
        deviceModuleFactory.getVoiceInputDeviceModule().addVoiceInputListener(
                new VoiceInputDeviceModule.IVoiceInputListener() {
                    @Override
                    public void onStartRecord() {
                        LogUtil.d(TAG, "onStartRecord");
                        startRecording();
                    }

                    @Override
                    public void onFinishRecord() {
                        LogUtil.d(TAG, "onFinishRecord");
                        stopRecording();
                    }

                    public void onSucceed(int statusCode) {
                        LogUtil.d(TAG, "onSucceed-statusCode:" + statusCode);
                        if (statusCode != 200) {
                            stopRecording();
                            Toast.makeText(Charity.this,
                                    getResources().getString(com.baidu.duer.dcs.R.string.voice_err_msg),
                                    Toast.LENGTH_SHORT)
                                    .show();
                        }
                    }

                    @Override
                    public void onFailed(String errorMessage) {
                        LogUtil.d(TAG, "onFailed-errorMessage:" + errorMessage);
                        stopRecording();
                        Toast.makeText(Charity.this,
                                getResources().getString(com.baidu.duer.dcs.R.string.voice_err_msg),
                                Toast.LENGTH_SHORT)
                                .show();
                    }
                });

        deviceModuleFactory.createAlertsDeviceModule();

        deviceModuleFactory.createAudioPlayerDeviceModule();
        deviceModuleFactory.getAudioPlayerDeviceModule().addAudioPlayListener(
                new IMediaPlayer.SimpleMediaPlayerListener() {
                    @Override
                    public void onPaused() {
                        super.onPaused();
                        pauseOrPlayButton.setText(getResources().getString(com.baidu.duer.dcs.R.string.audio_paused));
                        isPause = true;
                    }

                    @Override
                    public void onPlaying() {
                        super.onPlaying();
                        pauseOrPlayButton.setText(getResources().getString(com.baidu.duer.dcs.R.string.audio_playing));
                        isPause = false;
                    }

                    @Override
                    public void onCompletion() {
                        super.onCompletion();
                        pauseOrPlayButton.setText(getResources().getString(com.baidu.duer.dcs.R.string.audio_default));
                        isPause = false;
                    }

                    @Override
                    public void onStopped() {
                        super.onStopped();
                        pauseOrPlayButton.setText(getResources().getString(com.baidu.duer.dcs.R.string.audio_default));
                        isPause = true;
                    }
                });

        deviceModuleFactory.createSystemDeviceModule();
        deviceModuleFactory.createSpeakControllerDeviceModule();
        deviceModuleFactory.createPlaybackControllerDeviceModule();
        deviceModuleFactory.createScreenDeviceModule();
        deviceModuleFactory.getScreenDeviceModule()
                .addRenderVoiceInputTextListener(new ScreenDeviceModule.IRenderVoiceInputTextListener() {
                    @Override
                    public void onRenderVoiceInputText(RenderVoiceInputTextPayload payload) {
                        textViewRenderVoiceInputText.setText(payload.text);
                    }

                });
        // init唤醒
        wakeUp = new WakeUp(platformFactory.getWakeUp(),
                platformFactory.getAudioRecord());
        wakeUp.addWakeUpListener(wakeUpListener);
        // 开始录音，监听是否说了唤醒词
        wakeUp.startWakeUp();
    }

    private IWakeUp.IWakeUpListener wakeUpListener = new IWakeUp.IWakeUpListener() {
        @Override
        public void onWakeUpSucceed() {
            Toast.makeText(Charity.this,
                    getResources().getString(com.baidu.duer.dcs.R.string.wakeup_succeed),
                    Toast.LENGTH_SHORT)
                    .show();
            voiceButton.performClick();
        }
    };


    private void initOauth() {
        IOauth baiduOauth = new MyOauthImpl();
        Log.i("xxxxxxxxx","initOauth");
        if (baiduOauth.isSessionValid()) {
            HttpConfig.setAccessToken(baiduOauth.getAccessToken());
        } else {
            Log.i("xxxxxxxxx","authorize");
            baiduOauth.authorize();
        }
        Log.i("xxxxxxxxx","xxxxxx");
    }

    private void stopRecording() {
        wakeUp.startWakeUp();
        isStopListenReceiving = false;
        voiceButton.setText(getResources().getString(com.baidu.duer.dcs.R.string.stop_record));
        long t = System.currentTimeMillis() - startTimeStopListen;
        textViewTimeStopListen.setText(getResources().getString(com.baidu.duer.dcs.R.string.time_record, t));
    }

    private void startRecording() {
        wakeUp.stopWakeUp();
        isStopListenReceiving = true;
        deviceModuleFactory.getSystemProvider().userActivity();
        voiceButton.setText(getResources().getString(com.baidu.duer.dcs.R.string.start_record));
        textViewTimeStopListen.setText("");
        textViewRenderVoiceInputText.setText("");
    }

    private void doUserActivity() {
        deviceModuleFactory.getSystemProvider().userActivity();
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.voiceBtn) {
            if (!NetWorkUtil.isNetworkConnected(this)) {
                Toast.makeText(this,
                        getResources().getString(R.string.err_net_msg),
                        Toast.LENGTH_SHORT).show();
                return;
            }
            if (CommonUtil.isFastDoubleClick()) {
                return;
            }
            if (isStopListenReceiving) {
                platformFactory.getVoiceInput().stopRecord();
                isStopListenReceiving = false;
                return;
            }
            isStopListenReceiving = true;
            startTimeStopListen = System.currentTimeMillis();
            platformFactory.getVoiceInput().startRecord();
            doUserActivity();

        } else if (i == R.id.openLogBtn) {
            openAssignFolder(FileUtil.getLogFilePath());

        } else if (i == R.id.previousSongBtn) {
            platformFactory.getPlayback().previous(nextPreResponseListener);
            doUserActivity();

        } else if (i == R.id.nextSongBtn) {
            platformFactory.getPlayback().next(nextPreResponseListener);
            doUserActivity();

        } else if (i == R.id.pauseOrPlayBtn) {
            if (isPause) {
                platformFactory.getPlayback().play(playPauseResponseListener);
            } else {
                platformFactory.getPlayback().pause(playPauseResponseListener);
            }
            doUserActivity();

        } else {
        }
    }


    private IResponseListener playPauseResponseListener = new IResponseListener() {
        @Override
        public void onSucceed(int statusCode) {
            if (statusCode == 204) {
                Toast.makeText(Charity.this,
                        getResources().getString(com.baidu.duer.dcs.R.string.no_directive),
                        Toast.LENGTH_SHORT)
                        .show();
            }
        }

        @Override
        public void onFailed(String errorMessage) {
            Toast.makeText(Charity.this,
                    getResources().getString(com.baidu.duer.dcs.R.string.request_error),
                    Toast.LENGTH_SHORT)
                    .show();
        }
    };

    private IResponseListener nextPreResponseListener = new IResponseListener() {
        @Override
        public void onSucceed(int statusCode) {
            if (statusCode == 204) {
                Toast.makeText(Charity.this,
                        getResources().getString(com.baidu.duer.dcs.R.string.no_audio),
                        Toast.LENGTH_SHORT)
                        .show();
            }
        }

        @Override
        public void onFailed(String errorMessage) {
            Toast.makeText(Charity.this,
                    getResources().getString(com.baidu.duer.dcs.R.string.request_error),
                    Toast.LENGTH_SHORT)
                    .show();
        }
    };



    /**
     * 打开日志
     *
     * @param path 文件的绝对路径
     */
    private void openAssignFolder(String path) {
        File file = new File(path);
        if (!file.exists()) {
            Toast.makeText(Charity.this,
                    getResources().getString(com.baidu.duer.dcs.R.string.no_log),
                    Toast.LENGTH_SHORT)
                    .show();
            return;
        }
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(Uri.fromFile(file), "text/plain");
        try {
            startActivity(Intent.createChooser(intent,
                    getResources().getString(com.baidu.duer.dcs.R.string.open_file_title)));
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 先remove listener  停止唤醒,释放资源
        wakeUp.removeWakeUpListener(wakeUpListener);
        wakeUp.stopWakeUp();
        wakeUp.releaseWakeUp();

        if (dcsFramework != null) {
            dcsFramework.release();
        }
        webView.setWebViewClientListen(null);
        mTopLinearLayout.removeView(webView);
        webView.removeAllViews();
        webView.destroy();
    }


}
