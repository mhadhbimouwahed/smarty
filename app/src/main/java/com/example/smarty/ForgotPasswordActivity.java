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
                email.setError("Ce champ ne peut pas être vide");
            }else{
                ForgotPassword();
            }

        });

    }

    public void ForgotPassword(){
        firebaseAuth.sendPasswordResetEmail(email.getText().toString())
                .addOnSuccessListener(unused -> {
                    Toast.makeText(getApplicationContext(), "un email a été envoyé pour réinitialiser votre mot de passe",Toast.LENGTH_LONG).show();
                    email.setText("");
                    AlertDialog.Builder alertDialog=new AlertDialog.Builder(this);
                    alertDialog.create();
                    alertDialog.setTitle("Email envoyé");
                    alertDialog.setMessage("Veuillez vérifier votre courrier électronique et continuer la procédure à partir de là");
                    alertDialog.setPositiveButton("OK",(dialog,which)->dialog.dismiss());
                    alertDialog.show();
                }).addOnFailureListener(e -> {
            AlertDialog.Builder builder=new AlertDialog.Builder(this);
            builder.create();
            builder.setTitle("Erreur");
            builder.setMessage("Un problème est survenu lors de l'envoi des informations de réinitialisation");
            builder.setPositiveButton("OK",(dialog,which)->dialog.dismiss());
            email.setText("");
            builder.show();
        });
    }

}