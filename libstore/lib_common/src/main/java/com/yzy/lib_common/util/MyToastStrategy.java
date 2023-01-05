package com.yzy.lib_common.util;

import android.app.Application;
import android.widget.Toast;

import com.hjq.toast.ToastStrategy;


public class MyToastStrategy extends ToastStrategy {

   public int duration = Toast.LENGTH_SHORT;
    @Override
    protected int getToastDuration(CharSequence text) {
        return duration;
    }

    @Override
    public void registerStrategy(Application application) {
        if (application==null){
            return;
        }
        super.registerStrategy(application);
    }

}