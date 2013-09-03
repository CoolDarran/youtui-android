/**
 * Filename: WeiboConstParam.java  
 * Date:     2013-8-20
 * Author:   ldr
 * Copyright (c) 2009-2089 Guanzhou Bidaround Information Tech Ltd.
 * All rights reserved.
 *
 * Modifications:
 * Author: ldr
 * Desc:  
 */
package com.android.bidaround.invite.core;

/**
 * Description: 参数
 */
public interface ConstParam {

    public static final String CONSUMER_KEY = "1298711993";                      // appkey
    
    public static final String CONSUMER_SECRET = "95dc74902962858cfeeb3752cc7aaaaa";                  // secret
    
    public static final String REDIRECT_URL = "http://t.zhanfa.cc";                // url回调地址
    
    //传入scope权限
    public static final String SCOPE = "email,direct_messages_read,direct_messages_write," +
            "friendships_groups_read,friendships_groups_write,statuses_to_me_read," +
                "follow_app_official_microblog";
    
    public static final String PACKAGE_NAME = "com.android.bidaround.invite";
    
    public static final String KEY_HASH = "7a891d3e11d9b02a15f31f630890a4a8";
    
    public static final String SHARE_SERVER = "https://upload.api.weibo.com/2/statuses/upload.json";
    
    public static final String KEY_EXTRAS_SHARE_ITEM = "extras_share_item";
}

