package c.www.carovignoviva.monuments;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.squareup.picasso.Picasso;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

public class ViewPagerAdapter extends PagerAdapter {
    private Context context;
    private String[] imageUrls;

    ViewPagerAdapter(Context context, String[] imageUrls) {
        this.context = context;
        this.imageUrls = imageUrls;
    }

    @Override
    public int getCount() {
        return imageUrls.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        if (imageUrls[position] != "") {
            ImageView imageView = new ImageView(context);
            Picasso.get()
                    .load(imageUrls[position])
                    .fit()
                    .transform(new RoundedCornersTransformation(50, 5))
                    .centerCrop()
                    .into(imageView);
            container.addView(imageView);

            return imageView;
        }
        //TODO:Fare lista invece che 4 imagini statiche
        return null;
    }
    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}