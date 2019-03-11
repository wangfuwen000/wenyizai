package com.wenyizai.wangfuwen.wenyizai.dcs;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.baidu.duer.dcs.androidapp.DcsSampleBaseActivity;
import com.baidu.duer.dcs.androidapp.DcsSampleMainActivity;
import com.baidu.duer.dcs.oauth.api.BaiduDialog;
import com.baidu.duer.dcs.oauth.api.BaiduDialogError;
import com.baidu.duer.dcs.oauth.api.BaiduException;
import com.baidu.duer.dcs.oauth.api.BaiduOauthImplicitGrant;
import com.baidu.duer.dcs.util.LogUtil;
import com.wenyizai.wangfuwen.wenyizai.R;
import com.wenyizai.wangfuwen.wenyizai.activity.others.Charity;

import static com.wenyizai.wangfuwen.wenyizai.R.layout.dcs_sample_activity_oauth;

public class MyDcsSampleOAuthActivity extends DcsSampleBaseActivity implements View.OnClickListener {
    // 需要开发者自己申请client_id
    // client_id，就是oauth的client_id
    private static final String CLIENT_ID = "K2t8aZZq86AFohpYjxXw9bW6xofxwmM9";
    // 是否每次授权都强制登陆
    private boolean isForceLogin = false;
    // 是否每次都确认登陆
    private boolean isConfirmLogin = true;
    private EditText editTextClientId;
    private Button oauthLoginButton;
    private BaiduOauthImplicitGrant baiduOauthImplicitGrant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("xxxxxxxxx","DcsSampleOAuthActivity oncreate");
        setContentView(R.layout.dcs_sample_activity_oauth);
        initView();
        setOnClickListener();
    }

    private void setOnClickListener() {
        oauthLoginButton.setOnClickListener(this);
    }

    private void initView() {
        editTextClientId = (EditText) findViewById(com.baidu.duer.dcs.R.id.edit_client_id);
        oauthLoginButton = (Button) findViewById(com.baidu.duer.dcs.R.id.btn_login);
        editTextClientId.setText(CLIENT_ID);

        Log.i("xxxxxxxxx","DcsSampleOAuthActivityinitView");
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == com.baidu.duer.dcs.R.id.btn_login) {
            String clientId = editTextClientId.getText().toString();
            if (!TextUtils.isEmpty(clientId) && !TextUtils.isEmpty(clientId)) {
                baiduOauthImplicitGrant = new BaiduOauthImplicitGrant(clientId, MyDcsSampleOAuthActivity.this.getApplication());
                baiduOauthImplicitGrant.authorize(MyDcsSampleOAuthActivity.this, isForceLogin, isConfirmLogin, new BaiduDialog
                        .BaiduDialogListener() {
                    @Override
                    public void onComplete(Bundle values) {
                        Toast.makeText(MyDcsSampleOAuthActivity.this.getApplicationContext(),
                                getResources().getString(com.baidu.duer.dcs.R.string.login_succeed),
                                Toast.LENGTH_SHORT).show();
                        startMainActivity();
                    }

                    @Override
                    public void onBaiduException(BaiduException e) {

                    }

                    @Override
                    public void onError(BaiduDialogError e) {
                        if (null != e) {
                            String toastString = TextUtils.isEmpty(e.getMessage())
                                    ? MyDcsSampleOAuthActivity.this.getResources()
                                    .getString(com.baidu.duer.dcs.R.string.err_net_msg) : e.getMessage();
                            Toast.makeText(MyDcsSampleOAuthActivity.this.getApplicationContext(), toastString,
                                    Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancel() {
                        LogUtil.d("cancle", "I am back");
                    }
                });
            } else {
                Toast.makeText(MyDcsSampleOAuthActivity.this.getApplicationContext(),
                        getResources().getString(com.baidu.duer.dcs.R.string.client_id_empty),
                        Toast.LENGTH_SHORT).show();
            }

        } else {
        }
    }

    private void startMainActivity() {
        Log.i("xxxxxxxxx","DcsSampleMainActivityDcsSampleMainActivityDcsSampleMainActivity");
        Intent intent = new Intent(MyDcsSampleOAuthActivity.this, Charity.class);
        intent.putExtra("baidu", baiduOauthImplicitGrant);
        startActivity(intent);
        finish();
    }
}
