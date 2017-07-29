package com.huadin.assetstatistics.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import static android.R.attr.path;

/**
 * Created by 华电 on 2016/12/8.
 */

public class CameraUtil {


    public static void takePhoto(Activity activity, String imgName) {
        File file = new File(Environment.getExternalStorageDirectory(), "安全工器具/img");
        if(!file.exists()){
            file.mkdirs();
        }

        File image = new File(file, imgName + ".jpg");
        if (!image.exists()) {
            try {
                image.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
//            Uri photoURI = FileProvider.getUriForFile(context,"applicationId.fileprovider", image);
        }
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(image));
        activity.startActivityForResult(intent, Activity.DEFAULT_KEYS_DIALER);
    }



    //bitmap转换成byte形式
    public static byte[] Bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        return baos.toByteArray();
    }
}
