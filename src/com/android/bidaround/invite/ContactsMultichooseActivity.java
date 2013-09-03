/**
 * Filename: ContactsMultichoose.java  
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.android.bidaround.invite.core.ConstParam;
import com.android.bidaround.invite.core.IAShareItem;

import android.annotation.SuppressLint;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds;
import android.provider.ContactsContract.Contacts;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.ListView;

/**
 * Description: 多选联系人
 */
@SuppressLint("HandlerLeak")
public class ContactsMultichooseActivity extends ListActivity implements
        OnClickListener {
    private final int UPDATE_LIST = 1;
    ArrayList<String> contactsList; // 得到的所有联系人
    ArrayList<String> getContactsList; // 选择得到联系人
    private Button okbtn;
    private Button cancelbtn;
    private ProgressDialog proDialog;
    private IAShareItem shareItem;

    Thread getcontacts;
    Handler updateListHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch(msg.what){

            case UPDATE_LIST:
                if(proDialog != null) {
                    proDialog.dismiss();
                }
                updateList();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contacts_multichoose);
        contactsList = new ArrayList<String>();
        getContactsList = new ArrayList<String>();
        shareItem = getIntent().getParcelableExtra(ConstParam.KEY_EXTRAS_SHARE_ITEM);
        if(shareItem == null){
            shareItem = new IAShareItem();
        }
        
        final ListView listView = getListView();
        listView.setItemsCanFocus(false);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        okbtn = (Button)findViewById(R.id.btn_contacts_done);
        cancelbtn = (Button)findViewById(R.id.btn_contact_back);
        okbtn.setOnClickListener(this);
        cancelbtn.setOnClickListener(this);

        getcontacts = new Thread(new GetContacts());
        getcontacts.start();
        proDialog = ProgressDialog.show(ContactsMultichooseActivity.this, "loading",
                "loading", true, true);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    void updateList() {
        if(contactsList != null)
            setListAdapter(new ArrayAdapter(this,
                    android.R.layout.simple_list_item_multiple_choice,
                    contactsList));

    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        // 选中
        if(((CheckedTextView)v).isChecked()) {
            CharSequence num = ((CheckedTextView)v).getText();
            // 截取phone number
            String phoneNum = num.toString().substring(
                    (num.toString()).indexOf("\n") + 1);
//            if(isPhoneNumberValid(phoneNum)) {
                getContactsList.add(phoneNum);
//            } else {
//                Toast.makeText(getApplicationContext(), "电话号码非法！",
//                        Toast.LENGTH_LONG).show();
//                ((CheckedTextView)v).setChecked(false);
//            }
        }
        // 取消选中
        if(!((CheckedTextView)v).isChecked()) {
            CharSequence num = ((CheckedTextView)v).getText();
            if((num.toString()).indexOf("[") > 0) {
                String phoneNum = num.toString().substring(
                        (num.toString()).indexOf("\n") + 1);
                getContactsList.remove(phoneNum);
                Log.d("remove_num", "" + phoneNum);
            } else {
                getContactsList.remove(num.toString().substring(
                        (num.toString()).indexOf("\n") + 1));
                Log.d("remove_num", "" + num.toString());
            }
        }
        super.onListItemClick(l, v, position, id);
    }

    /**
     * 判断是否为有效电话号码
     * 
     * @param phoneNum
     * @return
     */
    @SuppressWarnings("unused")
    private boolean isPhoneNumberValid(String phoneNum) {

        boolean isValid = false;
        String expression = "^\\(?(\\d{3})\\)?[- ]?(\\d{3})[- ]?(\\d{5})$";
        String expression2 = "^\\(?(\\d{3})\\)?[- ]?(\\d{4})[- ]?(\\d{4})$";
        String expression3 = "^\\(?(\\d{4})\\)?[- ]?(\\d{3})[- ]?(\\d{4})$";
        String expression4 = "^\\(?(\\d{1}[ ]?d{3})\\)?[- ]?(\\d{3})[- ]?(\\d{4})$";
        CharSequence inputStr = phoneNum;
        Pattern pattern = Pattern.compile(expression);
        Matcher matcher = pattern.matcher(inputStr);
        Pattern pattern2 = Pattern.compile(expression2);
        Matcher matcher2 = pattern2.matcher(inputStr);
        Pattern pattern3 = Pattern.compile(expression3);
        Matcher matcher3 = pattern3.matcher(inputStr);
        Pattern pattern4 = Pattern.compile(expression4);
        Matcher matcher4 = pattern4.matcher(inputStr);
        if(matcher.matches() || matcher2.matches() || matcher3.matches()
                || matcher4.matches()) {
            isValid = true;
        }
        return isValid;
    }

    /**
     * Description: 获取联系人列表线程
     */
    class GetContacts implements Runnable {
        @Override
        public void run() {
            Uri uri = ContactsContract.Contacts.CONTENT_URI;
            String[] projection = new String[]{ContactsContract.Contacts._ID,
                    ContactsContract.Contacts.DISPLAY_NAME,
                    ContactsContract.Contacts.PHOTO_ID};
            String selection = ContactsContract.Contacts.IN_VISIBLE_GROUP
                    + " = 1";
            String sortOrder = ContactsContract.Contacts.DISPLAY_NAME
                    + " COLLATE LOCALIZED ASC";
            // 获取详细信息
            Cursor cursor = getContentResolver().query(uri, projection,
                    selection, null, sortOrder);
            Cursor phonecur = null;

            while(cursor.moveToNext()) {

                // 取得联系人名字
                int nameFieldColumnIndex = cursor
                        .getColumnIndex(android.provider.ContactsContract.PhoneLookup.DISPLAY_NAME);
                String name = cursor.getString(nameFieldColumnIndex);
                // 取得联系人ID
                String contactId = cursor.getString(cursor
                        .getColumnIndex(Contacts._ID));
                phonecur = getContentResolver().query(
                        CommonDataKinds.Phone.CONTENT_URI, null,
                        CommonDataKinds.Phone.CONTACT_ID + " = " + contactId,
                        null, null);
                // 取得电话号码(可能存在多个号码)
                while(phonecur.moveToNext()) {
                    String strPhoneNumber = phonecur.getString(phonecur
                            .getColumnIndex(CommonDataKinds.Phone.NUMBER));
                    if(strPhoneNumber.length() > 4)
                        contactsList.add(name + "\n" + strPhoneNumber + "");
                }
            }
            if(phonecur != null)
                phonecur.close();
            cursor.close();

            Message msg1 = new Message();
            msg1.what = UPDATE_LIST;
            updateListHandler.sendMessage(msg1);
        }
    }

    @Override
    protected void onDestroy() {
        contactsList.clear();
        getContactsList.clear();
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
        case R.id.btn_contacts_done:
            Intent i = new Intent();
            if((getContactsList != null) && (getContactsList.size() > 0)) {
                shareItem.setContactsList(getContactsList);
                Bundle b = new Bundle();
                b.putParcelable(ConstParam.KEY_EXTRAS_SHARE_ITEM, shareItem);
                i.putExtras(b);
            }
            //切换activity至sendsms
            i.setClass(ContactsMultichooseActivity.this, SendSMSActivity.class);
            startActivity(i);
            ContactsMultichooseActivity.this.finish();
            break;
        case R.id.btn_contact_back:
            ContactsMultichooseActivity.this.finish();
            break;
        default:
            break;
        }
    }

}
