package com.example.smarty;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

public class MainPage extends AppCompatActivity {
    private Button logout;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        firebaseAuth=FirebaseAuth.getInstance();
        logout=findViewById(R.id.logout);



        logout.setOnClickListener(x->{

            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
        });
    }

    @Override
    protected void onStart(){
        super.onStart();
        if(FirebaseAuth.getInstance().getCurrentUser().getEmail().equals("adminpage@gmail.com")){
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
            FirebaseAuth.getInstance().signOut();
        }
    }

}