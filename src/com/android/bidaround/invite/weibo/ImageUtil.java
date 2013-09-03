/**
 * Filename: ImageUtil.java  
 * Date:     2013-8-22
 * Author:   ldr
 * Copyright (c) 2009-2089 Guanzhou Bidaround Information Tech Ltd.
 * All rights reserved.
 *
 * Modifications:
 * Author: ldr
 * Desc:  
 */
package com.android.bidaround.invite.weibo;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

/**
 * Description: imageπ§æﬂ¿‡
 */
public class ImageUtil {
    /**
     * bitmap 2 byte[]
     * 
     * @param bm
     * @return
     */
    public static byte[] Bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    /**
     * byte[] 2 bitmap
     * 
     * @param b
     * @return
     */
    public static Bitmap Bytes2Bitmap(byte[] b) {
        if(b.length != 0) {
            return BitmapFactory.decodeByteArray(b, 0, b.length);
        } else {
            return null;
        }
    }

    /**
     * byte[] 2 bitmap
     * 
     * @param b
     * @param widthAndHeight
     * @return
     */
    public static Bitmap Bytes2Bitmap(byte[] b, int widthAndHeight) {
        if(b.length != 0) {
            return zoomBitmap(BitmapFactory.decodeByteArray(b, 0, b.length),
                    widthAndHeight, widthAndHeight);
        } else {
            return null;
        }
    }

    /**
     * byte[] 2 file
     * 
     * @param b
     * @param context
     * @return
     */
    public static File Bytes2File(byte[] b, Context context) {
        FileOutputStream fstream = null;
        File file = null;
        try {
            file = new File(context.getFilesDir().getPath().toString()
                    + "/qrcode.png");
            fstream = new FileOutputStream(file);
            fstream.write(b);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        } finally {
            if(fstream != null) {
                try {
                    fstream.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return file;
    }

    /**
     * zoom bitmap
     * 
     * @param bitmap
     * @param width
     * @param height
     * @return
     */
    public static Bitmap zoomBitmap(Bitmap bitmap, int width, int height) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        Matrix matrix = new Matrix();
        float scaleWidth = ((float)width / w);
        float scaleHeight = ((float)height / h);
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap newbmp = Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true);
        return newbmp;
    }

    /**
     * byte[] 2 binary
     * 
     * @param bytes
     * @return
     */
    public static String Bytes2Binary(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * Byte.SIZE);
        for(int i = 0; i < Byte.SIZE * bytes.length; i++)
            sb.append((bytes[i / Byte.SIZE] << i % Byte.SIZE & 0x80) == 0 ? '0'
                    : '1');
        return sb.toString();
    }
}
