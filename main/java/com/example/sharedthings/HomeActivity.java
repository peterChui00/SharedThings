package com.example.sharedthings;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class HomeActivity extends AppCompatActivity {

    ImageView iv_home, iv_addRequest, iv_user_info;
    ListView lv_home_requests;
    ProgressDialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        iv_home = findViewById(R.id.iv_home);
        iv_addRequest = findViewById(R.id.iv_addRequest);
        iv_user_info = findViewById(R.id.iv_user_info);
        lv_home_requests = findViewById(R.id.lv_home_requests);

        // Load requests
        readRequest();

        // Reload requests
        iv_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                readRequest();
            }
        });

        iv_addRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, AddRequestActivity.class);
                startActivity(intent);
                HomeActivity.this.finish();
            }
        });

        iv_user_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, UserInfoActivity.class);
                startActivity(intent);
                HomeActivity.this.finish();
            }
        });
    }

    private void readRequest() {
        loading =  ProgressDialog.show(this,"Loading requests","please wait",false,
                true);
        String app_script_url = "https://script.google" +
                ".com/macros/s/AKfycbwjF_IS4XsRzhSMFc1sRCAPEjHg3f0BgS-ASBc48-tXXUJBQzw/exec" +
                "?action=readRequest";

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, app_script_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                       parseRequests(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(HomeActivity.this, error.toString(),
                        Toast.LENGTH_SHORT).show();
            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    private void parseRequests(String response) {

        ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = jsonObject.getJSONArray("requests");

            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject temp = jsonArray.getJSONObject(i);

                String title = temp.getString("title");
                String type = temp.getString("type");
                String price = temp.getString("price");
                String des = temp.getString("des");
                String username = temp.getString("username");
                String email = temp.getString("email");
                String phone = temp.getString("phone");

                HashMap<String, String> request = new HashMap<>();
                request.put("title", title);
                request.put("type", type);
                request.put("price", price);
                request.put("des", des);
                request.put("username", username);
                request.put("email", email);
                request.put("phone", phone);
                arrayList.add(request);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        SimpleAdapter adapter = new SimpleAdapter(this, arrayList, R.layout.request_list,
                new String[]{"username","title","type","price", "des", "email",
                        "phone"},
                new int[]{R.id.tv_requestList_username, R.id.tv_requestList_title,
                        R.id.tv_requestList_type, R.id.tv_requestList_price,
                        R.id.tv_requestList_des, R.id.tv_requestList_email,
                        R.id.tv_requestList_phone});


        lv_home_requests.setAdapter(adapter);
        loading.dismiss();
    }


}