package com.daobao.asus.workmanagerdemo;

import android.support.annotation.NonNull;
import android.util.Log;
import androidx.work.Data;
import androidx.work.Worker;

/**
 * Created by db on 2018/9/20.
 */
public class TestWorker extends Worker {
    private String TAG = "test";
    @NonNull
    @Override
    public Result doWork() {
        String data = getInputData().getString("workerData");
        Log.d(TAG,"doWork:"+data);
        Data responseData = new Data.Builder().putString("response","ok").build();
        setOutputData(responseData);
        return Result.SUCCESS;
    }
}
