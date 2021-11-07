package c.www.carovignoviva.monuments;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;

import c.www.carovignoviva.R;
import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class CustomListViewHome extends ArrayAdapter<Monumento> {
    private int  resourceLayout;
    private Context mContext;
    CustomListViewHome(Context context, int textViewResourceId,
                       ArrayList<Monumento> objects) {
        super(context, textViewResourceId, objects);
        mContext=context;
        resourceLayout=textViewResourceId;
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Monumento p = getItem(position);
        if (p != null) {
            LayerDrawable layerDrawable = (LayerDrawable) ContextCompat.getDrawable(mContext, R.drawable.card_background);
            assert layerDrawable != null;
            GradientDrawable gradientDrawable = (GradientDrawable) layerDrawable
                    .findDrawableByLayerId(R.id.forma);
            gradientDrawable.setColor(Color.WHITE); // change color
            View v = convertView;

            if (v == null) {
                LayoutInflater vi;
                vi = LayoutInflater.from(mContext);
                v = vi.inflate(resourceLayout, null);
            }


            TextView ttvisite = v.findViewById(R.id.textViewListVisite);
            TextView tt2 = v.findViewById(R.id.TextViewListTitle);
            TextView distance = v.findViewById(R.id.distance);
            DecimalFormat df = new DecimalFormat("0.00");
            if (p.getDistance()!=0) {
                distance.setText(df.format(p.getDistance())+" KM");
            }
            if (tt2 != null) {
                tt2.setText(p.getNome());
            }

            if (ttvisite != null) {
                ttvisite.setText(p.getOrario());
            }
            ImageView img = v.findViewById(R.id.imageViewMonument);
            Picasso.get().load(p.getImage()[0])
                    .resize(600, 600)
                    .transform(new CropCircleTransformation())
                    .into(img);
            //img.setImageDrawable(new RetriveImageInternet().execute(p.getImage()).get(););


            return v;
        }
        return null;
    }




}
