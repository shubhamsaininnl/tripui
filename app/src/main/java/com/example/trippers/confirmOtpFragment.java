package com.example.trippers;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.app.Activity.RESULT_OK;

public class confirmOtpFragment extends Fragment {

    private static final int PERMISSION_REQUEST_ID = 100;
    private static final int SMS_CONSENT_REQUEST = 2;
    TextInputEditText enterOtp;
    MaterialButton verify;
    Pattern pattern;
    Dialog dialog;
    EditText eText1, eText2, eText3, eText4, eText5, eText6;

    @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view= inflater.inflate(R.layout.confirm_otp, container, false);
            pattern = Pattern.compile("(|^)\\d{6}");
            verify= (MaterialButton) view.findViewById(R.id.verify);
            eText1= (EditText) view.findViewById(R.id.eText1);
            eText2= (EditText) view.findViewById(R.id.eText2);
            eText3= (EditText) view.findViewById(R.id.eText3);
            eText4= (EditText) view.findViewById(R.id.eText4);
            eText5= (EditText) view.findViewById(R.id.eText5);
            eText6= (EditText) view.findViewById(R.id.eText6);

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setView(R.layout.progress);
            dialog = builder.create();
            if (true){
                dialog.show();
            }
            else {
            dialog.dismiss();
            }

            Task<Void> task = SmsRetriever.getClient(getContext()).startSmsUserConsent(null);
            IntentFilter intentFilter = new IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION);
            getActivity().registerReceiver(smsVerificationReceiver, intentFilter);

            verify.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent= new Intent(getActivity(), HomePage.class);
                    startActivity(intent);
                }
            });

            return  view;
        }

    private final BroadcastReceiver smsVerificationReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (SmsRetriever.SMS_RETRIEVED_ACTION.equals(intent.getAction())) {
                Bundle extras = intent.getExtras();
                Status smsRetrieverStatus = (Status) extras.get(SmsRetriever.EXTRA_STATUS);

                switch (smsRetrieverStatus.getStatusCode()) {
                    case CommonStatusCodes.SUCCESS:
                        // Get consent intent
                        Log.d("Shubham_", "Success");
                        Intent consentIntent = extras.getParcelable(SmsRetriever.EXTRA_CONSENT_INTENT);
                        try {
                            // Start activity to show consent dialog to user, activity must be started in
                            // 5 minutes, otherwise you'll receive another TIMEOUT intent
                            startActivityForResult(consentIntent, SMS_CONSENT_REQUEST);
                        } catch (ActivityNotFoundException e) {
                            // Handle the exception ...
                        }
                        break;
                    case CommonStatusCodes.TIMEOUT:
                        Log.d("Shubham_", "Timeout");
                        // Time out occurred, handle the error.
                        break;
                }
            }
        }
    };


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            // ...
            case SMS_CONSENT_REQUEST:
                if (resultCode == RESULT_OK) {
                    // Get SMS message content
                    String message = data.getStringExtra(SmsRetriever.EXTRA_SMS_MESSAGE);
                    // Extract one-time code from the message and complete verification
                    // `sms` contains the entire text of the SMS message, so you will need
                    // to parse the string.
                   // String oneTimeCode = parseOneTimeCode(message); // define this function
                    Log.d("Shubham_", "message is:" + message);
                    if(message!=null)
                    {
                        Matcher m = pattern.matcher(message);
                        if(m.find()) {
                            int n= Integer.parseInt(m.group(0));
                            String a,b,c,d,e,f;
                            f= Integer.toString(n%10);
                            n=n/10;

                            e= Integer.toString(n%10);
                            n=n/10;

                            d= Integer.toString(n%10);
                            n=n/10;

                            c= Integer.toString(n%10);
                            n=n/10;

                            b= Integer.toString(n%10);
                            n=n/10;

                            a= Integer.toString(n%10);

                            eText1.setText(a);
                            eText2.setText(b);
                            eText3.setText(c);
                            eText4.setText(d);
                            eText5.setText(e);
                            eText6.setText(f);


                            new CountDownTimer(2000, 1000) {
                                public void onFinish() {
                                    dialog.dismiss();
                                }
                                public void onTick(long millisUntilFinished) {
                                    // millisUntilFinished    The amount of time until finished.
                                }
                            }.start();
                        }

                        else
                        {
                            //something went wrong
                        }
                    }

                    // send one time code to the server
                } else {
                    // Consent canceled, handle the error ...
                }
                break;
        }
    }
}
