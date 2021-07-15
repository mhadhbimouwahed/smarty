package com.example.smarty;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class MainActivity extends AppCompatActivity {

    private EditText email;
    private EditText password;
    private TextView login;
    private TextView forgotPassword;
    private TextView signup;
    private ProgressBar progressBar;


    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;


    private static String TAG="FAILURE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        firebaseAuth = FirebaseAuth.getInstance();


        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        login = findViewById(R.id.login);
        forgotPassword = findViewById(R.id.forgotPassword);
        signup = findViewById(R.id.signup);

        progressBar=findViewById(R.id.progress_bar);

        forgotPassword.setOnClickListener(x->{
            startActivity(new Intent(getApplicationContext(),ForgotPasswordActivity.class));
        });

        signup.setOnClickListener(x->{
            startActivity(new Intent(getApplicationContext(),SignUpActivity.class));
        });

        login.setOnClickListener(x->{
            if(email.getText().toString().equals("adminpage@gmail.com")&&password.getText().toString().equals("123adminpage456")){
                startActivity(new Intent(getApplicationContext(),AdminActivity.class));
            }
            if(email.getText().toString().length()==0){
                email.setError("This field cannot be empty");
            }
            if(password.getText().toString().length()==0){
                password.setError("This field cannot be empty");
            }
            else{
                LoginMan();
            }
        });

    }

    @Override
    protected void onStart(){
        super.onStart();
        FirebaseUser user=firebaseAuth.getCurrentUser();
        if(user!=null){
            startActivity(new Intent(getApplicationContext(),MainPage.class));
        }
    }

    private void LoginMan() {
        firebaseAuth.signInWithEmailAndPassword(email.getText().toString(),password.getText().toString())
                .addOnCompleteListener(this,l->{
                   if(l.isSuccessful()){
                       startActivity(new Intent(getApplicationContext(),MainPage.class));
                       Toast.makeText(getApplicationContext(),"Logged in successfully",Toast.LENGTH_LONG).show();
                   }
                });
    }
}