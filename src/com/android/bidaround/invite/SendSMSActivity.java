/**
 * Filename: SendSMS.java  
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

import java.util.ArrayList;
import java.util.Iterator;

import com.android.bidaround.invite.core.ConstParam;
import com.android.bidaround.invite.core.IAShareItem;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

/**
 * Description: send sms
 */
public class SendSMSActivity extends Activity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //读出数据        
        IAShareItem shareItem = getIntent().getParcelableExtra(ConstParam.KEY_EXTRAS_SHARE_ITEM);
        if(shareItem == null){
            shareItem = new IAShareItem();
        }
        ArrayList<String> getPhoneList = shareItem.getContactsList();
        String phoneNumber = "";
        if(!getPhoneList.isEmpty()){
            StringBuilder buffer = new StringBuilder(getPhoneList.size() * 16);
            Iterator<String> it = getPhoneList.iterator();
            while(it.hasNext()){
                String num = it.next();
                buffer.append(num);
              //号码须;号间隔
                buffer.append(";");
            }
            phoneNumber = buffer.toString();
        }
        send(phoneNumber, shareItem.getContents());
    }

    private void send(String number, String message){
        Uri uri = Uri.parse("smsto:" + number);
        Intent sendIntent = new Intent(Intent.ACTION_VIEW, uri);
        sendIntent.putExtra("sms_body", message);
        startActivity(sendIntent);
        SendSMSActivity.this.finish();
    }
}
