package c.www.carovignoviva.events;

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

public class CustomListViewEvents extends ArrayAdapter<Event> {
    private int  resourceLayout;
    private Context mContext;
    CustomListViewEvents(Context context, int textViewResourceId,
                         ArrayList<Event> objects) {
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

        Event p = getItem(position);

        if (p != null) {
            TextView ttvisite = v.findViewById(R.id.textViewListVisiteEvent);
            TextView ttvisite2 = v.findViewById(R.id.textViewListVisiteEvent2);
            TextView tt2 = v.findViewById(R.id.TextViewListTitleEvent);

            if (tt2 != null) {
                tt2.setText(p.getNome());
           }

            if (ttvisite != null) {
                ttvisite.setText(p.getOra_inizio());
                ttvisite2.setText(p.getOra_fine());
            }
            ImageView img= v.findViewById(R.id.imageListEventi);
            Picasso.get().load(p.getImage())
                    .resize(400,400)
                    .transform(new CropCircleTransformation())
                    .into(img);


        }


        return v;
    }




}
