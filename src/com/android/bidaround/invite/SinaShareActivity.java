/**
 * Filename: SinaShare.java  
 * Date:     2013-8-22
 * Author:   ldr
 * Copyright (c) 2009-2089 Guanzhou Bidaround Information Tech Ltd.
 * All rights reserved.
 *
 * Modifications:
 * Author: ldr
 * Desc:  
 */
package com.android.bidaround.invite;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.bidaround.invite.core.ConstParam;
import com.android.bidaround.invite.core.IAShareItem;
import com.android.bidaround.invite.weibo.AccessTokenKeeper;
import com.android.bidaround.invite.weibo.ImageUtil;
import com.android.bidaround.invite.weibo.WeiboShareHandler;

/**
 * Description: 分享到微博
 */
@SuppressLint("NewApi")
public class SinaShareActivity extends Activity {

    public static final int SHOW_PROGRESS_DIALOG = 2;
    public static final int HIDE_PROGRESS_DIALOG = 3;

    private String Tag = "Share2weibo";

    // 待分享的内容。
    private IAShareItem shareItem;
    private Button shareButton;
    private EditText editText;
    private ImageButton mentionButton;
    private ImageButton topicButton;
    private TextView count;
    private ProgressDialog pd;
    private Builder dialogDialogBuilder;
    private ImageView sharePic;
    // 待上传的图片
    private Bitmap thumbnail;
    private byte[] img;

    // 分享结果
    String shareResult;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(Looper.getMainLooper()) {
        public void handleMessage(android.os.Message msg) {
            switch(msg.what){
            case SHOW_PROGRESS_DIALOG:
                pd.show();
                break;
            case HIDE_PROGRESS_DIALOG:
                pd.hide();
                break;
            default:
                break;
            }
        }
    };

    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.weibo_editor);
        
        shareItem = getIntent().getParcelableExtra(ConstParam.KEY_EXTRAS_SHARE_ITEM);
        
        dialogDialogBuilder = new Builder(this);

        editText = (EditText)findViewById(R.id.weibo_share_edittext);
        editText.setText(shareItem.getContents());

        sharePic = (ImageView)findViewById(R.id.weibo_share_pic);
        sharePic.setClickable(true);
        sharePic.setVisibility(View.INVISIBLE);

        if(shareItem == null) {
            shareItem = new IAShareItem();
        } else if(shareItem.getImg() != null && shareItem.getImg().length > 0) {
            // 上传图片
            try {
                thumbnail = ImageUtil.Bytes2Bitmap(shareItem.getImg(), 120);
                sharePic.setBackground(new BitmapDrawable(getResources(),
                        thumbnail));
                sharePic.setVisibility(View.VISIBLE);
                sharePic.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        AlertDialog ad = dialogDialogBuilder.create();
                        ad.setButton2("取消",
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,
                                            int which) {
                                        dialog.dismiss();
                                    }
                                });
                        if(img == null) {
                            ad.setMessage("是否不分享该图片？");
                            ad.setButton("不分享",
                                    new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(
                                                DialogInterface dialog,
                                                int which) {
                                            img = shareItem.getImg();
                                            shareItem.setImg(null);
                                        }
                                    });
                        } else {
                            ad.setMessage("是否分享该图片？");
                            ad.setButton("分享",
                                    new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(
                                                DialogInterface dialog,
                                                int which) {
                                            shareItem.setImg(img);
                                            img = null;
                                        }
                                    });
                        }
                        ad.show();
                    }
                });
                System.gc();
            } catch (Exception e) {
                Log.e(Tag, "Exception", e);
            }
        }
        pd = new ProgressDialog(this);
        pd.setIndeterminate(true);
        pd.setCancelable(true);
        pd.setCanceledOnTouchOutside(true);
        pd.setMessage("正在处理，请稍候…");
        shareButton = (Button)findViewById(R.id.btn_weibo_share);
        editText = (EditText)findViewById(R.id.weibo_share_edittext);
        mentionButton = (ImageButton)findViewById(R.id.weibo_share_button_mention);
        topicButton = (ImageButton)findViewById(R.id.weibo_share_button_topic);
        count = (TextView)findViewById(R.id.weibo_share_text_count);
        count.setText(editText.length() + "/140");

        topicButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                SpannableString span = new SpannableString(" #要发表的话题#");
                int end = editText.getSelectionEnd();
                int start = editText.getSelectionStart();
                editText.getEditableText().delete(Math.min(start, end),
                        Math.max(start, end));
                editText.append(span);
                if(editText.getEditableText().charAt(start + 1) == '#') {
                    start++;
                }
                editText.setSelection(start + 1, start + span.length() - 2);
            }
        });

        mentionButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                SpannableString span = new SpannableString(" @要通知的用户");
                int end = editText.getSelectionEnd();
                int start = editText.getSelectionStart();
                editText.getEditableText().delete(Math.min(start, end),
                        Math.max(start, end));
                editText.append(span);
                editText.setSelection(start + 2, start + span.length());
            }
        });

        shareButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Thread t = new Thread(new Runnable() {

                    @Override
                    public void run() {
                        try {
                            handler.sendEmptyMessage(SHOW_PROGRESS_DIALOG);
                            shareItem.setContents(editText.getEditableText()
                                    .toString());
                            shareResult = WeiboShareHandler.share2weibo(
                                    shareItem, AccessTokenKeeper
                                            .readAccessToken(getApplication())
                                            .getToken(),
                                    getApplicationContext());
                        } catch (Exception e) {
                            Log.e(Tag, "Exception", e);
                        } finally {
                            handler.sendEmptyMessage(HIDE_PROGRESS_DIALOG);
                            if(shareResult.equals("200")) {
                                Toast.makeText(getApplicationContext(), "分享成功",
                                        Toast.LENGTH_LONG).show();
                                setContentView(R.layout.invite_share);
                            }
                        }
                    }
                });
                t.start();
            }
        });

        editText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                    int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                    int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                count.setText(s.length() + "/140");
            }
        });
    }

    @Override
    protected void onDestroy() {
        pd.dismiss();
        sharePic.setBackground(null);
        if(thumbnail != null && !thumbnail.isRecycled()) {
            thumbnail.recycle();
        }
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            setContentView(R.layout.invite_share);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    
}
