/**
 * Filename: SendMail.java  
 * Date:     2013-8-19
 * Author:   ldr
 * Copyright (c) 2009-2089 Guanzhou Bidaround Information Tech Ltd.
 * All rights reserved.
 *
 * Modifications:
 * Author: ldr
 * Desc:  
 */
package com.android.bidaround.invite;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

/**
 * Description: �����ʼ�����
 */
public class SendMailActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("SendMail", "start send mail");
        sendMail();
    }

    /**
     * �����ʼ�����
     */
    private int sendMail() {
        //�ʼ������ߣ����飬�����Ƕ�λ�����ߣ� 
        String[] reciver = new String[]{};  
        String mySbuject = "����Email";  
        String[] myCc = new String[]{};  
        String mybody = "����Email";  
        Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);  
        emailIntent.setType("plain/text"); 
        emailIntent.putExtra(Intent.EXTRA_EMAIL, reciver);  
        emailIntent.putExtra(Intent.EXTRA_CC, myCc);  
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, mySbuject);  
        emailIntent.putExtra(Intent.EXTRA_TEXT, mybody);  
        startActivity(Intent.createChooser(emailIntent, "��ѡ���ʼ��������"));  
  
        SendMailActivity.this.finish();
        return 1;  
    }
    
}
