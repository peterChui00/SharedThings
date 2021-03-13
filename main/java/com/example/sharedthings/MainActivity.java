package com.example.sharedthings;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class MainActivity extends AppCompatActivity {

    Button btn_login, btn_register;
    EditText et_login_username, et_login_password;
    String userEntry_name, userEntry_pw;
    ProgressDialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Boolean loginStatus = getSharedPreferences("user", MODE_PRIVATE)
                .getBoolean("loginStatus", false);
        if(loginStatus){
            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
            startActivity(intent);
            MainActivity.this.finish();
        }

        btn_login = findViewById(R.id.btn_login);
        btn_register = findViewById(R.id.btn_register);
        et_login_username = findViewById(R.id.et_login_username);
        et_login_password = findViewById(R.id.et_login_password);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userEntry_name = et_login_username.getText().toString().trim();
                userEntry_pw = et_login_password.getText().toString().trim();
                login();
            }
        });

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,RegisterActivity.class);
                startActivity(intent);
                MainActivity.this.finish();
            }
        });
    }

    private void login() {
        loading = ProgressDialog.show(this, "Logging in", "Please wait...", false, true);
        String app_script_url = "https://script.google.com/macros/s/AKfycbwjF_IS4XsRzhSMFc1sRCAPEjHg3f0BgS-ASBc48-tXXUJBQzw/exec?action=readUser";

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, app_script_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        validateUser(response);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, error.toString(),
                                Toast.LENGTH_SHORT).show();
                    }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    private void validateUser(String response) {
        boolean isValid = false;
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = jsonObject.getJSONArray("user");

            for (int i = 0; i < jsonArray.length(); i++){
                JSONObject temp = jsonArray.getJSONObject(i);
                String userID = temp.getString("userid");
                String username = temp.getString("username");
                String password = temp.getString("password");
                String email = temp.getString("email");
                String phone = temp.getString("phone");

                if(userEntry_name.equals(username) && userEntry_pw.equals(password)){
                    isValid = true;
                    SharedPreferences pref = getSharedPreferences("user", MODE_PRIVATE);
                    pref.edit()
                            .putString("uid", userID)
                            .putString("username", username)
                            .putString("pw", password)
                            .putString("email", email)
                            .putString("phone", phone)
                            .putBoolean("loginStatus", true)
                            .apply();
                }
            }

            if (isValid){
                Toast.makeText(MainActivity.this, "Login successfully!",
                        Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                startActivity(intent);
                MainActivity.this.finish();
            } else {
                Toast.makeText(MainActivity.this, "Wrong username or password!",
                        Toast.LENGTH_SHORT).show();
            }

        } catch (JSONException JSONe){
            JSONe.printStackTrace();
        }
        loading.dismiss();
    }


}