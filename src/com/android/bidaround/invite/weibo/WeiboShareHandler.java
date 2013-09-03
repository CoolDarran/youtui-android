/**
 * Filename: WeiboShareHandler.java  
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

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.Charset;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.protocol.HTTP;

import com.android.bidaround.invite.core.IAShareItem;
import com.android.bidaround.invite.core.ConstParam;

import android.content.Context;
import android.util.Log;

/**
 * Description: ΢����������
 */
public class WeiboShareHandler {
    private final static String Tag = "SinaShare";

    /**
     * ʹ��HttpClient����
     * 
     * @param shareItem
     * @param accessToken
     * @param context
     * @return
     */
    public static String share2weibo(IAShareItem shareItem,
            String accessToken, Context context) {

        String result = "";
        // ����HttpPost HttpClient����
        HttpPost httpRequest = new HttpPost(ConstParam.SHARE_SERVER);
        HttpClient httpClient = HttpClient4Weibo.getNewHttpClient();
        try {
            // �������������������
            MultipartEntity entity = new MultipartEntity(
                    HttpMultipartMode.BROWSER_COMPATIBLE, null,
                    Charset.forName(HTTP.UTF_8));
            entity.addPart("access_token", new StringBody(accessToken));
            entity.addPart(
                    "status",
                    new StringBody(URLEncoder.encode(shareItem.getContents(),
                            "utf-8")));
            entity.addPart(
                    "pic",
                    new FileBody(ImageUtil.Bytes2File(shareItem.getImg(),
                            context), "image/png"));
            httpRequest.setEntity(entity);

            // �������󲢵ȴ���Ӧ
            Log.i(Tag + "_executing_request:", httpRequest.getRequestLine()
                    .toString());
            HttpResponse httpResponse = httpClient.execute(httpRequest);

            int statusCode = httpResponse.getStatusLine().getStatusCode();

            // ��״̬��Ϊ200,OK
            if(statusCode == HttpStatus.SC_OK) {
                // ��ȡ��������
                result = String.valueOf(statusCode);
            } else {
                Log.e(Tag, httpResponse.getStatusLine().toString());
            }
        } catch (ClientProtocolException e) {
            Log.e(Tag, e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            Log.e(Tag, e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            Log.e(Tag, e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

}
