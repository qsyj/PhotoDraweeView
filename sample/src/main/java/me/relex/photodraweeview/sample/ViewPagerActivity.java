package me.relex.photodraweeview.sample;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.imagepipeline.image.ImageInfo;

import me.relex.circleindicator.CircleIndicator;
import me.relex.photodraweeview.PhotoDraweePagerAdapter;
import me.relex.photodraweeview.PhotoDraweeView;
import me.relex.photodraweeview.PhotoDraweeViewPager;

public class ViewPagerActivity extends AppCompatActivity {

    private Toolbar mToolbar;

    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, ViewPagerActivity.class));
    }

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewpager);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setNavigationOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                onBackPressed();
                            }
                        });

        CircleIndicator indicator = (CircleIndicator) findViewById(R.id.indicator);
        MultiTouchViewPager viewPager = (MultiTouchViewPager) findViewById(R.id.view_pager);
        viewPager.setOffscreenPageLimit(3);
        viewPager.setPageMargin(30);
        viewPager.setAdapter(new DraweePagerAdapter());
        viewPager.setOnPageChangeListener(new PhotoDraweeViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//                Log.e(ViewPagerActivity.class.getSimpleName(), "onPageScrolled positionOffset:" + positionOffset + ",positionOffsetPixels:" + positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public class DraweePagerAdapter extends PhotoDraweePagerAdapter {

        private int[] mDrawables = new int[] {
                R.drawable.viewpager_1, R.drawable.viewpager_2, R.drawable.viewpager_3,R.drawable.viewpager_1, R.drawable.viewpager_2, R.drawable.viewpager_3,R.drawable.viewpager_1, R.drawable.viewpager_2, R.drawable.viewpager_3
        };
        /*private int[] mDrawables = new int[] {
                R.drawable.viewpager_1
        };*/
        @Override public int getCount() {
            return mDrawables.length;
        }

        @Override public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override public Object instantiateItem(ViewGroup viewGroup, int position) {
            String res = "res:///" + mDrawables[position];
            final PhotoDraweeView photoDraweeView = new PhotoDraweeView(viewGroup.getContext());
            photoDraweeView.setPhotoUri(res, R.drawable.ic_error,
                    ScalingUtils.ScaleType.CENTER, -1,ScalingUtils.ScaleType.CENTER, true,new PhotoDraweeView.DownLoadListener() {
                        @Override
                        public void onFinalImageSet(PhotoDraweeView view, Uri uri, String id, ImageInfo imageInfo, Animatable animatable) {

                        }

                        @Override
                        public void onIntermediateImageSet(PhotoDraweeView view, Uri uri, String id, ImageInfo imageInfo) {

                        }

                        @Override
                        public void onFailure(PhotoDraweeView view, String id, Throwable throwable) {

                        }

                        @Override
                        public void onIntermediateImageFailed(PhotoDraweeView view, String id, Throwable throwable) {

                        }
                    });
            try {
                viewGroup.addView(photoDraweeView, ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return photoDraweeView;
        }
    }
}
