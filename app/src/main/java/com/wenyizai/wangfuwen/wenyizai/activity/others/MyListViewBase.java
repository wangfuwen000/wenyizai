package com.wenyizai.wangfuwen.wenyizai.activity.others;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.wenyizai.wangfuwen.wenyizai.R;
import com.wenyizai.wangfuwen.wenyizai.service.MyApplication;
import com.wenyizai.wangfuwen.wenyizai.utils.LogUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;



//import com.orhanobut.logger.Logger;

/**
 * Created by user on 15/8/24.
 */
public class MyListViewBase extends Activity {

    private ListView listView;

    String key = "";

    //定义一个动态数组
    ArrayList<HashMap<String, Object>> listItem;

    /**
     * Called when the activity is first created.
     */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_demo1);

        Bundle bundle = this.getIntent().getExtras();
        key = bundle.getString("key");

        listView = (ListView) findViewById(R.id.iv);
        MyAdapter mAdapter = new MyAdapter(this);//得到一个MyAdapter对象
        listView.setAdapter(mAdapter);//为ListView绑定Adapter

//        /**为ListView添加点击事件*/
//        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
//                Log.v("MyListViewBase", "你点击了ListView条目" + arg2);//在LogCat中输出信息
//            }
//        });
    }


    /**
     * 获取数据从本地文件中
     */
    private ArrayList<HashMap<String, Object>> getDataFromLocalFile() {
        String log = LogUtil.ReadLog();
        ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
        try {
            JSONObject jsonObject = new JSONObject(log);
            JSONArray jsonArray = jsonObject.getJSONArray("key");
            for (int i = 0; i < jsonArray.length(); i++) {
                HashMap<String, Object> map = new HashMap<String, Object>();
                JSONObject item = jsonArray.getJSONObject(i);
                Object ItemTile = item.getString("ItemTitle");
                Object ItemText = item.getString("ItemText");

                map.put("ItemTitle", ItemTile);
                map.put("ItemText", ItemText);
                list.add(map);

            }
        } catch (Exception e) {

        }
        return list;
    }


    /**
     * 获取数据从内存中
     */
    private ArrayList<HashMap<String, Object>> getDateFromRaw() {

        ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
        /**为动态数组添加数据*/
        for (int i = 0; i < 30; i++) {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("ItemTitle", "第" + i + "行");
            map.put("ItemText", "这是第" + i + "行");
            list.add(map);
        }
        return list;
    }

    /**
     * 添加一个得到数据的方法，方便使用
     */
    private ArrayList<HashMap<String, Object>> getDateFromService() {
        ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
        try {
            JSONObject jsonObject = new JSONObject(key);
            JSONArray jsonArray = jsonObject.getJSONArray("key");
            for (int i = 0; i < jsonArray.length(); i++) {
                HashMap<String, Object> map = new HashMap<String, Object>();
                JSONObject item = jsonArray.getJSONObject(i);
                Object ItemTile = item.getString("ItemTitle");
                Object ItemText = item.getString("ItemText");

                map.put("ItemTitle", ItemTile);
                map.put("ItemText", ItemText);
                list.add(map);

            }
        } catch (Exception e) {

        }
        return list;
    }

    private ArrayList<HashMap<String, Object>> getListDatas() {
        ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
        if (MyApplication.havareponse) {
            list = getDataFromLocalFile();
        } else {
            list = getDateFromService();
        }

//        Logger.init("cat");
//        Logger.d("大家好，我是%d年出生的%s", 1994, "爱学习的少年");
        // 人造crash,崩溃追踪系统是 Crashlytics 和 Parse Crash Reporting https://testerhome.com/topics/3982
        int i = 1;
        System.out.println(i / 0);

        return list;
    }

    /**
     * 新建一个类继承BaseAdapter，实现视图与数据的绑定
     */
    private class MyAdapter extends BaseAdapter {
        private LayoutInflater mInflater;//得到一个LayoutInfalter对象用来导入布局

        /**
         * 构造函数
         */
        public MyAdapter(Context context) {
            this.mInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return getListDatas().size();//返回数组的长度
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        /**
         * 书中详细解释该方法
         */
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            //观察convertView随ListView滚动情况
            Log.v("MyListViewBase", "getView " + position + " " + convertView);

            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.item2, null);
                holder = new ViewHolder();
                /**得到各个控件的对象*/
                holder.title = (TextView) convertView.findViewById(R.id.ItemTitle);
                holder.text = (TextView) convertView.findViewById(R.id.ItemText);
                holder.bt = (Button) convertView.findViewById(R.id.ItemButton);
                convertView.setTag(holder);//绑定ViewHolder对象
            } else {
                holder = (ViewHolder) convertView.getTag();//取出ViewHolder对象
            }
            /**设置TextView显示的内容，即我们存放在动态数组中的数据*/
            holder.title.setText(getListDatas().get(position).get("ItemTitle").toString());
            holder.text.setText(getListDatas().get(position).get("ItemText").toString());
            holder.bt.setBackgroundResource(R.drawable.bird2);

            /**为Button添加点击事件*/
            holder.bt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(MyListViewBase.this, "您点击的位置是:" + position, Toast.LENGTH_SHORT).show();
                    Log.v("MyListViewBase", "你点击了按钮" + position);//打印Button的点击信息

                }
            });

            return convertView;
        }

    }

    /**
     * 存放控件
     */
    public final class ViewHolder

    {
        public TextView title;
        public TextView text;
        public Button bt;
    }
}