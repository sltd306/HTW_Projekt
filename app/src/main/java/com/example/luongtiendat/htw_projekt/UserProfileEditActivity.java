package com.example.luongtiendat.htw_projekt;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UserProfileEditActivity extends AppCompatActivity {

    private TextInputLayout mDisplayName,mEmail,mPhone;
    private Button mSavebtn;
    private Toolbar mToolBar;

    private DatabaseReference mStatusDatabase;
    private FirebaseUser mCurrentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile_edit);

        mDisplayName = (TextInputLayout)findViewById(R.id.user_edit_name);
        mEmail = (TextInputLayout) findViewById(R.id.user_edit_email);
        mPhone = (TextInputLayout) findViewById(R.id.user_edit_phone);
        mSavebtn = (Button)findViewById(R.id.user_save_btn);

        mToolBar = (Toolbar)findViewById(R.id.profile_edit_appbar);
        setSupportActionBar(mToolBar);
        getSupportActionBar().setTitle("User Profile Edit");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        String current_uid= mCurrentUser.getUid();

        mStatusDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(current_uid);
        String name_value = getIntent().getStringExtra("name_value");
        String phone_value = getIntent().getStringExtra("phone_value");
        String email_value = getIntent().getStringExtra("email_value");

        mDisplayName.getEditText().setText(name_value);
        mEmail.getEditText().setText(email_value);
        mPhone.getEditText().setText(phone_value);

        mSavebtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                String name = mDisplayName.getEditText().getText().toString();
                String phone = mPhone.getEditText().getText().toString();
                String email = mEmail.getEditText().getText().toString();

                mStatusDatabase.child("name").setValue(name).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){

                            Toast.makeText(UserProfileEditActivity.this, "Name Update Successfully! ", Toast.LENGTH_SHORT).show();
                        }else
                        {
                            Toast.makeText(UserProfileEditActivity.this, "Name fail", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                mStatusDatabase.child("phone").setValue(phone).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){

                            Toast.makeText(UserProfileEditActivity.this, "Phone Update Successfully! ", Toast.LENGTH_SHORT).show();
                        }else
                        {
                            Toast.makeText(UserProfileEditActivity.this, "Phone fail", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                mStatusDatabase.child("email").setValue(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){

                            Toast.makeText(UserProfileEditActivity.this, "Email Update Successfully! ", Toast.LENGTH_SHORT).show();
                        }else
                        {
                            Toast.makeText(UserProfileEditActivity.this, "Email fail", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });


    }
}
