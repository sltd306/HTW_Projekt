package com.example.luongtiendat.htw_projekt;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import at.markushi.ui.CircleButton;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth mAuth;
    private DatabaseReference mUserDatabase;

    Toolbar mToolBar;

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToogle;

    private NavigationView navigationView;

    private TextView mDisplayName;
    private CircleButton mAuftragErstellenBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolBar = (Toolbar)findViewById(R.id.main_appbar);
        setSupportActionBar(mToolBar);
        getSupportActionBar().setTitle("JobHilfe");

        mAuth = FirebaseAuth.getInstance();


        mDrawerLayout = (DrawerLayout)findViewById(R.id.main_drawer);
        mToogle = new ActionBarDrawerToggle(this,mDrawerLayout,R.string.open,R.string.close);
        mDrawerLayout.addDrawerListener(mToogle);
        mToogle.syncState();
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        navigationView = (NavigationView)findViewById(R.id.main_navigationView);
        navigationView.setNavigationItemSelectedListener(this);

        mAuftragErstellenBtn = (CircleButton)findViewById(R.id.main_auftrag_erstellen);
        mAuftragErstellenBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent auftragErstellenIntent = new Intent(MainActivity.this,AuftragErstellenActivity.class);
                startActivity(auftragErstellenIntent);

            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseUser currentUser= mAuth.getCurrentUser();
        if(currentUser == null){
            sendToStart();
        }
    }

    private void sendToStart() {
        Intent startIntent= new Intent(MainActivity.this,StartActivity.class);
        startActivity(startIntent);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_settings){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawerLayout = (DrawerLayout)findViewById(R.id.main_drawer);
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.nav_drawer,menu);
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.nav_profil){
            Intent profileIntent = new Intent(MainActivity.this,UserProfileActivity.class);
            startActivity(profileIntent);

        } else if(id == R.id.nav_auftrag){
            Intent auftragIntent = new Intent(MainActivity.this,AllAuftragListeActivity.class);
            startActivity(auftragIntent);

        }else if(id == R.id.nav_abmelden) {
            FirebaseAuth.getInstance().signOut();
            sendToStart();
        }

        return false;
    }
}
