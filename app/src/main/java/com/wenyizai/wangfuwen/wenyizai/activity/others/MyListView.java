package com.wenyizai.wangfuwen.wenyizai.activity.others;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.wenyizai.wangfuwen.wenyizai.R;

/**
 * Created by user on 15/8/24.
 */
public class MyListView extends Activity {


    private ListView lv;
    private static final String[] strs = new String[]{"first", "second", "third", "fourth"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_demo1);

        lv = (ListView) findViewById(R.id.iv);

        //simmple view
        // lv.setAdapter(new ArrayAdapter<String>(this,android.R.constraintlayout1.simple_list_item_1,strs));

        //single choice
        lv.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_single_choice, strs));
        lv.setChoiceMode(ListView.CHOICE_MODE_SINGLE);


        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                setTitle("您点击了位置" + i + "行");
                Toast.makeText(MyListView.this, "xxx" + i, Toast.LENGTH_LONG).show();
            }
        });

    }
}
