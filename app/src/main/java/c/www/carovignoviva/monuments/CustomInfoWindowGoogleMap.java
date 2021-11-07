package c.www.carovignoviva.monuments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import c.www.carovignoviva.R;
import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class CustomInfoWindowGoogleMap implements GoogleMap.InfoWindowAdapter{

    private final Context context;
    private ArrayList<Monumento> monumenti;
    CustomInfoWindowGoogleMap(Context ctx, ArrayList<Monumento> monument){
        monumenti=monument;
        context = ctx;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        @SuppressLint("InflateParams") View view =((Activity)context).getLayoutInflater().inflate(R.layout.info_windows, null);
        TextView title = view.findViewById(R.id.title);
       // TextView details = view.findViewById(R.id.details);
        ImageView img = view.findViewById(R.id.imageView2);
       // TextView description = view.findViewById(R.id.description);
       // TextView trasport = view.findViewById(R.id.transport);
        int i=0;
        while (!marker.getTitle().equals(monumenti.get(i).getMarker().getTitle())) i++;
        title.setText(marker.getTitle());
        if (marker.getTag() == null ) {
            Log.i("prepareInfoView", "fetching image");
            Picasso.get().load(monumenti.get(i).getImage()[0]).fetch(new MarkerCallback(marker));
        }
        else  {
            Log.wtf("prepareInfoView", "building info window");
        }
        Log.i("prepareInfoView", "fetching image");

        Picasso.get().load(monumenti.get(i).getImage()[0])
                .resize(350,350)
                .transform(new CropCircleTransformation()).into(img);
        return view;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }
    static class MarkerCallback implements Callback {
        Marker marker;

        MarkerCallback(Marker marker) {
            this.marker=marker;
        }


        @Override
        public void onSuccess() {
            Log.i("ERROR", "image got, should rebuild window");
            if (marker != null && marker.isInfoWindowShown()) {
                Log.i("OK", "conditions met, redrawing window");
                marker.setTag(Boolean.TRUE);
                marker.showInfoWindow();
            }
        }

        @Override
        public void onError(Exception e) {

        }
    }
}

