package com.cname.nada;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.cname.nada.functions.UserID;

import org.json.JSONException;
import org.json.JSONObject;

public class SignupInitialInfoActivity2 extends AppCompatActivity {

    Button saveButton;
    EditText positionEdit, belongEdit, callEdit, faxEdit, dateEdit;
    private String url1 = "http://ec2-3-37-249-141.ap-northeast-2.compute.amazonaws.com:8080/belong/save/";
    private final String TAG = this.getClass().getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_singup_initial_info2);

        saveButton = (Button)findViewById(R.id.saveButton);
        positionEdit =(EditText) findViewById(R.id.positionEdit);
        belongEdit =(EditText) findViewById(R.id.belongEdit);
        callEdit =(EditText) findViewById(R.id.callEdit);
        faxEdit =(EditText) findViewById(R.id.faxEdit);
        dateEdit =(EditText) findViewById(R.id.dateEdit);

        positionEdit.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode==event.KEYCODE_ENTER)
                    return true;
                return false;
            }
        });
        belongEdit.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode==event.KEYCODE_ENTER)
                    return true;
                return false;
            }
        });

        positionEdit.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode==event.KEYCODE_ENTER)
                    return true;
                return false;
            }
        });

        callEdit.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode==event.KEYCODE_ENTER)
                    return true;
                return false;
            }
        });

        faxEdit.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode==event.KEYCODE_ENTER)
                    return true;
                return false;
            }
        });

        dateEdit.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode==event.KEYCODE_ENTER)
                    return true;
                return false;
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                RequestQueue queue = Volley.newRequestQueue(SignupInitialInfoActivity2.this);

                JSONObject parameter = new JSONObject();
                try{
                    parameter.put("user_id", UserID.getUserId());
                    parameter.put("belong_data", belongEdit.getText());
                    parameter.put("position_data", positionEdit.getText());
                    parameter.put("tel_data", callEdit.getText());
                    parameter.put("fax_data", faxEdit.getText());
                    parameter.put("start_data", dateEdit.getText());
                    parameter.put("fin_data", null);
                }catch (JSONException e){
                    e.printStackTrace();
                }

                try {
                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url1, parameter,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    Intent intent = new Intent(SignupInitialInfoActivity2.this, MainActivity.class);
                                    startActivity(intent);
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast toast = Toast.makeText(SignupInitialInfoActivity2.this, "유저 정보가 정상적으로 전송되지 않습니다.", Toast.LENGTH_LONG);
                                    toast.show();

                                    error.printStackTrace();
                                    Log.d(TAG, "Post Fail");
                                }
                            });
                }catch (Exception e){ e.printStackTrace();}


            }
        });
    }
}