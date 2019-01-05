package com.asa.amber;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class CustomerSettingsActivity extends AppCompatActivity {

    private EditText mNameField, mPhoneField, mEmailField;

    private DatabaseReference mCustomerDatabase;

    private String mEmail, mName, mPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_settings);
        mEmailField = findViewById(R.id.email);
        mNameField = findViewById(R.id.name);
        mPhoneField = findViewById(R.id.phone);

        Button mBack = findViewById(R.id.back);
        Button mConfirm = findViewById(R.id.confirm);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String userID = mAuth.getCurrentUser().getUid();
        mCustomerDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child("Customers")
                .child(userID);

        getUserInfo();

        mConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUserInformation();
            }
        });

        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void getUserInfo() {
        mCustomerDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {
                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();

                    if (map.get("email") != null) {
                        mEmail = map.get("email").toString();
                        mEmailField.setText(mEmail);
                    }
                    if (map.get("name") != null) {
                        mName = map.get("name").toString();
                        mNameField.setText(mName);
                    }
                    if (map.get("phone") != null) {
                        mPhone = map.get("phone").toString();
                        mPhoneField.setText(mPhone);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void saveUserInformation() {
        mEmail = mEmailField.getText().toString();
        mName = mNameField.getText().toString();
        mPhone = mPhoneField.getText().toString();
        Map userInfo = new HashMap();
        userInfo.put("email", mEmail);
        userInfo.put("name", mName);
        userInfo.put("phone", mPhone);
        mCustomerDatabase.updateChildren(userInfo);
        finish();
    }
}
