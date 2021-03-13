package com.example.sharedthings;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class UserInfoActivity extends AppCompatActivity {
    ImageView iv_home, iv_addRequest, iv_user_info, iv_userInfo_icon;
    TextView tv_userInfo_username, tv_userInfo_uid, tv_userInfo_uidHeader, tv_userInfo_email,
            tv_userInfo_emailHeader, tv_userInfo_phone, tv_userInfo_phoneHeader;
    Button btn_userInfo_logout, btn_userInfo_addUserIcon;
    private static final int GET_IMAGE = 1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        iv_home = findViewById(R.id.iv_home);
        iv_addRequest = findViewById(R.id.iv_addRequest);
        iv_user_info = findViewById(R.id.iv_user_info);
        iv_userInfo_icon = findViewById(R.id.iv_userInfo_icon);
        tv_userInfo_username = findViewById(R.id.tv_userInfo_username);
        tv_userInfo_uid = findViewById(R.id.tv_userInfo_uid);
        tv_userInfo_uidHeader = findViewById(R.id.tv_userInfo_uidHeader);
        tv_userInfo_email = findViewById(R.id.tv_userInfo_email);
        tv_userInfo_emailHeader = findViewById(R.id.tv_userInfo_emailHeader);
        tv_userInfo_phone = findViewById(R.id.tv_userInfo_phone);
        tv_userInfo_phoneHeader = findViewById(R.id.tv_userInfo_phoneHeader);
        btn_userInfo_logout = findViewById(R.id.btn_userInfo_logout);
        btn_userInfo_addUserIcon = findViewById(R.id.btn_userInfo_addUserIcon);

        String username = getSharedPreferences("user", MODE_PRIVATE)
                .getString("username", "");
        String uid = getSharedPreferences("user", MODE_PRIVATE)
                .getString("uid", "");
        String email = getSharedPreferences("user", MODE_PRIVATE)
                .getString("email", "");
        String phone = getSharedPreferences("user", MODE_PRIVATE)
                .getString("phone", "");
        /*String iconUriString = getSharedPreferences("user", MODE_PRIVATE)
                .getString("iconURI", "");

        if(iconUriString!=""){
            Toast.makeText(UserInfoActivity.this, iconUriString,
                    Toast.LENGTH_SHORT).show();
            Uri iconURI = Uri.parse(iconUriString);
            iv_userInfo_icon.setImageURI(iconURI);
        }*/

        tv_userInfo_username.setText(username);
        tv_userInfo_uid.setText(uid);
        tv_userInfo_email.setText(email);
        tv_userInfo_phone.setText(phone);

        btn_userInfo_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences pref = getSharedPreferences("user", MODE_PRIVATE);
                pref.edit().putBoolean("loginStatus", false).apply();
                Intent intent = new Intent(UserInfoActivity.this, MainActivity.class);
                startActivity(intent);
                UserInfoActivity.this.finish();
            }
        });

        btn_userInfo_addUserIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent getGalleryImage = new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityForResult(getGalleryImage, GET_IMAGE);
            }
        });

        iv_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserInfoActivity.this, HomeActivity.class);
                startActivity(intent);
                UserInfoActivity.this.finish();
            }
        });

        iv_addRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserInfoActivity.this, AddRequestActivity.class);
                startActivity(intent);
                UserInfoActivity.this.finish();
            }
        });


    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == GET_IMAGE && data != null){
            Uri uri = data.getData();
            /*String uriString = uri.toString();
            SharedPreferences pref = getSharedPreferences("user", MODE_PRIVATE);
            pref.edit()
                    .putString("iconURI", uriString)
                    .apply();*/
            iv_userInfo_icon.setImageURI(uri);
        }
    }
}