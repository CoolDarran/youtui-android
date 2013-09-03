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
 * Description: 发送邮件邀请
 */
public class SendMailActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("SendMail", "start send mail");
        sendMail();
    }

    /**
     * 发送邮件邀请
     */
    private int sendMail() {
        //邮件接收者（数组，可以是多位接收者） 
        String[] reciver = new String[]{};  
        String mySbuject = "测试Email";  
        String[] myCc = new String[]{};  
        String mybody = "测试Email";  
        Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);  
        emailIntent.setType("plain/text"); 
        emailIntent.putExtra(Intent.EXTRA_EMAIL, reciver);  
        emailIntent.putExtra(Intent.EXTRA_CC, myCc);  
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, mySbuject);  
        emailIntent.putExtra(Intent.EXTRA_TEXT, mybody);  
        startActivity(Intent.createChooser(emailIntent, "请选择邮件发送软件"));  
  
        SendMailActivity.this.finish();
        return 1;  
    }
    
}
