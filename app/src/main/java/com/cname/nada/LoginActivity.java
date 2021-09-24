package com.cname.nada;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.cname.nada.functions.UserID;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    GoogleSignInClient mGoogleSignInClient;
    private final int RC_SIGN_IN = 123;
    private final String TAG = this.getClass().getSimpleName();
    SignInButton googleSignBt;
    private Button fakeGoogle;
    Button logoutBt, toTheMainBt;
    TextView tempoTextView;
    String url1 = "http://ec2-3-37-249-141.ap-northeast-2.compute.amazonaws.com:8080/user/login/google/";
    String personToken, personName, personGivenName, personFamilyName, personEmail, personId;
    Uri personPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        fakeGoogle = (Button) findViewById(R.id.GoogleLoginFake);
        googleSignBt = findViewById(R.id.GoogleLoginButton);
        fakeGoogle.setOnClickListener(this);
//        googleSignBt.setOnClickListener(this);

        logoutBt = findViewById(R.id.logoutBt);
        logoutBt.setOnClickListener(this);

        toTheMainBt = (Button) findViewById(R.id.ToTheMainButton);
        toTheMainBt.setOnClickListener(this);

        tempoTextView = (TextView) findViewById(R.id.hellotextview);

        // [START configure_signin]
        // 앱에 필요한 사용자 데이터를 요청하도록 로그인 옵션을 설정한다.
// DEFAULT_SIGN_IN parameter는 유저의 ID와 기본적인 프로필 정보를 요청하는데 사용된다.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestIdToken("1055755332536-5ecrjokk429a4eb7cbk0sqmq9qa6mo80.apps.googleusercontent.com")
//                      바로 위에 문장 주석 풀면 handlesigninresult에서 apiexpection 발생한다.
                .requestEmail() // email addresses도 요청함
                .build();
        // [END configure_signin]

// 위에서 만든 GoogleSignInOptions을 사용해 GoogleSignInClient 객체를 만듬
        mGoogleSignInClient = GoogleSignIn.getClient(LoginActivity.this, gso);

//        googleSignBt.setScopes(gso.getScopeArray());

        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, task -> {
                    Log.d(TAG, "onClick:logout success ");
                    mGoogleSignInClient.revokeAccess()
                            .addOnCompleteListener(this, task1 -> Log.d(TAG, "onClick:revokeAccess success "));
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.GoogleLoginFake:
                googleSignBt.performClick();
                signIn();
                break;
            case R.id.logoutBt:
                mGoogleSignInClient.signOut()
                        .addOnCompleteListener(this, task -> {
                            Log.d(TAG, "onClick:logout success ");
                            mGoogleSignInClient.revokeAccess()
                                    .addOnCompleteListener(this, task1 -> Log.d(TAG, "onClick:revokeAccess success "));
                        });
                break;
            case R.id.ToTheMainButton:
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
                break;
        }
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.

            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);

            Map<String, String> post_json = new HashMap<>();
            post_json.put("name", personName);
            post_json.put("email", personEmail);

            final JSONObject parameter = (JSONObject) new JSONObject(post_json);

            RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url1, parameter,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Toast toast = Toast.makeText(getApplicationContext(), "유저 정보가 서버로 전송되었습니다.", Toast.LENGTH_LONG);
                            toast.show();

                            try {
                                FileOutputStream outputStream = openFileOutput("userId.txt", Context.MODE_PRIVATE);
                                outputStream.write(response.get("id").toString().getBytes());
                                outputStream.close();
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            } catch (IOException | JSONException e) {
                                e.printStackTrace();
                            }
                            try {
                                UserID.setUserId(response.get("id").toString());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            Log.d(TAG, "Post success : " + parameter);

//                            try {
//                                if(response.get("result").toString().equals("true")) {
                                    Intent intent = new Intent(LoginActivity.this, SignupInitialInfoActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
//                                } else {
//                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                                    startActivity(intent);
//                                }
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast toast = Toast.makeText(getApplicationContext(), "유저 정보가 정상적으로 전송되지 않습니다.", Toast.LENGTH_LONG);
                            toast.show();

                            error.printStackTrace();
                            Log.d(TAG, "Post Fail");
                        }
                    });

            queue.add(jsonObjectRequest);
        }
    }

     private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
                GoogleSignInAccount acct = completedTask.getResult(ApiException.class);

            if (acct != null) {
                personToken = acct.getIdToken();
                personName = acct.getDisplayName();
                personGivenName = acct.getGivenName();
                personFamilyName = acct.getFamilyName();
                personEmail = acct.getEmail();
                personId = acct.getId();
                personPhoto = acct.getPhotoUrl();

                Log.d(TAG, "handleSignInResult:personName " + personName);
                Log.d(TAG, "handleSignInResult:personGivenName " + personGivenName);
                Log.d(TAG, "handleSignInResult:personEmail " + personEmail);
                Log.d(TAG, "handleSignInResult:personId " + personId);
                Log.d(TAG, "handleSignInResult:personFamilyName " + personFamilyName);
                Log.d(TAG, "handleSignInResult:personPhoto " + personPhoto);

//                SendUserInfo sendUserInfo = new SendUserInfo();
//                sendUserInfo.execute();

//                SendJsonDataToServer sendJsonDataToServer= new SendJsonDataToServer();
//                sendJsonDataToServer.start();

//                AsyncTask<String, String, String> result = new SendJsonDataToServer().execute(url1);
            }
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            e.printStackTrace();

        }
    }
}