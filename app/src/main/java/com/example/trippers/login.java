package com.example.trippers;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.transition.ChangeImageTransform;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class login extends Fragment {

    TextInputEditText ePhone;
    MaterialButton login;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        ePhone= (TextInputEditText) view.findViewById(R.id.ePhone);
        login= (MaterialButton) view.findViewById(R.id.login);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //login_user();
                confirmOtpFragment mfragment= new confirmOtpFragment();
                FragmentManager fm= getFragmentManager();
                fm.beginTransaction().replace(R.id.fragment, mfragment).commit();
            }
        });
        return view;
    }


    public void login_user()
    {
            String phone= ePhone.getText().toString();
            if (phone.length() == 0) {
                Toast.makeText(getContext(), "Please Enter Phone Number", Toast.LENGTH_LONG).show();
            }

            else if (phone.length() > 0 && phone.length() <=9) {
                Toast.makeText(getContext(), "Please Enter Phone Number", Toast.LENGTH_LONG).show();
            }

            else {
                try {
                    phone = "91" + phone;
                    RequestQueue requestQueue = Volley.newRequestQueue(getContext());
                    String URL = "https://trip-planners.herokuapp.com/api/users/login";
                    JSONObject jsonBody = new JSONObject();
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

}
