package models;

import play.db.ebean.Model;
import javax.persistence.*;

/**
 * Created by gabriel.perez on 27/01/16.
 * Made as a template by leonel.atencio on 13/03/16
 * This class haves all the variables and methods to use the location services.
 * It is the main logic of this specific application.
 */

@Entity
@Table(name = "location")
public class Location extends Model {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int idLocation;
    @Column(columnDefinition = "float(10,6)")
    private float startLat;
    @Column(columnDefinition = "float(10,6)")
    private float startLong;
    @Column(columnDefinition = "float(10,6)")
    private float endLat;
    @Column(columnDefinition = "float(10,6)")
    private float endLong;
    @Column(columnDefinition = "float(10,6)")
    private float currentLat;
    @Column(columnDefinition = "float(10,6)")
    private float currentLong;
    private short statusLocation;
    private String follow;

    public static Finder<Integer,Location> find = new Finder<Integer, Location>(
      Integer.class,Location.class
    );

    public Location() {
    }

    public Location(float startLat, float startLong, float endLat, float endLong, float currentLat, float currentLong, short statusLocation, String follow) {
        this.startLat = startLat;
        this.startLong = startLong;
        this.endLat = endLat;
        this.endLong = endLong;
        this.currentLat = currentLat;
        this.currentLong = currentLong;
        this.statusLocation = statusLocation;
        this.follow = follow;
    }

    public void setStartLat(float startLat) {
        this.startLat = startLat;
    }

    public void setStartLong(float startLong) {
        this.startLong = startLong;
    }

    public void setEndLat(float endLat) {
        this.endLat = endLat;
    }

    public void setEndLong(float endLong) {
        this.endLong = endLong;
    }

    public void setCurrentLat(float currentLat) {
        this.currentLat = currentLat;
    }

    public void setCurrentLong(float currentLong) {
        this.currentLong = currentLong;
    }

    public void setStatusLocation(short statusLocation) {
        this.statusLocation = statusLocation;
    }

    public void setFollow(String follow) {
        this.follow = follow;
    }

    public int getIdLocation() {
        return idLocation;
    }

    public float getStartLat() {
        return startLat;
    }

    public float getStartLong() {
        return startLong;
    }

    public float getEndLat() {
        return endLat;
    }

    public float getEndLong() {
        return endLong;
    }

    public float getCurrentLat() {
        return currentLat;
    }

    public float getCurrentLong() {
        return currentLong;
    }

    public short getStatusLocation() {
        return statusLocation;
    }

    public String getFollow() {
        return follow;
    }

    public static Location getById(int id){
        return find.byId(id);
    }

}
