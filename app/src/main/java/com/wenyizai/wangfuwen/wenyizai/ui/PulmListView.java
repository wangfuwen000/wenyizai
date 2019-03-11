package com.wenyizai.wangfuwen.wenyizai.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.AbsListView;
import android.widget.HeaderViewListAdapter;
import android.widget.ListView;

import com.wenyizai.wangfuwen.wenyizai.R;
import com.wenyizai.wangfuwen.wenyizai.activity.home.MainActivity;
import com.wenyizai.wangfuwen.wenyizai.adapter.PulmImplAdapter;

import java.util.List;



/**
 * Created by wangfuwen on 16/10/30.
 */

public class PulmListView extends ListView {

    private static final String TAG = MainActivity.class.getSimpleName();
    private LoadingMoreView mLoadingMoreView;
    private boolean mIsLoading;
    private boolean mIsPageFinished;
    private OnScrollListener onScrollListener;


    public PulmListView(Context context) {
        super(context);
        init();
    }

    public PulmListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PulmListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void  init(){
        mIsLoading = false; //初始化时没有处于加载状态
        mIsPageFinished = false;//初始化时默认还有更多可以加载
        mLoadingMoreView = new LoadingMoreView(getContext()); //实列一个FooterView

        setOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                //试用用户设置的OnscrollLisener
                if (onScrollListener != null) {
                    onScrollListener.onScrollStateChanged(view, scrollState);
                }

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                //调用用户设置的OnScrollListener
                if (onScrollListener != null) {
                    onScrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
                }

                //FisrtVisibleItem是当前屏幕能显示第一个元素的位置
                //visibleItemCount是当前屏幕能显示的元素的个数
                //totalItemCount是ListView包含元素的个数
                int lastVisibleItem = firstVisibleItem + visibleItemCount;
                if (!mIsLoading && !mIsPageFinished && lastVisibleItem == totalItemCount) {
                    if (mOnPullUpLoadMoreListner != null) {
                        mIsLoading = true; // 将加载更多进行时状态设置为true
                        showLoadMoreView(); // 显示加载更多布局
                        mOnPullUpLoadMoreListner.OnPullUpLoadMoreListner(); // 调用用户设置的加载更多回调接口
                    }
                }
            }
        });


    }
    private void showLoadMoreView() {
        // 这里将加载更多的根布局id设置为id_load_more_layout, 便于用户自定制加载更多布局.
        if (findViewById(R.id.loadingmoreview) == null) {
            addFooterView(mLoadingMoreView);
        }
    }


    /**
     * 加载更多结束后ListView回调方法.
     *
     * @param isPageFinished 分页是否结束
     * @param newItems       分页加载的数据
     * @param isFirstLoad    是否第一次加载数据(用于配置下拉刷新框架使用, 避免出现页面闪现)
     */

    public  void  onFinishLoading(boolean isPageFinished, List<?> newItems, boolean isFirstLoad){
        mIsLoading =false; // 标记当前已经没有加载更多的程序在执行
        setPageFinished(isPageFinished); // 设置分页是否结束标志并移除FooterView
        //添加更新后的数据
        if(newItems != null && newItems.size()>0){
            PulmImplAdapter adapter = (PulmImplAdapter) ((HeaderViewListAdapter) getAdapter()).getWrappedAdapter();
            adapter.addMoreItems(newItems, isFirstLoad);
        }

    }

    private void  setPageFinished(boolean ispageFinished){
        mIsPageFinished = ispageFinished;
        removeFooterView(mLoadingMoreView);

    }

    /**
     * 上拉加载更多的回调接口
     */
    public interface OnPullUpLoadMoreListner {
        void  OnPullUpLoadMoreListner();
    }

    private OnPullUpLoadMoreListner mOnPullUpLoadMoreListner;

    /**
     * 设置上拉加载更多的回调接口.
     * @paraml 上拉加载更多的回调接口
     */
    public void setmOnPullUpLoadMoreListner(OnPullUpLoadMoreListner mOnPullUpLoadMoreListner) {
        Log.i(TAG,"invoked method setmOnPullUpLoadMoreListner");
        this.mOnPullUpLoadMoreListner = mOnPullUpLoadMoreListner;
    }
}
