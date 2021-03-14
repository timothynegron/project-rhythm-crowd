package com.example.rhythmcrowd.Activities;

import android.content.Intent;
import android.os.Bundle;

import com.example.rhythmcrowd.Model.Constants;
import com.example.rhythmcrowd.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    // Attributes
    private FloatingActionButton fab;
    private FirebaseAuth mAuth;


    //**********************************************************************************************
    // Method: onCreate(Bundle savedInstanceState)
    //
    // Purpose: Apart of Android OS Activity Lifecycle. Overriding the method to display
    // activity main xml file. Also sets functionality of floating action button, initializes
    // class attributes.
    //
    //
    //**********************************************************************************************
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeAttributes();
        setFab();
        setToolbar();
        startActivity(new Intent(this, SignInActivity.class));
    }

    //**********************************************************************************************
    // Method: setToolbar()
    //
    // Purpose: To create a toolbar.
    //
    //
    //**********************************************************************************************
    private void setToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    //**********************************************************************************************
    // Method: setFab()
    //
    // Purpose: To provide functionality to the floating action button.
    //
    //
    //**********************************************************************************************
    private void setFab() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), AddPostActivity.class);
                startActivity(intent);
            }
        });
    }

    //**********************************************************************************************
    // Method: initializeAttributes()
    //
    // Purpose: To initialize class attributes.
    //
    //
    //**********************************************************************************************
    private void initializeAttributes() {
        fab = findViewById(R.id.fabAddComment);
        mAuth = FirebaseAuth.getInstance();
    }

    //**********************************************************************************************
    // Method: onCreateOptionsMenu(Menu menu)
    //
    // Purpose: To inflate menu_main xml file.
    //
    //
    //**********************************************************************************************
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    //**********************************************************************************************
    // Method: onOptionsItemSelected(MenuItem item){}
    //
    // Purpose: To provide functionality for OptionsMenu.
    //
    //
    //**********************************************************************************************
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        // Go to about activity if selected about menu item
        if (id == R.id.action_about){
            Intent intent = new Intent(getBaseContext(), AboutActivity.class);
            startActivity(intent);
        }

        // Go to settings activity if selected settings menu item
        if (id == R.id.action_settings) {
            Intent intent = new Intent(getBaseContext(), SettingsActivity.class);
            intent.putExtra(Constants.DOCUMENT_KEY, mAuth.getUid());
            startActivity(intent);
        }

        // Go to authenticated user profile is selected profile menu item
        if(id == R.id.action_profile){
            Intent intent = new Intent(getBaseContext(), ProfileActivity.class);
            intent.putExtra(Constants.DOCUMENT_KEY, mAuth.getUid());
            startActivity(intent);
        }

        // Sign out if selected sign out menu item
        if(id == R.id.action_sign_out){
            mAuth.signOut();
            recreate();
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
