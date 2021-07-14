package com.example.smarty;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;



import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class MainActivity extends AppCompatActivity {

    private EditText email;
    private EditText password;
    private TextView login;
    private TextView forgotPassword;
    private TextView signup;

    private FirebaseAuth firebaseAuth;

    private static String TAG="FAILURE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        firebaseAuth=FirebaseAuth.getInstance();


        email=findViewById(R.id.email);
        password=findViewById(R.id.password);
        login=findViewById(R.id.login);
        forgotPassword=findViewById(R.id.forgotPassword);
        signup=findViewById(R.id.signup);




        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(email.getText().toString().equals("")){
                    email.setError("this field cannot be empty");
                }
                else if(password.getText().toString().equals("")){
                    password.setError("this field cannot be empty");
                }else{
                    if(email.getText().toString().equals("adminpage@gmail.com")&&password.getText().toString().equals("123adminpage456")){
                        email.setText("");
                        password.setText("");
                        startActivity(new Intent(getApplicationContext(),AdminActivity.class));
                    }else{
                        Login();
                    }
                }
            }
        });

        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createDialog();
            }
        });


        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),SignUpActivity.class);
                startActivity(intent);
            }
        });


        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),ForgotPasswordActivity.class);
                startActivity(intent);
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

    public void Login(){


            firebaseAuth.signInWithEmailAndPassword(email.getText().toString(),password.getText().toString())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){

                                startActivity(new Intent(getApplicationContext(),MainPage.class));

                            }else{
                                Toast.makeText(getApplicationContext(),"there was an error loggin in",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

    }

    private void createDialog(){
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("Facebook Login");
        builder.setMessage("you can login with facebook instead");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Toast.makeText(getApplicationContext(),"successsssss",Toast.LENGTH_SHORT).show();

            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        builder.show();
    }

    private void updateUI(FirebaseUser user){
        if (user!=null){
            Toast.makeText(getApplicationContext(),"welcome "+user.getDisplayName(),Toast.LENGTH_LONG);
        }
    }

}