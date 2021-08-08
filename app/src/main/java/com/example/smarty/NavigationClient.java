package com.example.smarty;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.smarty.databinding.ActivityNavigationClientBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.HashMap;
import java.util.Map;

public class NavigationClient extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityNavigationClientBinding binding;
    TextView userEmail;
    TextView userFirstNameAndLastName;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    DatabaseReference database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        binding = ActivityNavigationClientBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        firebaseAuth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance().getReference("Users");


        setSupportActionBar(binding.appBarNavigationClient.toolbarr);

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow,R.id.nav_cart)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_navigation_client);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        View header=navigationView.getHeaderView(0);
        userEmail=header.findViewById(R.id.userEmail);
        userFirstNameAndLastName=header.findViewById(R.id.userFirstNameAndLastName);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.navigation_client, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.logout_client:
                logout();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void logout() {

        user= firebaseAuth.getCurrentUser();
        if (user!=null){
            firebaseAuth.signOut();
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_navigation_client);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    protected void onStart() {
        super.onStart();
        user= firebaseAuth.getCurrentUser();
        if(user!=null){
            database.child(user.getUid()).get().addOnCompleteListener(task->{
                if(task.isSuccessful()){
                    userEmail.setText(task.getResult().child("Email").getValue().toString());
                    userFirstNameAndLastName.setText(task.getResult().child("FirstName").getValue().toString()+" "+task.getResult().child("LastName").getValue().toString());

                }else{
                    Toast.makeText(getApplicationContext(), "failed to display username and email", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(failure->{
                Toast.makeText(getApplicationContext(), "please check you internet connection", Toast.LENGTH_SHORT).show();
            });
        }else{
            firebaseAuth.signOut();
        }
    }
}