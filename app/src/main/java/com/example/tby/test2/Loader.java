package com.example.tby.test2;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by tby on 2016/11/2.
 */

public class Loader implements Parcelable {


    protected Loader(Parcel in) {
    }

    public static final Creator<Loader> CREATOR = new Creator<Loader>() {
        @Override
        public Loader createFromParcel(Parcel in) {
            return new Loader(in);
        }

        @Override
        public Loader[] newArray(int size) {
            return new Loader[size];
        }
    };


    @Override
    public int describeContents() {
        return 1;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }


}