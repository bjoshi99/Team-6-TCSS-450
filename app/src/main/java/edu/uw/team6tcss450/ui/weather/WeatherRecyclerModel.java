package edu.uw.team6tcss450.ui.weather;

public class WeatherRecyclerModel {
    private String time, temp;

    WeatherRecyclerModel(String time, String temp) {
        this.time = time;
        this.temp = temp;
    }

    public String getTime() { return time; }
    public String getTemp() { return temp; }
    public void setTime(String time) { this.time = time; }
    public void setTemp(String temp) { this.temp = temp; }
}

