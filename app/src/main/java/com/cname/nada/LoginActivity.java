package com.cname.nada;

import android.content.AsyncQueryHandler;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.AsyncTaskLoader;

import com.cname.nada.functions.RequestHttpURLConnection;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static android.content.ContentValues.TAG;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    GoogleSignInClient mGoogleSignInClient;
    private final int RC_SIGN_IN = 123;
    private static final String TAG = "LoginActivity";
    SignInButton googleSignBt;
    private Button fakeGoogle;
    Button logoutBt, toTheMainBt;
    TextView tempoTextView;
    String url1 = "http://ec2-3-37-249-141.ap-northeast-2.compute.amazonaws.com:8080/user/login/google";
    String personToken, personName, personGivenName, personFamilyName, personEmail, personId, statement;
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

        // 앱에 필요한 사용자 데이터를 요청하도록 로그인 옵션을 설정한다.
// DEFAULT_SIGN_IN parameter는 유저의 ID와 기본적인 프로필 정보를 요청하는데 사용된다.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestIdToken("1055755332536-5ecrjokk429a4eb7cbk0sqmq9qa6mo80.apps.googleusercontent.com")
//                      바로 위에 문장 주석 풀면 handlesigninresult에서 apiexpection 발생한다.
                .requestEmail() // email addresses도 요청함
                .build();

// 위에서 만든 GoogleSignInOptions을 사용해 GoogleSignInClient 객체를 만듬
        mGoogleSignInClient = GoogleSignIn.getClient(LoginActivity.this, gso);

        // 기존 로그인 계정 확인
        GoogleSignInAccount gsa = GoogleSignIn.getLastSignedInAccount(LoginActivity.this);

        // 로그인 되어있을때
        if (gsa != null && gsa.getId() != null) {
//            updateUI(acct);
        }

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

                Log.d(TAG, "handleSignInResult:personName "+personName);
                Log.d(TAG, "handleSignInResult:personGivenName "+personGivenName);
                Log.d(TAG, "handleSignInResult:personEmail "+personEmail);
                Log.d(TAG, "handleSignInResult:personId "+personId);
                Log.d(TAG, "handleSignInResult:personFamilyName "+personFamilyName);
                Log.d(TAG, "handleSignInResult:personPhoto "+personPhoto);

//                SendUserInfo sendUserInfo = new SendUserInfo();
//                sendUserInfo.execute();

//                SendJsonDataToServer sendJsonDataToServer= new SendJsonDataToServer();
//                sendJsonDataToServer.start();

                AsyncTask<String, String, String> result = new SendJsonDataToServer().execute(url1);
            }
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            e.printStackTrace();

        }
    }


    private static String makeJsonMsg(String name, String email) {
        String retMsg = "";

        JSONStringer jsonStringer = new JSONStringer();

        try {
            retMsg = jsonStringer.object().key("name").value(name).key("email").value(email).endObject().toString();
            Log.d (TAG, "send string = " + retMsg);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return retMsg;
    }

    public class SendJsonDataToServer extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... urls) {
            try {
                String JsonMsg = makeJsonMsg(personName, personEmail);

                OutputStream os = null;
                InputStream is = null;
                ByteArrayOutputStream baos = null;
                HttpURLConnection conn = null;
                String response = "";

                try {
                    URL url = new URL(urls[0]);
                    conn = (HttpURLConnection)url.openConnection();
                    conn.setConnectTimeout(5 * 1000);
                    conn.setReadTimeout(5 * 1000);
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Cache-Control", "no-cache");
                    conn.setRequestProperty("Content-Type", "application/json");
                    conn.setRequestProperty("Accept", "application/json");
                    conn.setDoOutput(true);
                    conn.setDoInput(true);

                    os = conn.getOutputStream();
                    os.write(JsonMsg.getBytes());
                    os.flush();
                    int responseCode = conn.getResponseCode();

                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        is = conn.getInputStream();
                        baos = new ByteArrayOutputStream();
                        byte[] byteBuffer = new byte[1024];
                        byte[] byteData = null;
                        int nLength = 0;
                        while ((nLength = is.read(byteBuffer, 0, byteBuffer.length)) != -1) {
                            baos.write(byteBuffer, 0, nLength);
                        }
                        byteData = baos.toByteArray();
                        response = new String(byteData);

                        Log.i(TAG, "DATA response = " + response);
                    }
                    return response;
                } catch (MalformedURLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    return null;
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    return null;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            tempoTextView.setText(statement);
        }
    }
}