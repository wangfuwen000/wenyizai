package com.wenyizai.wangfuwen.wenyizai.activity.others;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.wenyizai.wangfuwen.wenyizai.R;
import com.wenyizai.wangfuwen.wenyizai.activity.base.BaseActivity;

/**
 * Created by user on 15/12/1.
 */
public class ImageLoaderActivity extends BaseActivity {

    String imageUrl = "http://123.57.11.138:8080/images/20151201143432.jpg";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.imageloader);
//        Logger.d("ddd");
        final ImageView mimageView = (ImageView)findViewById(R.id.image);

        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.ic_stub)
                .showImageOnFail(R.drawable.ic_error)
                .cacheInMemory(true).cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565).build();

        ImageLoader.getInstance().displayImage(imageUrl,mimageView,options);


        ImageSize imageSize = new ImageSize(400,400);
//        ImageLoader.getInstance().loadImage(imageUrl,imageSize,options, new SimpleImageLoadingListener() {
//            @Override
//            public void onLoadingStarted(String s, View view) {
//
//            }
//
//            @Override
//            public void onLoadingFailed(String s, View view, FailReason failReason) {
//
//            }
//
//            @Override
//            public void onLoadingComplete(String s, View view, Bitmap bitmap) {
//                mimageView.setImageBitmap(bitmap);
//
//            }
//
//            @Override
//            public void onLoadingCancelled(String s, View view) {
//
//            }
//        });


    }

    @Override
    protected void initVariables() {

    }

    @Override
    protected void initView(Bundle savedInstanceState) {

    }

    @Override
    protected void loadData() {

    }
}
