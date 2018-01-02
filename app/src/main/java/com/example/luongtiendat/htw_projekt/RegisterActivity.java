package com.example.luongtiendat.htw_projekt;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    TextInputLayout mDisplayName,mEmail,mPassword;
    Button mRegister;
    Toolbar mToolbar;

    private FirebaseAuth mAuth;
    private DatabaseReference myRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        mDisplayName = (TextInputLayout)findViewById(R.id.register_display_name);
        mEmail = (TextInputLayout)findViewById(R.id.register_email);
        mPassword = (TextInputLayout)findViewById(R.id.register_password);
        mRegister = (Button)findViewById(R.id.register_btn);

        mToolbar = (Toolbar)findViewById(R.id.register_appbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Neu Account Erstellen");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String display_name = mDisplayName.getEditText().getText().toString();
                String email = mEmail.getEditText().getText().toString();
                String password = mPassword.getEditText().getText().toString();

                register_user(display_name,email,password);
            }
        });

    }

    private void register_user(final String display_name, final String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){

                    FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
                    String uid = current_user.getUid();

                    myRef = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);

                    HashMap<String,String> userMap = new HashMap<>();
                    userMap.put("name",display_name);
                    userMap.put("email",email);
                    userMap.put("phone","default");
                    userMap.put("auftrag_count","0");
                    userMap.put("bewertung_count","0");
                    userMap.put("image","default");
                    userMap.put("thumb_image","default");

                    myRef.setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Intent mainIntent = new Intent(RegisterActivity.this,MainActivity.class);
                                mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(mainIntent);
                                finish();
                            }

                        }
                    });
                }else{
                        Toast.makeText(RegisterActivity.this, "You got some error.", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
