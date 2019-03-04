package com.mvw.medicalvisualteaching.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.mvw.medicalvisualteaching.R;
import com.mvw.medicalvisualteaching.config.Constant;
import com.mvw.medicalvisualteaching.utils.SPUtil;

import java.util.ArrayList;

/**
 * 引导页
 * Created by a on 2016/11/18.
 */
public class GuideActivity extends BaseActivity{

    /** 存放ViewPager图片地址的数组 */
    private int[] guide_drawable = { R.mipmap.guide1, R.mipmap.guide2, R.mipmap.guide3,R.mipmap.guide4 };
    /** 存放图片的集合 */
    private ArrayList<ImageView> guide_imageView = new ArrayList<>();
    private Context context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        context = this;
        init();
    }

    private void init(){
        ViewPager viewPager = (ViewPager) findViewById(R.id.vp_guide);
        for(int i : guide_drawable){
            ImageView imageView = new ImageView(this);
            imageView.setImageResource(i);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            guide_imageView.add(imageView);
        }
        viewPager.setAdapter(new MyPagerAdapter());
        // 对viewPager最后一页设置点击监听，点击后进入主页面
        guide_imageView.get(guide_imageView.size() - 1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //进入登录页
                SPUtil.getInstance(context).save(Constant.GUIDE_FLAG,false);
                startActivity(new Intent(context,LoginActivity.class));
                finish();
            }
        });
    }

    /** ViewPager的适配器 */
    class MyPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return guide_imageView.size();
        }

        // 相当于适配器的getView
        // container:就是ViewPager
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            // 得到集合中的图片，添加到容器中，并返回
            ImageView imageView = guide_imageView.get(position);
            container.addView(imageView);
            return imageView;

        }
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
        @Override
        public boolean isViewFromObject(View View, Object object) {
            return View == object;
        }
    }
}
