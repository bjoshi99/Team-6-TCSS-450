package edu.uw.team6tcss450.ui.weather;

public class WeatherModel {

    private String mDayOfWeek, mTempHigh, mTempLow;
    private int mImage;

    WeatherModel(String mDayOfWeek, int mImage, String mTempHigh, String mTempLow) {
        this.mDayOfWeek = mDayOfWeek;
        this.mImage = mImage;
        this.mTempHigh = mTempHigh;
        this.mTempLow = mTempLow;
    }

    public String getDayOfWeek() { return mDayOfWeek; }
    public int getImage() { return mImage; }
    public String getTempHigh() { return mTempHigh; }
    public String getTempLow() { return mTempLow; }
}
