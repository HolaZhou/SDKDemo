package com.test.manySDK;

import android.os.Bundle;

import com.unity3d.player.UnityPlayerActivity;

import com.calculation.demo.CalculationClass;

import  com.example.xunfei.SynthesizerVoice;

public class MainActivity extends UnityPlayerActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        SynthesizerVoice sy = SynthesizerVoice.getInstance();
        sy.init(MainActivity.this);
    }

    public void StartSpeaking()
    {
        SynthesizerVoice sy = SynthesizerVoice.getInstance();
        sy.StartSpeaking();
    }

    public int Add(int a,int b)
    {
        return CalculationClass.GetInstance().Add(a,b);
    }
}
