package c.www.carovignoviva.citta;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import c.www.carovignoviva.R;
import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class CustomListViewCitta extends ArrayAdapter<Citta> {
    private int  resourceLayout;
    private Context mContext;
    CustomListViewCitta(Context context, int textViewResourceId,
                        ArrayList<Citta> objects) {
        super(context, textViewResourceId, objects);
        mContext=context;
        resourceLayout=textViewResourceId;
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(mContext);
            v = vi.inflate(resourceLayout, null);
        }

        Citta p = getItem(position);

        if (p != null) {
            TextView tt2 = v.findViewById(R.id.TextViewListTitleCitta);

            if (tt2 != null) {
                tt2.setText(p.getNome());
           }

            ImageView img= v.findViewById(R.id.imageListCitta);
            Picasso.get().load(p.getImage())
                    .resize(600,600)
                    .transform(new CropCircleTransformation())
                    .into(img);


        }


        return v;
    }




}
