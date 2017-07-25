package com.huadin.assetstatistics.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by admin on 2017/7/19.
 */

public class IntentUtils {
    public static void startActivity(Activity context, Class clazz, Bundle bundle, boolean isFinish){
        Intent intent = new Intent(context,clazz);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        context.startActivity(intent);
        if(isFinish){
            context.finish();
        }
    }


}
