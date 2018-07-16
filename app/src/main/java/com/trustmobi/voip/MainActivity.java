package com.trustmobi.voip;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import static com.trustmobi.mixin.voip.SettingsCompat.REQUEST_SYSTEM_ALERT_WINDOW;
import static com.trustmobi.voip.VoipUtil.serverUrl;


public class MainActivity extends AppCompatActivity {
    EditText edit_domain;
    EditText edit_username;
    EditText edit_pwd;
    EditText edit_to;

    private boolean isDebug = true;

    private int REQUEST_CODE = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        edit_domain = findViewById(R.id.domain);
        edit_username = findViewById(R.id.username);
        edit_pwd = findViewById(R.id.password);
        edit_to = findViewById(R.id.to);
        edit_domain.setText(serverUrl);
        checkPermission();
        edit_domain.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                serverUrl = s.toString();
            }
        });
        NarrowTips();
    }

    private void NarrowTips() {
        VoipUtil.setNarrowCallBack(this);
    }

    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {
            //进入到这里代表没有权限.
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CALL_PHONE) ||
                    ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) ||
                    ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.RECORD_AUDIO) ||
                    ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA) ||
                    ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_CONTACTS)
                    ) {
                //已经禁止提示了
                Toast.makeText(MainActivity.this, "您已禁止该权限，需要重新开启。", Toast.LENGTH_SHORT).show();
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CALL_PHONE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.RECORD_AUDIO,
                                Manifest.permission.CAMERA,
                                Manifest.permission.READ_CONTACTS
                        },
                        REQUEST_CODE);

            }

        }
    }

    public void openVoip(View view) {
        VoipUtil.startService(this);


    }

    public void login(View view) {
        VoipUtil.login(edit_username.getText().toString(), edit_pwd.getText().toString());


    }

    public void logout(View view) {
        VoipUtil.stopService(this);
    }

    public void call(View view) {
        String callName = edit_to.getText().toString().trim();
        VoipUtil.outgoing(this, callName, false, "1234567890123456789012345678901234567890");
    }

    public void video(View view) {
        String callName = edit_to.getText().toString().trim();
        VoipUtil.outgoing(this, callName, true, "1234567890123456789012345678901234567890");
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_SYSTEM_ALERT_WINDOW) {
            VoipUtil.openNarrow();
        }
    }


}
