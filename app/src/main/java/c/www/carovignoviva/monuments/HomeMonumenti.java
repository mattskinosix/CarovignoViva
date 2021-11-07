package c.www.carovignoviva.monuments;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import c.www.carovignoviva.R;

public class HomeMonumenti extends FragmentActivity implements OnMapReadyCallback, LocationListener {
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    LocationManager locationManager;
    String provider;

    private ArrayList<Monumento> monumenti = new ArrayList<>();

    SlidingUpPanelLayout slidingUpPanelLayout;
    private GoogleMap mMap;
    Location location;
    ProgressDialog nDialog;
    FirebaseDatabase database;
    DatabaseReference refDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boolean reachable = false;
        FirebaseApp.initializeApp(this);
        nDialog = new ProgressDialog(this);
        nDialog.setMessage("Loading..");
        nDialog.setTitle("Sto consultando l'enciclopedia");
        nDialog.setIndeterminate(false);
        nDialog.setCancelable(true);
        nDialog.show();


        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        provider = locationManager.getBestProvider(new Criteria(), false);
        if (Locale.getDefault().getDisplayLanguage().equals("English"))
            this.getMonumentFromFirebase("carovigno", "monumenti");
        else
            this.getMonumentFromFirebase("carovigno", "monumenti");
        //CARICO I MONUMENTI DALLA PAGINA PHP
        // monumenti = new Monumento().monumentoFromJson( getBaseContext());
        if (provider != null && location != null && adapter != null)
            setDistances(location.getLatitude(), location.getLongitude());
        setContentView(R.layout.slideup);
        MapFragment mapFragment = MapFragment.newInstance();
        getFragmentManager().beginTransaction().add(R.id.map, mapFragment).commit();
        mapFragment.getMapAsync(this);
        slidingUpPanelLayout = findViewById(R.id.sliding_layout);

    }

    private void getMonumentFromFirebase(String... voids) {
        database = FirebaseDatabase.getInstance();

        DatabaseReference myRef = database.getReference("city/" + voids[0] + "/");
        myRef.get().addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                nDialog.dismiss();
                error();
            }
        });
        myRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {

                JSONArray json_array = new JSONArray();
                for (DataSnapshot children : task.getResult().child(voids[1]).getChildren()) {
                    Log.i("test", children.getKey());
                    JSONObject json_obj = new JSONObject();
                    for (DataSnapshot children2 : task.getResult().child(voids[1]).child(children.getKey()).getChildren()) {
                        try {

                            Log.i("test", children2.getKey());
                            json_obj.put(children2.getKey(), children2.getValue());

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    for (DataSnapshot children2 : task.getResult().child("image").child(children.getKey()).getChildren()) {
                        try {

                            Log.i("test", children2.getKey());
                            json_obj.put("img" + (Integer.parseInt(children2.getKey()) + 1), children2.getValue());

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    json_array.put(json_obj);
                }
                try {
                    Log.i("test", json_array.toString());
                    monumenti = new Monumento().monumentoFromJson(json_array);
                    if (monumenti != null) {
                        addMarker();
                        creaLista();
                        setCustomInfoWindows();
                        nDialog.dismiss();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            if (provider != null) locationManager.requestLocationUpdates(provider, 400, 1, this);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

            if (locationManager != null) locationManager.removeUpdates(this);
        }
    }

    private void error() {
        setContentView(R.layout.error_network);
        TextView textView = findViewById(R.id.errorview);
        textView.setText("intetnet è spento");

    }

    public boolean isOnline() {
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    private CustomListViewHome adapter;

    private void creaLista() {
        //CREO LA LISTA CON I DATI CONTENUTI NEL VETTORE DI MONUMENTI
        ListView listView = findViewById(R.id.listv);

        // creo e istruisco l'adattatore
        adapter = new CustomListViewHome(this, R.layout.listitem, monumenti);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adattatore, final View componente, int pos, long id) {
                double latitude = monumenti.get(pos).getLatitude() + .0002;
                double longitude;
                longitude = monumenti.get(pos).getLongitude();
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 19.0f));
                monumenti.get(pos).getMarker().showInfoWindow();
                slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            }
        });

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        checkLocationPermission();

        if (provider != null) {
            googleMap.setMyLocationEnabled(true);
            location = locationManager.getLastKnownLocation(provider);

        }
        addMarker();
        setCustomInfoWindows();
        ImageButton slideup = findViewById(R.id.button_up);
        slideup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
            }
        });
        //ESPANDO IL PANNELLO SLIDING
        slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);


    }


    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                new AlertDialog.Builder(this)
                        .setTitle(R.string.title_location_permission)
                        .setMessage(R.string.text_location_permission)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(HomeMonumenti.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {
                        //Request location updates:
                        provider = locationManager.getBestProvider(new Criteria(), false);

                        if (provider != null) {
                            locationManager.requestLocationUpdates(provider, 400, 1, this);
                            setDistances(locationManager.getLastKnownLocation(provider).getLatitude(), locationManager.getLastKnownLocation(provider).getLongitude());

                        }

                    }

                } else {

                }
                return;
            }

        }

    }

    private void setCustomInfoWindows() {
        if (mMap != null) {
            CustomInfoWindowGoogleMap customInfoWindow = new CustomInfoWindowGoogleMap(this, monumenti);
            mMap.setInfoWindowAdapter(customInfoWindow);
            mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {

                @Override
                public void onInfoWindowClick(Marker arg0) {
                    Intent intent = new Intent(HomeMonumenti.this, Information.class);
                    int i = 0;
                    //TO DO
                    while (!arg0.getTitle().equals(monumenti.get(i).getMarker().getTitle())) i++;
                    intent.putExtra("monumenti", monumenti.get(i));
                    startActivity(intent);
                }
            });
        }
    }

    private void setDistances(double lat, double lon) {
        for (Monumento monumento : monumenti) {
            monumento.setDistanceToMonument(lat, lon);
        }
        if (Build.VERSION.SDK_INT >= 24) monumenti.sort(new ComparatorDistance());
        if (adapter != null) adapter.notifyDataSetChanged();
    }


    private void addMarker() {
        if (mMap != null)
        for (Monumento monumento : monumenti) {
            monumento.setMarker(mMap.addMarker(new MarkerOptions()
                    .title(monumento.getNome())
                    .position(new LatLng(monumento.getLatitude(), monumento.getLongitude()))));
        }
    }


    @Override
    public void onLocationChanged(Location location) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            location = locationManager.getLastKnownLocation(provider);
            setDistances(location.getLatitude(), location.getLongitude());

        }


    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            location = locationManager.getLastKnownLocation(provider);
            setDistances(location.getLatitude(), location.getLongitude());

        }
    }

    @Override
    public void onProviderEnabled(String provider) {
        this.provider = locationManager.getBestProvider(new Criteria(), false);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            location = locationManager.getLastKnownLocation(provider);
            setDistances(location.getLatitude(), location.getLongitude());

        };
    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
