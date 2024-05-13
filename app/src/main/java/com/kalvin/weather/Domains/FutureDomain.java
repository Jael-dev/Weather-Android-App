package com.kalvin.weather.Domains;

public class FutureDomain {
    private String day;
    private String status;
    private int low;
    private int high;
    private String pic;

    public FutureDomain(String day, String status, int low, int high, String pic) {
        this.day = day;
        this.status = status;
        this.low = low;
        this.high = high;
        this.pic = pic;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getLow() {
        return low;
    }

    public void setLow(int low) {
        this.low = low;
    }

    public int getHigh() {
        return high;
    }

    public void setHigh(int high) {
        this.high = high;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }
}
