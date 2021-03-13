package com.example.sharedthings;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
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

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    Button btn_backToLogin, btn_confirmRegister;
    EditText et_reg_username, et_reg_pw, et_reg_email, et_reg_phone_num;
    ProgressDialog loading;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        btn_backToLogin = findViewById(R.id.btn_backToLogin);
        btn_confirmRegister = findViewById(R.id.btn_confirmRegister);
        et_reg_username = findViewById(R.id.et_reg_username);
        et_reg_pw = findViewById(R.id.et_reg_pw);
        et_reg_email = findViewById(R.id.et_reg_email);
        et_reg_phone_num = findViewById(R.id.et_reg_phone_num);
        et_reg_username.setText("");
        et_reg_pw.setText("");
        et_reg_email.setText("");
        et_reg_phone_num.setText("");


        btn_backToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                startActivity(intent);
                RegisterActivity.this.finish();
            }
        });

        btn_confirmRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String username = et_reg_username.getText().toString().trim();
                final String pw = et_reg_pw.getText().toString().trim();
                final String email = et_reg_email.getText().toString().trim();
                final String phone_num = et_reg_phone_num.getText().toString().trim();

                if(username.length()>0 &&
                        pw.length()>0 &&
                        ((email.length()>3 && email.contains("@")) || phone_num.length()>8 )){
                    register(username, pw, email, phone_num);
                } else {
                    Toast.makeText(RegisterActivity.this, "Blank/invalid input!",
                        Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void register(final String username, final String pw, final String email, final String phone_num) {
        loading = ProgressDialog.show(this, "Registering", "Please wait...", false, true);
        String app_script_url = "https://script.google.com/macros/s/AKfycbwjF_IS4XsRzhSMFc1sRCAPEjHg3f0BgS-ASBc48-tXXUJBQzw/exec";

            // Instantiate the RequestQueue.
            RequestQueue queue = Volley.newRequestQueue(this);

            // Request a string response from the provided URL.
            StringRequest stringRequest = new StringRequest(Request.Method.POST, app_script_url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            loading.dismiss();
                            Toast.makeText(RegisterActivity.this,
                                    "Successfully Registered!", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                            startActivity(intent);
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    loading.dismiss();
                    Toast.makeText(RegisterActivity.this, error.toString(),
                            Toast.LENGTH_SHORT).show();

                }
            }){
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> parms = new HashMap<>();
                    parms.put("action", "register");
                    parms.put("username", username);
                    parms.put("pw", pw);
                    parms.put("email", email);
                    parms.put("phone_num", phone_num);
                    return parms;
                }
            };

            // Add the request to the RequestQueue.
            queue.add(stringRequest);



    }
}