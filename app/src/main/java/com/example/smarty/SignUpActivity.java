package com.example.smarty;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

public class SignUpActivity extends AppCompatActivity {
    private EditText firstName;
    private EditText lastName;
    private EditText email;
    private EditText password;
    private TextView signup;
    private ProgressBar progressBar;
    private FirebaseAuth firebaseAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        firebaseAuth=FirebaseAuth.getInstance();

        firstName=findViewById(R.id.firstName);
        lastName=findViewById(R.id.lastName);
        email=findViewById(R.id.email_s);
        password=findViewById(R.id.password_s);
        signup=findViewById(R.id.signup_s);
        progressBar=findViewById(R.id.progress_bar_s);


        progressBar.setVisibility(View.INVISIBLE);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(firstName.getText().toString().length()==0){
                    firstName.setError("this field cannot be empty");
                }
                else if(lastName.getText().toString().length()==0){
                    lastName.setError("this field cannot be empty");
                }
                else if(email.getText().toString().length()==0){
                    email.setError("this field cannot be empty");
                }
                else if(password.getText().toString().length()==0){
                    password.setError("this field cannot be empty");
                }
                else if(password.getText().toString().length()>10){
                    password.setError("the password is 10 characters maximum");
                }else{
                    progressBar.setVisibility(View.VISIBLE);
                    Register();
                }


            }
        });
    }
    public void Register(){
        firebaseAuth.createUserWithEmailAndPassword(email.getText().toString(),password.getText().toString()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                User user=new User(email.getText().toString().trim(),password.getText().toString().trim(),firstName.getText().toString().trim(),lastName.getText().toString().trim());
                Task<Void> users = FirebaseDatabase.getInstance().getReference("Users").child(String.valueOf(FirebaseAuth.getInstance().getCurrentUser().getUid())).setValue(user);

                if(task.isSuccessful()){
                    Toast.makeText(getApplicationContext(),"created account successfully",Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(getApplicationContext(),MainPage.class);

                    startActivity(intent);
                    finish();
                }else{
                    Toast.makeText(getApplicationContext(),"Sign up failed",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}