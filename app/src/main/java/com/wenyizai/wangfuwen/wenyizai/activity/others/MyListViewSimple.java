package com.wenyizai.wangfuwen.wenyizai.activity.others;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.wenyizai.wangfuwen.wenyizai.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by user on 15/8/24.
 */
public class MyListViewSimple extends Activity {

    private ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_demo1);

        lv = (ListView) findViewById(R.id.iv);

        ArrayList<HashMap<String, Object>> listitem = new ArrayList<HashMap<String, Object>>();

        for (int i = 0; i < 10; i++) {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("ItemImage", R.drawable.market);
            map.put("ItemTitle", "第" + i + "行");
            map.put("ItemText", "这是第" + i + "行");
            listitem.add(map);
        }

        SimpleAdapter simpleAdapter = new SimpleAdapter(this, listitem,
                R.layout.item,
                new String[]{"ItemImage", "ItemTitle", "ItemText"},
                new int[]{R.id.ItemImage, R.id.ItemTitle, R.id.ItemText});

        lv.setAdapter(simpleAdapter);

    }
}
