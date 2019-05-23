package com.example.hyperfit.tools;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

//活动管理器
public class ActivityCollector {
    public static List<Activity> activities = new ArrayList<>();

//    添加活动
    public static void addActivity(Activity activity){
        activities.add(activity);
    }
//    移除单个活动
    public static void removeActivate(Activity activity){
        activities.remove(activity);
    }

    public static void finishAll(){
        for (Activity activity : activities){
            if (!activity.isFinishing()){
                activity.finish();
            }
        }
    }
}
