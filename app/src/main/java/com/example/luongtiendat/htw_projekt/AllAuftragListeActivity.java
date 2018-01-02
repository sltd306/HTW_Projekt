package com.example.luongtiendat.htw_projekt;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.example.luongtiendat.htw_projekt.Model.Auftrag;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AllAuftragListeActivity extends AppCompatActivity {

    private Toolbar mToolBar;
    private RecyclerView mMeinAuftragList;

    private DatabaseReference mMeinAuftragDatabase,mUserDatabase;
    private FirebaseUser mCurrentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_auftrag_liste);

        mToolBar = (Toolbar) findViewById(R.id.all_auftrag_liste_appbar);
        setSupportActionBar(mToolBar);

        getSupportActionBar().setTitle("Alle Auftrag Liste");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mMeinAuftragList = (RecyclerView)findViewById(R.id.auftrag_list);
        mMeinAuftragList.setHasFixedSize(true);
        mMeinAuftragList.setLayoutManager(new LinearLayoutManager(this));
        mMeinAuftragDatabase = FirebaseDatabase.getInstance().getReference().child("Auftrags");

    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<Auftrag,AuftragViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Auftrag, AuftragViewHolder>(
                Auftrag.class,
                R.layout.auftrag_singer_layout,
                AuftragViewHolder.class,
                mMeinAuftragDatabase
        ) {
            @Override
            protected void populateViewHolder(AuftragViewHolder viewHolder, final Auftrag model, int position) {

                viewHolder.setTitel(model.getTitel());
                viewHolder.setArbeitOrt(model.getArbeit_ort());

                final String auftrag_id = getRef(position).getKey();
                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent auftragBewerbenIntent = new Intent(AllAuftragListeActivity.this,AuftragBewerbenActivity.class);
                        auftragBewerbenIntent.putExtra("auftrag_id",auftrag_id);
                        startActivity(auftragBewerbenIntent);
                    }
                });


            }
        };
        mMeinAuftragList.setAdapter(firebaseRecyclerAdapter);
    }

    public static class AuftragViewHolder extends RecyclerView.ViewHolder{

        View mView;

        public AuftragViewHolder(View itemView) {
            super(itemView);

            mView =itemView;
        }

        public  void setTitel (String titel){
            TextView auftragTitelView = mView.findViewById(R.id.auftrag_view_id);
            auftragTitelView.setText(titel);
        }

        public  void setDatum (String datum){
            TextView auftragDatumView = mView.findViewById(R.id.auftrag_view_datum);
            auftragDatumView.setText(datum);
        }
        public  void setArbeitOrt (String arbeit_ort){
            TextView auftragArbeitOrtView = mView.findViewById(R.id.auftrag_view_arbeit_ort);
            auftragArbeitOrtView.setText(arbeit_ort);
        }
    }
}
