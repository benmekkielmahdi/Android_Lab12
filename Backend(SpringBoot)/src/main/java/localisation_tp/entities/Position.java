package localisation_tp.entities;


import jakarta.persistence.*;

@Entity
@Table(name = "position")
public class Position {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double latitude;
    private double longitude;
    private String date;
    private String imei;

    public Position() {}

    public Position(Long id, double latitude, double longitude, String date, String imei) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.date = date;
        this.imei = imei;
    }

    // Getters & Setters

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public double getLatitude() { return latitude; }
    public void setLatitude(double latitude) { this.latitude = latitude; }

    public double getLongitude() { return longitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public String getImei() { return imei; }
    public void setImei(String imei) { this.imei = imei; }
}
