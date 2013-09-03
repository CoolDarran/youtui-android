/**
 * Filename: WeiboShareItem.java  
 * Date:     2013-8-22
 * Author:   ldr
 * Copyright (c) 2009-2089 Guanzhou Bidaround Information Tech Ltd.
 * All rights reserved.
 *
 * Modifications:
 * Author: ldr
 * Desc:  
 */
package com.android.bidaround.invite.core;

import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Description: 分享的内容
 */
public class IAShareItem implements Parcelable {
    // 文本内容
    private String contents;
    // 图片信息
    private byte[] img;
    // 联系人信息
    private ArrayList<String> contactsList;

    public IAShareItem(String contents) {
        this.contents = contents;
    }

    public IAShareItem(String contents, ArrayList<String> contactsList) {
        this.contents = contents;
        this.contactsList = contactsList;
    }

    public IAShareItem(String contents, byte[] img) {
        this.contents = contents;
        this.img = img;
    }

    public IAShareItem() {
    }

    public IAShareItem(Parcel in) {
        readFromParcel(in);
    }

    @SuppressWarnings("unchecked")
    private void readFromParcel(Parcel in) {
        contents = in.readString();
        int imgSize = in.readInt();
        if(imgSize != -1){
            img = new byte[imgSize];
            in.readByteArray(img);
        }
        contactsList = in.readArrayList(String.class.getClassLoader());
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public byte[] getImg() {
        return img;
    }

    public void setImg(byte[] img) {
        this.img = img;
    }

    public ArrayList<String> getContactsList() {
        return contactsList;
    }

    public void setContactsList(ArrayList<String> contactsList) {
        this.contactsList = contactsList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(contents);
        if(img != null) {
            dest.writeInt(img.length);
        }
        dest.writeByteArray(img);
        dest.writeList(contactsList);
    }

    public static final Parcelable.Creator<IAShareItem> CREATOR = new Creator<IAShareItem>() {

        @Override
        public IAShareItem[] newArray(int size) {
            return new IAShareItem[size];
        }

        @Override
        public IAShareItem createFromParcel(Parcel source) {
            return new IAShareItem(source);
        }
    };

}
