package com.example.smarty;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {
    private EditText email;
    private FirebaseAuth firebaseAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        firebaseAuth=FirebaseAuth.getInstance();

        email=findViewById(R.id.email_f);
        TextView resetButton = findViewById(R.id.forgotPassword_f);

        resetButton.setOnClickListener(v -> {
            if(email.getText().toString().length()==0){
                email.setError("this field cannot be empty");
            }else{
                ForgotPassword();
            }

        });

    }

    public void ForgotPassword(){
        firebaseAuth.sendPasswordResetEmail(email.getText().toString())
                .addOnSuccessListener(unused -> {
                    Toast.makeText(getApplicationContext(),"an email was sent to reset your password",Toast.LENGTH_LONG).show();
                    email.setText("");
                    AlertDialog.Builder alertDialog=new AlertDialog.Builder(this);
                    alertDialog.create();
                    alertDialog.setTitle("Email sent");
                    alertDialog.setMessage("Please check your email successfully and continue the procedure from there");
                    alertDialog.setPositiveButton("Okay",(dialog,which)->dialog.dismiss());
                    alertDialog.show();
                }).addOnFailureListener(e -> {
            AlertDialog.Builder builder=new AlertDialog.Builder(this);
            builder.create();
            builder.setTitle("Error");
            builder.setMessage("There was a problem sending the reset information");
            builder.setPositiveButton("Okay",(dialog,which)->dialog.dismiss());
            email.setText("");
            builder.show();
        });
    }

}