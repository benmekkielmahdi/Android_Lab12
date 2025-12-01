package com.example.localisation_tp;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;
import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.BoundingBox;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

public class MapsActivity extends AppCompatActivity {

    private MapView map;
    private RequestQueue requestQueue;
    private String apiUrl = "http://10.0.2.2:8080/api/positions"; // ton API Spring Boot

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Obligatoire : User-Agent pour OSMDroid
        Configuration.getInstance().setUserAgentValue(getPackageName());

        setContentView(R.layout.activity_maps);

        map = findViewById(R.id.map);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setMultiTouchControls(true);

        requestQueue = Volley.newRequestQueue(this);

        // Charger et afficher les positions
        loadPositions();
    }

    private void loadPositions() {
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, apiUrl, null,
                response -> {
                    try {
                        BoundingBox bounds = null;

                        for (int i = 0; i < response.length(); i++) {
                            JSONObject obj = response.getJSONObject(i);
                            double lat = obj.getDouble("latitude");
                            double lon = obj.getDouble("longitude");
                            String date = obj.getString("date");

                            // Créer un point et un marqueur
                            GeoPoint point = new GeoPoint(lat, lon);
                            Marker marker = new Marker(map);
                            marker.setPosition(point);
                            marker.setTitle("Date : " + date);
                            map.getOverlays().add(marker);

                            // Calculer les limites pour centrer la carte
                            if (bounds == null) {
                                bounds = new BoundingBox(lat, lon, lat, lon);
                            } else {
                                bounds = new BoundingBox(
                                        Math.max(bounds.getLatNorth(), lat),
                                        Math.max(bounds.getLonEast(), lon),
                                        Math.min(bounds.getLatSouth(), lat),
                                        Math.min(bounds.getLonWest(), lon)
                                );
                            }
                        }

                        // Centrer et zoomer sur toutes les positions

                        // Centrer et zoomer sur toutes les positions
                        if (bounds != null) {
                            map.zoomToBoundingBox(bounds, true); // utiliser MapView directement
                        }
                        map.invalidate(); // rafraîchir la carte


                        map.invalidate(); // rafraîchir la carte

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                },
                error -> error.printStackTrace()
        );

        requestQueue.add(request);
    }
}
