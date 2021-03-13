package com.example.sharedthings;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class AddRequestActivity extends AppCompatActivity {

    ImageView iv_home, iv_addRequest, iv_user_info;
    EditText et_addRequest_title, et_addRequest_price, et_addRequest_description;
    RadioGroup radioGroup;
    Button btn_addRequest, btn_resetRequest;
    ProgressDialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_request);

        iv_home = findViewById(R.id.iv_home);
        iv_addRequest = findViewById(R.id.iv_addRequest);
        iv_user_info = findViewById(R.id.iv_user_info);
        et_addRequest_title = findViewById(R.id.et_addRequest_title);
        et_addRequest_price = findViewById(R.id.et_addRequest_price);
        et_addRequest_description = findViewById(R.id.et_addRequest_description);
        radioGroup = findViewById(R.id.radioGroup);
        btn_addRequest = findViewById(R.id.btn_addRequest);
        btn_resetRequest = findViewById(R.id.btn_resetRequest);
        et_addRequest_price.setText("$");

        iv_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddRequestActivity.this, HomeActivity.class);
                startActivity(intent);
                AddRequestActivity.this.finish();
            }
        });


        iv_user_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddRequestActivity.this, UserInfoActivity.class);
                startActivity(intent);
                AddRequestActivity.this.finish();
            }
        });

        btn_addRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String title = et_addRequest_title.getText().toString().trim();
                final String price = et_addRequest_price.getText().toString().trim();
                final String des = et_addRequest_description.getText().toString().trim();
                if(title.length()>0 && price.length()>0 && des.length()>0 && price.contains("$")){
                    addRequest(title, des, price);
                    resetRequest();
                } else if(!price.contains("$")){
                    Toast.makeText(AddRequestActivity.this,
                            "Input $ symbol in price!", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(AddRequestActivity.this,
                            "Blank input!", Toast.LENGTH_LONG).show();
                }

            }
        });

        btn_resetRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetRequest();
            }
        });

    }

    private void resetRequest() {
        et_addRequest_title.setText("");
        et_addRequest_price.setText("$");
        et_addRequest_description.setText("");
    }

    private void addRequest(final String title, final String des, final String price) {
        loading =  ProgressDialog.show(this,"Adding request","please wait",false,
                true);

        final String type;
        switch (radioGroup.getCheckedRadioButtonId()){
            case R.id.radioBtn1:
                type = "Transfer";
                break;
            case R.id.radioBtn2:
                type = "Borrow";
                break;
            default:
                type = "";
                break;
        };

        final String uid = getSharedPreferences("user", MODE_PRIVATE)
                .getString("uid", "");
        String app_script_url = "https://script.google.com/macros/s/AKfycbwjF_IS4XsRzhSMFc1sRCAPEjHg3f0BgS-ASBc48-tXXUJBQzw/exec";

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, app_script_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loading.dismiss();
                        Toast.makeText(AddRequestActivity.this,
                                "Successfully added a new request!", Toast.LENGTH_LONG).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AddRequestActivity.this, "Add request fail: " + error.toString(),
                        Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> parms = new HashMap<>();
                parms.put("action", "addRequest");
                parms.put("uid", uid);
                parms.put("title", title);
                parms.put("type", type);
                parms.put("price", price);
                parms.put("des", des);
                return parms;
            }
        };

        // Add the request to the RequestQueue.
        queue.add(stringRequest);


    }
}