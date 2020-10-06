package pg.eti.ksg.ProjektInzynierski.DatabaseEntities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.sql.Date;

@Entity
public class Points {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "point_id")
    private Long id;

    @ColumnInfo(name="route_id")
    private Long routeId;

    private double lat;
    private double lng;

    @ColumnInfo(name = "point_date")
    private Date date;

    public Points() {
    }

    public Points(Long routeId, double lat, double lng, Date date) {
        this.routeId = routeId;
        this.lat = lat;
        this.lng = lng;
        this.date = date;
    }

    public Long getId() {
        return id;
    }

    public Long getRouteId() {
        return routeId;
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }

    public Date getDate() {
        return date;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setRouteId(Long routeId) {
        this.routeId = routeId;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
