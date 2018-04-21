package com.calculation.demo;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by Administrator on 2018/4/19.
 */

public class CalculationClass extends Activity {
    private static CalculationClass Instance = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public static CalculationClass GetInstance()
    {
        if(Instance == null)
        {
            Instance = new CalculationClass();
        }
        return  Instance;
    }

    public int Add(int a,int b)
    {
        return a + b;
    }

    public static int AddOne(int a)
    {
        return a+1;
    }
}
