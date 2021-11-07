package c.www.carovignoviva.monuments;

import android.content.Context;

import com.google.android.gms.maps.model.Marker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

class Monumento implements Serializable {
    private String codice;
    private String nome;
    private double longitude;
    private double latitude;
    private String description;
    private String[] image=new String[4];
    private String detail;
    private String orario;
    private float distance;
    private transient Marker marker;


    ArrayList<Monumento>  monumentoFromJson(JSONArray jsonarray) throws JSONException {

        ArrayList<Monumento> monumenti=new ArrayList<> ();
        for (int i = 0; i < jsonarray.length(); i++) {
            JSONObject jsonobject = jsonarray.getJSONObject(i);
            Monumento monumento=new Monumento();
            monumento.nome = jsonobject.getString("nome");
            monumento.longitude = jsonobject.getDouble("longitude");
            monumento.latitude= jsonobject.getDouble("latitude");
            monumento.description = jsonobject.getString("description");
            monumento.detail= jsonobject.getString("detail");
            monumento.orario= jsonobject.getString("orario");
            if (!jsonobject.isNull("img1"))
                monumento.image[0]=jsonobject.getString("img1");
            if (!jsonobject.isNull("img2"))
                monumento.image[1]=jsonobject.getString("img2");
            if (!jsonobject.isNull("img3"))
                monumento.image[2]=jsonobject.getString("img3");
            if (!jsonobject.isNull("img4"))
                monumento.image[3]=jsonobject.getString("img4");
            monumento.distance=0;
            monumenti.add(monumento);
        }
        return monumenti;
    }

    void setDistanceToMonument(double lat_a, double lng_a) {

        double earthRadius = 6371; //meters
        double dLat = Math.toRadians(latitude-lat_a);
        double dLng = Math.toRadians(longitude-lng_a);
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(Math.toRadians(lat_a)) * Math.cos(Math.toRadians(latitude)) *
                        Math.sin(dLng/2) * Math.sin(dLng/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        distance= (float) (earthRadius * c);
    }

    String getCodice() {
        return codice;
    }

    String getNome() {
        return nome;
    }

    double getLongitude() {
        return longitude;
    }

    double getLatitude() {
        return latitude;
    }

    String getDescription() {
        return description;
    }

    String[] getImage() {
        return image;
    }

    String getDetail() {
        return detail;
    }

    String getOrario() {
        return orario;
    }

    Marker getMarker() {
        return marker;
    }

    void setMarker(Marker marker) {
        this.marker = marker;
    }

    float getDistance() {
        return distance;
    }


}
