package betaar.pk.Models;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Huzaifa Asif on 27-Jun-18.
 */

public class MapGetterSetter {

    private String user_id;
    private String title;
    private String diploma_type;
    private String distance;
    private LatLng latLong;

    public MapGetterSetter(LatLng latLong, String title) {
        this.latLong = latLong;
        this.title = title;
    }

    public MapGetterSetter(String user_id, String title, String diploma_type, String distance, LatLng latLong) {
        this.user_id = user_id;
        this.title = title;
        this.diploma_type = diploma_type;
        this.distance = distance;
        this.latLong = latLong;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getDiploma_type() {
        return diploma_type;
    }

    public void setDiploma_type(String diploma_type) {
        this.diploma_type = diploma_type;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public LatLng getLatLong() {
        return latLong;
    }

    public void setLatLong(LatLng latLong) {
        this.latLong = latLong;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
