package com.example.luongtiendat.htw_projekt;

import android.icu.text.DateFormat;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.luongtiendat.htw_projekt.Model.Auftrag;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;

public class AuftragBewerbenActivity extends AppCompatActivity {

    private TextView mTitel,mAuftragReferen,mArbeitGeber,mArbeitOrt,mArbeitZeit,mStellenBeschreibung,
                        mBeginnArbeit,mVergutung;

    private TextView mUserPhone,mUseremail;
    private Toolbar mToolBar;
    private Button mBewerbung;

    private DatabaseReference mAuftragDatabase,mBewerbungRequestDatabase,mBewerbungDatabase;
    private DatabaseReference mUserDatabase;
    private FirebaseUser mCurrent_User;

    private String mBewerben_stats;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auftrag_bewerben);

        mAuftragReferen = (TextView)findViewById(R.id.auftrag_id);
        mTitel = (TextView)findViewById(R.id.auftrag_titel);
        mArbeitOrt = (TextView)findViewById(R.id.auftrag_arbeit_ort);
        mArbeitZeit = (TextView)findViewById(R.id.auftrag_arbeit_zeit);
        mStellenBeschreibung = (TextView)findViewById(R.id.auftrag_stellen_beschreibung);
        mBeginnArbeit = (TextView)findViewById(R.id.auftrag_begin_arbeit);
        mVergutung = (TextView)findViewById(R.id.auftrag_vergutung);

        mUserPhone = (TextView)findViewById(R.id.auftrag_kontakt_phone);
        mUseremail = (TextView)findViewById(R.id.auftrag_kontakt_email);
        mArbeitGeber = (TextView)findViewById(R.id.auftrag_arbeit_geber);

        mToolBar = (Toolbar)findViewById(R.id.auftrag_bewerben_appbar);
        setSupportActionBar(mToolBar);
        getSupportActionBar().setTitle("Auftrag Bewerbung");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mBewerbung = (Button)findViewById(R.id.auftrag_bewerben_btn);

        mBewerben_stats ="not_bewerben";

        final String auftrag_id = getIntent().getStringExtra("auftrag_id");

        mAuftragDatabase = FirebaseDatabase.getInstance().getReference().child("Auftrags").child(auftrag_id);
        mBewerbungRequestDatabase = FirebaseDatabase.getInstance().getReference().child("Bewerben_reg");
        mCurrent_User = FirebaseAuth.getInstance().getCurrentUser();



        mAuftragDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String titel = dataSnapshot.child("titel").getValue().toString();
                String arbeit_ort = dataSnapshot.child("arbeit_ort").getValue().toString();
                String arbeit_zeit = dataSnapshot.child("arbeit_zeit").getValue().toString();
                String arbeit_beginn = dataSnapshot.child("beginn").getValue().toString();
                String stellen_bes = dataSnapshot.child("stellen_beschreibung").getValue().toString();
                String vergutung = dataSnapshot.child("vergutung").getValue().toString();
                final String userId = dataSnapshot.child("userId").getValue().toString();

                mTitel.setText(titel);
                mArbeitOrt.setText(arbeit_ort);
                mArbeitZeit.setText(arbeit_zeit);
                mStellenBeschreibung.setText(stellen_bes);
                mBeginnArbeit.setText(arbeit_beginn);
                mVergutung.setText(vergutung);
                mAuftragReferen.setText(auftrag_id);


                mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);

                mUserDatabase.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String display_name = dataSnapshot.child("name").getValue().toString();
                        String display_phone = dataSnapshot.child("phone").getValue().toString();
                        String display_email = dataSnapshot.child("email").getValue().toString();

                        mArbeitGeber.setText(display_name);
                        mUserPhone.setText(display_phone);
                        mUseremail.setText(display_email);





                        if(mCurrent_User.getUid() == userId){

                          //  Toast.makeText(AuftragBewerbenActivity.this,"Gleich",Toast.LENGTH_SHORT).show();

                            mBewerbungRequestDatabase.child(mCurrent_User.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if(dataSnapshot.hasChild(auftrag_id)){
                                        String req_type = dataSnapshot.child(userId).child("request_type").getValue().toString();

                                        if(req_type.equals("received_request")){
                                            mBewerben_stats = "req_received";
                                            mBewerbung.setText("Accept Bewerbung Request");
                                        }else if (req_type.equals("sent_request")){
                                            mBewerben_stats = "req_sent";
                                            mBewerbung.setText("Cancel Bewerbung Request");

                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }else {

                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mBewerbung.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {

                mBewerbung.setEnabled(false);

                // ------------------------ Noch nicht bewerben

                if(mBewerben_stats.equals("not_bewerben")){
                    mBewerbungRequestDatabase.child(auftrag_id).child(mCurrent_User.getUid()).child("request_type").setValue("sent_request")
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        mBewerbungRequestDatabase.child(mCurrent_User.getUid()).child(auftrag_id).child("request_type").setValue("received_request").addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful()){
                                                    mBewerben_stats = "req_sent";
                                                    mBewerbung.setText("Cancel Bewerbung Request");
                                                    Toast.makeText(AuftragBewerbenActivity.this,"Request Sent Successfull.",Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });

                                    }else {
                                        Toast.makeText(AuftragBewerbenActivity.this,"Request Sent Fail !!!.",Toast.LENGTH_SHORT).show();
                                    }
                                    mBewerbung.setEnabled(true);
                                }
                            });
                }

                // ------------------------ Schon beworben
                if(mBewerben_stats.equals("req_sent")){
                    mBewerbungRequestDatabase.child(auftrag_id).child(mCurrent_User.getUid()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                mBewerbungRequestDatabase.child(mCurrent_User.getUid()).child(auftrag_id).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){
                                            mBewerben_stats = "not_bewerben";
                                            mBewerbung.setText("Send Bewerbung Request");
                                            mBewerbung.setEnabled(true);

                                        }

                                    }
                                });
                            }

                        }
                    });
                }

                // ------------------------ die Bewerbung annimmt

                if(mBewerben_stats.equals("req_received")){
                    final String currentDate = DateFormat.getDateTimeInstance().format(new Date());

                    mBewerbungDatabase.child(mCurrent_User.getUid()).child(auftrag_id).setValue(currentDate).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            mBewerbungDatabase.child(auftrag_id).child(mCurrent_User.getUid()).setValue(currentDate).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    mBewerbungRequestDatabase.child(mCurrent_User.getUid()).child(auftrag_id).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            mBewerbungRequestDatabase.child(auftrag_id).child(mCurrent_User.getUid()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    mBewerbung.setEnabled(true);
                                                    mBewerben_stats = "beworben";
                                                    mBewerbung.setText("Denin this Bewerbung");
                                                }
                                            });

                                        }
                                    });
                                }
                            });
                        }
                    });

                }



            }
        });



    }
}
