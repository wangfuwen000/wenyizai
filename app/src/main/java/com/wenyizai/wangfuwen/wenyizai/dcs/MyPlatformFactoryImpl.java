package com.wenyizai.wangfuwen.wenyizai.dcs;

import android.content.Context;
import android.os.Looper;

import com.baidu.duer.dcs.androidsystemimpl.AudioRecordThread;
import com.baidu.duer.dcs.androidsystemimpl.HandlerImpl;
import com.baidu.duer.dcs.androidsystemimpl.alert.AlertsFileDataStoreImpl;
import com.baidu.duer.dcs.androidsystemimpl.audioinput.AudioVoiceInputImpl;
import com.baidu.duer.dcs.androidsystemimpl.playbackcontroller.IPlaybackControllerImpl;
import com.baidu.duer.dcs.androidsystemimpl.player.AudioTrackPlayerImpl;
import com.baidu.duer.dcs.androidsystemimpl.player.MediaPlayerImpl;
import com.baidu.duer.dcs.androidsystemimpl.wakeup.WakeUpImpl;
import com.baidu.duer.dcs.systeminterface.IAlertsDataStore;
import com.baidu.duer.dcs.systeminterface.IAudioInput;
import com.baidu.duer.dcs.systeminterface.IAudioRecord;
import com.baidu.duer.dcs.systeminterface.IHandler;
import com.baidu.duer.dcs.systeminterface.IMediaPlayer;
import com.baidu.duer.dcs.systeminterface.IPlatformFactory;
import com.baidu.duer.dcs.systeminterface.IPlaybackController;
import com.baidu.duer.dcs.systeminterface.IWakeUp;
import com.baidu.duer.dcs.systeminterface.IWebView;

import java.util.concurrent.LinkedBlockingDeque;

/**
 * Created by wangfuwen on 2017/11/21.
 */

public class MyPlatformFactoryImpl implements IPlatformFactory {
    private IHandler mainHandler;
    private IAudioInput voiceInput;
    private IWebView webView;
    private IPlaybackController playback;
    private Context context;
    private IAudioRecord audioRecord;
    private LinkedBlockingDeque<byte[]> linkedBlockingDeque = new LinkedBlockingDeque<>();

    public MyPlatformFactoryImpl(Context context) {
        this.context = context.getApplicationContext();
    }

    @Override
    public IHandler createHandler() {
        return new HandlerImpl();
    }

    @Override
    public IHandler getMainHandler() {
        if (mainHandler == null) {
            mainHandler = new HandlerImpl(Looper.getMainLooper());
        }

        return mainHandler;
    }

    @Override
    public IAudioRecord getAudioRecord() {
        if (audioRecord == null) {
            audioRecord = new AudioRecordThread(linkedBlockingDeque);
        }
        return audioRecord;
    }

    @Override
    public IWakeUp getWakeUp() {
        return new WakeUpImpl(context, linkedBlockingDeque);
    }

    @Override
    public IAudioInput getVoiceInput() {
        if (voiceInput == null) {
            voiceInput = new AudioVoiceInputImpl(linkedBlockingDeque);
        }

        return voiceInput;
    }

    @Override
    public IMediaPlayer createMediaPlayer() {
        return new MyMediaPlayerImpl();
    }

    @Override
    public IMediaPlayer createAudioTrackPlayer() {
        return new AudioTrackPlayerImpl();
    }

    public IAlertsDataStore createAlertsDataStore() {
        return new AlertsFileDataStoreImpl();
    }

    @Override
    public IWebView getWebView() {
        return webView;
    }

    @Override
    public IPlaybackController getPlayback() {
        if (playback == null) {
            playback = new IPlaybackControllerImpl();
        }

        return playback;
    }

    public void setWebView(IWebView webView) {
        this.webView = webView;
    }
}