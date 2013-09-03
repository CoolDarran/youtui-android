package com.android.bidaround.invite.weibo;

import com.weibo.sdk.android.Oauth2AccessToken;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * �������ڱ���Oauth2AccessToken��sharedpreference�����ṩ��ȡ����
 * Description:
 */
public class AccessTokenKeeper {
	private static final String PREFERENCES_NAME = "invite_assistant";
	/**
	 * ����accesstoken��sharedpreferences
	 * @param context
	 * @param token
	 */
	public static void keepAccessToken(Context context, Oauth2AccessToken token) {
		SharedPreferences pref = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_APPEND);
		Editor editor = pref.edit();
		editor.putString("token", token.getToken());
		editor.putLong("expiresTime", token.getExpiresTime());
		editor.commit();
	}
	/**
	 * ���sharedpreference
	 * @param context
	 */
	public static void clear(Context context){
	    SharedPreferences pref = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_APPEND);
	    Editor editor = pref.edit();
	    editor.clear();
	    editor.commit();
	}

	/**
	 * ��SharedPreference��ȡaccesstoken
	 * @param context
	 * @return
	 */
	public static Oauth2AccessToken readAccessToken(Context context){
		Oauth2AccessToken token = new Oauth2AccessToken();
		SharedPreferences pref = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_APPEND);
		token.setToken(pref.getString("token", ""));
		token.setExpiresTime(pref.getLong("expiresTime", 0));
		return token;
	}
}
