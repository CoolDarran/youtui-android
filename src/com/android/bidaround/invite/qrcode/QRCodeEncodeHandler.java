/**
 * Filename: QRCodeEncodeHandler.java  
 * Date:     2013-8-22
 * Author:   ldr
 * Copyright (c) 2009-2089 Guanzhou Bidaround Information Tech Ltd.
 * All rights reserved.
 *
 * Modifications:
 * Author: ldr
 * Desc:  
 */
package com.android.bidaround.invite.qrcode;

import android.graphics.Bitmap;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;

/**
 * Description: Éú³É¶þÎ¬Âë
 */
public final class QRCodeEncodeHandler {

    public static Bitmap createQRCode(String contentsToEncode,
            int widthAndHeight) throws WriterException {

        if(contentsToEncode == null) {
            return null;
        }
        
        Bitmap bitmap = null;
        
        QRCodeEncoder qrCodeEncoder = new QRCodeEncoder(contentsToEncode, null,
                Contents.Type.TEXT, BarcodeFormat.QR_CODE.toString(), widthAndHeight);

        try {
            bitmap = qrCodeEncoder.encodeAsBitmap();
        } catch (WriterException e) {
            e.printStackTrace();
        }

        return bitmap;
    }
}
