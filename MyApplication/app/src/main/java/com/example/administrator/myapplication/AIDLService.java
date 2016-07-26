package com.example.administrator.myapplication;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.Process;
import android.os.RemoteException;

/**
 * Created by Administrator on 2016/6/27.
 */
public class AIDLService extends Service {

    @Override
    public void onCreate() {
        super.onCreate();
        System.out.print("AIDLService onCreate");
    }

    @Override
    public IBinder onBind(Intent intent) {
        System.out.println("AIDLService onBind");
        return mBinder;
    }


    private final IRegisterService.Stub mBinder = new IRegisterService.Stub() {

        @Override
        public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString) throws RemoteException {
            System.out.println("Thread: " + Thread.currentThread().getName());
            System.out.println("basicTypes aDouble: " + aDouble +" anInt: " + anInt+" aBoolean " + aBoolean+" aString " + aString);

        }

        @Override
        public int getPid() throws RemoteException {
            System.out.println("Thread: " + Thread.currentThread().getName());
            System.out.println("DDService getPid ");
            return Process.myPid();
        }
    };
}
