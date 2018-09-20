package com.daobao.asus.workmanagerdemo;

import android.annotation.TargetApi;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.Observer;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import java.util.concurrent.TimeUnit;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkContinuation;
import androidx.work.WorkManager;
import androidx.work.WorkStatus;

public class MainActivity extends AppCompatActivity {

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //约束条件      setRequiredNetworkType在特定网络下执行
        Constraints mConstraints = new Constraints.Builder()
                .setRequiresBatteryNotLow(true)//不在电量不足时执行
                .setRequiresCharging(true)//在充电时执行
                .setRequiresStorageNotLow(true)//不在存储容量不足时执行
                .setRequiresDeviceIdle(true)//在待机状态执行
                .build();
        //传入参数
        Data data = new Data.Builder().putString("workerData","helloWorld").build();
        //构造只执行一次的worker
        OneTimeWorkRequest testWorker = new OneTimeWorkRequest.Builder(TestWorker.class)
                .setInputData(data)
                //.setInitialDelay(5,TimeUnit.SECONDS)  //延时执行worker
                .setConstraints(mConstraints).build();
        //重复执行的worker
//        PeriodicWorkRequest periodicWorkRequest = new PeriodicWorkRequest.Builder(TestWorker.class,15,
//                TimeUnit.SECONDS).setConstraints(mConstraints)
//                .setInputData(data).build();
        //放入任务队列
        WorkManager.getInstance().enqueue(testWorker);

        //按顺序执行worker
//        WorkManager.getInstance().beginWith(A).then(B).enqueue(); //按顺序逐个执行
//        WorkManager.getInstance().beginWith(A,B).then(C).enqueue();//A,B同时执行,然后执行C

//        //同时执行任务链1和2,执行完成后执行E
//        WorkContinuation workContinuation1 = WorkManager.getInstance().beginWith(A).then(B);
//        WorkContinuation workContinuation2 = WorkManager.getInstance().beginWith(C).then(D);
//        WorkContinuation workContinuation3 = WorkContinuation.combine(workContinuation1,workContinuation2).then(E);
//        workContinuation3.enqueue();

        //订阅返回数据
        WorkManager.getInstance()
                .getStatusById(testWorker.getId())
                .observeForever(new Observer<WorkStatus>() {
                    @Override
                    public void onChanged(@Nullable WorkStatus workStatus) {
                        Data response = workStatus.getOutputData();
                        Log.d("test","response:"+response.getString("response"));
                    }
         });
        //取消任务
        //WorkManager.getInstance().cancelWorkById(testWorker.getId());

    }
}
