package com.example.xunfei;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.widget.Toast;
import android.os.Bundle;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;
import com.iflytek.speech.*;
import com.iflytek.*;
import com.iflytek.cloud.SpeechUtility;

/**
 * Created by Administrator on 2018/4/20.
 */

public class SynthesizerVoice {
    // 单利
    private static SynthesizerVoice instance;

    // 暂存 UnityPlayerActivity 的 Context
    private static Context unityContext;
    // 暂存 UnityPlayerActivity 的 Activity
    private static Activity unityActivity;

    // 语音合成对象
    private SpeechSynthesizer speechSynthesizer;

    // 默认发言人
    private String voicer = "xiaoyan";

    // 引擎类型
    private String mEngineType = SpeechConstant.TYPE_CLOUD;

    // 获取单利
    public static SynthesizerVoice getInstance()
    {
        if (instance == null)
        {
            instance = new SynthesizerVoice();
        }

        return instance;
    }

    // 构造函数
    public SynthesizerVoice()
    {
    }

    // 在 MainActivity 中调用初始化，传入 MainActivity.this
    public void init(Context _context) {
        // 获取到 UnityPlayerActivity 的 Context 和 Activity
        unityContext = _context.getApplicationContext();
        unityActivity = (Activity) _context;

        // 初始化讯飞 SDK
        // "appid=" + "5a30c837" 注意："appid=" 中等号前后都不要加任何字符必须紧按着 5a30c837 为我当前应用的appid
        // 创建应用会生成一个唯一的 appid
        SpeechUtility.createUtility(unityContext, "appid=" + "5ad984b6");

        // 初始化语音合成对象，传入 Context 和 监听回调
        speechSynthesizer = SpeechSynthesizer.createSynthesizer(_context, mTtsInitListener);
    }

    // 设置参数
    private void setParam() {
        // 清空参数
        speechSynthesizer.setParameter(SpeechConstant.PARAMS, null);
        // 根据合成引擎设置相应参数
        if (mEngineType.equals(SpeechConstant.TYPE_CLOUD)) {
            speechSynthesizer.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
            // 设置在线合成发音人
            speechSynthesizer.setParameter(SpeechConstant.VOICE_NAME, voicer);
            //设置合成语速
            speechSynthesizer.setParameter(SpeechConstant.SPEED, "50");
            //设置合成音调
            speechSynthesizer.setParameter(SpeechConstant.PITCH, "50");
            //设置合成音量
            speechSynthesizer.setParameter(SpeechConstant.VOLUME, "50");
        } else {
            speechSynthesizer.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_LOCAL);
            // 设置本地合成发音人 voicer为空，默认通过语记界面指定发音人。
            speechSynthesizer.setParameter(SpeechConstant.VOICE_NAME, "");
            /**
             * TODO 本地合成不设置语速、音调、音量，默认使用语记设置
             * 开发者如需自定义参数，请参考在线合成参数设置
             */
        }
        //设置播放器音频流类型
        speechSynthesizer.setParameter(SpeechConstant.STREAM_TYPE, "5");
        // 设置播放合成音频打断音乐播放，默认为true
        speechSynthesizer.setParameter(SpeechConstant.KEY_REQUEST_FOCUS, "true");

        // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
        // 注：AUDIO_FORMAT参数语记需要更新版本才能生效
        speechSynthesizer.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
        //speechSynthesizer.setParameter(SpeechConstant.TTS_AUDIO_PATH, Environment.getExternalStorageDirectory()+"/msc/tts.wav");
    }

    // 外部调用开始播放
    public void StartSpeaking() {
        // 需要合成声音的文字
        String text = "哈哈哈哈哈哈哈哈哈哈快说出来";
        // 设置参数
        setParam();

        // 开始播放
        int code = speechSynthesizer.startSpeaking(text, mTtsListener);

        if (code != ErrorCode.SUCCESS) {
            showToast("语音合成失败,错误码: " + code);
        } else {
            showToast("语音合成成功啦");
        }
    }


    // 播放回调
    private SynthesizerListener mTtsListener = new SynthesizerListener() {
        @Override
        public void onSpeakBegin() {
        }

        @Override
        public void onSpeakPaused() {
        }

        @Override
        public void onSpeakResumed() {
        }

        @Override
        public void onBufferProgress(int percent, int beginPos, int endPos,
                                     String info) {
            // 合成进度
        }

        @Override
        public void onSpeakProgress(int percent, int beginPos, int endPos) {
            // 播放进度
        }

        @Override
        public void onCompleted(SpeechError error) {
            if (error == null) {
                showToast("播放完成");
            } else if (error != null) {
                showToast(error.getPlainDescription(true));
            }
        }

        @Override
        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
            // 以下代码用于获取与云端的会话id，当业务出错时将会话id提供给技术支持人员，可用于查询会话日志，定位出错原因
            // 若使用本地能力，会话id为null
            //  if (SpeechEvent.EVENT_SESSION_ID == eventType) {
            //      String sid = obj.getString(SpeechEvent.KEY_EVENT_SESSION_ID);
            //      Log.d(TAG, "session id =" + sid);
            //  }
        }
    };

    /**
     * 初始化监听。
     */
    private InitListener mTtsInitListener = new InitListener() {
        @Override
        public void onInit(int code) {
            //Log.d(TAG, "InitListener init() code = " + code);
            if (code != ErrorCode.SUCCESS) {
                showToast("初始化失败,错误码：" + code);
            } else {
                // 初始化成功，之后可以调用startSpeaking方法
                // 注：有的开发者在onCreate方法中创建完合成对象之后马上就调用startSpeaking进行合成，
                // 正确的做法是将onCreate中的startSpeaking调用移至这里
                showToast("初始化成功");
            }
        }
    };

    // 传入 meg，弹出一个 Toast 操作
    public static void showToast(final String meg) {
        unityActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(unityContext, meg, Toast.LENGTH_LONG).show();
            }
        });
    }

    // 弹出一个提示窗口，窗口需要的文字信息从strings.xml 里面获取，点击确认关闭
    public static void showAlertDialog(final String _title, final String _content) {
        unityActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(unityActivity);
                builder.setTitle(_title).setMessage(_content).setPositiveButton("Down", null);
                builder.show();
            }
        });
    }

}
