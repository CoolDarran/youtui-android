/**
 * Filename: InviteAssistantHandler.java  
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

import android.annotation.SuppressLint;
import android.os.Parcelable;

/**
 * Description: �����¼�����ӿ�
 */
@SuppressLint("ParcelCreator")
public interface InviteAssistantHandler extends Parcelable {
    /**
     * ��������Ϊ��ʼʱ����
     * @param shareItem
     */
    void onShareStart(IAShareItem shareItem);
    /**
     * ��΢��������Ϊ����ʱ����������������
     * @param shareItem
     */
    void onShareComplete(IAShareItem shareItem);
    /**
     * ��������Ȩʧ��ʱ����
     */
    void onAuthError();
    /**
     * ��������Ȩ�ɹ�����ȡTokenʱ����
     */
    void onAuthSuccess(String accessToken);
}
