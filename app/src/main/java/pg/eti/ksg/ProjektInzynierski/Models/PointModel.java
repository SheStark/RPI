package pg.eti.ksg.ProjektInzynierski.Models;

public class PointModel {
    private double lat;
    private double lng;
    private String date;
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public PointModel(double lat, double lng, String date) {
        this.lat = lat;
        this.lng = lng;
        this.date = date;
    }



    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }

    public String getDate() {
        return date;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public void setDate(String date) {
        this.date = date;
    }


}

