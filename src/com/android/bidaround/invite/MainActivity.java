package com.android.bidaround.invite;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

import com.android.bidaround.invite.core.IAShareItem;
import com.android.bidaround.invite.core.InviteAssistant;
import com.android.bidaround.invite.qrcode.QRCodeEncodeHandler;
import com.android.bidaround.invite.weibo.ImageUtil;
import com.google.zxing.WriterException;

public class MainActivity extends Activity {
    private Button invite;
    private Button smsInvite;
    private Button mailInvite;
    private Button recommend;
    private Button qrcodeInvite;
    private ImageView qrImgView;
    private String Tag = "InviteAssistant";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        final InviteAssistant inviteAssistant = InviteAssistant.instance(this);
        invite = (Button)findViewById(R.id.btn_invite);

        invite.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // 初始化invite_share布局
                setContentView(R.layout.invite_share);
                smsInvite = (Button)findViewById(R.id.btn_sms_invite);
                mailInvite = (Button)findViewById(R.id.btn_mail_invite);
                recommend = (Button)findViewById(R.id.btn_recommend);
                qrcodeInvite = (Button)findViewById(R.id.btn_qrcode_invite);
                qrImgView = (ImageView)findViewById(R.id.img_qr);

                smsInvite.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        intent.setClass(MainActivity.this,
                                ContactsMultichooseActivity.class);
                        startActivity(intent);
                    }
                });

                mailInvite.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        intent.setClass(MainActivity.this,
                                SendMailActivity.class);
                        startActivity(intent);
                    }
                });

                recommend.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
//                        Intent intent = new Intent();
//                        intent.setClass(MainActivity.this,
//                                SinaAuthorizationActivity.class);
//                        startActivity(intent);
                        // TODO test 默认上传二维码
                        try {
                            Bitmap qrCode = QRCodeEncodeHandler.createQRCode("Bidaround Invite Assistant", 350);
                            IAShareItem shareItem = new IAShareItem("呵呵哈哈",ImageUtil.Bitmap2Bytes(qrCode));
                            inviteAssistant.tryWeiboAuthorization(MainActivity.this, shareItem);
                        } catch (WriterException we) {
                            Log.e(Tag, "Exception", we);
                        }                         
                    }
                });

                qrcodeInvite.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        try {
                            Bitmap qrCodeBitmap = QRCodeEncodeHandler
                                    .createQRCode(
                                            "QRCode",
                                            500);
                            qrImgView.setImageBitmap(qrCodeBitmap);
                        } catch (WriterException e) {
                            // TODO: handle exception
                        }
                    }
                });
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

}
