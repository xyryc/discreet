package com.example.chatapplication.view.auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.chatapplication.R;
import com.example.chatapplication.databinding.ActivityPhoneLoginBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.concurrent.TimeUnit;

public class PhoneLoginActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private ActivityPhoneLoginBinding binding;
    private static String TAG = "PhoneLoginActivity";

    private FirebaseAuth mAuth;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    private ProgressDialog progressDialog;

    private FirebaseUser firebaseUser;
    private FirebaseFirestore firestore;

    String[] country = {"Bangladesh", "Russia", "Palestine", "China", "Turkey", "Japan", "Iran", "USA", "KSA", "Other"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_phone_login);


        //getting the instance of Spinner and applying onItemSelectedListener on it
        Spinner spin = findViewById(R.id.spinner_country);
        spin.setOnItemSelectedListener(this);

        //Creating the ArrayAdapter instance having the country list
        ArrayAdapter<String> aa = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, country);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spin.setAdapter(aa);


        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if(firebaseUser!=null){
            startActivity(new Intent(this, SetUserInfoActivity.class));
        }


        progressDialog = new ProgressDialog(this);

        binding.btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if(binding.btnNext.getText().toString().equals("Next")) {
                if(binding.btnNext.getText().toString().equals("Send Code")) {
                    progressDialog.setMessage("Please Wait");
                    progressDialog.show();

                    String phone = "+"+binding.edCodeCountry.getText().toString()+binding.edPhone.getText().toString();
                    startPhoneNumberVerification(phone);
                }
                else{
                    progressDialog.setMessage("Verifying..");
                    verifyPhoneNumberWithCode(mVerificationId, binding.edCode.getText().toString());
                }
            }
        });

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                Log.d(TAG, "onVerificationCompleted: Complete");
                signInWithPhoneAuthCredential(phoneAuthCredential);
                progressDialog.dismiss();
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                Log.d(TAG, "onVerificationFailed: "+e.getMessage());
            }

            @Override
            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                Log.d(TAG, "onCodeSent:" + verificationId);

                // Save verification ID and resending token so we can use them later
                mVerificationId = verificationId;
                mResendToken = token;

                binding.btnNext.setText("Continue");
                progressDialog.dismiss();
            }

        };
    }

    private void startPhoneNumberVerification(String phoneNumber){
        //[START start_phone_auth]
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        //phone number to verify
                60,          //timeout duration
                TimeUnit.SECONDS,   //Unit of timeout
                this,        //Activity for callback binding
                mCallbacks);        //OnVerificationStateChangedCallbacks
        //[END start_phone_auth]

        //mVerificationInProgress = true;
    }

    private void verifyPhoneNumberWithCode(String VerificationId, String code){
        //[START verify_with_code]
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(VerificationId, code);
        //[END verify_with_code]
        signInWithPhoneAuthCredential(credential);
    }


    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = task.getResult().getUser();
                            startActivity(new Intent(PhoneLoginActivity.this, SetUserInfoActivity.class));

                            //if (user != null) {
                            //    String userId = user.getUid();
                            //    Users users = new Users(userId,
                            //            "",
                            //    user.getPhoneNumber(),
                            //    "",
                            //    "",
                            //    "",
                            //    "",
                            //    "",
                            //    "",
                            //    "");

                            //    firestore.collection("Users").document("UserInfo").collection (userId)
                            //           .add(users).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            //                @Override
                            //                public void onSuccess(DocumentReference documentReference){
                            //
                            //                }
                            //            });

                            //} else {
                            //    Toast.makeText(getApplicationContext(), "Something Error",Toast.LENGTH_SHORT).show();
                            //}


                            //startActivity(new Intent(PhoneLoginActivity.this,SetUserInfoActivity.class));
                        }
                        else {
                            // Sign in failed, display a message and update the UI
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                Log.d(TAG, "onComplete: Error Code");
                            }
                            // Update UI
                        }
                    }
                });
    }
    //END sign_in_with_phone

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id){
        Toast.makeText(getApplicationContext(), country[position], Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent){

    }

}