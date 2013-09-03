/**
 * Filename: SinaAuthorizationActivity.java  
 * Date:     2013-8-20
 * Author:   ldr
 * Copyright (c) 2009-2089 Guanzhou Bidaround Information Tech Ltd.
 * All rights reserved.
 *
 * Modifications:
 * Author: ldr
 * Desc:  
 */
package com.android.bidaround.invite;

import java.text.SimpleDateFormat;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.bidaround.invite.core.ConstParam;
import com.android.bidaround.invite.core.IAShareItem;
import com.android.bidaround.invite.core.InviteAssistant;
import com.android.bidaround.invite.weibo.AccessTokenKeeper;
import com.weibo.sdk.android.Oauth2AccessToken;
import com.weibo.sdk.android.Weibo;
import com.weibo.sdk.android.WeiboAuthListener;
import com.weibo.sdk.android.WeiboDialogError;
import com.weibo.sdk.android.WeiboException;

/**
 * Description: 进行新浪微博授权认证
 */
public class SinaAuthorizationActivity extends Activity {

    private Weibo mWeibo;

    public static Oauth2AccessToken accessToken;

    private IAShareItem shareItem;

    private String Tag = "weibo_authorization";

    @SuppressLint("SimpleDateFormat")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 初始化操作
        shareItem = getIntent().getParcelableExtra(
                ConstParam.KEY_EXTRAS_SHARE_ITEM);
        if(shareItem == null){
            shareItem = new IAShareItem();
        }
        mWeibo = Weibo.getInstance(ConstParam.CONSUMER_KEY,
                ConstParam.REDIRECT_URL, ConstParam.SCOPE);

        // 获取授权信息
        SinaAuthorizationActivity.accessToken = AccessTokenKeeper
                .readAccessToken(getApplication());

        if(SinaAuthorizationActivity.accessToken.isSessionValid()) {
            String date = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss")
                    .format(new java.util.Date(
                            SinaAuthorizationActivity.accessToken
                                    .getExpiresTime()));
            Toast.makeText(
                    getApplicationContext(),
                    "access_token 仍在有效期内,无需再次登录: \naccess_token:"
                            + SinaAuthorizationActivity.accessToken.getToken()
                            + "\n有效期：" + date, Toast.LENGTH_LONG).show();
            // 跳转至微博分享
            InviteAssistant.instance(getApplication()).weiboShare(
                    SinaAuthorizationActivity.this, shareItem);
        } else {
            // 进行授权操作
            Log.i(Tag, "do authorize");
            mWeibo.anthorize(SinaAuthorizationActivity.this,
                    new AuthDialogListener());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        finish();
        super.onDestroy();
    }

    class AuthDialogListener implements WeiboAuthListener {

        @Override
        public void onCancel() {
            Toast.makeText(getApplicationContext(), "取消认证", Toast.LENGTH_LONG)
                    .show();
        }

        @Override
        public void onComplete(Bundle values) {
            // 获取accesstoken expiresin
            String token = values.getString("access_token");
            String expires_in = values.getString("expires_in");
            SinaAuthorizationActivity.accessToken = new Oauth2AccessToken(
                    token, expires_in);
            if(SinaAuthorizationActivity.accessToken.isSessionValid()) {
                AccessTokenKeeper
                        .keepAccessToken(getApplication(), accessToken);
                Toast.makeText(getApplicationContext(), "认证成功",
                        Toast.LENGTH_SHORT).show();
                // 跳转至微博分享
                InviteAssistant.instance(getApplication()).weiboShare(
                        SinaAuthorizationActivity.this, shareItem);
            }
        }

        @Override
        public void onError(WeiboDialogError e) {
            Toast.makeText(getApplicationContext(), "认证失败：" + e.getMessage(),
                    Toast.LENGTH_LONG).show();
        }

        @Override
        public void onWeiboException(WeiboException e) {
            Toast.makeText(getApplicationContext(), "认证异常：" + e.getMessage(),
                    Toast.LENGTH_LONG).show();
        }

    }
}
