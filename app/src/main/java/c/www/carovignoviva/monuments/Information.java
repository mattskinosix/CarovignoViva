package c.www.carovignoviva.monuments;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.maps.OnStreetViewPanoramaReadyCallback;
import com.google.android.gms.maps.StreetViewPanorama;
import com.google.android.gms.maps.StreetViewPanoramaFragment;
import com.google.android.gms.maps.model.LatLng;

import c.www.carovignoviva.R;

public class Information extends Activity implements OnStreetViewPanoramaReadyCallback {


    private StreetViewPanorama streetView;
    Monumento city;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.informazioni_complete);


        Intent intent=getIntent();
            city=(Monumento)intent.getSerializableExtra("monumenti");


            //   TextView description = view.findViewById(R.id.description);
            // TextView trasport = view.findViewById(R.id.transport);
            final StreetViewPanoramaFragment streetViewPanoramaFragment =
                    (StreetViewPanoramaFragment) getFragmentManager()
                            .findFragmentById(R.id.streetviewpanorama);
            streetViewPanoramaFragment.getStreetViewPanoramaAsync(this);
            TextView text= findViewById(R.id.descrizione_info_complete);
        text.setText(Html.fromHtml(city.getDescription()));
        TextView titolo= findViewById(R.id.titolo_info_complete);
            titolo.setText(city.getNome());
        ViewPager viewPager = findViewById(R.id.Imagepager);

        ViewPagerAdapter adapter = new ViewPagerAdapter(this, city.getImage());
        viewPager.setAdapter(adapter);

        ImageButton navigatore= findViewById(R.id.navigatore);
        ImageButton streetbutton= findViewById(R.id.streetview);

            navigatore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri gmmIntentUri = Uri.parse("geo:"+city.getLatitude()+","+city.getLongitude()+"?q="+city.getNome());
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);


            }
        });

        streetbutton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                FrameLayout frame= findViewById(R.id.framefull);
                frame.setVisibility(View.VISIBLE);
            }
        });

        streetbutton.setVisibility(View.VISIBLE);
        ImageButton close= findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                FrameLayout frame= findViewById(R.id.framefull);
                frame.setVisibility(View.GONE);
            }
        });

    }


    @Override
    public void onStreetViewPanoramaReady(StreetViewPanorama streetViewPanorama) {
        streetViewPanorama.setPosition(new LatLng(city.getLatitude(),city.getLongitude()));
        streetView=streetViewPanorama;

    }



}
