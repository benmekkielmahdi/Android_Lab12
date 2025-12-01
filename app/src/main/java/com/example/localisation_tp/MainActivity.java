package com.example.localisation_tp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private double latitude;
    private double longitude;
    RequestQueue requestQueue;

    String insertUrl = "http://10.0.2.2:8080/api/positions";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        requestQueue = Volley.newRequestQueue(getApplicationContext());

        LocationManager locationManager =
                (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        // Vérifier permission GPS
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }

        // Actualiser toutes les 60 sec et 150 m
        locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                60000,
                150,
                new LocationListener() {

                    @Override
                    public void onLocationChanged(Location location) {
                        latitude = location.getLatitude();
                        longitude = location.getLongitude();

                        String msg = "LAT: " + latitude + " - LON: " + longitude;
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();

                        addPosition(latitude, longitude);
                    }

                    @Override
                    public void onStatusChanged(String provider, int status, Bundle extras) { }

                    @Override
                    public void onProviderEnabled(String provider) { }

                    @Override
                    public void onProviderDisabled(String provider) { }
                }
        );

        // Bouton pour afficher la carte
        Button btnShowMap = findViewById(R.id.btnShowMap);
        btnShowMap.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, MapsActivity.class);
            startActivity(intent);
        });
    }

    void addPosition(final double lat, final double lon) {

        StringRequest request = new StringRequest(Request.Method.POST, insertUrl,
                response -> Toast.makeText(MainActivity.this, "Position envoyée !", Toast.LENGTH_SHORT).show(),
                error -> {
                    Toast.makeText(MainActivity.this, "Erreur d’envoi : " + error.toString(), Toast.LENGTH_LONG).show();
                    error.printStackTrace();
                }) {

            @Override
            public byte[] getBody() throws AuthFailureError {
                JSONObject json = new JSONObject();
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    json.put("latitude", lat);
                    json.put("longitude", lon);
                    json.put("date", sdf.format(new Date()));

                    String deviceId = Settings.Secure.getString(
                            getContentResolver(),
                            Settings.Secure.ANDROID_ID
                    );
                    json.put("imei", deviceId);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return json.toString().getBytes();
            }

            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }
        };

        requestQueue.add(request);
    }
}
