package com.example.smarty;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
        TextView signin_anonymously=findViewById(R.id.signin_anonymously);

        progressBar=findViewById(R.id.progress_bar);
        logo=findViewById(R.id.logo);
        animation=AnimationUtils.loadAnimation(getApplicationContext(),R.anim.animation);

        logo.setOnClickListener(x-> logo.startAnimation(animation));


        forgotPassword.setOnClickListener(x-> startActivity(new Intent(getApplicationContext(),ForgotPasswordActivity.class)));

        signup.setOnClickListener(x-> startActivity(new Intent(getApplicationContext(),SignUpActivity.class)));


        login.setOnClickListener(x->{
            if(email.getText().toString().length()==0){
                email.setError("Ce champ ne peut pas ??tre vide");
            }
            else if(password.getText().toString().length()==0){
                password.setError("Ce champ ne peut pas ??tre vide");
            }
            else{
                progressBar.setVisibility(View.VISIBLE);
                LoginMan();
            }
        });
        
        signin_anonymously.setOnClickListener(x->{
            firebaseAuth.signInAnonymously().addOnCompleteListener(task->{
                if (task.isSuccessful()){
                    startActivity(new Intent(getApplicationContext(),NotLoggedInActivity.class));
                    Toast.makeText(getApplicationContext(), "connect?? anonymement avec succ??s", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplicationContext(), "impossible de se connecter de mani??re anonyme", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(failure->{
                Log.d("ERROR_SIGNIN_ANONYMOUSLY",failure.getMessage()); 
            });

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
                           Toast.makeText(getApplicationContext(),"Connect?? avec succ??s",Toast.LENGTH_LONG).show();
                       }

                   }else{
                       email.setText("");
                       password.setText("");
                       AlertDialog.Builder alertDialog=new AlertDialog.Builder(this);
                       alertDialog.create();
                       alertDialog.setTitle("Erreur");
                       alertDialog.setMessage("Veuillez v??rifier ?? nouveau votre adresse e-mail et votre mot de passe");
                       alertDialog.setPositiveButton("OK", (dialog, which) -> dialog.dismiss());
                       alertDialog.show();
                       progressBar.setVisibility(View.INVISIBLE);
                   }
                });
    }
}