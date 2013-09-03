/**
 * Filename: InviteAssistant.java  
 * Date:     2013-8-26
 * Author:   ldr
 * Copyright (c) 2009-2089 Guanzhou Bidaround Information Tech Ltd.
 * All rights reserved.
 *
 * Modifications:
 * Author: ldr
 * Desc:  
 */
package com.android.bidaround.invite.core;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;

import com.android.bidaround.invite.ContactsMultichooseActivity;
import com.android.bidaround.invite.SendMailActivity;
import com.android.bidaround.invite.SinaAuthorizationActivity;
import com.android.bidaround.invite.SinaShareActivity;

/**
 * Description: activity�������
 */
public class InviteAssistant{
    //ͬ��
    static final Object mInstanceSync = new Object();
    //�ڲ�ȫ��Ψһʵ��
    static InviteAssistant mInstance;
    
    //����API
    static public InviteAssistant instance(Context context){
        return getInstance(context.getMainLooper());
    }    

    InviteAssistant() {
    }

    /**
     * �ڲ�API����������ⲿAPI����
     * @param mainLooper
     * @return
     */
    private static InviteAssistant getInstance(Looper mainLooper) {
        synchronized (mInstanceSync) {
            if(mInstance != null){
                return mInstance;
            }
            mInstance = new InviteAssistant();
        }
        return mInstance;
    }
    
    public void sendMail(Context context, IAShareItem shareItem){        
        Intent intent = new Intent(context, SendMailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(ConstParam.KEY_EXTRAS_SHARE_ITEM, shareItem);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }
    
    public void sendSMS(Context context, IAShareItem shareItem){
        Intent intent = new Intent(context, ContactsMultichooseActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(ConstParam.KEY_EXTRAS_SHARE_ITEM, shareItem);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }
    
    public void tryWeiboAuthorization(Context context, IAShareItem shareItem){
        Intent intent = new Intent();
        intent.setClass(context,SinaAuthorizationActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(ConstParam.KEY_EXTRAS_SHARE_ITEM, shareItem);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }
    
    public void weiboShare(Context context, IAShareItem shareItem){
        Intent intent = new Intent(context, SinaShareActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(ConstParam.KEY_EXTRAS_SHARE_ITEM, shareItem);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }
}
