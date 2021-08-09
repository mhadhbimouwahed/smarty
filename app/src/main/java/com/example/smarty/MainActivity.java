package com.example.smarty;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;


public class MainActivity extends AppCompatActivity {

    private EditText email;
    private EditText password;
    private ImageView logo;
    private ProgressBar progressBar;
    private FirebaseAuth firebaseAuth;
    private Animation animation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        firebaseAuth = FirebaseAuth.getInstance();

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        TextView login = findViewById(R.id.login);
        TextView forgotPassword = findViewById(R.id.forgotPassword);
        TextView signup = findViewById(R.id.signup);

        progressBar=findViewById(R.id.progress_bar);
        logo=findViewById(R.id.logo);
        animation=AnimationUtils.loadAnimation(getApplicationContext(),R.anim.animation);

        logo.setOnClickListener(x-> logo.startAnimation(animation));


        forgotPassword.setOnClickListener(x-> startActivity(new Intent(getApplicationContext(),ForgotPasswordActivity.class)));

        signup.setOnClickListener(x-> startActivity(new Intent(getApplicationContext(),SignUpActivity.class)));


        login.setOnClickListener(x->{
            if(email.getText().toString().length()==0){
                email.setError("This field cannot be empty");
            }
            else if(password.getText().toString().length()==0){
                password.setError("This field cannot be empty");
            }
            else{
                progressBar.setVisibility(View.VISIBLE);
                LoginMan();
            }


        });



    }

    @Override
    protected void onStart(){
        super.onStart();


        progressBar.setVisibility(View.INVISIBLE);
        FirebaseUser user=firebaseAuth.getCurrentUser();
        if(user!=null){
            startActivity(new Intent(getApplicationContext(),NavigationClient.class));
            if(Objects.equals(user.getEmail(), "adminpage@gmail.com")){
                startActivity(new Intent(getApplicationContext(),AdminActivity.class));
            }
        }

    }
    @Override
    protected void onStop(){
        super.onStop();
        progressBar.setVisibility(View.INVISIBLE);
    }

    private void LoginMan() {
        firebaseAuth.signInWithEmailAndPassword(email.getText().toString(),password.getText().toString())
                .addOnCompleteListener(this,l->{
                   if(l.isSuccessful()){
                       if(Objects.equals(Objects.requireNonNull(firebaseAuth.getCurrentUser()).getEmail(), "adminpage@gmail.com")){
                           startActivity(new Intent(getApplicationContext(),AdminActivity.class));
                           Toast.makeText(getApplicationContext(),"Welcome admin",Toast.LENGTH_SHORT).show();
                       }else{
                           startActivity(new Intent(getApplicationContext(),NavigationClient.class));
                           Toast.makeText(getApplicationContext(),"Logged in successfully",Toast.LENGTH_LONG).show();
                       }

                   }else{
                       email.setText("");
                       password.setText("");
                       AlertDialog.Builder alertDialog=new AlertDialog.Builder(this);
                       alertDialog.create();
                       alertDialog.setTitle("Error");
                       alertDialog.setMessage("This account doesn't exist");
                       alertDialog.setPositiveButton("Okay", (dialog, which) -> dialog.dismiss());
                       alertDialog.show();
                       progressBar.setVisibility(View.INVISIBLE);
                   }
                });
    }
}