package com.example.trippers;

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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.credentials.Credential;
import com.google.android.gms.auth.api.credentials.HintRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.hbb20.CountryCodePicker;

import static android.app.Activity.RESULT_OK;

public class main_fragment extends Fragment implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

        TextInputEditText eName, ePhoneNumber;
        MaterialButton getOtp;
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
            getOtp= (MaterialButton) view.findViewById(R.id.getOtp);
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

            getOtp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    confirmOtpFragment fragment= new confirmOtpFragment();
                    FragmentManager fm = getFragmentManager();
                    fm.beginTransaction().replace(R.id.fragment,fragment).commit();

                }
            });

            return  view;

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
