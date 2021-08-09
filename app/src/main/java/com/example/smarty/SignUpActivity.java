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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.UUID;

public class SignUpActivity extends AppCompatActivity {
    private EditText firstName;
    private EditText lastName;
    private EditText email;
    private EditText password;
    private TextView signup;
    private ProgressBar progressBar;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firestore;
    private CollectionReference collectionReference;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        firebaseAuth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        databaseReference=database.getReference("Users");
        firstName=findViewById(R.id.firstName);
        lastName=findViewById(R.id.lastName);
        email=findViewById(R.id.email_s);
        password=findViewById(R.id.password_s);
        signup=findViewById(R.id.signup_s);
        progressBar=findViewById(R.id.progress_bar_s);
        firestore=FirebaseFirestore.getInstance();
        collectionReference=firestore.collection("Users");
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
                else{
                    progressBar.setVisibility(View.VISIBLE);
                    Register();
                }


            }
        });
    }
    public void Register(){

        if (email.getText().toString().equals("adminpage@gmail.com")){
            email.setError("This field canno be empty");
        }

        firebaseAuth.createUserWithEmailAndPassword(email.getText().toString(),password.getText().toString()).addOnCompleteListener(this,task->{
           if (task.isSuccessful()){

               HashMap<String,String> user_to_create=new HashMap<>();
               user_to_create.put("Email",email.getText().toString());
               user_to_create.put("FirstName",firstName.getText().toString());
               user_to_create.put("LastName",lastName.getText().toString());
               user_to_create.put("Password",password.getText().toString());
               user_to_create.put("UserID",firebaseAuth.getUid());
               databaseReference.child(firebaseAuth.getUid()).setValue(user_to_create);
               collectionReference.document(firebaseAuth.getUid()).set(user_to_create);
               Toast.makeText(getApplicationContext(), "user created successfully", Toast.LENGTH_SHORT).show();
               startActivity(new Intent(getApplicationContext(),NavigationClient.class));

           }else{
               Toast.makeText(getApplicationContext(), "failed to sign up", Toast.LENGTH_SHORT).show();
           }
        }).addOnFailureListener(failure->{
            Toast.makeText(getApplicationContext(), "please check your internet connection", Toast.LENGTH_SHORT).show();
        });

    }
}