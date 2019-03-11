package com.wenyizai.wangfuwen.wenyizai.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import com.wenyizai.wangfuwen.wenyizai.R;
import com.wenyizai.wangfuwen.wenyizai.entity.Fruit;
import com.wenyizai.wangfuwen.wenyizai.utils.NotificationUtil;

import java.util.List;


/**
 * Created by wangfuwen on 2016/12/17.
 */

public class FruitAdapter extends RecyclerView.Adapter<FruitAdapter.ViewHoder> {

    private Context mcontext;
    private List<Fruit> mFruit;


    public FruitAdapter(List<Fruit> fruitList) {
        mFruit = fruitList;
    }

    @Override
    public ViewHoder onCreateViewHolder(ViewGroup parent, int viewType) {

        if(mcontext == null){
            mcontext = parent.getContext();
        }
        View view = LayoutInflater.from(mcontext).inflate(R.layout.fruit_item,parent,false);
        final ViewHoder viewHoder = new ViewHoder(view);
        viewHoder.fruitImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = viewHoder.getAdapterPosition();
                Fruit fruit = mFruit.get(position);
                Toast.makeText(v.getContext(),"you clicked"+fruit.getName(),Toast.LENGTH_SHORT).show();

            }
        });


        viewHoder.fruitName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = viewHoder.getAdapterPosition();
                Fruit fruit = mFruit.get(position);
                Toast.makeText(v.getContext(),"you clicked"+fruit.getName(),Toast.LENGTH_SHORT).show();
//                NotificationUtil notificationUtil = new NotificationUtil(1);
            }
        });

//        return new ViewHoder(view);
        return viewHoder;
    }

    @Override
    public int getItemCount() {
        return mFruit.size();
    }


    @Override
    public void onBindViewHolder(ViewHoder holder, int position) {

        Fruit fruit = mFruit.get(position);
        holder.fruitName.setText(fruit.getName());
        Glide.with(mcontext).load(fruit.getImageId()).into(holder.fruitImage);

    }

    static class ViewHoder extends RecyclerView.ViewHolder{

        CardView cardView;
        ImageView fruitImage;
        TextView fruitName;

        public ViewHoder(View view){
            super(view);
            cardView = (CardView)view;
            fruitImage = (ImageView) view.findViewById(R.id.fruit_image);
            fruitName = (TextView) view.findViewById(R.id.fruit_name);
        }

    }


}
