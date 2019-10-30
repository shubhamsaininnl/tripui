package com.example.trippers;

import android.app.DownloadManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;


import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.credentials.Credential;
import com.google.android.gms.auth.api.credentials.HintRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.hbb20.CountryCodePicker;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.LOCATION_SERVICE;

public class main_fragment extends Fragment implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

        TextInputEditText eName, ePhoneNumber;
        MaterialButton register;
        View view;
        String mobNumber;
        CountryCodePicker ccp;
        TextView text123;

        private static int flag=0;
        private final static int RESOLVE_HINT = 1011;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            view=  inflater.inflate(R.layout.fragment_main, container, false);
            register= (MaterialButton) view.findViewById(R.id.register);
            eName= (TextInputEditText) view.findViewById(R.id.eName);
            ePhoneNumber= (TextInputEditText) view.findViewById(R.id.ePhoneNumber);
          //  ccp= (CountryCodePicker) view.findViewById(R.id.ccp);

          //  ccp.registerCarrierNumberEditText(ePhoneNumber);

            ePhoneNumber.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean b) {
                    Log.d("Shubham", "Ephone number ");

                    if(flag==0) {
                        flag=1;
                        requestPhoneNumber();
                    }
                }
            });

            register.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getOneTimePassword();

                   /* confirmOtpFragment fragment= new confirmOtpFragment();
                    FragmentManager fm = getFragmentManager();
                    fm.beginTransaction().replace(R.id.fragment,fragment).commit();*/

                }
            });

            return  view;

        }

        public void getOneTimePassword() {
                String name = eName.getText().toString();
                String phone = ePhoneNumber.getText().toString();

                if (phone.length() == 0) {
                    Toast.makeText(getContext(), "Please Enter Phone Number", Toast.LENGTH_LONG).show();
                } else if (phone.length() > 0 && phone.length() <= 9) {
                    Toast.makeText(getContext(), "Please Enter Valid Number", Toast.LENGTH_LONG).show();
                }

                else {
                    try {
                        phone = "91" + phone;
                        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
                        String URL = "https://trip-planners.herokuapp.com/api/users/register";
                        JSONObject jsonBody = new JSONObject();
                        jsonBody.put("name", name);
                        jsonBody.put("phone", phone);
                        final String requestBody = jsonBody.toString();

                        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.i("Shubham_", response);
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e("Shubham_", error.toString());
                            }
                        }) {
                            @Override
                            public String getBodyContentType() {
                                return "application/json; charset=utf-8";
                            }

                            @Override
                            public byte[] getBody() throws AuthFailureError {
                                try {
                                    return requestBody == null ? null : requestBody.getBytes("utf-8");
                                } catch (UnsupportedEncodingException uee) {
                                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                                    return null;
                                }
                            }

                            @Override
                            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                                String responseString = "";
                                if (response != null) {
                                    responseString = String.valueOf(response.statusCode);
                                    // can get more details such as response.headers
                                }
                                return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
                            }
                        };

                        requestQueue.add(stringRequest);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
        }

    protected void requestPhoneNumber() {
        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(view.getContext())
                .addApi(Auth.CREDENTIALS_API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        googleApiClient.connect();
        HintRequest hintRequest = new HintRequest.Builder()
                .setPhoneNumberIdentifierSupported(true)
                .build();
        PendingIntent intent = Auth.CredentialsApi.getHintPickerIntent(googleApiClient, hintRequest);
        try {
            startIntentSenderForResult(intent.getIntentSender(), RESOLVE_HINT, null, 0, 0, 0, null);
        } catch (IntentSender.SendIntentException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESOLVE_HINT) {
            if (resultCode == RESULT_OK) {
                Credential credential = data.getParcelableExtra(Credential.EXTRA_KEY);
                if (credential != null) {
                    mobNumber = credential.getId();
                    Log.d("Shubham_", "activity result");
                    ePhoneNumber.setText(mobNumber);

                } else {
                    ePhoneNumber.setText("No phone number available");
                }
            }
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

}
