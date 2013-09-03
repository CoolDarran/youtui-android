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
 * Description: 分享事件处理接口
 */
@SuppressLint("ParcelCreator")
public interface InviteAssistantHandler extends Parcelable {
    /**
     * 当分享行为开始时触发
     * @param shareItem
     */
    void onShareStart(IAShareItem shareItem);
    /**
     * 当微博分享行为结束时触发，其他不触发
     * @param shareItem
     */
    void onShareComplete(IAShareItem shareItem);
    /**
     * 当调用授权失败时触发
     */
    void onAuthError();
    /**
     * 当调用授权成功并获取Token时触发
     */
    void onAuthSuccess(String accessToken);
}
