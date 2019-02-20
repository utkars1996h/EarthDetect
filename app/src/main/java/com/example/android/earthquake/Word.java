package com.example.android.earthquake;

public class Word {

    private Double mMagnitude;
    private String mArea;
    private Long mTimeinMilli;
    private String mUrl;

    public Word(Double Magnitude, String Area,Long TimeinMilli,String Url) {
        mMagnitude = Magnitude;
        mArea = Area;
        mTimeinMilli =TimeinMilli;
        mUrl = Url;
    }

    public Double getmMagnitude() {
        return mMagnitude;
    }

    public String getmArea() {
        return mArea;
    }


    public Long getmTimeinMilli(){
        return mTimeinMilli;
    }

    public String getmUrl(){return mUrl;}

}
