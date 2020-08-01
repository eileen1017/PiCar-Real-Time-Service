package com.example.picarprojectfinal;

import android.os.Parcel;
import android.os.Parcelable;

public class PicarData implements Parcelable {
    private String dev_id;
    private String timestamp;
    private Float distance;

    public PicarData(String devID, String time, Float dist){
        dev_id = devID;
        timestamp = time;
        distance = dist;
    }

    public PicarData(Parcel in) {
        dev_id = in.readString();
        timestamp = in.readString();
        if (in.readByte() == 0) {
            distance = null;
        } else {
            distance = in.readFloat();
        }
    }

    public static final Creator<PicarData> CREATOR = new Creator<PicarData>() {
        @Override
        public PicarData createFromParcel(Parcel in) {
            return new PicarData(in);
        }

        @Override
        public PicarData[] newArray(int size) {
            return new PicarData[size];
        }
    };

    public String getID(){
        return dev_id;
    }

    public String getTime(){
        return timestamp;
    }

    public Float getDistance(){
        return distance;
    }

    @Override
    public int describeContents() {
        return this.hashCode();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(dev_id);
        dest.writeString(timestamp);
        if (distance == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeFloat(distance);
        }
    }
}

