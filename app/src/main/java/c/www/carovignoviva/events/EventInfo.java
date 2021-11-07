package c.www.carovignoviva.events;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import c.www.carovignoviva.R;
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

public class EventInfo extends Activity  {


    Event event;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info_evento);


        Intent intent=getIntent();
            event =(Event)intent.getSerializableExtra("Citta");
            TextView text= findViewById(R.id.descrizione_info_evento);
        text.setText(Html.fromHtml(event.getDescription()));
        TextView titolo= findViewById(R.id.titolo_info_evento);
            titolo.setText(event.getNome());

        ImageView img = findViewById(R.id.image_evento);
        Picasso.get().load(event.getImage())
                .transform(new RoundedCornersTransformation(50,2))
                .into(img);
        ImageButton navigatore= findViewById(R.id.navigatore_evento);
            navigatore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri gmmIntentUri = Uri.parse("geo:0,0?q="+ event.getLuogo());
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);


            }
        });
    }





}
